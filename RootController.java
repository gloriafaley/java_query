package com.query.generation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.commons.logging.Logger;
import com.commons.logging.LoggerFactory;
import com.commons.rest.representations.Api;
import com.commons.rest.representations.Link;
import com.commons.rest.representations.ResourceCollection;
import com.commons.rest.util.LinkBuilder;
import com.commons.rest.util.UriBuilder;

@RestController
@RequestMapping({ "", "/" })
public class RootController
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${server.context-path}")
    private String contextPath;

    // filled by other controllers as they initialize via the @PostConstruct annotation.
    private List<Link> links = null;

    public String getContextPath()
    {
        return contextPath;
    }

    public List<Link> getLinks()
    {
        synchronized( this ) {
            if ( links == null ) {
                links = new ArrayList<Link>();

                URI providersURI = UriBuilder.builder("{contextRoot}").pathVariable("contextRoot", contextPath).create();

                links.add(LinkBuilder.builder(providersURI.toString(), HttpMethod.HEAD, "/").create());

                links.add(LinkBuilder.builder(providersURI.toString(), HttpMethod.GET, "/") //
                          .type(ResourceCollection.MEDIA_TYPE_BASE_VALUE) //
                          .itemType(MediaType.ALL_VALUE) //
                          .create());
            }
        }
        return links;
    }

    @RequestMapping(method = { RequestMethod.GET },
            produces = { MediaType.APPLICATION_JSON_VALUE, Api.MEDIA_TYPE_JSON_VALUE })
    public ResponseEntity<Api> get()
    {
        logger.debug("Responding with API metadata");
        return new ResponseEntity<Api>(new Api(getLinks()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<String> isAlive(HttpServletRequest request)
    {
        logger.debug("Responding server status is OK.");
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
