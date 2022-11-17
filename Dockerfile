FROM openjdk:8-jre-alpine
COPY build/libs/*jar ./app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "./app.jar"]