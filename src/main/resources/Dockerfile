#define base docker image
FROM openjdk:17
LABEL maintainer="Donation Teams"
ADD target/donation-0.0.1-SNAPSHOT.jar donation-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","donation-0.0.1-SNAPSHOT.jar"]