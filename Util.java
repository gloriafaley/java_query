package com.query.generation.factory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.POJONode;
import com.commons.logging.Logger;
import com.commons.logging.LoggerFactory;
import com.commons.rest.exceptions.ResourceException;
import com.commons.rest.exceptions.http.BadRequestException;
import com.commons.rest.exceptions.http.NotFoundException;
import com.query.generation.Constants;
import com.query.generation.service.ExceptionUtil;
import com.query.generation.support.L10nMessages;
import com.query.generation.support.LogMessages;

public class Util
{
    private static Logger log = LoggerFactory.getLogger(Util.class);

    /**
     * Equivalent to <code>parse(String, Util.createObjectMapper(), Util.createIdMappings())</code>
     * @param json string to be parsed into a {@link JsonNode} tree.
     * @return JSON node tree of parsed objects.
     */
    public static JsonNode parse(String json)
    {
        // note: this method is used by Velocity Macros
        ObjectMapper objectMapper = Util.createObjectMapper();
        Map<String, Object> idMappings = Util.createIdMappings();
        JsonNode jsonTree = Util.parse(json, objectMapper, idMappings);
        return jsonTree;
    }

    /**
     * Parses the json into a json node tree of parsed object and populates the idMappings with all objects
     * that contain a "$id" field and uses that as the key to each.
     * @param json JSON text.
     * @param objectMapper object mapper.
     * @param idMappings map of "$id" values to their objects.
     * @return JSON node tree of parsed objects.
     */
    public static JsonNode parse(String json, ObjectMapper objectMapper, Map<String, Object> idMappings)
    {
        // note: this method is used by Velocity Macros
        /* Parse json text and create a tree to find the nodes. */
        JsonNode jsonTree = null;
        if ( json != null ) {
            try {
                ObjectReader reader = objectMapper.reader(Object.class);
                jsonTree = reader.readTree(json);
            }
            catch (ResourceException e) {
                throw e;
            }
            catch (Throwable e) {
                // catches any NPE's or other undocumented errors from delayed parsing.
                throw newExceptionCannotParse(e);
            }
            /* the id mappings are any object inside of that original json with an "id" property. */
            loadIdMappings(idMappings, jsonTree, objectMapper);
        }
        return jsonTree;
    }

    public static Object toValue(JsonNode jsonTree, ObjectMapper objectMapper)
    {
        try
        {
            return objectMapper.treeToValue(jsonTree, Object.class);
        }
        catch (ResourceException e) {
            throw e;
        }
        catch (JsonProcessingException e)
        {
            throw newExceptionCannotParse(e);
        }
        catch (Throwable e) {
            // catches any NPE's or other undocumented errors from delayed parsing.
            throw newExceptionCannotParse(e);
        }
    }

    /**
     * Creates a Jackson object mapper.
     * @return object mapper.
     */
    public static ObjectMapper createObjectMapper()
    {
        // note: this method is used by Velocity Macros
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    /**
     * Creates an empty map of strings to objects.
     * @return empty map.
     */
    public static Map<String, Object> createIdMappings()
    {
        // note: this method is used by Velocity Macros
        Map<String, Object> idMappings = new LinkedHashMap<String, Object>();
        return idMappings;
    }

    public static void loadIdMappings(Map<String, Object> idMappings, JsonNode jsonTree, ObjectMapper objectMapper)
    {
        if (jsonTree.isObject()) {
            JsonNode idNode = jsonTree.get("id");
            if (idNode != null && !idNode.isMissingNode()) {
                String id = idNode.asText();
                Object object;
                try {
                    object = objectMapper.treeToValue(jsonTree, Object.class);
                    idMappings.put(id, object);
                }
                catch (JsonProcessingException e) {
                    log.warn(LogMessages.WARN_MSG_ERROR_LOADING_JSON_FMT_LOG, e);
                }
            }
            Iterator<Entry<String, JsonNode>> fields = jsonTree.fields();
            while (fields.hasNext()) {
                Entry<String, JsonNode> next = fields.next();
                JsonNode value = next.getValue();
                loadIdMappings(idMappings, value, objectMapper);
            }
        }
        else if (jsonTree.isArray()) {
            Iterator<JsonNode> elements = jsonTree.elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                loadIdMappings(idMappings, element, objectMapper);
            }
        }
        else {
            // do nothing for missing or value nodes
        }
    }

