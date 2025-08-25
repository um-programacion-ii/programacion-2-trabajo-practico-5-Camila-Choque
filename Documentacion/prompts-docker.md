# Prompts de Docker y Docker Compose

## Prompt 1: Levantar y gestionar contenedores de bases de datos

### Prompt Utilizado:
"Explicame el funcionamiento de estos comandos."

```bash
# Levantar MySQL y PostgreSQL
docker compose up -d

# Verificar que los contenedores estén corriendo
docker compose ps

# Ver logs de los contenedores
docker compose logs -f

# Detener contenedores
docker compose down

# Detener y eliminar volúmenes
docker compose down -v
```
## Respuesta obtenida:
### Explicación rápida de cada comando:
```bash
docker compose up -d
```
- Levanta los contenedores en segundo plano (-d) según la configuración de docker-compose.yml.

```bash
docker compose ps
```
- Muestra el estado de los contenedores activos.

```bash
docker compose logs -f
```
- Muestra los logs en tiempo real de los contenedores (-f sigue los logs).

```bash
docker compose down
```
- Detiene los contenedores y libera recursos.

```bash
docker compose down -v
```
- Detiene contenedores y elimina los volúmenes asociados, borrando los datos de las bases de datos.

## Modificaciones Realizadas:

- Se agregó explicación línea por línea de cada comando.
- Se incluyó advertencia sobre la eliminación de volúmenes (-v) para no perder datos importantes.

## Explicación del Prompt:

- Se utilizó este prompt para obtener una guía paso a paso de cómo manejar contenedores de bases de datos con Docker Compose, asegurando que los comandos fueran claros y sus funciones entendibles.

## Aprendizajes Obtenidos:

- Cómo levantar contenedores de MySQL y PostgreSQL de forma rápida.
- Cómo verificar el estado de los contenedores y monitorear logs en tiempo real.
- La diferencia entre detener contenedores (down) y eliminar volúmenes (down -v).
- Buenas prácticas para manejar contenedores sin afectar el sistema local.

# Prompts de Spring Boot

## Prompt 2: Ejecutar Aplicación con Diferentes Profiles

### Prompt Utilizado:

```bash
# Con H2 (desarrollo)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Con MySQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql

# Con PostgreSQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres

```
## Respuesta Recibida:

- ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
Ejecuta la aplicación usando el profile dev. Normalmente se usa una base de datos H2 en memoria para desarrollo y pruebas rápidas. Permite correr la app sin necesidad de configurar bases de datos externas.

- ./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
Ejecuta la aplicación usando el profile mysql. Con este profile, Spring Boot cargará la configuración de conexión a MySQL definida en application-mysql.yml o application.yml. Permite probar la app con una base de datos MySQL real.

- ./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
Ejecuta la aplicación usando el profile postgres. Spring Boot cargará la configuración de PostgreSQL, conectándose a una base de datos PostgreSQL real para pruebas o desarrollo.


## Modificaciones Realizadas:

- Se explicó línea por línea qué hace cada comando y qué base de datos utiliza.
- Se incluyó explicación de la funcionalidad de los profiles en Spring Boot.

## Explicación del Prompt:

- Se utilizó este prompt para comprender cómo ejecutar la aplicación con distintos entornos de configuración, permitiendo probar la app con H2, MySQL o PostgreSQL de manera flexible y sin cambiar código.

## Aprendizajes Obtenidos:

- Cómo usar profiles de Spring Boot para distintos entornos.
- Diferencia entre ejecutar con H2 (ligero) y bases de datos reales como MySQL o PostgreSQL.
- Buenas prácticas para desarrollo y pruebas usando diferentes perfiles.
