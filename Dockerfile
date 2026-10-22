FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY . .
RUN mvn -B package -pl bot -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /opt/freedom-bot
COPY --from=build /build/bot/target/freedom-bot.jar freedom-bot.jar
CMD ["java", "-jar", "freedom-bot.jar"]
