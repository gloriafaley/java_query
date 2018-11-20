package com.query.generation;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.commons.logging.Logger;
import com.commons.logging.LoggerFactory;
import com.commons.rest.collections.CollectionLinksBuilder;
import com.commons.rest.collections.CollectionLinksFactory;
import com.commons.rest.collections.query.QueryOptions;
import com.commons.rest.exceptions.ResourceException;
import com.commons.rest.exceptions.http.BadRequestException;
import com.commons.rest.exceptions.http.ServerErrorException;
import com.commons.rest.representations.Link;
import com.commons.rest.util.LinkBuilder;
import com.commons.rest.util.UriBuilder;
import com.query.generation.exception.GenerationException;
import com.query.generation.factory.Util;
import com.query.generation.representations.AbstractTemplateRequest;
import com.query.generation.representations.Catalog;
import com.query.generation.representations.GenerationRequest;
import com.query.generation.representations.GenerationResponse;
import com.query.generation.representations.querymodel.DataSelection;
import com.query.generation.representations.querymodel.QueryRequest;
import com.query.generation.service.ExceptionUtil;
import com.query.generation.service.RequestService;
import com.query.generation.support.L10nMessages;
import com.query.generation.support.LogMessages;
import com.query.generation.support.RB;

@RestController()
@RequestMapping(value = Constants.REQUEST_RELAY_BASE_URI)
public class RequestRelayController
{
    private static final String PROPERTY_NAME_ID                = "id";
    private static final String PROPERTY_NAME_MODEL             = "model";
    private static final String PROPERTY_NAME_OPTIONS           = "options";
    private static final String PROPERTY_NAME_PROCESSORNAME     = "processorName";
    private static final String PROPERTY_NAME_PROCESSOROPTIONS  = "processorOptions";
    private static final String PROPERTY_NAME_SELECTIONPATH     = "selectionPath";
    private static final String PROPERTY_NAME_SELECTIONDATANAME = "selectionDataName";
    private static final String PROPERTY_NAME_TEMPLATEPATH      = "templatePath";
    private static final String PROPERTY_NAME_TEMPLATENAME      = "templateName";
    private static final String PROPERTY_NAME_TEMPLATECATALOG   = "templateCatalog";

