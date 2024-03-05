FROM openjdk:21
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]