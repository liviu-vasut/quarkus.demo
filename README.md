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
