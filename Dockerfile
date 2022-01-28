FROM openjdk:16-slim AS build
WORKDIR /src
COPY ./ /src
RUN ./mvnw clean install

FROM openjdk:16-slim
EXPOSE 8088
#RUN useradd -m unsigs
#USER unsigs
#WORKDIR /home/unsigs
VOLUME /data
VOLUME /logs
COPY --from=build /src/target/*.jar unsigs-be.jar
ENTRYPOINT ["java","-jar","/unsigs-be.jar"]
