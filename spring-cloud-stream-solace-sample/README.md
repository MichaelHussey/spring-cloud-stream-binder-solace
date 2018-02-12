# spring-cloud-stream-solace-sample
Sample showing usage of spring-cloud-stream-binder-solace

## Getting Started

Edit the <code>src/main/resources/application.properties</code> file and set the parameters for connection to 
a Solace Message Router.

Compile the binder and this demo program. Then run using the Maven target as follows: 

```
mvn install
cd spring-cloud-stream-solace-sample
mvn spring-boot:run
```
The sample program listens on two Solace topic names <code>test/one</code> and <code>test/two</code>. 