    private static Logger log = LoggerFactory.getLogger(RequestRelayController.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private RootController rootController;

    public RequestRelayController()
    {
    }

    // ---- publishing links ----

    private Link _generationRelayGetLink;

    @PostConstruct
    public void publishLinks()
    {
        URI uri = UriBuilder
                .builder("{contextRoot}" //
                        + Constants.REQUEST_RELAY_BASE_URI) // contextRoot is from yaml property
                .pathVariable("contextRoot", //
                        rootController.getContextPath())
                .create();

        log.info(LogMessages.INFO_MSG_URI_FMT_LOG, uri.toString());

        _generationRelayGetLink = LinkBuilder.builder(uri.toString(), HttpMethod.POST, Constants.REQUEST_RELAY) //
                .type(GenerationRequest.ACCEPT_TYPE) //
                .responseType(MediaType.APPLICATION_JSON_VALUE) //
                .title(RB.getMessage(L10nMessages.MODEL_RELAY_GEN_REQ_TITLE_TXT)) //
                .create();
        rootController.getLinks().add(_generationRelayGetLink);

        _generationRelayGetLink = LinkBuilder.builder(uri.toString(), HttpMethod.POST, Constants.REQUEST_RELAY) //
                .type(QueryRequest.ACCEPT_TYPE) //
                .responseType(MediaType.APPLICATION_JSON_VALUE) //
                .title(RB.getMessage(L10nMessages.MODEL_RELAY_QUERY_GEN_TITLE_TXT)) //
                .create();
        rootController.getLinks().add(_generationRelayGetLink);
    }

    // --------------------------------------------------------------------------------------------------------
    /**
     * relay a generic relay model to the Generation Request model and submit it.
     * @param request actual http request.
     * @param representation Generic relay model.
     * @param locale locale of the request.
     * @return generation text in a resource collection.
     * @throws ServerErrorException error processing request
     * @throws BadRequestException error in request
     */
    @RequestMapping(method = RequestMethod.POST, //
    consumes = { GenerationRequest.ACCEPT_TYPE_JSON }, //
    produces = { MediaType.APPLICATION_JSON_VALUE, GenerationResponse.ACCEPT_TYPE_JSON }) //
    public ResponseEntity<GenerationResponse> relayGeneration(HttpServletRequest request,
            @RequestBody Object representation, Locale locale) throws ServerErrorException, BadRequestException
    {
        if (log.isDebugEnabled())
        {
            log.debug(ExceptionUtil.from(request) + " - generate using a GenerationRelay representation request.");
            log.debug("representation=[" + representation + "]");
            if (representation != null)
            {
                log.debug(this.getClass().getName() + "   class = " + representation.getClass().getName());
            }
        }

        if (!(representation instanceof Map<?, ?>))
        {
            throw ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12410_COULD_NOT_PARSE_JSON,
                    L10nMessages.ERR_MSG_INVALID_GENERATION_REQUEST_RELAY_CONTENTS_TXT);
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) representation;

        Object id0 = payload.get(PROPERTY_NAME_ID);
        Object model0 = payload.get(PROPERTY_NAME_MODEL);
        Object options0 = payload.get(PROPERTY_NAME_OPTIONS);
        Object processorName0 = payload.get(PROPERTY_NAME_PROCESSORNAME);
        Object processorOptions0 = payload.get(PROPERTY_NAME_PROCESSOROPTIONS);

        if (log.isDebugEnabled())
        {
            log.debug("relay to generation request: id=" + id0 //
                + ", options=" + options0 //
                + ", model=" + model0 //
                + ", processorName=" + processorName0 //
                + ", processorOptions=" + processorOptions0);
        }

        Map<String, String> options = toOptions(options0);
        Map<String, String> processorOptions = toOptions(processorOptions0);
        Object model = toModel(model0);

        GenerationRequest generationRequest = newGenerationRequest(model, options, processorOptions);

        GenerationResponse result = null;
        try
        {
            result = requestService.generate(generationRequest);
        }
        catch (GenerationException e)
        {
            // this generation exception that is able to be translated into a server error exception
            int errorCode = e.getErrorCode() != 0 ? e.getErrorCode() : Constants.ERRORCODE_12400_GENERATION_FAILED;
            throw ExceptionUtil.newServerErrorException(errorCode, e, L10nMessages.ERR_MSG_GENERATION_FAILED_TXT);
        }

        if (log.isDebugEnabled())
        {
            log.debug("result=" + (result == null ? null : ("[" + result + "]")));
        }
        if (result == null)
        {
            throw ExceptionUtil.newServerErrorException(Constants.ERRORCODE_12401_GENERATE_RETURNED_NULL,
                    L10nMessages.ERR_MSG_GENERATE_RETURNED_NULL_TXT);
        }

        ResponseEntity<GenerationResponse> responseEntity = newCollectionResponseEntity(result);
        return responseEntity;
    }

    // --------------------------------------------------------------------------------------------------------
    /**
     * relay a generic relay model to the Query Request model and submit it.
     * @param request actual http request.
     * @param representation Generic relay model.
     * @param locale locale of the request.
     * @return generation text in a resource collection.
     * @throws ServerErrorException error processing request
     * @throws BadRequestException error in request
     */
    @RequestMapping(method = RequestMethod.POST, //
    consumes = { QueryRequest.ACCEPT_TYPE_JSON }, //
    produces = { MediaType.APPLICATION_JSON_VALUE, GenerationResponse.ACCEPT_TYPE_JSON }) //
    public ResponseEntity<GenerationResponse> relayQuery(HttpServletRequest request,
            @RequestBody Object representation, Locale locale) throws ServerErrorException, BadRequestException
    {
        if (log.isDebugEnabled())
        {
            log.debug(ExceptionUtil.from(request) + " - generate using a GenerationRelay representation request.");
            log.debug("representation=[" + representation + "]");
            if (representation != null)
            {
                log.debug("   class = " + representation.getClass().getName());
            }
        }

        if (!(representation instanceof Map<?, ?>))
        {
            throw ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12410_COULD_NOT_PARSE_JSON,
                    L10nMessages.ERR_MSG_INVALID_GENERATION_QUERY_RELAY_CONTENTS_TXT);
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) representation;

        Object id0 = payload.get(PROPERTY_NAME_ID);
        Object model0 = payload.get(PROPERTY_NAME_MODEL);
        Object options0 = payload.get(PROPERTY_NAME_OPTIONS);
        Object processorName0 = payload.get(PROPERTY_NAME_PROCESSORNAME);
        Object processorOptions0 = payload.get(PROPERTY_NAME_PROCESSOROPTIONS);

        if (log.isDebugEnabled())
        {
            log.debug("relay to query request: id=" + id0 //
                    + ", options=" + options0 //
                    + ", model=" + model0 //
                    + ", processorName=" + processorName0 //
                    + ", processorOptions=" + processorOptions0);
        }

