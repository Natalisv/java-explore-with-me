FROM eclipse-temurin:11-jre-jammy
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/schema.sql /docker-entrypoint-initdb.d/
RUN apt-get update && apt-get install -y postgresql-client
ENTRYPOINT ["java","-jar","/app.jar"]
