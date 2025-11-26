# Build stage
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Package stage
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/vulnerable-app-0.0.1-SNAPSHOT.jar .
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "vulnerable-app-0.0.1-SNAPSHOT.jar"]
