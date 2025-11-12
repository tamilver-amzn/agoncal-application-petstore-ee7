FROM maven:3.8-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /app/src/
RUN mvn clean package

FROM jboss/wildfly:24.0.1.Final
COPY --from=build /app/target/*.war /opt/jboss/wildfly/standalone/deployments/
USER jboss
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s CMD curl -f http://localhost:8080/ || exit 1
