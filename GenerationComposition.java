package com.query.generation.representations;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GenerationComposition extends AbstractComposition implements Serializable
{
    private static final long serialVersionUID = -2942844722479609198L;

    public final static int    GENERATION_COMPOSITION_VERSION = 1;
    public final static String MEDIA_TYPE_BASE_VALUE          = "application/vnd.generation.composition";
    public final static String MEDIA_TYPE_JSON_VALUE          = MEDIA_TYPE_BASE_VALUE + "+json";
    public final static String MEDIA_TYPE_XML_VALUE           = MEDIA_TYPE_BASE_VALUE + "+xml";

    private int version = GENERATION_COMPOSITION_VERSION;

    List<GeneratorDefinition> generatorDefinitions;
    List<TemplateDefinition> templateDefinitions;
    List<TemplateDependenceRule> templateDependenceRules;

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
     * @return the generatorDefinitions
     */
    public List<GeneratorDefinition> getGeneratorDefinitions()
    {
        return generatorDefinitions;
    }


    /**
     * @param generatorDefinitions the generatorDefinitions to set
     */
    public void setGeneratorDefinitions(List<GeneratorDefinition> generatorDefinitions)
    {
        this.generatorDefinitions = generatorDefinitions;
    }


    /**
     * @return the templateDefinitions
     */
    public List<TemplateDefinition> getTemplateDefinitions()
    {
        return templateDefinitions;
    }


    /**
     * @param templateDefinitions the templateDefinitions to set
     */
    public void setTemplateDefinitions(List<TemplateDefinition> templateDefinitions)
    {
        this.templateDefinitions = templateDefinitions;
    }

    /**
     * @return the templateDependenceRule
     */
    public List<TemplateDependenceRule> getTemplateDependenceRules()
    {
        return templateDependenceRules;
    }


    /**
     * @param templateDependenceRule the templateDependenceRule to set
     */
    public void setTemplateDependenceRules(List<TemplateDependenceRule> templateDependenceRule)
    {
        this.templateDependenceRules = templateDependenceRule;
    }


}