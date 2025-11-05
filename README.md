"# Base-de-Datos" 
# 📢 Portal de Avisos Universitario

Sistema CRUD desarrollado en **Java** con conexión a **MySQL**, que permite gestionar avisos académicos entre profesores y estudiantes.

---

## 🚀 Descripción

El **Portal de Avisos** busca mejorar la comunicación entre profesores y estudiantes de una institución educativa.  
Cada profesor puede publicar, editar o eliminar avisos, mientras que los estudiantes solo pueden visualizar los existentes.

Incluye:
- Inicio de sesión por correo institucional.
- Roles diferenciados (`profesor` / `estudiante`).
- CRUD completo de avisos.
- Interfaz gráfica con **Swing**.
- Conexión a base de datos mediante **MySQL JDBC Connector**.

---


---

## ⚙️ Requisitos

- **Java JDK 17** o superior  
- **MySQL Server**  
- **MySQL Workbench**  
- **Visual Studio Code** o **NetBeans**  
- Conector JDBC de MySQL (`mysql-connector-j-9.5.0.jar`)

---

## 🧩 Configuración de la Base de Datos

Ejecuta en **MySQL Workbench**:

```sql
DROP DATABASE IF EXISTS portal_avisos;
CREATE DATABASE portal_avisos;
USE portal_avisos;

CREATE TABLE usuario (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(100) NOT NULL UNIQUE,
  rol ENUM('profesor', 'estudiante') NOT NULL
);

CREATE TABLE aviso (
  id_aviso INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(150) NOT NULL,
  contenido TEXT NOT NULL,
  fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  id_profesor INT,
  FOREIGN KEY (id_profesor) REFERENCES usuario(id_usuario)
);

-- Inserta usuarios de prueba
INSERT INTO usuario (nombre, correo, rol) VALUES
('Profesor Juan', 'juan@uni.com', 'profesor'),
('Estudiante Ana', 'ana@uni.com', 'estudiante');

🖥️ Ejecución del Proyecto
1️⃣ Compilar

Desde la raíz del proyecto:

javac -cp "lib/mysql-connector-j-9.5.0.jar;src" -d bin src/database/*.java src/models/*.java src/dao/*.java src/ui/*.java

2️⃣ Ejecutar
java -cp "lib/mysql-connector-j-9.5.0.jar;bin" ui.LoginGUI

🧰 Tecnologías Utilizadas:

☕ Java 17
🧩 JDBC (Java Database Connectivity)
🗄️ MySQL 8+
🖥️ Swing (Interfaz gráfica)
🧠 Modelo DAO (Data Access Object)