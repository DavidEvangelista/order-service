FROM eclipse-temurin:21-jdk-alpine AS build
  
  # Set the working directory
WORKDIR /app
  
  # Copy Gradle wrapper and configuration
COPY gradlew* build.gradle settings.gradle .
COPY gradle gradle
  
  # Resolve dependencies
RUN ./gradlew dependencies --no-daemon
  
  # Copy source code
COPY src src
  
  # Build the application with production profile
RUN ./gradlew clean build -x test -Pprofile=dev --no-daemon
  
  # Runtime image
FROM eclipse-temurin:21-jre-alpine
  
  # Set the working directory
WORKDIR /app
  
  # Copy the built jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar
  
  # Expose port
EXPOSE 8080
  
  # Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]