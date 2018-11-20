package com.query.generation.representations.querymodel;

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
@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type", defaultImpl = DataSourceColumn.class)
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
@JsonPropertyOrder({ "type" }) // keep the type field first
public class DataSourceColumn implements ExternalResource, GroupByItem, OrderByItem, SelectedItem, Expression
{
    public enum DataSourceColumnRepType
    {
        COLUMN
    }

    /* typing information for possible polymorphism */
    @JsonProperty(required = true, defaultValue = Constants.TYPE_VALUE_COLUMN)
    public DataSourceColumn.DataSourceColumnRepType getType()
    {
        return DataSourceColumnRepType.COLUMN;
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

    public String tableName;
    @JsonProperty(required = true)
    public String columnName;

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
     * @return the columnName
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

}