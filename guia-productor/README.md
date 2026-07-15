# Microservicio productor

Recibe `POST /guias`, valida el JSON y lo publica como mensaje persistente en RabbitMQ. No usa H2 ni S3.

## Local

```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

Puerto: `8080`.
