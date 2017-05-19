# jackson-processor
Annotation Processor for Jackson library

JacksonProcessor generates implementation of _JsonSerializer_ and _JsonDeserializer_ classes.

To generate JsonSerializer, class must be marked with Jackson _@JsonSerialize_ annotation. 
To generate JsonDeserializer, class must be marked with Jackson _@JsonDeserialize_ annotation.

If _using_ value is absent in annotation, generated classes will be automatically registered as default 
serializer/deserializer. (_using_ property will be set)

To disable automatic registration for particular class simply set _using=JsonSerializer.None.class_
To disable automatic registration for all classes set processor option _AUTO_REGISTRATION=false_. 

You can register generated classes through module:
_mapper.registerModule(new APTModule());_


#### Tests
Tests integrated in main project. 

For proper tests execution need to disable annotation processors for project. 

### Benchmarks

Data for serialization taken from [jvm-serializers](https://github.com/eishay/jvm-serializers/wiki)

#### Test platform
OS: Mac OS X
JVM: Oracle Corporation 1.8.0_121
CPU: Intel Core i5 2,4 GHz

#### Current results:

##### Summary
Speedup compared to default method. 

method | unordered Deserialization | ordered Deserialization | Serialization
---|---|---|---
afterburner | ~4%  | ~24% |  ~24%
handwritten | ~10% | ~38% |  ~34%
processor   | ~7%  | ~36% |  ~34%

Benchmark             |   (method) | (orderedProperties) | Mode | Cnt   |  Score  |  Error | Units
---|---|---|---|---|---|---|---
Deserialization.media |  afterBurner |                 true |  avgt |   45 |  3258.910 | ± 40.761  | ns/op
Deserialization.media |  afterBurner |                false | avgt |   45 |  4115.280 | ± 78.264  | ns/op
Deserialization.media |  handWritten |                true |  avgt |   45 |  2657.641 | ± 27.735  | ns/op
Deserialization.media |  handWritten |               false | avgt |   45 |  3887.021 | ± 55.742  | ns/op
Deserialization.media |   reflection |                true |  avgt |   45 |  4290.085 | ± 43.724  | ns/op
Deserialization.media |   reflection |               false | avgt |   45 |  4270.587 | ± 42.514  | ns/op
Deserialization.media |    processor |                true |  avgt |   45 |  2764.893 | ± 31.379  | ns/op
Deserialization.media |    processor |               false | avgt |   45 |  4000.650 | ± 51.507  | ns/op
Serialization.media |    afterBurner |                  N/A |  avgt |   45 |  1968.629 | ± 24.605  | ns/op
Serialization.media |    handWritten |                 N/A |  avgt |   45 |  1711.002 | ± 34.012  | ns/op
Serialization.media |     reflection |                 N/A |  avgt |   45 |  2572.149 | ± 33.514  | ns/op
Serialization.media |      processor |                 N/A |  avgt |   45 |  1703.740 | ± 29.353  | ns/op
