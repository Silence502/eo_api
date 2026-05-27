# ── Stage 1 : Build ──────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Copie des fichiers de configuration Gradle en premier pour profiter du cache des layers
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon --quiet

# Copie des sources et build du JAR
COPY src ./src
RUN ./gradlew bootJar --no-daemon --quiet

# ── Stage 2 : Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre-alpine

# Exécution en utilisateur non-root (bonne pratique de sécurité)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR /app

COPY --from=builder /app/build/libs/*SNAPSHOT.jar api-app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "api-app.jar"]