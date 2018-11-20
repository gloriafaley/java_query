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
@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type", defaultImpl = DataSourceJoin.class)
@JsonPropertyOrder({ "type" }) // keep the type field first
public class DataSourceJoin implements DataSource
{
    public enum DataSourceJoinRepType
    {
        JOIN
    }

    /* typing information for possible polymorphism */
    @JsonProperty(required = true, defaultValue = Constants.TYPE_VALUE_JOIN)
    public DataSourceJoin.DataSourceJoinRepType getType()
    {
        return DataSourceJoinRepType.JOIN;
    }

    @JsonProperty(required = true, defaultValue = "INNER_JOIN")
    public JoinType   joinType = JoinType.INNER_JOIN;
    @JsonProperty(required = true)
    public DataSource left;
    @JsonProperty(required = true)
    public DataSource right;
    @JsonProperty(required = true)
    public Filter     onCondition;

    /**
     * @return the joinType
     */
    public JoinType getJoinType()
    {
        return joinType;
    }

    /**
     * @param joinType the joinType to set
     */
    public void setJoinType(JoinType joinType)
    {
        this.joinType = joinType;
    }

    /**
     * @return the left
     */
    public DataSource getLeft()
    {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(DataSource left)
    {
        this.left = left;
    }

    /**
     * @return the right
     */
    public DataSource getRight()
    {
        return right;
    }

    /**
     * @param right the right to set
     */
    public void setRight(DataSource right)
    {
        this.right = right;
    }

    /**
     * @return the onCondition
     */
    public Filter getOnCondition()
    {
        return onCondition;
    }

    /**
     * @param onCondition the onCondition to set
     */
    public void setOnCondition(Filter onCondition)
    {
        this.onCondition = onCondition;
    }

}