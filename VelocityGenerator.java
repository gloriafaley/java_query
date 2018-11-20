package com.query.generation.generator;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.ResourceManagerImpl;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;
import org.apache.velocity.tools.generic.DateTool;

import com.commons.logging.Logger;
import com.commons.logging.LoggerFactory;
import com.commons.rest.exceptions.ResourceException;
import com.query.generation.Constants;
import com.query.generation.model.RequestEntity;
import com.query.generation.representations.Catalog;
import com.query.generation.representations.GenerationResponse;
import com.query.generation.service.ExceptionUtil;
import com.query.generation.service.RequestService;
import com.query.generation.support.L10nMessages;
import com.query.generation.support.LogMessages;

public class VelocityGenerator extends AbstractGenerator
{
    private static final Logger log = LoggerFactory.getLogger(VelocityGenerator.class);

    public void processTemplate(RequestService requestService, GenerationResponse response, RequestEntity requestEntity)
    {
        if (log.isDebugEnabled())
        {
            log.debug("processVelocityTemplate(...) - starting generation.");
        }

        String selectionDataName = requestEntity.getSelectionDataName();
        Map<String,String> generatorProperties = requestEntity.getGeneratorProperties();
        Map<String, Object> idMappings = requestEntity.getIdMappings();
        String templateName = requestEntity.getTemplateName();
        String templateEncoding = requestEntity.getTemplateEncoding();
        Object content = requestEntity.getContent();
        Catalog catalog = requestEntity.getCatalog();

        // For auto-generating dateTime in the Header portion of the templates
        DateTool dateTool = new DateTool();

        Map<String, Object> dictionary = new LinkedHashMap<String, Object>();

        if (selectionDataName == null)
        {
            dictionary.put(DICTIONARY_NAME_DATANAME_DEFAULT, content);
        }
        else if (selectionDataName.trim().isEmpty())
        {
            writeUnwrappedFields(dictionary, content);
            dictionary.put(DICTIONARY_NAME_DATANAME_WHEN_UNWRAPPED, content);
        }
        else
        {
            dictionary.put(selectionDataName, content);
        }

        dictionary.put(DICTIONARY_NAME_GENERATOR_PROPERTIES, generatorProperties);
        dictionary.put(DICTIONARY_NAME_OBJECT_IDS, idMappings);
        dictionary.put(DICTIONARY_NAME_ENCODING, templateEncoding);
        dictionary.put(DICTIONARY_NAME_REQUEST_SERVICE_OBJECT, requestService);
        dictionary.put(DICTIONARY_NAME_DATE_TOOL, dateTool);
        dictionary.put("generatorDefinition", generatorDefinition);

        /* setup */
        /*  first, get and initialize an engine  */
        VelocityEngine velocityEngine = null;
        if (generatorProperties == null) {
            velocityEngine = new VelocityEngine();
        } else {
            Properties properties = new Properties();
            properties.putAll(generatorProperties);
            velocityEngine = new VelocityEngine(properties);
        }

        try
        {
            // log4j logging:
            //        ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this);
            //        ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
            //        ve.setProperty("runtime.log.logsystem.log4j.logger", log.getName());

            // LogChute to this class:
            if (generatorProperties == null || !generatorProperties.containsKey(VelocityEngine.RUNTIME_LOG_LOGSYSTEM))
            {
                velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, new LogChute() {
                    @Override
                    public void init(RuntimeServices rs) throws Exception
                    {
                        //System.out.println(this.getClass().getName() + ":rs = " + rs);
                        log.debug("runtime services=" + rs);
                    }

                    @Override
                    public void log(int level, String message)
                    {
                        // System.out.println(this.getClass().getName() + ": log level = " + level + " \"" + message + "\"");
                        switch (level) {
                            case TRACE_ID:
                                log.trace(message);
                                break;
                            case DEBUG_ID:
                                log.debug(message);
                                break;
                            case INFO_ID:
                                log.info(message);
                                break;
                            case WARN_ID:
                                log.warn(message);
                                break;
                            case ERROR_ID:
                                log.error(message);
                                break;
                            default:
                                log.warn(message);
                                break;
                        }
                    }

                    @Override
                    public void log(int level, String message, Throwable t)
                    {
                        //System.out.println(this.getClass().getName() + ": log level = " + level + " \"" + message + "\"");
                        // t.printStackTrace();
                        switch (level) {
                            case TRACE_ID:
                                log.trace(message, t);
                                break;
                            case DEBUG_ID:
                                log.debug(message, t);
                                break;
                            case INFO_ID:
                                log.info(message, t);
                                break;
                            case WARN_ID:
                                log.warn(message, t);
                                break;
                            case ERROR_ID:
                                log.error(message, t);
                                break;
                            default:
                                log.warn(message, t);
                                break;
                        }
                    }

                    @Override
                    public boolean isLevelEnabled(int level)
                    {
                        switch (level) {
                            case TRACE_ID:
                                return log.isTraceEnabled();
                            case DEBUG_ID:
                                return log.isDebugEnabled();
                            case INFO_ID:
                                return log.isInfoEnabled();
                            case WARN_ID:
                                return log.isWarnEnabled();
                            case ERROR_ID:
                                return log.isErrorEnabled();
                            default:
                                return log.isWarnEnabled();
                        }
                    }
                });
            }

            /// Add our own ResourceLoader that hooks into the Folder Service.
            /// see https://velocity.apache.org/engine/releases/velocity-1.5/developer-guide.html#resourceloaders
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_MANAGER_CLASS,
                    VelocityGenerator.FolderServiceResourceManager.class.getName());
            // To run from Eclipse using the file system or in Petrichor from a jar
            // - specifying both the file resource and classpath resource loaders.
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file,classpath");

