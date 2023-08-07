FROM amazoncorretto:11

WORKDIR server_app

COPY ./target/*.jar app.jar

ENTRYPOINT ["java","-jar","/server_app/app.jar"]