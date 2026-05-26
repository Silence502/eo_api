# 1. Image de base : On utilise une version allégée de Java 25 (très rapide à télécharger)
FROM eclipse-temurin:25-jre-alpine

# 2. On crée un dossier /app dans le conteneur et on s'y place
WORKDIR /app

# 3. On copie le fichier JAR généré par Gradle vers notre conteneur
# On utilise un joker (*) pour ne pas avoir à changer le nom si la version (0.0.1) change
COPY build/libs/*SNAPSHOT.jar api-app.jar

# 4. On indique que notre conteneur va communiquer sur le port 8000
EXPOSE 8000

# 5. La commande exécutée quand le conteneur démarre
ENTRYPOINT ["java", "-jar", "api-app.jar"]