FROM eclipse-temurin:11.0.15_10-jre-alpine as build
WORKDIR /home
RUN mkdir -p /home/server/build/libs /home/server/src/main/resources
COPY build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar /home/server/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar
COPY src/main/resources /home/server/src/main/resources
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "/home/server/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar"]