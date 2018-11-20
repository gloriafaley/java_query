package com.query.generation.representations.querymodel;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect()
@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type", defaultImpl = DataSourceTable.class)
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
@JsonPropertyOrder({ "type" }) // keep the type field first
public class DataSourceTable implements ExternalResource, DataSource
{
    public enum DataSourceTableRepType
    {
        TABLE
    }

    /* typing information for possible polymorphism */
    @JsonProperty(required = true, defaultValue = Constants.TYPE_VALUE_TABLE)
    public DataSourceTable.DataSourceTableRepType getType()
    {
        return DataSourceTableRepType.TABLE;
    }

    @JsonProperty(required = true)
    private String id = UUID.randomUUID().toString();

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String            schemaName;
    public String            tableName;
    public String            alias;
    public List<QueryOption> options;

    /**
     * @return the schemaName
     */
    public String getSchemaName()
    {
        return schemaName;
    }

    /**
     * @param schemaName the schemaName to set
     */
    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
    }

    /**
     * @return the tableName
     */
    public String getTableName()
    {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
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

    /**
     * @return the options
     */
    public List<QueryOption> getOptions()
    {
        return options;
    }

    /**
     * @param options the options to set
     */
    public void setOptions(List<QueryOption> options)
    {
        this.options = options;
    }

}