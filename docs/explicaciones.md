## clases
### nomina
 Una nomina pertenece a una sola persona

### persona
Una persona puede tener una o varias nóminas.  
Una persona puede participar en varios proyectos

### proyecto
Un proyecto puede tener muchas personas 

## Relaciones
- Persona y nomina: 1:N (Llave foránea la tiene nomina: id_persona)
- Persona y proyecto: M:N (Tiene tabla intermedia en la BBDD)

- Una persona puede existir sin una nómina, pero una nómina no puede existir sin estar relacionada a una persona.
- No se puede eliminar a una persona que esté relacionada a una nómina (por la llave foránea)
- No se puede eliminar a una persona o proyecto relacionados entre si sin eliminar primero su vinculo (en la tabla intermedia)

## SOBRE MAVEN
Capas MVC
- Mi proyecto respeta las capas MVC también, solo que me falta cambiar 
nombres de paquetes y ubicaciones, si fuese necesario.
- Mi repositorio es modelo
- Mi servicio es parte del modelo también
- Y en un proyecto del profesor, la base de Datos también era parte del modelo (aunque eso me confundió)
- Los errores podrían estar en el código real, no en el código del test
- si quiero ver errores, entonces debo forzarlos en el código real

