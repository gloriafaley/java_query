package com.query.generation.factory;

import java.util.function.BiFunction;

import com.commons.logging.Logger;
import com.commons.logging.LoggerFactory;
import com.query.generation.model.RequestEntity;
import com.query.generation.representations.querymodel.QueryRequest;


public class QueryRequestToRequestEntityTranslator //
extends AbstractRequestEntityTranslator<QueryRequest> //
        implements BiFunction<QueryRequest, RequestEntity, RequestEntity>
{
    private static final String PROPERTY_NAME_DATA_SELECTION = "dataSelection";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /* (non-Javadoc)
     * @see java.util.function.Function#apply(java.lang.Object)
     */
    @Override
    public RequestEntity apply(QueryRequest representation, RequestEntity model)
    {
        logger.debug("Translating '" + representation + "' to its corresponding service model.");
        return super.apply(representation, model);
    }

    protected String getRootDocumentPropertyName()
    {
        return PROPERTY_NAME_DATA_SELECTION;
    }

    protected Object getRootDocument(QueryRequest representation)
    {
        return representation.getDataSelection();
    }
}
