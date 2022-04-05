FROM openjdk:17-jdk-alpine
ADD target/lotto-generator-0.0.1-SNAPSHOT.jar .
CMD java -jar lotto-generator-0.0.1-SNAPSHOT.jar