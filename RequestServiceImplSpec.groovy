package com.query.generation.service

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.commons.rest.exceptions.http.BadRequestException
import com.commons.rest.exceptions.http.NotFoundException;
import com.query.generation.Constants
import com.query.generation.generator.TestUtils;
import com.query.generation.representations.GenerationRequest
import com.query.generation.representations.querymodel.QueryRequest;

import spock.lang.Specification
import spock.lang.Subject

/**
 * @author <a href="mailto:james.kelley@sas.com">James Kelley</a>
 */
@Subject(RequestServiceImpl)
class RequestServiceImplSpec extends Specification {

    String OUTPUT = "\n-- toString --\ndata={" + "a=test-template-print.vm" + "}\n-- traverse \"data\" --\n#/" + "a=test-template-print.vm" + "\n-- eof --\n";

    def "isOk"() {
        given: "an Request service implementation"
        RequestServiceImpl requestService = new RequestServiceImpl()
        requestService.with { catalogService = Mock(CatalogServiceImpl) }

        GenerationRequest generationRequest = TestUtils.fromJson(GenerationRequest.class, "{" //
                +"\"templateCatalog\":null," //
                +"\"document\":{\"a\":\"test-template-print.vm\"},"//
                +"\"templateName\":\"...\"," //
                +"\"templatePath\":\"/a\"" //
                +"}")
        System.out.println(generationRequest)
        
        when: "generate is called"
        Object response = requestService.generate(generationRequest)
        println (response)

        then: "expected return"
        TestUtils.fixCrNlToNL(response.outputText) == OUTPUT
        response.catalog == null
    }

    // error code tests
    
    def "ERRORCODE_xxxxx_VELOCITY_COULD_NOT_FIND_TEMPLATE_NAMED_WITH_ENCODING"() {
        given: "an Request service implementation"
        RequestServiceImpl requestService = new RequestServiceImpl()
        requestService.with { catalogService = Mock(CatalogServiceImpl) }

        QueryRequest queryRequest = TestUtils.fromJson(QueryRequest.class,"{" //
                +"\"templateCatalog\":null," //
                +"\"dataSelection\":{\"a\":\"x\"},"//
                +"\"templateName\":\"test-template-missing.vm\"," //
                +"\"templateEncoding\":\"UTF-XX\""//
                +"}")
            
        when: "generate is called"
        Object response = requestService.generate(queryRequest)
        println (response)

        then:
        NotFoundException e = thrown()
        println(e)
        e.errorCode == Constants.ERRORCODE_12405_TEMPLATE_NAMED_WITH_ENCODING_NOT_FOUND
        e.errorCode == 12405
        e.message == "Could not find template named \"test-template-missing.vm\" to open with encoding \"UTF-XX\"."
    }

    def "ERRORCODE_xxxxx_VELOCITY_COULD_NOT_READ_TEMPLATE_NAMED_WITH_ENCODING"() {
        given: "an Request service implementation"
        RequestServiceImpl requestService = new RequestServiceImpl()
        requestService.with { catalogService = Mock(CatalogServiceImpl) }

        QueryRequest queryRequest = TestUtils.fromJson(QueryRequest.class,"{" //
                +"\"templateCatalog\":null," //
                +"\"dataSelection\":{\"a\":\"x\"},"//
                +"\"templateName\":\"test-template-print.vm\"," //
                +"\"templateEncoding\":\"UTF-XX\""//
                +"}")
            
        when: "generate is called"
        Object response = requestService.generate(queryRequest)
        println (response)

        then:
        BadRequestException e = thrown()
        e.printStackTrace(System.out)
        e.errorCode == Constants.ERRORCODE_12406_COULD_NOT_READ_TEMPLATE_NAMED_WITH_ENCODING
        e.errorCode == 12406
        e.message == "Could not read template named \"test-template-print.vm\" to open with encoding \"UTF-XX\"."
    }
    
//    def "ERRORCODE_xxxxx_TEMPLATE_NAME_PROPERTY_IS_MISSING"() {
//        given: "an Request service implementation"
//        RequestServiceImpl requestService = new RequestServiceImpl()
//        requestService.with { catalogService = Mock(CatalogServiceImpl) }
//
//        QueryRequest queryRequest = TestUtils.fromJson(QueryRequest.class,"{" //
//                +"\"templateCatalog\":null," //
//                +"\"dataSelection\":{}"//
//                //+"\"templateName\":\"\"," //
//                //+"\"templatePath\":null" //
//                +"}")
//            
//        when: "generate is called"
//        Object response = requestService.generate(queryRequest)
//        println (response)
//
//        then: "a NotFoundException will be thrown"
//        NotFoundException e = thrown()
//        e.printStackTrace(System.out)
//        e.errorCode == Constants.ERRORCODE_12413_PROPERTY_OBJECT_IS_NOT_FOUND
//        e.errorCode == 12413
//        e.message == "The \"/templateName\" property was not found."
//    }

