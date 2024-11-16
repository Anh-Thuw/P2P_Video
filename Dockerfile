# Stage 1: Build the application
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package

# Stage 2: Create the final image
FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/target/P2P_video_call-1.0-SNAPSHOT.jar .
CMD ["java", "-jar", "P2P_video_call-1.0-SNAPSHOT.jar"]
