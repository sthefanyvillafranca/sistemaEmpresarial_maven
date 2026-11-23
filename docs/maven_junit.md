## Proyecto Maven + JUnit

Es mi SistemaEmpresarial pasado a Maven para poder hacerle tests con JUnit 5.

### Qué cambió respecto a mi proyecto original
- El código está en `src/main/java` y los tests en `src/test/java` 
- El driver de MySQL y JUnit vienen del `pom.xml`.
- `BaseDeDatos.conectar()` dirección, usuario y contraseña de
  `application.properties` ya no están escritos en el código como mi versión inicial.
- Interfaces `InterfaceControlador` e `InterfaceVista` (método `menu()`),
  que cumplen `ControladorMaestro` y `Consola`.
- Interfaz `InterfaceDatos` (`agregar`, `modificar`, `eliminar`), que cumple `BaseDeDatos`:
  los tres llaman a mi `ejecutarSQL`.
- Quité la conexión a la API.

### Base de datos para los tests
Claude, al hacer las pruebas, me sugirió usar `empresa_qa` como molde y no directamente,
porque los tests vacían las tablas antes de cada prueba y eso borraría lo que hubiera
en `empresa_qa` cada vez que se ejecutan.

Por eso:
- Los tests usan `empresa_test` (`src/test/resources/application.properties`).
- La estructura de las tablas se copia de `empresa_qa` con `CREATE TABLE ... LIKE`, que solo lee.
- `empresa` (la de mi proyecto original) no se toca nunca.
- Antes de borrar nada, los tests comprueban que la dirección termina en `_test`
  (`src/test/java/soporte/BdDePruebas.java`).

### Tests
- Sin base de datos: `PersonaTest`, `NominaTest`, `ProyectoTest`, `FicheroTest`.
- Con base de datos: `BaseDeDatosTest`, `ServicioSqlTest`, `ServicioPersonaTest`,
  `ServicioNominaTest`, `ServicioProyectoTest`, `ServicioMaestroTest`.
- Sin tests: los controladores, `Consola` y `Lector` (leen del teclado e imprimen).
- Los try/catch están en los controladores, no en los tests.
