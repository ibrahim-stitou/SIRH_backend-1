# Stage 1: Build the JAR
FROM gradle:8.3-jdk20 AS builder
USER root
WORKDIR /home/gradle/project

# Use temporary writable Gradle home to avoid permission issues
ENV GRADLE_USER_HOME=/tmp/.gradle

# Copy build scripts and dependencies first (for caching)
COPY ../build.gradle ../settings.gradle ./
COPY ../gradle ./gradle

# Download dependencies (cached)
RUN gradle build --no-daemon || true

# Copy source code
COPY ../src ./src

# Build JAR (skip tests for faster dev builds)
RUN gradle clean build -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:20-jre-alpine
WORKDIR /app

# Copy built JAR from builder
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]