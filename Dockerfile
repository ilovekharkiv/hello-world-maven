FROM openjdk:8-jre-alpine

EXPOSE 8080

COPY ./target/my-app-*.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT ["java", "-jar", "my-app-*.jar"]
CMD java -jar my-app-*.jar