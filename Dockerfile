# Build stage
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Download dependencies and build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Create directory for logs with proper permissions
RUN mkdir -p /var/log && \
    chown -R spring:spring /var/log

# Create directory for the app with proper permissions
RUN mkdir -p /app && \
    chown -R spring:spring /app

# Copy the built artifact from build stage
COPY --from=build /app/target/*.jar app.jar

# Set ownership of the application jar
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Environment variables with defaults (can be overridden during container run)
ENV SERVER_PORT=8082 \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xms512m -Xmx512m"

# Expose the application port
EXPOSE ${SERVER_PORT}

# Health check to verify the application is running properly
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${SERVER_PORT}/actuator/health || exit 1

# Start the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]