FROM openjdk:17-alpine

WORKDIR /app

ARG JAR_FILE=/build/libs/*-SNAPSHOT.jar

COPY ${JAR_FILE} k-talk.jar

ENTRYPOINT ["java", "-jar", "/app/k-talk.jar"]

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime