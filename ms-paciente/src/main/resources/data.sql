-- ============================================================
-- Script inicial Oracle ADB - ms-paciente
-- Las tablas se crean via Hibernate (ddl-auto=update)
-- Este script inserta datos iniciales idempotentes
-- ============================================================

INSERT INTO prevision (id, nombre, tipo, descripcion, activo)
  SELECT seq_prevision.NEXTVAL, 'FONASA Tramo A', 'FONASA', 'Fonasa tramo A - Sin copago', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM prevision WHERE nombre = 'FONASA Tramo A');

INSERT INTO prevision (id, nombre, tipo, descripcion, activo)
  SELECT seq_prevision.NEXTVAL, 'FONASA Tramo B', 'FONASA', 'Fonasa tramo B', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM prevision WHERE nombre = 'FONASA Tramo B');

INSERT INTO prevision (id, nombre, tipo, descripcion, activo)
  SELECT seq_prevision.NEXTVAL, 'FONASA Tramo C', 'FONASA', 'Fonasa tramo C', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM prevision WHERE nombre = 'FONASA Tramo C');

INSERT INTO prevision (id, nombre, tipo, descripcion, activo)
  SELECT seq_prevision.NEXTVAL, 'FONASA Tramo D', 'FONASA', 'Fonasa tramo D', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM prevision WHERE nombre = 'FONASA Tramo D');

INSERT INTO prevision (id, nombre, tipo, descripcion, activo)
  SELECT seq_prevision.NEXTVAL, 'ISAPRE Cruz Blanca', 'ISAPRE', 'Isapre Cruz Blanca', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM prevision WHERE nombre = 'ISAPRE Cruz Blanca');

INSERT INTO prevision (id, nombre, tipo, descripcion, activo)
  SELECT seq_prevision.NEXTVAL, 'ISAPRE Banmedica', 'ISAPRE', 'Isapre Banmedica', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM prevision WHERE nombre = 'ISAPRE Banmedica');

INSERT INTO prevision (id, nombre, tipo, descripcion, activo)
  SELECT seq_prevision.NEXTVAL, 'Particular', 'PARTICULAR', 'Sin prevision', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM prevision WHERE nombre = 'Particular');

INSERT INTO paciente (
    id, rut, nombre, apellido, fecha_nacimiento, genero, email, telefono,
    direccion, prevision_id, activo, fecha_registro, edad, acompanado
)
SELECT seq_paciente.NEXTVAL, '12345678-9', 'Juan', 'Perez', DATE '1990-04-10',
       'MASCULINO', 'juan.perez@example.com', '+56912345678',
       'Av. Siempre Viva 123',
       (SELECT id FROM prevision WHERE nombre = 'FONASA Tramo B' AND ROWNUM = 1),
       1, SYSTIMESTAMP, 36, 0
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM paciente WHERE rut = '12345678-9');

INSERT INTO paciente (
    id, rut, nombre, apellido, fecha_nacimiento, genero, email, telefono,
    direccion, prevision_id, activo, fecha_registro, edad, acompanado
)
SELECT seq_paciente.NEXTVAL, '22333444-5', 'Maria', 'Lopez', DATE '2015-02-15',
       'FEMENINO', 'maria.lopez@example.com', '+56911223344',
       'Los Aromos 456',
       (SELECT id FROM prevision WHERE nombre = 'FONASA Tramo A' AND ROWNUM = 1),
       1, SYSTIMESTAMP, 11, 1
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM paciente WHERE rut = '22333444-5');
