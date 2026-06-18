-- ============================================================
-- Script inicial Oracle ADB - ms-doctor
-- Las tablas se crean via Hibernate (ddl-auto=update)
-- Este script inserta datos iniciales idempotentes
-- ============================================================

INSERT INTO especialidad (id, nombre, descripcion, activo)
  SELECT seq_especialidad.NEXTVAL, 'Medicina General', 'Atencion medica general', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM especialidad WHERE nombre = 'Medicina General');

INSERT INTO especialidad (id, nombre, descripcion, activo)
  SELECT seq_especialidad.NEXTVAL, 'Pediatria', 'Especialidad en salud infantil', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM especialidad WHERE nombre = 'Pediatria');

INSERT INTO especialidad (id, nombre, descripcion, activo)
  SELECT seq_especialidad.NEXTVAL, 'Cardiologia', 'Especialidad del corazon y sistema cardiovascular', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM especialidad WHERE nombre = 'Cardiologia');

INSERT INTO especialidad (id, nombre, descripcion, activo)
  SELECT seq_especialidad.NEXTVAL, 'Traumatologia', 'Especialidad del sistema musculo-esqueletico', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM especialidad WHERE nombre = 'Traumatologia');

INSERT INTO especialidad (id, nombre, descripcion, activo)
  SELECT seq_especialidad.NEXTVAL, 'Dermatologia', 'Especialidad de la piel', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM especialidad WHERE nombre = 'Dermatologia');

INSERT INTO especialidad (id, nombre, descripcion, activo)
  SELECT seq_especialidad.NEXTVAL, 'Ginecologia', 'Especialidad de salud femenina', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM especialidad WHERE nombre = 'Ginecologia');

INSERT INTO especialidad (id, nombre, descripcion, activo)
  SELECT seq_especialidad.NEXTVAL, 'Neurologia', 'Especialidad del sistema nervioso', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM especialidad WHERE nombre = 'Neurologia');

INSERT INTO especialidad (id, nombre, descripcion, activo)
  SELECT seq_especialidad.NEXTVAL, 'Psiquiatria', 'Especialidad de salud mental', 1 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM especialidad WHERE nombre = 'Psiquiatria');

INSERT INTO doctor (
    id, rut, nombre, apellido, email, telefono, numero_registro,
    especialidad_id, activo, fecha_contratacion
)
SELECT seq_doctor.NEXTVAL, '11222333-4', 'Ana', 'Gomez',
       'ana.gomez@clinica.local', '+56987654321', 'REG-MED-1001',
       (SELECT id FROM especialidad WHERE nombre = 'Medicina General' AND ROWNUM = 1),
       1, DATE '2022-03-01'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE rut = '11222333-4');

INSERT INTO doctor (
    id, rut, nombre, apellido, email, telefono, numero_registro,
    especialidad_id, activo, fecha_contratacion
)
SELECT seq_doctor.NEXTVAL, '99888777-6', 'Carlos', 'Rojas',
       'carlos.rojas@clinica.local', '+56912349876', 'REG-MED-1002',
       (SELECT id FROM especialidad WHERE nombre = 'Cardiologia' AND ROWNUM = 1),
       1, DATE '2021-08-10'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM doctor WHERE rut = '99888777-6');
