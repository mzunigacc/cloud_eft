# Pruebas JWT con Azure AD B2C

## Valores configurados

- Issuer: `https://cloudnatives4matias.b2clogin.com/01e6be97-416e-481f-85b8-0e2c7f8a3303/v2.0/`
- Audience: `2bce333b-2b1c-4369-a6fb-ab14f2c70df4`
- Scope requerido: `guias.readwrite`
- Claim de rol: `extension_guiaRole`
- Roles aceptados: `gestion`, `descarga` o `descargas`

## Autorización

- Productor `POST /guias`: scope `guias.readwrite` + rol `gestion`.
- Consumidor `GET /guias/{numeroGuia}/download`: scope + rol `gestion`, `descarga` o `descargas`.
- Resto de `/guias/**` en consumidor: scope + rol `gestion`.
- `/actuator/health`: público.

## Ejecución local sin seguridad

```bash
SPRING_PROFILES_ACTIVE=local docker-compose up --build
```

## Ejecución segura

```bash
cp .env.example .env
# completar credenciales AWS Academy en .env o exportarlas

docker-compose up --build -d
```

## Resultados esperados

- Sin token: `401 Unauthorized`.
- Token inválido, expirado, issuer o audience incorrectos: `401 Unauthorized`.
- Token válido con rol no permitido: `403 Forbidden`.
- Token `gestion`: puede crear, consultar, modificar, eliminar y descargar.
- Token `descarga`/`descargas`: solo puede descargar.

En Postman seleccione **Authorization → Bearer Token** y pegue un access token nuevo. No guarde tokens reales en Git.
