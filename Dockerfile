FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml* build.gradle* gradle.properties* ./
COPY src ./src
RUN mvn clean package -DskipTests

FROM ubuntu:20.04
WORKDIR /app

# Create non-root user
RUN useradd --create-home --shell /bin/bash app \
    && chown -R app:app /app
USER app

# Copy JAR file
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
CMD ["java", "-jar app.jar"]