# Microservicio consumidor

Escucha `guia.procesamiento`, genera la guía, la sube a S3 y persiste sus datos en H2. Si `descripcionPedido` es exactamente `error`, rechaza el mensaje sin reencolarlo y RabbitMQ lo mueve a `guia.dlq`.

## Local

Exporta las credenciales temporales de AWS Academy y ejecuta:

```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

Puerto: `8081`. H2: `http://localhost:8081/h2-console`.
