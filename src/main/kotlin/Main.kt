package org.example

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

// 1. Definición de la tabla usando ORM Exposed
object Estudiante : Table() {
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 100)
    val edad = integer("edad")
    val nivelEstres = varchar("nivel_estres", 50)

    override val primaryKey = PrimaryKey(id)
}

fun main() {
    // 2. Conectar a SQLite en la carpeta 'data'
    Database.connect("jdbc:sqlite:data/bienestar.db", driver = "org.sqlite.JDBC")

    // 3. Crear la tabla si no existe (dentro de una transacción)
    transaction {
        SchemaUtils.create(Estudiante)
    }

    var opcion: Int = 0

    while (opcion != 6) {
        println("\n=== MENU: BIENESTAR ESTUDIANTIL ===")
        println("1. Registrar estudiante (nombre, edad y estres)")
        println("2. Ver estudiantes registrados")
        println("3. Buscar estudiante por nombre")
        println("4. Actualizar estado de estres")
        println("5. Eliminar estudiante")
        println("6. Ver Resumen Final y Salir")
        print("Elige una opción (1-6): ")

        opcion = readln().toIntOrNull() ?: 0

        when (opcion) {
            1 -> {
                println("\n--- 1. REGISTRAR ESTUDIANTE ---")
                print("Ingresa el nombre: ")
                val nombreInput = readln()

                print("Ingresa la edad: ")
                val edadInput = readln().toIntOrNull() ?: 0

                print("¿Cómo te sientes hoy? (Bien, Estresado, Muy Estresado): ")
                val estresInput = readln()

                // 4. Guardar en la base de datos usando ORM
                transaction {
                    Estudiante.insert {
                        it[nombre] = nombreInput
                        it[edad] = edadInput
                        it[nivelEstres] = estresInput
                    }
                }
                println("¡Estudiante guardado en la base de datos exitosamente!")
            }
            2 -> {
                println("\n--- 2. VER ESTUDIANTES REGISTRADOS ---")
                // 5. Leer desde la base de datos usando ORM
                transaction {
                    val estudiantes = Estudiante.selectAll().toList()
                    if (estudiantes.isEmpty()) {
                        println("No hay estudiantes registrados.")
                    } else {
                        estudiantes.forEach {
                            println("ID: ${it[Estudiante.id]} - Nombre: ${it[Estudiante.nombre]} - Edad: ${it[Estudiante.edad]} - Estrés: ${it[Estudiante.nivelEstres]}")
                        }
                    }
                }
            }
            3 -> {
                println("\n--- 3. BUSCAR ESTUDIANTE ---")
                print("Ingresa el nombre o letras a buscar: ")
                val busqueda = readln()

                transaction {
                    // Operador 'like' para buscar coincidencias parciales
                    val resultados = Estudiante.selectAll().where { Estudiante.nombre like "%$busqueda%" }.toList()
                    if (resultados.isEmpty()) {
                        println("No se encontraron estudiantes con '$busqueda'.")
                    } else {
                        resultados.forEach {
                            println("Encontrado -> ID: ${it[Estudiante.id]} - Nombre: ${it[Estudiante.nombre]} - Estrés: ${it[Estudiante.nivelEstres]}")
                        }
                    }
                }
            }
            4 -> {
                println("\n--- 4. ACTUALIZAR ESTADO DE ESTRÉS ---")
                print("Ingresa el ID del estudiante a modificar: ")
                val idModificar = readln().toIntOrNull() ?: 0
                print("Ingresa el nuevo estado de estrés: ")
                val nuevoEstres = readln()

                transaction {
                    // Actualización usando la condición 'eq' (igual a)
                    val filasAfectadas = Estudiante.update({ Estudiante.id eq idModificar }) {
                        it[nivelEstres] = nuevoEstres
                    }
                    if (filasAfectadas > 0) {
                        println("¡El estado de estrés se actualizó correctamente!")
                    } else {
                        println("Error: No existe un estudiante con el ID $idModificar.")
                    }
                }
            }
            5 -> {
                println("\n--- 5. ELIMINAR ESTUDIANTE ---")
                print("Ingresa el ID del estudiante a eliminar: ")
                val idEliminar = readln().toIntOrNull() ?: 0

                transaction {
                    // Eliminación física usando 'deleteWhere'
                    val filasAfectadas = Estudiante.deleteWhere { Estudiante.id eq idEliminar }
                    if (filasAfectadas > 0) {
                        println("¡Estudiante eliminado de la base de datos de forma permanente!")
                    } else {
                        println("Error: No existe un estudiante con el ID $idEliminar.")
                    }
                }
            }
            6 -> {
                println("\nSaliendo del menú y cerrando el sistema...")

                // Opcional: Mostrar total al salir
                transaction {
                    val total = Estudiante.selectAll().count()
                    println("Total de estudiantes en la base de datos: $total")
                }
            }
            else -> {
                println("\nOpción incorrecta. Por favor, elige 1, 2 o 3.")
            }
        }
    }
}