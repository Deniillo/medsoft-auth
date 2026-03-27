FROM gradle:8.14-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar
COPY certs/keystore.p12 /app/certs/keystore.p12

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "app.jar"]