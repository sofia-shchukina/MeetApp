FROM openjdk:18

ENV ENVIRONMENT=prod

LABEL maintainer="sofiashchukina@outlook.com"

ADD backend/target/meetApp.jar meetApp.jar

CMD [ "sh", "-c", "java -Dserver.port=$PORT -jar /meetApp.jar" ]
