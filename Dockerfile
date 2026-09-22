FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

COPY --from=build /app/target/bank-backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]