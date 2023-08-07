FROM amazoncorretto:11

WORKDIR gateway_app

COPY ./target/*.jar app.jar

ENTRYPOINT ["java","-jar","/gateway_app/app.jar"]