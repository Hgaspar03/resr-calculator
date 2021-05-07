FROM openjdk:11
EXPOSE 8081
ADD /target/calculator.jar calculator.jar
ENTRYPOINT ["java", "-jar", "calculator.jar"]