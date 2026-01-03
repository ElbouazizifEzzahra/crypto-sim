# ============================================
# Build Stage
# ============================================
FROM maven:3.9.4-eclipse-temurin-17 AS builder
# Set working directory
WORKDIR /app
# Copy Maven files (pom.xml) first for better layer caching
COPY pom.xml .
# Download dependencies first (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B
# Copy source code
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests
# ============================================
# Runtime Stage
# ============================================
#FROM eclipse-temurin:17-jre-alpine
FROM amazoncorretto:17-alpine
# Add a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# Set working directory
WORKDIR /app
# Copy the JAR from the build stage
COPY --from=builder /app/target/*.jar app.jar
# Expose application port
EXPOSE 8080
# Set JVM options
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"
# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1
# Run the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/app.jar"]