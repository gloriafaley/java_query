package com.query.generation.representations;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class GeneratorDefinition extends AbstractComposition implements Serializable
{
    private static final long serialVersionUID = -3238757988408241472L;

    public final static int    GENERATION_GENERATOR_DEFINITION_VERSION = 1;
    public final static String MEDIA_TYPE_BASE_VALUE                   = "application/vnd.generation.generator.definition";
    public final static String MEDIA_TYPE_JSON_VALUE                   = MEDIA_TYPE_BASE_VALUE + "+json";
    public final static String MEDIA_TYPE_XML_VALUE                    = MEDIA_TYPE_BASE_VALUE + "+xml";

    private int version = GENERATION_GENERATOR_DEFINITION_VERSION;

    private String       templateType;
    private List<String> resourceTypes;
    private String       generatorTypeName;

    public GeneratorDefinition()
    {
        super();
    }

    /**
     * @return the version
     */
    public int getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version)
    {
        this.version = version;
    }

    /**
     * @return the templateType
     */
    public String getTemplateType()
    {
        return templateType;
    }

    /**
     * @param templateType the templateType to set
     */
    public void setTemplateType(String templateType)
    {
        this.templateType = templateType;
    }

    /**
     * @return the resourceTypes
     */
    public List<String> getResourceTypes()
    {
        return resourceTypes;
    }

    /**
     * @param resourceTypes the resourceTypes to set
     */
    public void setResourceTypes(List<String> resourceTypes)
    {
        this.resourceTypes = resourceTypes;
    }

    /**
     * @return the generatorTypeName
     */
    public String getGeneratorTypeName()
    {
        return generatorTypeName;
    }

    /**
     * @param generatorTypeName the generatorTypeName to set
     */
    public void setGeneratorTypeName(String generatorTypeName)
    {
        this.generatorTypeName = generatorTypeName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    { /*I18NOK:BLK*/
        return "GeneratorDefinition [getVersion()=" + getVersion() + ", getGeneratorTypeName()="
                + getGeneratorTypeName() + ", getTemplateType()=" + getTemplateType() + ", getResourceTypes()="
                + getResourceTypes() + ", getId()=" + getId() + ", getName()=" + getName() + ", getDescription()="
                + getDescription() + ", getType()=" + getType() + "]";
    }

}
