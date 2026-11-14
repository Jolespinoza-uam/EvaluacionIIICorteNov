--CREATE DATABASE bd_laboratorio_orm;

CREATE SCHEMA lab1;

CREATE TABLE lab1.tipo_equipo(
	id_tipo SERIAL PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL UNIQUE,
	descripcion VARCHAR(150)
);

CREATE TABLE lab1.equipo(
	id_equipo SERIAL PRIMARY KEY,
	codigo VARCHAR(50) NOT NULL UNIQUE,
	marca VARCHAR(50) NOT NULL,
	estado VARCHAR(20) NOT NULL CHECK(estado IN ('Disponible', 'En mantenimiento', 'Fuera de servicio')),
	cantidad INT NOT NULL CHECK(cantidad >= 0),
	id_tipo INT NOT NULL REFERENCES lab1.tipo_equipo(id_tipo)
);

INSERT INTO lab1.tipo_equipo (nombre, descripcion)
VALUES
    ('Computadora', 'Computadoras del laboratorio'),
    ('Impresora', 'Equipos de impresión'),
    ('Proyector', 'Equipos de proyección');

INSERT INTO lab1.equipo (codigo, marca, estado, cantidad, id_tipo)
VALUES ('PC-001', 'Dell',   'Disponible',       5, 1),
    ('PC-002', 'HP',     'En mantenimiento', 2, 1),
    ('PC-003', 'Lenovo', 'Fuera de servicio', 0, 1),
    ('IMP-001', 'HP',     'Disponible',       3, 2),
    ('IMP-002', 'Epson',  'En mantenimiento', 1, 2),
    ('IMP-003', 'Canon',  'Fuera de servicio', 1, 2),
    ('PRO-001', 'Epson',  'Disponible',       2, 3),
    ('PRO-002', 'BenQ',   'En mantenimiento', 1, 3)