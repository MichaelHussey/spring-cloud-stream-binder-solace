# spring-cloud-stream-solace-sample
Sample showing usage of spring-cloud-stream-binder-solace

## Getting Started

Edit the <code>src/main/resources/application.properties</code> file and set the parameters for connection to 
a Solace Message Router. See the [Configuration](../README.md#configuration-options) section of the 
Binder documentation for a list of properties.

Compile the binder and this demo program and then run using the Maven target as follows: 

```
git clone https://github.com/MichaelHussey/spring-cloud-stream-binder-solace
cd spring-cloud-stream-binder-solace
mvn install
cd spring-cloud-stream-solace-sample
mvn spring-boot:run -Drun.arguments="--server.port=8082"
```

By default the application's REST interface is on port 8080, this may be changed by setting `server.port` in the configuration
file or passing it on the command line. 

You can also run the fat jar created by Maven:

```
cd spring-cloud-stream-solace-sample
java -jar target/spring-cloud-stream-solace-sample-0.1.jar
```

### Subscribing to topics

The sample program listens on two Solace topic names <code>test/one</code> and <code>test/two</code>. 

### Publishing to topics

Any data posted to <code>/publish</code> will be published as binary attachement in a message on topic <code>test/three</code>. For example:

```
curl -X POST -d "Just a little bit of data" localhost:8080/publish
```