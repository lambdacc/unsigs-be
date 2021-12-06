FROM openjdk:16-slim
EXPOSE 8088
ADD ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/unsigs-be-0.0.1.jar"]