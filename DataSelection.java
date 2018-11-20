package com.query.generation.representations.querymodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect()
@JsonPropertyOrder({ "type" }) // keep the type field first
public class DataSelection
{
    public enum DataSelectionRepType
    {
        QUERY
    }

    /* typing information for possible polymorphism */
    @JsonProperty(required = true, defaultValue = Constants.TYPE_VALUE_QUERY)
    public DataSelection.DataSelectionRepType getType()
    {
        return DataSelectionRepType.QUERY;
    }

    @JsonProperty(required = false)
    public List<ExternalResource> externalResources;

    @JsonProperty(required = true)
    private DataSelectionArguments arguments = new DataSelectionArguments();

    /**
     * @return the arguments
     */
    public DataSelectionArguments getArguments()
    {
        return arguments;
    }

    /**
     * @param arguments the arguments to set
     */
    public void setArguments(DataSelectionArguments arguments)
    {
        this.arguments = arguments;
    }

    public List<ExternalResource> getExternalResources()
    {
        return externalResources;
    }

    public void setExternalResources(List<ExternalResource> externalResources)
    {
        this.externalResources = externalResources;
    }

}