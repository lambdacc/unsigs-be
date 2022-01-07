FROM openjdk:16-slim
EXPOSE 8088
#RUN useradd -m unsigs
#USER unsigs
#WORKDIR /home/unsigs
VOLUME /data
VOLUME /logs
COPY target/*.jar unsigs-be-0.1.jar
ENTRYPOINT ["java","-jar","/unsigs-be-0.1.jar"]
