# Query Generation Service


Query Services  is a Java-based Spring enabled component that in the rapid development of clients and applications software. Every client or application needs access to data, and CQS is a suite of services that allows the client to discover and meta-information about your data, build a specific data transformation and generate "code", e.g. queries, filters, joins, actions, etc. that can be executed on variety of platforms and using a variety of data sources.  

Using other microservices such as the Transformation service, the query generation service will allow a client to build sub-transformation that fit within a single transformation that can be part of a large recipe for execution on any platform.  These transformation definition is based on data reference points provided from the Data Sources and Data Tables Microservice and a transformation road map or scheme that is used in both the generation of code and UI input/output definitions.

### Terms

#### Data Transformation:
An instance of a data transformation is defined as: A set of one or more operations upon data that are performed as an "atomic" operation.

#### Sub-transformation
An instance of a data sub-transformation that cannot exist independently but can exist as an individual "atomic" operation within a transformation.

#### Recipe or "Recipe Instance":
An instance of a recipe is defined as: The set of data transformations and the data mapping between the physical data reference and the internal logical data item references used within the transformation modeling.

#### Generation:
The creation of execution code for a target language or generation of metadata for system execution for a given "target" platform.

#### Schema:
Describes your existing data format in clear, human- and machine-readable documentation and provides complete structural validation.

#### JSON Hyper-Schema:
Describes your existing API (with no new structures required) with links (including URI Templates for target URIs) and forms (specify a JSON Schema for the desired data).

#### Sub-Schema or Nested Schema
Where we can refer to a schema snippet from elsewhere in the schema using the $ref keyword. The easiest way to describe $ref is that it gets logically replaced with the thing that it points to.


### Representations Jar:

Threpresentations package contains the representation classes that are applicable to the query generation service.  These classes are used for 
serialization/deserialization of JSON and exist within a separate jar such that they are completely de-coupled from the underlying service implementation.  

Note that these classes are separate from the model (or domain) classes even though, at least initially, they are
almost identical.  This is done to allow the model to evolve and change without breaking any consumers.  As long as a 
translation layer (in this case, the ModelToRepFactory class) can convert representation objects into model objects,
the service can (and must) handle back-level clients sending older versions of the representation objects.

This separation between implementation and representations can be seen by looking at the project structure itself.  Two subprojects are used (representations and service) such that each will publish their own jar file.  