            velocityEngine.setProperty("file.resource.loader.class", FileResourceLoader.class.getName());
            velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

            velocityEngine.init();
        }
        catch (ResourceException e)
        {
            log.error(LogMessages.ERR_MSG_GENERATOR_EXCEPTION_THROWN_LOG, e);
            throw e;
        }
        catch (Throwable e)
        {
            log.error(LogMessages.ERR_MSG_GENERATOR_EXCEPTION_THROWN_LOG, e);
            throw ExceptionUtil.newServerErrorException(Constants.ERRORCODE_12402_GENERATION_FAILED_DUE_TO_EXCEPTION, e,
                    L10nMessages.ERR_MSG_GENERATION_FAILED_DUE_TO_EXCEPTION_TXT);
        }

        /*  next, get the Template  */
        org.apache.velocity.Template template;
        if (templateEncoding == null)
        {
            try
            {
                template = velocityEngine.getTemplate(templateName);
            }
            catch (ResourceNotFoundException e2)
            {
                throw ExceptionUtil.newNotFoundException(Constants.ERRORCODE_12403_TEMPLATE_NOT_FOUND, e2,
                        L10nMessages.ERR_MSG_GENERATION_COULD_NOT_FIND_TEMPLATE_NAMED_ARG0_FMT_TXT, templateName);
            }
            catch (ParseErrorException e3)
            {
                throw ExceptionUtil.newBadRequestException(Constants.ERRORCODE_12404_COULD_NOT_READ_TEMPLATE_NAMED, e3,
                        L10nMessages.ERR_MSG_GENERATION_COULD_NOT_READ_TEMPLATE_NAMED_ARG0_FMT_TXT, templateName);
            }
        }
        else
        {
            try
            {
                template = velocityEngine.getTemplate(templateName, templateEncoding);
            }
            catch (ResourceNotFoundException e4)
            {
                throw ExceptionUtil.newNotFoundException(
                        Constants.ERRORCODE_12405_TEMPLATE_NAMED_WITH_ENCODING_NOT_FOUND, e4,
                        L10nMessages.ERR_MSG_GENERATION_COULD_NOT_FIND_TEMPLATE_NAMED_ARG0_TO_OPEN_WITH_ENCODING_ARG1_FMT_TXT,
                        templateName, templateEncoding);
            }
            catch (ParseErrorException e1)
            {
                throw ExceptionUtil.newBadRequestException(
                        Constants.ERRORCODE_12406_COULD_NOT_READ_TEMPLATE_NAMED_WITH_ENCODING, e1,
                        L10nMessages.ERR_MSG_GENERATION_COULD_NOT_READ_TEMPLATE_NAMED_ARG0_TO_OPEN_WITH_ENCODING_ARG1_FMT_TXT,
                        templateName, templateEncoding);
            }
        }
        // TODO: catalog should be used here to store or retrieve the resource

        /* Build the data-model */
        VelocityContext context = new VelocityContext();
        if (!dictionary.isEmpty())
        {
            Set<Entry<String, Object>> entrySet = dictionary.entrySet();
            for (Entry<String, Object> entry : entrySet)
            {
                context.put(entry.getKey(), entry.getValue());
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("velocityEngine : " + velocityEngine);
            log.debug(" template : " + template);
            log.debug(" context : " + context);
        }

        StringWriter writer = new StringWriter();
        try
        {
            /* now render the template into a StringWriter */
            template.merge(context, writer);

            if (log.isDebugEnabled())
            {
                log.debug(" output: " + writer.toString());
            }
        }
        catch (ResourceException e)
        {
            log.error(LogMessages.ERR_MSG_GENERATOR_EXCEPTION_THROWN_LOG, e);
            throw e;
        }
        catch (Throwable e)
        {
            log.error(LogMessages.ERR_MSG_GENERATOR_EXCEPTION_THROWN_LOG, e);
            throw ExceptionUtil.newServerErrorException(Constants.ERRORCODE_12402_GENERATION_FAILED_DUE_TO_EXCEPTION, e,
                    L10nMessages.ERR_MSG_GENERATION_FAILED_DUE_TO_EXCEPTION_TXT);
        }
        String output = writer.toString();

        if (log.isDebugEnabled())
        {
            log.debug("processVelocityTemplate(...) - returning generated text.");
        }

        response.setCatalog(catalog);
        response.setOutputText(output);
    }

    static public final class FolderServiceResourceManager
            implements org.apache.velocity.runtime.resource.ResourceManager
    {
        ResourceManagerImpl delegate = new ResourceManagerImpl();

        @Override
        public void initialize(RuntimeServices rs)
        {
            delegate.initialize(rs);
        }

        @Override
        public Resource getResource(String resourceName, int resourceType, String encoding)
                throws ResourceNotFoundException, ParseErrorException
        {
            if (log.isDebugEnabled())
            {
                log.debug("ourRM:getResource(" + (resourceName == null ? null : ("\"" + resourceName + "\"")) + ","
                        + resourceType + "," + (encoding == null ? null : ("\"" + encoding + "\"")) + ")");
            }
            // TODO: look up the resource in the Folder Service if it starts with the right scheme.
            //       If it doesn't, look in our cache of default Folder Service entries for it's default Folder Service URL.
            // TODO: catalog should be used here to store or retrieve the resource
            return delegate.getResource(resourceName, resourceType, encoding);
        }

        @Override
        public String getLoaderNameForResource(String resourceName)
        {
            if (log.isDebugEnabled())
            {
                log.debug("getLoaderNameForResource(" + (resourceName == null ? null : ("\"" + resourceName + "\""))
                        + ")");
            }
            return delegate.getLoaderNameForResource(resourceName);
        }
    }
}