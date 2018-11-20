package com.query.generation.representations;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.commons.rest.representations.util.DateDeserializer;
import com.commons.rest.representations.util.DateSerializer;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect()
public class Catalog extends CatalogSummary
{

    public static final String MEDIA_TYPE_BASE_VALUE = "application/vnd.catalog";
    public static final String MEDIA_TYPE_JSON_VALUE = MEDIA_TYPE_BASE_VALUE + "+json";

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date creationTimeStamp;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date modifiedTimeStamp;

    private String createdBy;

    private String modifiedBy;

    private String content;

    public Date getCreationTimeStamp()
    {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(Date creationTimeStamp)
    {
        this.creationTimeStamp = creationTimeStamp;
    }

    public Date getModifiedTimeStamp()
    {
        return modifiedTimeStamp;
    }

    public void setModifiedTimeStamp(Date modifiedTimeStamp)
    {
        this.modifiedTimeStamp = modifiedTimeStamp;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String _content)
    {
        this.content = _content;
    }

    @Override
    public String toString()
    {
        return "Catalog [id=" + id + ", name=" + name + ", description=" + description + "]";
    }

}
