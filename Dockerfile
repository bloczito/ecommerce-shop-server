FROM gradle:7-jdk11 as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon


FROM openjdk:11
EXPOSE 8080
WORKDIR /app

RUN apt-get update && apt-get install sqlite3 -y


RUN touch shopp.db
COPY loader.sql .

RUN sqlite3 shopp.db ".read loader.sql"

COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker-sample.jar

ENTRYPOINT ["java","-jar","/app/ktor-docker-sample.jar"]

#FROM gradle:7-jdk11
#
#RUN apt update
#RUN apt install sqlite3 -y
#
#WORKDIR /app
#
#RUN touch shopp.db
#COPY loader.sql .
#
#RUN sqlite3 shopp.db ".read loader.sql"
#
#EXPOSE 8080
#
#COPY build.gradle.kts .
#COPY gradle.properties .
#COPY gradlew .
#COPY settings.gradle.kts .
##COPY gradle gradle/
#COPY src src/
#
##COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker-sample.jar
##ENTRYPOINT ["java","-jar","/app/ktor-docker-sample.jar"]
#
##CMD ["./gradlew run"]
#CMD /bin/bash