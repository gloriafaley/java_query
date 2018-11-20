package com.query.generation.representations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
abstract class AbstractComposition
{
    /**
     * The object identifier.
     */
    private String id;

    /**
     * The object name.
     */
    private String name;

    /**
     * The object description.
     */
    private String description;

    /**
     * The type name for this object.
     */
    private String type;

    /**
     * @return the version
     */
    abstract public int getVersion();

    /**
     * @param version the version to set
     */
    abstract public void setVersion(int version);

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
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

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }
}
