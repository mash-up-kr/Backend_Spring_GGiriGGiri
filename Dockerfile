FROM eclipse-temurin:17.0.2_8-jre-alpine
WORKDIR /home
CMD ["mkdir", "-p", "/home/server/build/libs"]
CMD ["mkdir", "-p", "/home/server/src/main/resources"]
COPY build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar /home/server/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar
COPY src/main/resources /home/server/src/main/resources
CMD ["java", "-jar", "/home/server/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar"]