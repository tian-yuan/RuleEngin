FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD startup.sh /
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["/bin/sh", "/startup.sh"]