package com.query.generation;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.commons.logging.Logger;
import com.commons.logging.LoggerFactory;
import com.commons.rest.collections.CollectionLinksBuilder;
import com.commons.rest.collections.CollectionLinksFactory;
import com.commons.rest.collections.query.QueryOptions;
import com.commons.rest.exceptions.http.BadRequestException;
import com.commons.rest.exceptions.http.ServerErrorException;
import com.commons.rest.representations.Link;
import com.commons.rest.util.LinkBuilder;
import com.commons.rest.util.UriBuilder;
import com.query.generation.exception.GenerationException;
import com.query.generation.representations.Catalog;
import com.query.generation.representations.GenerationRequest;
import com.query.generation.representations.GenerationResponse;
import com.query.generation.representations.querymodel.QueryRequest;
import com.query.generation.service.ExceptionUtil;
import com.query.generation.service.RequestService;
import com.query.generation.support.L10nMessages;
import com.query.generation.support.LogMessages;
import com.query.generation.support.RB;

@RestController()
@RequestMapping(value = Constants.REQUEST_BASE_URI)
public class RequestController
{
    private static Logger log = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private RootController rootController;

    public RequestController() {
    }

    // ---- publishing links ----

    private Link _requestGetLink;

    @PostConstruct
    public void publishLinks()
    {
        URI uri = UriBuilder.builder("{contextRoot}" + Constants.REQUEST_BASE_URI) // contextRoot is from yaml property
                            .pathVariable("contextRoot", rootController.getContextPath()).create();

        log.info(LogMessages.INFO_MSG_URI_FMT_LOG);

        _requestGetLink = LinkBuilder.builder(uri.toString(), HttpMethod.POST, Constants.REQUEST) //
                                      .type(GenerationRequest.ACCEPT_TYPE) //
                                      .responseType(MediaType.APPLICATION_JSON_VALUE) //
                                      .title(RB.getMessage(L10nMessages.GENERATIONREQUEST_TITLE_TXT)) //
                                      .create();
        rootController.getLinks().add(_requestGetLink);

        try
        {
            // load dependency failure in spring caused if RB referenced here at times
            _requestGetLink.setTitle(RB.getMessage(L10nMessages.GENERATIONREQUEST_TITLE_TXT));
        }
        catch (Throwable e)
        {
            // ignore
        }
    }

    /**
     * POST to generate using the JSON for a com.query.generation.representations.GenerationRequest representational POJO object.
     *
     * @param request the {@link HttpServletRequest}
     * @param representation the {@link com.query.generation.representations.GenerationRequest} representation object
     * @param locale the {@link Locale} to be used.
     * @return the GenerationResponses in a collection wrapped in a {@link ResponseEntity}
     * @throws BadRequestException failure due to invalid information in the request
     * @throws ServerErrorException failure due to a server side problem
     */

    @RequestMapping(method = RequestMethod.POST, //
            consumes = { GenerationRequest.ACCEPT_TYPE_JSON }, //
    produces = { MediaType.APPLICATION_JSON_VALUE, GenerationResponse.ACCEPT_TYPE_JSON }) //
    public ResponseEntity<GenerationResponse> request(HttpServletRequest request,
            @RequestBody GenerationRequest representation, Locale locale)
                                                                      throws ServerErrorException, BadRequestException
    {
        if (log.isDebugEnabled())
        {
            log.debug(ExceptionUtil.from(request) + " - generate using a GenerationRequest representation request.");
            log.debug("representation=[" + representation + "]");
        }

        // delegate all the validation and processing to the service.
        GenerationResponse result = null;
        try
        {
            result = requestService.generate(representation);
        }
        catch (GenerationException e)
        {
            // this generation exception that is able to be translated into a server error exception
            int errorCode = e.getErrorCode() != 0 ? e.getErrorCode() : Constants.ERRORCODE_12400_GENERATION_FAILED;
            throw ExceptionUtil.newServerErrorException(errorCode, e, L10nMessages.ERR_MSG_GENERATION_FAILED_TXT);
        }

        if (log.isDebugEnabled())
        {
            log.debug("result=" + (result == null ? null : ("[" + result + "]")));
        }
        if (result == null)
        {
            throw ExceptionUtil.newServerErrorException(Constants.ERRORCODE_12401_GENERATE_RETURNED_NULL,
                                                        L10nMessages.ERR_MSG_GENERATE_RETURNED_NULL_TXT);
        }

        // setup for the response:
        String linkRequestUri = "{contextRoot}" + Constants.REQUEST_BASE_URI;
        String linkRelName = "generationRequest";
        String linkAcceptTypeJson = GenerationRequest.ACCEPT_TYPE_JSON;
        String linkApplicationJsonValue = MediaType.APPLICATION_JSON_VALUE;
        String responseName = RB.getMessage(L10nMessages.GENERATION_REQUEST_RESPONSE_TXT);

        // ResponseEntity<String> responseEntity = newSimpleResponseEntity(result);
        ResponseEntity<GenerationResponse> responseEntity = newResponseEntity(linkRequestUri, linkRelName,
                linkAcceptTypeJson, linkApplicationJsonValue, responseName, result);

        return responseEntity;
    }

    /**
     * POST to generate using the JSON for a com.query.generation.representations.QueryRequest representational POJO object.
     *
     * @param request the {@link HttpServletRequest}
     * @param representation the {@link com.query.generation.representations.querymodel.QueryRequest} representation object
     * @param locale the {@link Locale} to be used.
     * @return the GenerationResponses in a collection wrapped in a {@link ResponseEntity}
     * @throws BadRequestException failure due to invalid information in the request
     * @throws ServerErrorException failure due to a server side problem
     */

    @RequestMapping(method = RequestMethod.POST, //
            consumes = { QueryRequest.ACCEPT_TYPE_JSON }, //
    produces = { MediaType.APPLICATION_JSON_VALUE, GenerationResponse.ACCEPT_TYPE_JSON }) //
    public ResponseEntity<GenerationResponse> request(HttpServletRequest request,
            @RequestBody QueryRequest representation, Locale locale) throws ServerErrorException, BadRequestException
    {
        if (log.isDebugEnabled())
        {
            log.debug(ExceptionUtil.from(request)
                      + " - generate using a generation QueryRequest representation request.");
            log.debug("representation=[" + representation + "]");
        }

        // delegate all the validation and processing to the service.
        GenerationResponse result = null;
        try
        {
            result = requestService.generate(representation);
        }
        catch (GenerationException e)
        {
            // this generation exception that is able to be translated into a server error exception
            int errorCode = e.getErrorCode() != 0 ? e.getErrorCode() : Constants.ERRORCODE_12400_GENERATION_FAILED;
            throw ExceptionUtil.newServerErrorException(errorCode, e, L10nMessages.ERR_MSG_GENERATION_FAILED_TXT);
        }

        if (log.isDebugEnabled())
        {
            log.debug("result=" + (result == null ? null : ("[" + result + "]")));
        }
        if (result == null)
        {
            throw ExceptionUtil.newServerErrorException(Constants.ERRORCODE_12401_GENERATE_RETURNED_NULL,
                                                        L10nMessages.ERR_MSG_GENERATE_RETURNED_NULL_TXT);
        }

        // setup for the response:
        String linkRequestUri = "{contextRoot}" + Constants.REQUEST_BASE_URI;
        String linkRelName = "queryRequest";
        String linkAcceptTypeJson = QueryRequest.ACCEPT_TYPE_JSON;
        String linkApplicationJsonValue = MediaType.APPLICATION_JSON_VALUE;
        String responseName = RB.getMessage(L10nMessages.QUERY_REQUEST_RESPONSE_TXT);

        ResponseEntity<GenerationResponse> responseEntity = newResponseEntity(linkRequestUri, linkRelName,
                linkAcceptTypeJson, linkApplicationJsonValue, responseName, result);

        return responseEntity;
    }

    private ResponseEntity<GenerationResponse> newResponseEntity(String linkRequestUri, String linkRelName,
            String linkAcceptTypeJson, String linkApplicationJsonValue, String responseName, GenerationResponse result)
    {
        // no options, yet.  leave it empty
        QueryOptions options = QueryOptions.builder().create();

        // start the links with the request base uri as the path
        String generationRequestPath = UriBuilder.builder(linkRequestUri) // contextRoot is from yaml property
                .pathVariable("contextRoot", rootController.getContextPath()).create().getPath();
        List<Link> links = CollectionLinksBuilder.newInstance(generationRequestPath, options).buildLinks();
        Link requestLink = CollectionLinksFactory.create(generationRequestPath, linkRelName, linkAcceptTypeJson,
                linkApplicationJsonValue, HttpMethod.POST);
        links.add(requestLink);

        if (result.getCatalog() != null)
        {

            String generationCatalogPath = UriBuilder
                    .builder("{contextRoot}" + Constants.CATALOGS_BASE_URI + Constants.ENDPOINT_ID) // contextRoot is from yaml property
                                                     .pathVariable("contextRoot", rootController.getContextPath())
                    .pathVariable(Constants.ID, result.getCatalog().getId()).create().getPath();
            Link catalogLink = CollectionLinksFactory.create(generationCatalogPath, "generationCatalog",
                    Catalog.MEDIA_TYPE_JSON_VALUE, linkApplicationJsonValue, HttpMethod.GET);
            catalogLink.setTitle(Catalog.class.getSimpleName());
            links.add(catalogLink);
        }

        if ( result.getLinks() != null ) {
            links.addAll(result.getLinks());
        }
        result.setLinks(links);
        result.setName(responseName);

        ResponseEntity<GenerationResponse> responseEntity = new ResponseEntity<GenerationResponse>(result, HttpStatus.OK);
        return responseEntity;
    }
}
