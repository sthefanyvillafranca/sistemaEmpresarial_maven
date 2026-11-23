# sistemaEmpresarial (Maven + JUnit)

Gestor empresarial por consola en Java: personas, nóminas y proyectos,
con MySQL (JDBC) y ficheros de texto. Proyecto de Acceso a Datos, 2º DAM.

Es la versión Maven de
[sistemaEmpresarial](https://github.com/sthefanyvillafranca/sistemaEmpresarial),
con tests en JUnit 5.

## Configuración

Los datos de conexión a MySQL no están en el repositorio. Antes de ejecutar:

1. Copia `src/main/resources/application.properties.example` como
   `application.properties` en esa misma carpeta y rellena usuario y contraseña.
2. Haz lo mismo con `src/test/resources/application.properties.example`.

Los tests vacían las tablas antes de cada prueba, así que la base de datos de
los tests tiene que terminar en `_test`. Si no, los tests se paran sin borrar nada.

## Tests

```
mvn test
```

Más detalles en [docs/maven_junit.md](docs/maven_junit.md).
