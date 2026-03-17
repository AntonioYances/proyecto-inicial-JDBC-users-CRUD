# Saloplay JDBC - Módulo Users (CRUD)

## Requisitos
- Java 17
- Maven
- MySQL (BD: saloplay_db)

## Crear tabla
Ejecutar el script:
src/main/resources/db.sql

## Configuración
Editar:
src/main/java/com/saloplay/config/DatabaseConfig.java
y colocar la contraseña de MySQL.

## Ejecutar
mvn clean package
mvn exec:java -Dexec.mainClass="com.saloplay.app.Main"