    public static Object getRequiredObject(String path, ObjectMapper objectMapper, JsonNode jsonTree, Map<String, Object> idMappings)
    {
        boolean found = false;
        Object foundObject = null;
        if ( path!= null && (path.startsWith("/") || path.startsWith("#/") || path.isEmpty())) {
            if (path.startsWith("#/")) {  // path can't be null here
                path = path.substring(1);
            }

            try {
                JsonPointer jsonPointer = JsonPointer.valueOf(path);
                log.debug("getAtPath=" + jsonPointer);

                JsonNode at = jsonTree.at(jsonPointer);
                log.debug("  jsonNode=" + at + (at == null ? "" : ("(" + at.getClass() + ")")));
                if (at != null && !at.isMissingNode()) {
                    if (at.isPojo()) {
                        foundObject = ((POJONode) at).getPojo();
                    }
                    else {
                        foundObject = objectMapper.treeToValue(at, Object.class);
                    }
                    log.debug("  foundObject=" + foundObject
                              + (foundObject == null ? "" : ("(" + foundObject.getClass() + ")")));
                    found = true;
                }
            }
            catch (Throwable e) {
                // catches any NPE's or other undocumented errors from delayed parsing.
                throw newExceptionIsInvalid(path, e);
            }
        }
        else {
            // it 'might' be an ID...
            if ( idMappings.containsKey(path) ) {
                foundObject = idMappings.get(path);
                log.debug("  foundObject=" + foundObject
                          + (foundObject == null ? "" : ("(" + foundObject.getClass() + ")")));
                found = true;
            }
            else {
                throw newExceptionIdIsNotFound(path);
            }
        }
        if (!found) {
            throw newExceptionIsNotFound(path);
        }
        // finding "a":null is OK, but not finding "a" is an error.
        return foundObject;
    }

    public static Object getOptionalObject(String path, ObjectMapper objectMapper, JsonNode jsonTree, Map<String, Object> idMappings)
    {
        Object foundObject = null;
        if ( path!= null && (path.startsWith("/") || path.startsWith("#/") || path.isEmpty())) {
            if (path.startsWith("#/")) {  // path can't be null here
                path = path.substring(1);
            }

            try {
                JsonPointer jsonPointer = JsonPointer.valueOf(path);
                log.debug("getAtPath=" + jsonPointer);

                JsonNode at = jsonTree.at(jsonPointer);
                log.debug("  jsonNode=" + at + (at == null ? "" : ("(" + at.getClass() + ")")));
                if (at != null && !at.isMissingNode()) {
                    if (at.isPojo()) {
                        foundObject = ((POJONode) at).getPojo();
                    }
                    else {
                        foundObject = objectMapper.treeToValue(at, Object.class);
                    }
                    log.debug("  foundObject=" + foundObject
                              + (foundObject == null ? "" : ("(" + foundObject.getClass() + ")")));
                }
            }
            catch (Throwable e) {
                // catches any NPE's or other undocumented errors from delayed parsing.
                throw newExceptionIsInvalid(path, e);
            }
        }
        else {
            // it 'might' be an ID...
            if ( idMappings.containsKey(path) ) {
                foundObject = idMappings.get(path);
                log.debug("  foundObject=" + foundObject
                          + (foundObject == null ? "" : ("(" + foundObject.getClass() + ")")));
            }
        }
        return foundObject;
    }

    public static BadRequestException newExceptionCannotConvertToJSON(String path, Throwable e)
    {
        return ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12411_COULD_CONVERT_OBJECT_TO_JSON,
                                                    e,
                                                    L10nMessages.ERR_MSG_COULD_NOT_CONVERT_OBJECT_TO_JSON_FMT_TXT,
                                                    path);
    }

    public static BadRequestException newExceptionCannotParse(Throwable e)
    {
        return ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12410_COULD_NOT_PARSE_JSON,
                                                    e,
                                                    L10nMessages.ERR_MSG_COULD_NOT_PARSE_JSON_TXT);
    }

    public static BadRequestException newExceptionIsInvalid(String path, Throwable e)
    {
        return ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12412_PROPERTY_OBJECT_IS_INVALID,
                                                    e,
                                                    L10nMessages.ERR_MSG_PROPERTY_OBJECT_IS_INVALID_FMT_TXT,
                                                    path);
    }

    public static NotFoundException newExceptionIsNotFound(String path)
    {
        return ExceptionUtil.newNotFoundException(Constants.ERRORCODE_12413_PROPERTY_OBJECT_IS_NOT_FOUND,
                                                  L10nMessages.ERR_MSG_PROPERTY_OBJECT_NOT_FOUND_FMT_TXT,
                                                  path);
    }

    public static BadRequestException newExceptionIsNull(String path)
    {
        return ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12414_PROPERTY_OBJECT_IS_NULL,
                                                    L10nMessages.ERR_MSG_PROPERTY_OBJECT_IS_NULL_FMT_TXT,
                                                    path);
    }

    public static BadRequestException newExceptionIsEmptyValue(String path)
    {
        return ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12415_PROPERTY_VALUE_IS_EMPTY,
                                                    L10nMessages.ERR_MSG_PROPERTY_VALUE_IS_EMPTY_FMT_TXT,
                                                    path);
    }

    public static NotFoundException newExceptionIdIsNotFound(String path)
    {
        return ExceptionUtil.newNotFoundException(Constants.ERRORCODE_12416_PROPERTY_OBJECT_ID_IS_NOT_FOUND,
                                                  L10nMessages.ERR_MSG_PROPERTY_OBJECT_ID_NOT_FOUND_FMT_TXT,
                                                  path);
    }

}
