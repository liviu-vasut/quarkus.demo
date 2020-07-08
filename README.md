# Quarkus Demo

## What we'll build?

The application will try to showcase some of the features provided by Quarkus like:
* dependency injection
* health checks
* metrics
* resilience
* logging
* tracing

There are two microservices implemented, one for managing students and one for student profile pictures, and, for the sake of example, will also add a remote logging service.

The repository is structured so that you can follow the development of each step by checking out the appropriate branch and looking at the diffs.

## Creating the projects

### Create the students microservice

```bash
   mvn io.quarkus:quarkus-maven-plugin:1.4.2.Final:create \
       -DprojectGroupId=com.sv.demo \
       -DprojectArtifactId=quarkus-students \
       -DclassName="com.sv.demo.rest.json.StudentResource" \
       -Dpath="/student" \
       -Dextensions="resteasy-jackson"
```

### Create the student profiles microservice

```bash
   mvn io.quarkus:quarkus-maven-plugin:1.4.2.Final:create \
       -DprojectGroupId=com.sv.demo \
       -DprojectArtifactId=quarkus-student-profiles \
       -DclassName="com.sv.demo.rest.json.StudentProfileResource" \
       -Dpath="/profile" \
       -Dextensions="resteasy-jackson"
```

## Running the services

From within each service directory run
```bash
   ./mvnw compile quarkus:dev
```

## Container dependencies

**Note:** I'm using `podman` to run containers, but the commands used in this demo are completely compatible with `docker`. Just replace `podman` with `docker` or `alias podman='docker'`.

### AMQP

```bash
podman run -it --rm -p 8161:8161 -p 5672:5672 vromero/activemq-artemis:2.8.0-alpine
```

There is a management console at http://0.0.0.0:8161/console, you can login using the credentials from the `application.properties` file.

### Logstash

```bash
podman run -it --rm -v /tmp/logstash.conf:/usr/share/logstash/pipeline/logstash.conf -p 12201:12201/udp docker.io/library/logstash:7.8.0
```

The pipeline configuration is stored in `quarkus-students/logstash/logstash.conf` and should be copied in a path readable by the container (e.g. `/tmp/`) before running it. You can do that manually or use the provided shell script from within the same directory.

