# ===== Stage 1: build =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
# copia de dependencias primero para aprovechar cache
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline
# copia código y compila
COPY src ./src
RUN mvn -B -DskipTests package

# ===== Stage 2: runtime =====
FROM eclipse-temurin:21-jre
WORKDIR /app
# copia el jar generado (ajusta el patrón si tu jar no termina en -SNAPSHOT)
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8070
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
