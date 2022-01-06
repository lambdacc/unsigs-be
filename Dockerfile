# syntax=docker/dockerfile:experimental
FROM openjdk:16-slim
EXPOSE 8088
RUN addgroup --system unsig
RUN adduser --system unsig
RUN adduser unsig unsig
COPY . /home/unsig/unsig-be
WORKDIR /home/unsig/unsig-be
VOLUME /data
VOLUME /logs
RUN ./mvnw clean install -DskipTests
COPY target/*.jar unsigs-be-0.2.jar
ENTRYPOINT ["java","-jar","/unsigs-be-0.2.jar"]