        Map<String, String> options = toOptions(options0);
        Map<String, String> processorOptions = toOptions(processorOptions0);
        DataSelection dataSelection = toDataSelectionModel(model0);

        QueryRequest queryRequest = newQueryRequest(dataSelection, options, processorOptions);

        GenerationResponse result = null;
        try
        {
            result = requestService.generate(queryRequest);
        }
        catch (GenerationException e)
        {
            // this generation exception that is able to be translated into a server error exception
            int errorCode = e.getErrorCode() != 0 ? e.getErrorCode() : Constants.ERRORCODE_12400_GENERATION_FAILED;
            throw ExceptionUtil.newServerErrorException(errorCode, e, L10nMessages.ERR_MSG_GENERATION_FAILED_TXT);
        }

        if (log.isDebugEnabled())
        {
            log.debug("result=" + (result == null ? null : ("[" + result + "]")));
        }
        if (result == null)
        {
            throw ExceptionUtil.newServerErrorException(Constants.ERRORCODE_12401_GENERATE_RETURNED_NULL,
                    L10nMessages.ERR_MSG_GENERATE_RETURNED_NULL_TXT);
        }

        ResponseEntity<GenerationResponse> responseEntity = newCollectionResponseEntity(result);
        return responseEntity;
    }

    //-------------------------------------------------------------------------------------------

    private Map<String, String> toOptions(Object options0)
    {
        if (options0 != null && !(options0 instanceof Map<?, ?>))
        {
            throw ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12412_PROPERTY_OBJECT_IS_INVALID,
                    L10nMessages.ERR_MSG_INVALID_GENERATION_REQUEST_OPTIONS_TXT);
        }
        @SuppressWarnings("unchecked")
        Map<String, String> options = options0 == null ? null : ((Map<String, String>) options0);
        return options;
    }

    private Object toModel(Object model0)
    {
        if (model0 != null && (!(model0 instanceof String) && !(model0 instanceof Map<?, ?>)))
        {
            if (log.isDebugEnabled())
            {
                log.debug("model0=" + model0 + "(" + model0.getClass().getName() + ")");
            }
            throw ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12412_PROPERTY_OBJECT_IS_INVALID,
                    L10nMessages.ERR_MSG_INVALID_GENERATION_REQUEST_MODEL_TXT);
        }
        return model0;
    }

    private DataSelection toDataSelectionModel(Object model0)
    {
        DataSelection dataSelection = null;
        if (model0 != null)
        {
            if (model0 instanceof DataSelection)
            {
                dataSelection = (DataSelection) model0;
            }
            else if (model0 instanceof Map<?, ?>)
            {
                try
                {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectWriter writer = objectMapper.writer();
                    String json1 = writer.writeValueAsString(model0);
                    String json = json1;
                    dataSelection = objectMapper.reader(DataSelection.class).readValue(json);
                }
                catch (ResourceException e) {
                    throw e;
                }
                catch (JsonParseException e)
                {
                    throw Util.newExceptionCannotParse(e);
                }
                catch (JsonGenerationException e)
                {
                    throw Util.newExceptionCannotConvertToJSON(PROPERTY_NAME_MODEL, e);
                }
                catch (Throwable e)
                {
                    // catches any NPE's or other undocumented errors from delayed parsing.
                    throw Util.newExceptionCannotParse(e);
                }
            }
            else if (model0 instanceof String)
            {
                try
                {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectWriter writer = objectMapper.writer();
                    String json1 = writer.writeValueAsString(model0);
                    String json = json1;
                    dataSelection = objectMapper.reader(DataSelection.class).readValue(json);
                }
                catch (ResourceException e) {
                    throw e;
                }
                catch (JsonParseException e)
                {
                    throw Util.newExceptionCannotParse(e);
                }
                catch (JsonGenerationException e)
                {
                    throw Util.newExceptionCannotConvertToJSON(PROPERTY_NAME_MODEL, e);
                }
                catch (Throwable e)
                {
                    // catches any NPE's or other undocumented errors from delayed parsing.
                    throw Util.newExceptionCannotParse(e);
                }
            }
            else
            {
                throw ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12412_PROPERTY_OBJECT_IS_INVALID,
                        L10nMessages.ERR_MSG_INVALID_QUERY_REQUEST_DATA_SELECTION_MODEL_TXT);
            }
        }
        return dataSelection;
    }

    //-------------------------------------------------------------------------------------------

    private GenerationRequest newGenerationRequest(Object model, Map<String, String> options,
            Map<String, String> processorOptions)
    {
        GenerationRequest generationRequest = new GenerationRequest();
        generationRequest.setDocument(model);

        if (options != null && options.containsKey(PROPERTY_NAME_TEMPLATECATALOG))
        {
            generationRequest.setTemplateCatalogName(options.get(PROPERTY_NAME_TEMPLATECATALOG));
        }

        getSetOptions(generationRequest, options, processorOptions);

        return generationRequest;
    }

    private QueryRequest newQueryRequest(DataSelection dataSelection, Map<String, String> options,
            Map<String, String> processorOptions)
    {
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setDataSelection(dataSelection);

        if (options.containsKey(PROPERTY_NAME_TEMPLATECATALOG))
        {
            queryRequest.setTemplateCatalogName(options.get(PROPERTY_NAME_TEMPLATECATALOG));
        }

        getSetOptions(queryRequest, options, processorOptions);

        return queryRequest;
    }

    private void getSetOptions(AbstractTemplateRequest request, Map<String, String> options,
            Map<String, String> processorOptions)
    {
        // options:
        if (options != null)
        {
            if (options.containsKey(PROPERTY_NAME_TEMPLATENAME))
            {
                request.setTemplateName(options.get(PROPERTY_NAME_TEMPLATENAME));
            }
            if (options.containsKey(PROPERTY_NAME_TEMPLATEPATH))
            {
                request.setTemplatePath(options.get(PROPERTY_NAME_TEMPLATEPATH));
            }
            if (options.containsKey(PROPERTY_NAME_SELECTIONDATANAME))
            {
                request.setSelectionDataName(options.get(PROPERTY_NAME_SELECTIONDATANAME));
            }
            if (options.containsKey(PROPERTY_NAME_SELECTIONPATH))
            {
                request.setSelectionPath(options.get(PROPERTY_NAME_SELECTIONPATH));
            }
        }

        // processor options trump model options:
        if (processorOptions != null)
        {
            if (processorOptions.containsKey(PROPERTY_NAME_TEMPLATENAME))
            {
                request.setTemplateName(processorOptions.get(PROPERTY_NAME_TEMPLATENAME));
            }
            if (processorOptions.containsKey(PROPERTY_NAME_TEMPLATEPATH))
            {
                request.setTemplatePath(processorOptions.get(PROPERTY_NAME_TEMPLATEPATH));
            }
            if (processorOptions.containsKey(PROPERTY_NAME_SELECTIONDATANAME))
            {
                request.setSelectionDataName(processorOptions.get(PROPERTY_NAME_SELECTIONDATANAME));
            }
            if (processorOptions.containsKey(PROPERTY_NAME_SELECTIONPATH))
            {
                request.setSelectionPath(processorOptions.get(PROPERTY_NAME_SELECTIONPATH));
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------

    private ResponseEntity<GenerationResponse> newCollectionResponseEntity(GenerationResponse result)
    {
        // TODO: consider multi-part forms.
        // TODO: make sure class paths are not shown. - how?

        // no options, yet.  leave it empty
        QueryOptions options = QueryOptions.builder().create();

        // start the links with the request base uri as the path
        String generationRequestPath = UriBuilder.builder("{contextRoot}" + Constants.REQUEST_RELAY_BASE_URI) // contextRoot is from yaml property
                .pathVariable("contextRoot", rootController.getContextPath()).create().getPath();
        List<Link> links = CollectionLinksBuilder.newInstance(generationRequestPath, options).buildLinks();
        Link requestLink = CollectionLinksFactory.create(generationRequestPath, "generationRequest",
                GenerationRequest.ACCEPT_TYPE_JSON, MediaType.APPLICATION_JSON_VALUE, HttpMethod.POST);
        links.add(requestLink);

        if (result.getCatalog() != null)
        {
            String generationCatalogPath = UriBuilder
                    .builder("{contextRoot}" + Constants.CATALOGS_BASE_URI + Constants.ENDPOINT_ID) // contextRoot is from yaml property
                    .pathVariable("contextRoot", rootController.getContextPath())
                    .pathVariable(Constants.ID, result.getCatalog().getId()).create().getPath();
            Link catalogLink = CollectionLinksFactory.create(generationCatalogPath, "generationCatalog",
                    Catalog.MEDIA_TYPE_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE, HttpMethod.GET);
            catalogLink.setTitle(Catalog.class.getSimpleName());
            links.add(catalogLink);
        }

        if ( result.getLinks() != null ) {
            links.addAll(result.getLinks());
        }
        result.setLinks(links);

        ResponseEntity<GenerationResponse> responseEntity = new ResponseEntity<GenerationResponse>(result, HttpStatus.OK);
        return responseEntity;
    }
}
