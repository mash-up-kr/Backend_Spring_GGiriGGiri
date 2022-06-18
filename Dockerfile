FROM eclipse-temurin:17.0.2_8-jre-alpine
WORKDIR /home
CMD ["mkdir", "-p", "server/build/libs"]
CMD ["mkdir", "-p", "server/src/main/resources"]
COPY ./build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar ./server/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar
COPY ./src/main/resources ./server/src/main/resources
CMD ["java", "-jar", "./server/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar"]