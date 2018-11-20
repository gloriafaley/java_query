package com.query.generation.representations;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.commons.rest.representations.Link;

/**
 * The information for a response to a generation request.
 *
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect()
@JsonPropertyOrder(value = { "name", "outputText", "catalog" })
public class GenerationResponse
{
    public static final String ACCEPT_TYPE      = "application/vnd.sas.generation.response";
    public static final String ACCEPT_TYPE_JSON = ACCEPT_TYPE + "+json";
    public static final String ACCEPT_TYPES     = ACCEPT_TYPE_JSON;                         // + " " +  ACCEPT_TYPE_XML;
    public static final int SCHEMA_VERSION = 1;

    @JsonPropertyDescription("The representation version: 1")
    @JsonProperty()
    @Min(SCHEMA_VERSION)
    @Max(SCHEMA_VERSION)
    private int version = SCHEMA_VERSION;

    @JsonPropertyDescription("The name of this generation response.")
    private String name;

    @JsonPropertyDescription("The output text generated.")
    @JsonProperty(required = true)
    private String outputText;

    @JsonPropertyDescription("The catalog used if any or null if none.")
    private Catalog catalog;

    @JsonPropertyDescription("A list of Link objects.")
    private List<Link> links;

    /**
     * @return the outputText
     */
    public String getOutputText()
    {
        return outputText;
    }

    /**
     * @param outputText the outputText to set
     */
    public void setOutputText(String outputText)
    {
        this.outputText = outputText;
    }

    /**
     * @return the catalog
     */
    public Catalog getCatalog()
    {
        return catalog;
    }

    /**
     * @param catalog the catalog to set
     */
    public void setCatalog(Catalog catalog)
    {
        this.catalog = catalog;
    }

    /**
     * Set this instance's list of Link objects. The input list is not copied.
     * @param links the set of links for this collection.
     */
    public void setLinks(List<Link> links)
    {
        this.links = links;
    }

    /**
     * @return this object's list of Link instances. This list is not copied.
     */
    public List<Link> getLinks()
    {
        if (links == null)
        {
            links = new ArrayList<Link>(0);
        }
        return links;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public int getVersion()
    {
        return version;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return  getClass().getSimpleName()
                + " [name="
                + (getName() == null ? null : ("\"" + getName() + "\""))
                + ", outputText="
                + (getOutputText() == null ? null : ("\"" + getOutputText() + "\""))
                + ", catalog="
                + (getCatalog() == null ? null : ("(" + getCatalog() + ")"))
                + ", links="
                + (getLinks() == null ? null : ("(" + getLinks() + ")"))
                + "]";
    }

}