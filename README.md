# spring-cloud-stream-binder-solace
Spring Cloud Stream Binder for use with Solace Messaging Appliances

## Solace Binder Overview

The Solace Binder implementation maps each destination to a `Stream`.

## Getting Started

### Prerequisites
If you don't have a physical Solace Appliance available to you then use either a Virtual 
Message Router [VMR](http://dev.solace.com/downloads/) or register on [Solace Cloud](https://cloud.solace.com/).


### Getting the source

Grab a copy of the project using `git clone` from  [GitHub](https://github.com/MichaelHussey/spring-cloud-stream-binder-solace)

If you want to easily import the source into Eclipse (in order to inspect or modify) there is a Gradle task which generates a .project file with correctly configured source and classpaths

```
./gradlew eclipse
```

Then simply use `File | Import... | Existing Projects into Workspace` in the IDE

## Configuration Options

This section contains settings specific to the Solace Binder and bound channels.

For general binding configuration options and properties, please refer to the 
[Spring Cloud Stream core documentation](https://github.com/spring-cloud/spring-cloud-stream/blob/master/spring-cloud-stream-core-docs/src/main/asciidoc/spring-cloud-stream-overview.adoc#configuration-options).

## Authors

* **Mic Hussey** - *Initial work* - [Solace](https://github.com/MichaelHussey)

## License

This project is licensed under the Apache License, Version 2.0. - See the [LICENSE](LICENSE) file for details.

## Resources

For more information try these resources:

- The Solace Developer Portal website at: http://dev.solace.com
- Get a better understanding of [Solace technology](http://dev.solace.com/tech/).
- Check out the [Solace blog](http://dev.solace.com/blog/) for other interesting discussions around Solace technology
- Ask the [Solace community.](http://dev.solace.com/community/)

