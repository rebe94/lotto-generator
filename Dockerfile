FROM openjdk:17-jdk-alpine
ADD target/lotto-generator-0.0.1-SNAPSHOT.jar .
#EXPOSE 9090
CMD java -jar lotto-generator-0.0.1-SNAPSHOT.jar