#### 1st Stage ####
FROM maven:3-openjdk-17 AS builder
WORKDIR /maciek/app/src/
COPY src ./
COPY pom.xml ../
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package

#### 2nd Stage ####
FROM openjdk:17.0.1
WORKDIR /maciek/lib/
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar ./demo.jar
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n","-Djava.security.egd=file:/dev/./urandom","-jar","/lib/demo.jar"]