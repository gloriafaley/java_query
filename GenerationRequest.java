package com.query.generation.representations;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The information for a generation request.
 * If the <code>selectionPath</code> is <code>null</code> then the entire <code>document</code>
 * will be used as the data for the template.
 *
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect()
@JsonPropertyOrder(value = { "selectionPath", "templatePath", "document", "templateName" })
public class GenerationRequest extends AbstractTemplateRequest
{
    public static final String ACCEPT_TYPE      = "application/vnd.generation.request";
    public static final String ACCEPT_TYPE_JSON = ACCEPT_TYPE + "+json";
    public static final String ACCEPT_TYPES     = ACCEPT_TYPE_JSON;                        // + " " +  ACCEPT_TYPE_XML;
    public static final int SCHEMA_VERSION = 1;

    @JsonPropertyDescription("The representation version: 1")
    @JsonProperty()
    @Min(SCHEMA_VERSION)
    @Max(SCHEMA_VERSION)
    private int version = SCHEMA_VERSION;

    @JsonPropertyDescription("The top level json document containing the data to be used to complete the template.")
    @JsonProperty(required = true)
    Object document;

    public GenerationRequest() {
    }

    /**
     * @return the top level document to be parsed.
     */
    public Object getDocument()
    {
        return document;
    }

    /**
     * @param document the top level document.
     */
    public void setDocument(Object document)
    {
        this.document = document;
    }

    public int getVersion()
    {
        return version;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " [templateName="
               + (getTemplateName() == null ? null : ("\"" + getTemplateName() + "\""))
               + ", templatePath="
               + (getTemplatePath() == null ? null : ("\"" + getTemplatePath() + "\""))
               + ", templateCatalogName="
               + getTemplateCatalogName()
               + ", selectionPath="
               + (getSelectionPath() == null ? null : ("\"" + getSelectionPath() + "\""))
               + ", selectionDataName="
               + (getSelectionDataName() == null ? null : ("\"" + getSelectionDataName() + "\""))
               + ", document="
               + (getDocument() == null ? null : ("(" + getDocument() + ")"))
               + "]";
    }

}