## Prompts de Desarrollo 

## Prompt 1: Explicación sobre el uso de la anotación @Service
## Prompt Utilizado:

"¿Para qué sirve la anotación @Service en Spring Boot y cuál es la diferencia con @Component o @Repository?"

## Respuesta Recibida:
- @Service es una especialización de @Component que se utiliza para marcar clases de la capa de lógica de negocio.La diferencia con @Repository es que este último está pensado para la capa de persistencia, mientras que @Service deja más claro que ahí va la lógica de negocio.Aunque técnicamente son equivalentes (porque Spring trata a todos como @Component), usarlos correctamente mejora la legibilidad y el diseño en capas.

## Modificaciones Realizadas:

- Se organizo mejor las capas en el proyecto: controller, service y repository.

## Explicación del Prompt:

- Hice esta pregunta porque necesitaba entender cómo organizar correctamente las capas en Spring Boot y por qué elegir una anotación sobre otra.

## Aprendizajes Obtenidos:

- La importancia de usar anotaciones semánticas (@Service, @Repository) para que el código sea más claro.
- Que @Service y @Repository heredan de @Component, pero su propósito principal es documentar la intención de la clase.
- Buenas prácticas de separación de capas en una aplicación Spring Boot.

## Prompt 2: Implementación de Repositories y Services
## Prompt Utilizado:

- "Necesito que me ayudes a crear interfaces Repository para las entidades Empleado, Departamento y Proyecto usando Spring Data JPA.
Además, genera servicios (@Service) para cada entidad con métodos básicos: crear, listar, actualizar y eliminar."

## Respuesta Recibida:
- Se generaron interfaces EmpleadoRepository, DepartamentoRepository, ProyectoRepository extendiendo JpaRepository.
- Se generaron clases EmpleadoService, DepartamentoService, ProyectoService con métodos CRUD.

## Modificaciones Realizadas:

- Se renombraron métodos para usar nombres más expresivos (buscarPorId, listarTodos, guardar, eliminar).
- Se agregó manejo de Optional al buscar por ID.

## Explicación del Prompt:

- El objetivo fue automatizar la base de la capa de persistencia y lógica de negocio con la mínima repetición de código, aprovechando Spring Data.

## Aprendizajes Obtenidos:

- La simplicidad de JpaRepository para reducir código repetitivo.
- Buenas prácticas al encapsular la lógica en servicios en lugar de usar repositorios directamente en controladores.
- Importancia de manejar Optional para evitar NullPointerException.


