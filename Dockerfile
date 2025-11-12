FROM maven:3.8-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests

# WildFly base image
FROM jboss/wildfly:latest
LABEL maintainer="Petstore Application"

# Add admin user for the WildFly admin console
RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#70365 --silent

# Set up JVM options
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m -Djava.net.preferIPv4Stack=true"

# Deploy the application
COPY --from=build /app/target/*.war /opt/jboss/wildfly/standalone/deployments/

# Expose the HTTP, HTTPS, and admin ports
EXPOSE 8080 8443 9990

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# Run WildFly
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
