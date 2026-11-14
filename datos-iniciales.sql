BEGIN;

INSERT INTO lab1.tipo_equipo (nombre, descripcion)
VALUES ('Computadora', 'Computadoras del laboratorio'),
       ('Impresora', 'Equipos de impresión'),
       ('Proyector', 'Equipos de proyección')
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO lab1.equipo (codigo, marca, estado, cantidad, id_tipo)
SELECT datos.codigo, datos.marca, datos.estado, datos.cantidad, tipo.id_tipo
FROM (VALUES
    ('PC-001', 'Dell', 'Disponible', 5, 'Computadora'),
    ('PC-002', 'HP', 'En mantenimiento', 2, 'Computadora'),
    ('PC-003', 'Lenovo', 'Fuera de servicio', 0, 'Computadora'),
    ('IMP-001', 'HP', 'Disponible', 3, 'Impresora'),
    ('IMP-002', 'Epson', 'En mantenimiento', 1, 'Impresora'),
    ('IMP-003', 'Canon', 'Fuera de servicio', 1, 'Impresora'),
    ('PRO-001', 'Epson', 'Disponible', 2, 'Proyector'),
    ('PRO-002', 'BenQ', 'En mantenimiento', 1, 'Proyector')
) AS datos(codigo, marca, estado, cantidad, nombre_tipo)
JOIN lab1.tipo_equipo AS tipo ON tipo.nombre = datos.nombre_tipo
ON CONFLICT (codigo) DO NOTHING;

COMMIT;
