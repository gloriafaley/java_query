package com.query.generation.generator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.query.generation.model.RequestEntity;
import com.query.generation.representations.GenerationResponse;
import com.query.generation.representations.GeneratorDefinition;
import com.query.generation.service.RequestService;

public abstract class AbstractGenerator
{
    public static final String DICTIONARY_NAME_DATE_TOOL               = "date";
    public static final String DICTIONARY_NAME_REQUEST_SERVICE_OBJECT  = "requestService";
    public static final String DICTIONARY_NAME_ENCODING                = "encoding";
    public static final String DICTIONARY_NAME_OBJECT_IDS              = "objectIds";
    public static final String DICTIONARY_NAME_GENERATOR_PROPERTIES    = "generatorProperties";
    public static final String DICTIONARY_NAME_DATANAME_WHEN_UNWRAPPED = "_data";
    public static final String DICTIONARY_NAME_DATANAME_DEFAULT        = "data";

    GeneratorDefinition generatorDefinition;

    public AbstractGenerator()
    {
    }

    /**
     * Write only, use the delegated methods to read properties.
     * @param generatorDefinition the generatorDefinition to set
     */
    public void setGeneratorDefinition(GeneratorDefinition generatorDefinition)
    {
        this.generatorDefinition = generatorDefinition;
    }
        /**
     * Processes the request entity and it's template into the generation response.
     * @param requestService the calling request service implementation.
     * @param response the results of processing the template.
     * @param requestEntity the request entity.
     */
    abstract public void processTemplate(RequestService requestService, GenerationResponse response, RequestEntity requestEntity);

    /**
     * Returns true of the named template and/or template type should be processed by this generator.
     * @param requestEntity generation request.
     * @return true if this generator should process this generation request.
     */
    public boolean canHandle(RequestEntity requestEntity)
    {
        if (generatorDefinition != null && requestEntity != null)
        {
            String templateType = requestEntity.getTemplateType();
            if (templateType != null)
            {
                String templateType2 = generatorDefinition.getTemplateType();
                if (templateType2 != null)
                {
                    if (templateType2.equalsIgnoreCase(templateType))
                    {
                        return true;
                    }
                }
            }
            String templateName = requestEntity.getTemplateName();
            if (templateName != null)
            {
                List<String> resourceTypes = generatorDefinition.getResourceTypes();
                if (resourceTypes != null)
                {
                    String value = templateName.trim().toLowerCase();
                    for (String resourceType : resourceTypes)
                    {
                        if (value.endsWith(resourceType.toLowerCase()))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    void writeUnwrappedFields(Map<String, Object> dictionary, Object dataSelection)
    {
        if (dataSelection instanceof Map)
        {
            @SuppressWarnings("unchecked")
            Set<Entry<String, Object>> entrySet = ((Map<String, Object>) dataSelection).entrySet();
            for (Entry<String, Object> entry : entrySet)
            {
                dictionary.put(entry.getKey(), entry.getValue());
            }
        }
        else
        {
            Set<String> done = new HashSet<String>();
            Method[] methods = dataSelection.getClass().getMethods();
            for (Method method : methods)
            {
                String key = null;
                if (method.getName().startsWith("get") && method.getParameterCount() == 0)
                {
                    key = method.getName().substring(3);
                }
                else if (method.getName().startsWith("is") && method.getParameterCount() == 0)
                {
                    key = method.getName().substring(2);
                }
                if (key != null && key.length() > 0)
                {
                    char first = key.charAt(0);
                    key = Character.toLowerCase(first) + key.substring(2);
                    Object invoke;
                    try
                    {
                        invoke = method.invoke(dataSelection);
                        dictionary.put(key, invoke);
                        done.add(key);
                    }
                    catch (IllegalAccessException e)
                    {
                        // throw GenerationValidationUtil.newServerErrorException(e);
                    }
                    catch (IllegalArgumentException e)
                    {
                        // throw GenerationValidationUtil.newServerErrorException(e);
                    }
                    catch (InvocationTargetException e)
                    {
                        // throw GenerationValidationUtil.newServerErrorException(e);
                    }
                }
            }
            Field[] fields = dataSelection.getClass().getFields();
            for (Field field : fields)
            {
                String key = field.getName();
                if (field.isAccessible() && !done.contains(key))
                {
                    Object value;
                    try
                    {
                        value = field.get(dataSelection);
                        dictionary.put(key, value);
                        done.add(key);
                    }
                    catch (IllegalArgumentException e)
                    {
                        // throw GenerationValidationUtil.newServerErrorException(e);
                    }
                    catch (IllegalAccessException e)
                    {
                        // throw GenerationValidationUtil.newServerErrorException(e);
                    }
                }
            }
        }
    }

    /**
     * @see com.query.generation.representations.GeneratorDefinition#getVersion()
     * @return the version
     */
    public int getVersion()
    {
        return generatorDefinition.getVersion();
    }

    /**
     * @see com.query.generation.representations.GeneratorDefinition#getId()
     * @return the id
     */
    public String getId()
    {
        return generatorDefinition.getId();
    }

    /**
     * @see com.query.generation.representations.GeneratorDefinition#getName()
     * @return the name
     */
    public String getName()
    {
        return generatorDefinition.getName();
    }

    /**
     * @see com.query.generation.representations.GeneratorDefinition#getDescription()
     * @return the description
     */
    public String getDescription()
    {
        return generatorDefinition.getDescription();
    }

    /**
     * @see com.query.generation.representations.GeneratorDefinition#getType()
     * @return the type
     */
    public String getType()
    {
        return generatorDefinition.getType();
    }

    /**
     * @see com.query.generation.representations.GeneratorDefinition#getTemplateType()
     * @return the templateType
     */
    public String getTemplateType()
    {
        return generatorDefinition.getTemplateType();
    }

    /**
     * @see com.query.generation.representations.GeneratorDefinition#getResourceTypes()
     * @return the resourceTypes
     */
    public List<String> getResourceTypes()
    {
        return generatorDefinition.getResourceTypes();
    }

    /**
     * @see com.query.generation.representations.GeneratorDefinition#getGeneratorTypeName()
     * @return the generatorTypeName
     */
    public String getGeneratorTypeName()
    {
        return generatorDefinition.getGeneratorTypeName();
    }

}