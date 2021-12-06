FROM openjdk:16-slim
EXPOSE 8088
VOLUME /data
COPY target/*.jar unsigs-be-0.1.jar
ENTRYPOINT ["java","-jar","/unsigs-be-0.1.jar"]
