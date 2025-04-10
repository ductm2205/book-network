# Single-stage image for development with hot reloading
FROM maven:3.9.9-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Install dependencies (cached unless pom.xml changes)
RUN mvn dependency:resolve

# Expose the application port
EXPOSE 8080

# Run the app with Spring Boot Maven plugin (hot reloading enabled by DevTools)
CMD ["mvn", "spring-boot:run"]