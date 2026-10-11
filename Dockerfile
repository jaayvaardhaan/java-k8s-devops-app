# STAGE 1: Build and compile the Java code
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY Main.java .
RUN javac Main.java

# STAGE 2: Create the final, ultra-lightweight production image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# This line stays inside the file so Docker handles it!
COPY --from=builder /app/*.class .
EXPOSE 8080
CMD ["java", "Main"]
