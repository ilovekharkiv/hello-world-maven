FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY ./target/my-app-*.jar /usr/app/
WORKDIR /usr/app

CMD java -jar my-app-*.jar
