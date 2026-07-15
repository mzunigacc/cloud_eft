# Sumativa 3 — productor, consumidor, RabbitMQ, H2 y S3

## Arquitectura

`Postman -> productor:8080 -> RabbitMQ -> consumidor:8081 -> H2 + S3`

Mensaje erróneo: usar `"descripcionPedido": "error"`; el consumidor lo rechaza y RabbitMQ lo mueve a `guia.dlq`.

## Prueba local recomendada

1. Exportar credenciales temporales de AWS Academy.
2. Desde esta carpeta ejecutar:

```bash
docker compose up --build
```

3. RabbitMQ: `http://localhost:15672` (`guias` / `guias123`).
4. Crear guía:

```http
POST http://localhost:8080/guias
Content-Type: application/json
```

5. Consultar resultado procesado:

```http
GET http://localhost:8081/guias/GD-001
```

6. H2: `http://localhost:8081/h2-console`, JDBC `jdbc:h2:file:/app/data/guiasdb`, usuario `sa`, contraseña vacía.

La carpeta `data-consumidor` está montada como volumen, por lo que H2 sobrevive a la recreación del contenedor.
