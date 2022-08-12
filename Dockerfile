FROM eclipse-temurin:11.0.15_10-jre-alpine as build
WORKDIR /home
RUN mkdir -p /home/server/build/libs /home/ec2-user/src/main/resources
COPY build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar /home/ec2-user/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar
COPY src/main/resources /home/ec2-user/src/main/resources
RUN mkdir -p /var/log
VOLUME ["/var/log"]
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "/home/ec2-user/build/libs/gifticon-storm-0.0.1-SNAPSHOT.jar"]