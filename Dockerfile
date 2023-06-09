FROM gradle:7.6-jdk11

COPY . .

RUN gradle build

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "build/libs/homeBanking-0.0.1-SNAPSHOT.jar"]