    def "ERRORCODE_xxxxx_DATA_SELECTION_PROPERTY_IS_INVALID"() {
    }

//    def "ERRORCODE_xxxxx_DATA_SELECTION_PROPERTY_IS_MISSING"() {
//        given: "an Request service implementation"
//        RequestServiceImpl requestService = new RequestServiceImpl()
//        requestService.with { catalogService = Mock(CatalogServiceImpl) }
//
//        QueryRequest queryRequest = TestUtils.fromJson(QueryRequest.class,"{" //
//                +"\"templateCatalog\":null," //
//                // +"\"dataSelection\":null,"//
//                +"\"templateName\":\"a\"," //
//                +"\"templatePath\":null" //
//                +"}")
//            
//        when: "generate is called"
//        Object response = requestService.generate(queryRequest)
//        println (response)
//
//        then: "a NotFoundException will be thrown"
//        NotFoundException e = thrown()
//        e.printStackTrace(System.out)
//        e.errorCode == Constants.ERRORCODE_12413_PROPERTY_OBJECT_IS_NOT_FOUND
//        e.errorCode == 12413
//        e.message == "The \"/dataSelection\" property was not found."
//    }

    def "ERRORCODE_xxxxx_DATA_SELECTION_PROPERTY_IS_NULL"() {
        given: "an Request service implementation"
        RequestServiceImpl requestService = new RequestServiceImpl()
        requestService.with { catalogService = Mock(CatalogServiceImpl) }

        QueryRequest queryRequest = TestUtils.fromJson(QueryRequest.class,"{" //
                +"\"templateCatalog\":null," //
                +"\"dataSelection\":null,"//
                +"\"templateName\":null," //
                +"\"templatePath\":null" //
                +"}")
            
        when: "generate is called"
        Object response = requestService.generate(queryRequest)
        println (response)

        then: "a BadRequestException will be thrown"
        BadRequestException e = thrown()
        e.printStackTrace(System.out)
        e.errorCode == Constants.ERRORCODE_12414_PROPERTY_OBJECT_IS_NULL
        e.errorCode == 12414
        e.message == "The \"dataSelection\" property value is null."
    }

    def "ERRORCODE_xxxxx_COULD_NOT_PARSE_JSON"() {
        given: "an Request service implementation"
        RequestServiceImpl requestService = new RequestServiceImpl()
        requestService.with { catalogService = Mock(CatalogServiceImpl) }

      //  QueryRequest queryRequest = TestUtils.fromJson(QueryRequest.class,"")
            
        when: "generate is called"
        Object response = requestService.generate((QueryRequest)null)
        println (response)

        then: "a BadRequestException will be thrown"
        BadRequestException e = thrown()
        println(e)
        e.errorCode == Constants.ERRORCODE_12410_COULD_NOT_PARSE_JSON
        e.errorCode == 12410
        e.message == "Could not parse JSON."
    }

    def "ERRORCODE_xxxxx_COULD_NOT_PROCESS_DOCUMENT_AS_JSON"() {
        // from Generation Request document
        given: "an Request service implementation"
        RequestServiceImpl requestService = new RequestServiceImpl()
        requestService.with { catalogService = Mock(CatalogServiceImpl) }

        when: "generate is called"
        def request = new GenerationRequest()
        Object response = requestService.generate(generationRequest)
        println (response)

        then: "a BadRequestException will be thrown"
        BadRequestException e = thrown()
        e.printStackTrace(System.out)
        e.errorCode == Constants.ERRORCODE_12411_COULD_CONVERT_OBJECT_TO_JSON
        e.errorCode == 12411
        e.message == "Error converting \"document\" into JSON."

        where:
        foo = new Object() { public String  toString() { throw new JsonParseException("x") } }
        generationRequest = new GenerationRequest( templateName:"test-template-print.vm",
        templatePath:null, templateCatalogName:null, selectionPath:null,
        selectionDataName:null, document: foo )
    }

}
