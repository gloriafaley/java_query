package com.query.generation.representations.querymodel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect()
@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({ @Type(name = Constants.TYPE_VALUE_TABLE, value = DataSourceTable.class),
                @Type(name = Constants.TYPE_VALUE_JOIN, value = DataSourceJoin.class),
                @Type(name = Constants.TYPE_VALUE_QUERYTABLE, value = DataSourceQuery.class) })
@JsonPropertyOrder({ "type" }) // keep the type field first
public interface DataSource
{
}