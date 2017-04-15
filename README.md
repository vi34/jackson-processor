# jackson-processor
Annotation Processor for Jackson library

JacksonProcessor generates implementation of _JsonSerializer_ and _JsonDeserializer_ 
for _@GenerateClasses_ annotated classes. 





#####Benchmarks
Benchmarks located in separate test-project.



#####Tests
Tests integrated in main project. 

For proper tests execution need to directly configure in project settings annotation processors (lombok) used in project.
Otherwise JacksonProcessor will process tests before run, during compilation.  