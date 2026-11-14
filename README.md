# Inventario de equipos

Aplicación de escritorio para registrar, consultar, editar y eliminar equipos de laboratorio. Usa Java 26, Maven, JavaFX, Hibernate y PostgreSQL.

## Base de datos

El proyecto utiliza la base existente `bd_laboratorio_orm` y las tablas `lab1.tipo_equipo` y `lab1.equipo`. La conexión se configura en `src/main/resources/META-INF/persistence.xml`.

## Cómo ejecutar

1. Inicia PostgreSQL y abre el proyecto en IntelliJ IDEA como proyecto Maven con JDK 26.
2. En Run → Edit Configurations, crea o edita una configuración de tipo Application con `org.example.Launcher` en Main class.
3. En Environment variables, agrega `DB_PASSWORD` con la contraseña del usuario configurado en `persistence.xml`.
4. Guarda la configuración y ejecútala.

## Cómo probar

- Crear. Pulsa Nuevo, completa el formulario con un código único y pulsa Guardar.
- Consultar. Usa los filtros de tipo y estado por separado o juntos. Con los datos iniciales, Impresora y En mantenimiento muestran el equipo `IMP-002`. Pulsa Limpiar filtros para ver todos los equipos.
- Editar. Selecciona un equipo en la tabla, cambia sus datos y pulsa Guardar.
- Eliminar. Selecciona el equipo de prueba, pulsa Eliminar y confirma.

El filtro Tipo permite seleccionar un tipo por su nombre.
