package com.query.generation.representations;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class TemplateDefinition extends AbstractComposition implements Serializable
{
    private static final long serialVersionUID = 1144809541845459347L;

    public final static int    GENERATION_TEMPLATE_DEFINITION_VERSION = 1;
    public final static String MEDIA_TYPE_BASE_VALUE                  = "application/vnd.generation.template.definition";
    public final static String MEDIA_TYPE_JSON_VALUE                  = MEDIA_TYPE_BASE_VALUE + "+json";
    public final static String MEDIA_TYPE_XML_VALUE                   = MEDIA_TYPE_BASE_VALUE + "+xml";

    private int version = GENERATION_TEMPLATE_DEFINITION_VERSION;

    /**
     * the metadata for the schema/template combination.
     */
    private String metadata;

    /**
     * the schema file.
     */
    private String schema;

    /**
     * the template file.
     */
    private String template;

    /**
     * the template type.
     */
    private String templateType;

    /**
     * files that the template depends upon (ie #include, #parse, etc).
     */
    private List<String> templateDependencies;

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
     * @return the metadata
     */
    public String getMetadata()
    {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(String metadata)
    {
        this.metadata = metadata;
    }

    /**
     * @return the schema
     */
    public String getSchema()
    {
        return schema;
    }

    /**
     * @param schema the schema to set
     */
    public void setSchema(String schema)
    {
        this.schema = schema;
    }

    /**
     * @return the template
     */
    public String getTemplate()
    {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template)
    {
        this.template = template;
    }

    /**
     * @return the templateDependencies
     */
    public List<String> getTemplateDependencies()
    {
        return templateDependencies;
    }

    /**
     * @param templateDependencies the templateDependencies to set
     */
    public void setTemplateDependencies(List<String> templateDependencies)
    {
        this.templateDependencies = templateDependencies;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    { /*I18NOK:BLK*/
        return "TemplateDefinition [getVersion()=" + getVersion() + ", getTemplateType()=" + getTemplateType()
                + ", getTemplate()=" + getTemplate() + ", getSchema()=" + getSchema() + ", getMetadata()="
                + getMetadata() + ", getTemplateDependencies()=" + getTemplateDependencies() + ", getId()=" + getId()
                + ", getName()=" + getName() + ", getDescription()=" + getDescription() + ", getType()=" + getType()
                + "]";
    }

}