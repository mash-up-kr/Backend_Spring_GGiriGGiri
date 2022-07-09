FROM eclipse-temurin:11.0.15_10-jre-alpine as build
WORKDIR /home
CMD ["mkdir", "-p", "/home/server/build/libs"]
CMD ["mkdir", "-p", "/home/server/src/main/resources"]
COPY build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar /home/server/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar
COPY src/main/resources /home/server/src/main/resources
CMD ["java", "-jar", "-Dspring.profiles.active=production", "/home/server/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar"]