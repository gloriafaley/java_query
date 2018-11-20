package com.query.generation;


public final class Constants
{
    public static final String REQUEST          = "request";
    public static final String REQUEST_BASE_URI = "/" + REQUEST;

    public static final String CATALOGS          = "catalogs";
    public static final String CATALOGS_BASE_URI = "/" + CATALOGS;

    public static final String REQUEST_RELAY          = REQUEST + "/" + "relay";
    public static final String REQUEST_RELAY_BASE_URI = "/" + REQUEST_RELAY;

    public static final String ID          = "{id}";
    public static final String ENDPOINT_ID = "/" + ID;

    public static final int[][] ERRORCODE_RANGES_INCLUSIVE = { { 12400, 12499 } };

    // the following error codes are reflected in external documents, do not alter their id number once assigned.

    /** Generation request failed. **/
    public static final int ERRORCODE_12400_GENERATION_FAILED      = 12400;
    /** Generation returned a null result. **/
    public static final int ERRORCODE_12401_GENERATE_RETURNED_NULL = 12401;

    /** The generation engine returned an error. **/
    public static final int ERRORCODE_12402_GENERATION_FAILED_DUE_TO_EXCEPTION                     = 12402;
    /** Could not find template named '[template name]'. **/
    public static final int ERRORCODE_12403_TEMPLATE_NOT_FOUND                          = 12403;
    /** Could not read template named '[template name]'. **/
    public static final int ERRORCODE_12404_COULD_NOT_READ_TEMPLATE_NAMED               = 12404;
    /** Could not find template named '[template name]' to open with encoding '[encoding charset]'."; **/
    public static final int ERRORCODE_12405_TEMPLATE_NAMED_WITH_ENCODING_NOT_FOUND      = 12405;
    /** Could not read template named '[template name]' to open with encoding '[encoding charset]'."; **/
    public static final int ERRORCODE_12406_COULD_NOT_READ_TEMPLATE_NAMED_WITH_ENCODING = 12406;
    /** "Unknown type of template file '${templateName}'."; **/
    public static final int ERRORCODE_12407_UNKNOWN_TYPE_OF_TEMPLATE                    = 12407;

    /** Could not parse json.  **/
    public static final int ERRORCODE_12410_COULD_NOT_PARSE_JSON            = 12410;
    /** Could not parse json.  **/
    public static final int ERRORCODE_12411_COULD_CONVERT_OBJECT_TO_JSON    = 12411;
    /** The named property is invalid. **/
    public static final int ERRORCODE_12412_PROPERTY_OBJECT_IS_INVALID      = 12412;
    /** The named property is missing. **/
    public static final int ERRORCODE_12413_PROPERTY_OBJECT_IS_NOT_FOUND    = 12413;
    /** the named property is null. **/
    public static final int ERRORCODE_12414_PROPERTY_OBJECT_IS_NULL         = 12414;
    /** The named property value is empty. **/
    public static final int ERRORCODE_12415_PROPERTY_VALUE_IS_EMPTY         = 12415;
    /** An object with an id of "{0}" was not found. **/
    public static final int ERRORCODE_12416_PROPERTY_OBJECT_ID_IS_NOT_FOUND = 12416;

    /** A catalog id must be specified to update the catalog. **/
    public static final int ERRORCODE_12420_UPDATE_REQUIRES_THE_CATALOG_ID = 12420;
    /** A catalog id cannot be specified during catalog creation. **/
    public static final int ERRORCODE_12421_CANNOT_SPECIFY_THE_CATALOG_ID  = 12421;
    /** No catalog was provided in the body of the request. **/
    public static final int ERRORCODE_12422_NO_CATALOG_BODY_GIVEN          = 12422;
    /** Cannot update catalog because the id on the path '[id1]' does not match the id in the body of the request '[id2]'. **/
    public static final int ERRORCODE_12423_CANNOT_UPDATE_CATALOG_ID       = 12423;
    /** A catalog with id ''{0}'' was not found. **/
    public static final int ERRORCODE_12424_CATALOG_ID_NOT_FOUND           = 12424;

    /** The generation composition file failed to load. **/
    public static final int ERRORCODE_12430_GENERATION_COMPOSITION_FAILED_TO_LOAD = 12430;

    private Constants()
    {
    } //Do not instantiate

}