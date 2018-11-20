package com.query.generation.representations;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class TemplateDependenceRule extends AbstractComposition implements Serializable
{

    private static final long serialVersionUID = 3479603031600956239L;

    public final static int    TEMPLATE_DEPENDENCE_RULE_VERSION = 1;
    public final static String MEDIA_TYPE_BASE_VALUE         = "application/vnd.sas.generation.template.dependence.rule";
    public final static String MEDIA_TYPE_JSON_VALUE         = MEDIA_TYPE_BASE_VALUE + "+json";
    public final static String MEDIA_TYPE_XML_VALUE          = MEDIA_TYPE_BASE_VALUE + "+xml";

    private int version = TEMPLATE_DEPENDENCE_RULE_VERSION;

    private String       validTemplateType       = "FREEMARKER";               // "VELOCITY"
    private List<String> validResourceSuffixes   = null;                       // "ftl"; // "vm"
    private String       regexpPattern           = "(\\<#[\\w]+)\"([^\"]+)\""; // "(\\#[\\w]+)\"([^\"]+)\"";
    private int          regexpMatchTagGroup     = 1;
    private List<String> matchTags               = null;                       // { "<#include", "<#import" }; //"#include", "#parse"
    private int          regexpResourceNameGroup = 2;

    /**
     * @return the version
     */
    public int getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(int version)
    {
        this.version = version;
    }

    /**
     * @return the matchTags
     */
    public List<String> getMatchTags()
    {
        return matchTags;
    }

    /**
     * @param matchTags the matchTags to set
     */
    public void setMatchTags(List<String> matchTags)
    {
        this.matchTags = matchTags;
    }

    /**
     * @return the regexpMatchTagGroup
     */
    public int getRegexpMatchTagGroup()
    {
        return regexpMatchTagGroup;
    }

    /**
     * @param regexpMatchTagGroup the regexpMatchTagGroup to set
     */
    public void setRegexpMatchTagGroup(int regexpMatchTagGroup)
    {
        this.regexpMatchTagGroup = regexpMatchTagGroup;
    }

    /**
     * @return the regexpPattern
     */
    public String getRegexpPattern()
    {
        return regexpPattern;
    }

    /**
     * @param regexpPattern the regexpPattern to set
     */
    public void setRegexpPattern(String regexpPattern)
    {
        this.regexpPattern = regexpPattern;
    }

    /**
     * @return the regexpResourceNameGroup
     */
    public int getRegexpResourceNameGroup()
    {
        return regexpResourceNameGroup;
    }

    /**
     * @param regexpResourceNameGroup the regexpResourceNameGroup to set
     */
    public void setRegexpResourceNameGroup(int regexpResourceNameGroup)
    {
        this.regexpResourceNameGroup = regexpResourceNameGroup;
    }

    /**
     * @return the validResourceSuffixes
     */
    public List<String> getValidResourceSuffixes()
    {
        return validResourceSuffixes;
    }

    /**
     * @param validResourceSuffixes the validResourceSuffixes to set
     */
    public void setValidResourceSuffixes(List<String> validResourceSuffixes)
    {
        this.validResourceSuffixes = validResourceSuffixes;
    }

    /**
     * @return the validTemplateType
     */
    public String getValidTemplateType()
    {
        return validTemplateType;
    }

    /**
     * @param validTemplateType the validTemplateType to set
     */
    public void setValidTemplateType(String validTemplateType)
    {
        this.validTemplateType = validTemplateType;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    { /*I18NOK:BLK*/
        return "TemplateDependenceRule [getVersion()=" + getVersion() + ", getValidTemplateType()="
                + getValidTemplateType() + ", getValidResourceSuffixes()=" + getValidResourceSuffixes()
                + ", getRegexpPattern()=" + getRegexpPattern() + ", getRegexpMatchTagGroup()="
                + getRegexpMatchTagGroup() + ", getMatchTags()=" + getMatchTags() + ", getRegexpResourceNameGroup()="
                + getRegexpResourceNameGroup() + ", getId()=" + getId() + ", getName()=" + getName()
                + ", getDescription()=" + getDescription() + ", getType()=" + getType() + "]";
    }

}