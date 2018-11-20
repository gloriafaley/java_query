package com.query.generation.factory

import spock.lang.Specification
import spock.lang.Subject

import com.query.generation.factory.CatalogEntityToCatalogTranslator
import com.query.generation.model.CatalogEntity
import com.query.generation.representations.Catalog;;;


@Subject(CatalogEntityToCatalogTranslator)
class CatalogTranslatorSpec extends Specification {
    
    def "convert valid connection from jpa to dto using translator"() {
        given: "a CatalogEntityToCatalogTranslator translator"
        CatalogEntityToCatalogTranslator translator = new CatalogEntityToCatalogTranslator()
        
        when: "a catalog is converted"
        CatalogEntity catalogEntity = new CatalogEntity(id: id, name: name, description: description,
            creationTimeStamp: creationTimeStamp, modifiedTimeStamp: modifiedTimeStamp, createdBy: createdBy,
            modifiedBy: modifiedBy, content: content)
        Catalog catalog = translator.apply(catalogEntity)
        
        then: "validate the conversion"
        catalog.getId() == catalogEntity.getId()
        catalog.getName() == catalogEntity.getName()
        catalog.getDescription() == catalogEntity.getDescription()
        catalog.getCreationTimeStamp() == catalogEntity.getCreationTimeStamp()
        catalog.getModifiedTimeStamp() == catalogEntity.getModifiedTimeStamp()
        catalog.getCreatedBy() == catalogEntity.getCreatedBy()
        catalog.getModifiedBy() == catalogEntity.getModifiedBy()
        catalog.getContent() == catalogEntity.getContent()
                
        where:
        id = "123"
        name = "catalogName1"
        description = "catalogDescription1"
        creationTimeStamp = new Date(0)
        modifiedTimeStamp = new Date()
        createdBy = "James Kirk"
        modifiedBy = "Spock"
        content = UUID.randomUUID().toString()
    }
        
    def "check null passed into toDTO"() {
        given: "a CatalogEntityToCatalogTranslator translator"
        CatalogEntityToCatalogTranslator translator = new CatalogEntityToCatalogTranslator()
        
        when: "apply is called"
        translator.apply(catalogJPA)
        
        then: "a NullPointerException should be thrown"
        thrown NullPointerException
        
        where: "The connection is null"
        catalogJPA = (CatalogEntity) null
    }
 
}
