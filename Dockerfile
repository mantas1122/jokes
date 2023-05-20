# Stage 1: Build the application
FROM gradle:7.3.3-jdk17 as build

# Copy your source code
COPY . /home/gradle/src

# Change work directory
WORKDIR /home/gradle/src

# Build the application
RUN gradle clean build --no-daemon

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Copy the jar file from the build stage
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Expose the port the app runs in
EXPOSE 8080

# The application's entrypoint
ENTRYPOINT ["java","-jar","/app.jar"]
