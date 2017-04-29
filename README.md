# jackson-processor
Annotation Processor for Jackson library

JacksonProcessor generates implementation of _JsonSerializer_ and _JsonDeserializer_ 
for _@GenerateClasses_ annotated classes. 





##### Benchmarks



##### Tests
Tests integrated in main project. 

For proper tests execution need to directly configure in project settings annotation processors (lombok) used in project.
Otherwise JacksonProcessor will process tests before run, during compilation.  


##### lombok
Warning: if you use lombok for your data classes, you must delombok @Getter and @Setter annotations.

#### Benchmarks

Data for serialization taken from [jvm-serializers](https://github.com/eishay/jvm-serializers/wiki)

##### Test platform
OS: Mac OS X
JVM: Oracle Corporation 1.8.0_121
CPU: Intel Core i5 2,4 GHz

##### Current results:

Benchmark              |   (method)  | (orderedProperties) | Mode | Cnt   |  Score  |   Error | Units 
---|---|---|---|---|---|---|---
Deserialization.complex | afterBurner |                 true |  avgt |   12 | 6828.170 | ± 320.165  | ns/op
Deserialization.complex | afterBurner |                false  | avgt |   12 | 6522.100 | ± 240.694  | ns/op
Deserialization.complex | handWritten |                 true |  avgt |   12 | 4815.915 | ±  69.455  | ns/op
Deserialization.complex | handWritten |                false  | avgt |   12 | 6327.978 | ± 128.589  | ns/op
Deserialization.complex |  reflection |                 true |  avgt |   12 | 7181.827 | ±  92.104  | ns/op
Deserialization.complex |  reflection |                false  | avgt |   12 | 6788.323 | ±  75.550  | ns/op
Deserialization.complex |   processor |                 true |  avgt |   12 | 4914.867 | ± 103.582  | ns/op
Deserialization.complex |   processor |                false  | avgt |   12 | 6150.989 | ±  93.513  | ns/op
Deserialization.media   |    afterBurner |                 true |  avgt |   12 | 3137.808 | ±  44.896  | ns/op
Deserialization.media   |    afterBurner |                false  | avgt |   12 | 3898.676 | ± 114.018  | ns/op
Deserialization.media   |    handWritten |                 true |  avgt |   12 | 3770.690 | ±  64.202  | ns/op
Deserialization.media   |    handWritten |                false  | avgt |   12 | 3828.267 | ±  68.759  | ns/op
Deserialization.media   |     reflection |                 true |  avgt |   12 | 4040.832 | ±  84.916  | ns/op
Deserialization.media   |     reflection |                false  | avgt |   12 | 4062.269 | ±  73.955  | ns/op
Deserialization.media   |      processor |                 true |  avgt |   12 | 2594.665 | ± 115.824  | ns/op
Deserialization.media   |      processor |                false  | avgt |   12 | 3602.172 | ±  74.958  | ns/op
Deserialization.pojo    |     afterBurner |                 true |  avgt |   12 | 1931.029 | ± 121.693  | ns/op
Deserialization.pojo    |     afterBurner |                false  | avgt |   12 | 2051.600 | ±  39.462  | ns/op
Deserialization.pojo    |     handWritten |                 true |  avgt |   12 | 1531.269 | ±  36.272  | ns/op
Deserialization.pojo    |     handWritten |                false  | avgt |   12 | 1873.332 | ±  82.896  | ns/op
Deserialization.pojo    |      reflection |                 true |  avgt |   12 | 2079.607 | ±  32.887  | ns/op
Deserialization.pojo    |      reflection |                false  | avgt |   12 | 1954.356 | ±  51.855  | ns/op
Deserialization.pojo    |       processor |                 true |  avgt |   12 | 1479.935 | ±  31.143  | ns/op
Deserialization.pojo    |       processor |                false  | avgt |   12 | 2026.910 | ±  30.285  | ns/op
Serialization.complex |   afterBurner |                  N/A |  avgt |   12 | 3078.187 | ±  59.782  | ns/op
Serialization.complex |   handWritten |                  N/A |  avgt |   12 | 2820.247 | ± 220.320  | ns/op
Serialization.complex |    reflection |                  N/A |  avgt |   12 | 3328.746 | ±  85.225  | ns/op
Serialization.complex |     processor |                  N/A |  avgt |   12 | 2515.671 | ±  56.053  | ns/op
Serialization.media   |      afterBurner |                  N/A |  avgt |   12 | 1848.269 | ±  59.897  | ns/op
Serialization.media   |      handWritten |                  N/A |  avgt |   12 | 1580.711 | ±  99.210  | ns/op
Serialization.media   |       reflection |                  N/A |  avgt |   12 | 2560.697 | ±  67.762  | ns/op
Serialization.media   |        processor |                  N/A |  avgt |   12 | 1723.045 | ±  38.733  | ns/op
Serialization.pojo    |       afterBurner |                  N/A |  avgt |   12 |  982.031 | ±  25.332  | ns/op
Serialization.pojo    |       handWritten |                  N/A |  avgt |   12 |  934.719 | ±  11.623  | ns/op
Serialization.pojo    |        reflection |                  N/A |  avgt |   12 | 1088.997 | ±  27.153  | ns/op
Serialization.pojo    |         processor |                  N/A |  avgt |   12 |  885.677 | ±  17.784  | ns/op