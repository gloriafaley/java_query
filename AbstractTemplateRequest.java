package com.query.generation.representations;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect()
@JsonPropertyOrder(value = { "selectionPath", "templatePath", "document", "templateName" })
abstract public class AbstractTemplateRequest
{
    public static final String CREATE_TEMPLATE_CATALOG_TOKEN = "*CREATE*";

    @JsonPropertyDescription("A JSON pointer to the section of the document that holds the <code>data</code> for the template (if null then the entire document is used. IE \"#/\".)")
    protected String selectionPath;

    @JsonPropertyDescription("The name of the variable to hold the selected document data for template processing.  "
            + "By default (ie null) the selected section is stored as 'data' and it will be accessable as $data and its properties as $data.property1, $data.property2, etc.  "
            + "If set an empty string then only the properties will be accessible as $property1, $property2, etc.")
    protected String selectionDataName;

    @JsonPropertyDescription("A JSON pointer to the section of the document that holds the Template Service information to find the template.")
    protected String templatePath;

    @JsonPropertyDescription("The name of the template to use (if the template information is not in the document model).")
    @JsonProperty(required = true)
    protected String templateName;

    @JsonPropertyDescription("The name of the template catalog to use if set or \"*CREATE*\" if one is to be created.")
    protected String templateCatalogName;

    @JsonPropertyDescription("The template encoding to use if set or the default encoding for the midtier if not set.")
    protected String templateEncoding;

    @JsonPropertyDescription("The template type to use instead of the one inferred from the template name suffix.")
    protected String templateType;

    @JsonPropertyDescription("The native properties to be passed onto the template generator as key/value string pairs.")
    protected Map<String,String> generatorProperties;

    public AbstractTemplateRequest()
    {
        super();
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }

    public String getSelectionPath()
    {
        return selectionPath;
    }

    public void setSelectionPath(String selectionPath)
    {
        this.selectionPath = selectionPath;
    }

    public String getTemplatePath()
    {
        return templatePath;
    }

    public String getSelectionDataName()
    {
        return selectionDataName;
    }

    public void setSelectionDataName(String selectionDataName)
    {
        this.selectionDataName = selectionDataName;
    }

    public void setTemplatePath(String templatePath)
    {
        this.templatePath = templatePath;
    }

    /**
     * @return the templateEncoding
     */
    public String getTemplateEncoding()
    {
        return templateEncoding;
    }

    /**
     * @param templateEncoding the templateEncoding to set
     */
    public void setTemplateEncoding(String templateEncoding)
    {
        this.templateEncoding = templateEncoding;
    }

    /**
     * @return the generatorProperties
     */
    public Map<String,String> getGeneratorProperties()
    {
        return generatorProperties;
    }

    /**
     * @param generatorProperties the generatorProperties to set
     */
    public void setGeneratorProperties(Map<String,String> generatorProperties)
    {
        this.generatorProperties = generatorProperties;
    }

    /**
     * @return the templateCatalogName
     */
    public String getTemplateCatalogName()
    {
        return templateCatalogName;
    }


    /**
     * @param templateCatalogName the templateCatalogName to set
     */
    public void setTemplateCatalogName(String templateCatalogName)
    {
        this.templateCatalogName = templateCatalogName;
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

}