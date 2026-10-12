# Bienestar Estudiantil - Kotlin ORM

Un sistema de consola desarrollado en Kotlin para gestionar el registro, evaluación y seguimiento del nivel de estrés de los estudiantes. Este proyecto académico implementa persistencia de datos local y demuestra el uso de un ORM para evitar la escritura manual de sentencias TSQL, asegurando transacciones atómicas.

## Características principales (CRUD)
* **Creación:** Registro de nuevos estudiantes con su nivel de estrés actual.
* **Lectura:** Visualización de todos los registros almacenados.
* **Búsqueda Avanzada:** Filtrado de estudiantes por coincidencia de nombre usando el operador SQL `LIKE`.
* **Actualización:** Modificación del estado de estrés de un estudiante específico (operador `eq`).
* **Eliminación:** Borrado físico de registros en la base de datos.

## Tecnologías utilizadas
* **Lenguaje:** Kotlin
* **Gestor de dependencias:** Gradle
* **Base de datos:** SQLite (vía `sqlite-jdbc`)
* **ORM:** JetBrains Exposed (Core, DAO, JDBC)

## Instalación y Ejecución
1. Descargar el código fuente en formato `.zip` o clonar el repositorio.
2. Abrir el proyecto utilizando **IntelliJ IDEA**.
3. Permitir que Gradle sincronice automáticamente las dependencias (SQLite y Exposed).
4. Ejecutar el archivo `Main.kt`. 
*(Nota: El archivo de la base de datos `bienestar.db` se generará automáticamente en el directorio `/data` durante la primera ejecución).*
