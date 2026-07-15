FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q dependency:go-offline
COPY src src
RUN ./mvnw -q clean package -DskipTests
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/guia-consumidor-1.0.0.jar app.jar
RUN mkdir -p /app/data
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
