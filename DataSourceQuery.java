package com.query.generation.representations.querymodel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect()
@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type", defaultImpl = DataSourceQuery.class)
@JsonPropertyOrder({ "type" }) // keep the type field first
public class DataSourceQuery implements DataSource
{
    public enum DataSourceQueryRepType
    {
        QUERYTABLE
    }

    /* typing information for possible polymorphism */
    @JsonProperty(required = true, defaultValue = Constants.TYPE_VALUE_QUERYTABLE)
    public DataSourceQuery.DataSourceQueryRepType getType()
    {
        return DataSourceQueryRepType.QUERYTABLE;
    }

    public String              templateName;
    @JsonProperty(required = true)
    public InlineDataSelection query;
    public String              alias;

    /**
     * @return the templateName
     */
    public String getTemplateName()
    {
        return templateName;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }

    /**
     * @return the query
     */
    public InlineDataSelection getQuery()
    {
        return query;
    }

    /**
     * @param query the query to set
     */
    public void setQuery(InlineDataSelection query)
    {
        this.query = query;
    }

    /**
     * @return the alias
     */
    public String getAlias()
    {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias)
    {
        this.alias = alias;
    }

}