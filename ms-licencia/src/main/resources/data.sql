-- ============================================================
-- Script inicial Oracle ADB - ms-licencia
-- Las tablas se crean via Hibernate (ddl-auto=update)
-- Este script solo inserta los tipos de licencia iniciales
-- ============================================================

INSERT INTO tipo_licencia (id, codigo, nombre, descripcion, dias_maximos)
  SELECT seq_tipo_licencia.NEXTVAL, 'T1', 'Enfermedad o accidente comun', 'Licencia por enfermedad o accidente no laboral', 365 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM tipo_licencia WHERE codigo = 'T1');

INSERT INTO tipo_licencia (id, codigo, nombre, descripcion, dias_maximos)
  SELECT seq_tipo_licencia.NEXTVAL, 'T2', 'Accidente del trabajo o laboral', 'Accidente ocurrido en el trabajo o trayecto', 365 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM tipo_licencia WHERE codigo = 'T2');

INSERT INTO tipo_licencia (id, codigo, nombre, descripcion, dias_maximos)
  SELECT seq_tipo_licencia.NEXTVAL, 'T3', 'Medicina preventiva', 'Reposo preventivo indicado por medico', 60 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM tipo_licencia WHERE codigo = 'T3');

INSERT INTO tipo_licencia (id, codigo, nombre, descripcion, dias_maximos)
  SELECT seq_tipo_licencia.NEXTVAL, 'T4', 'Licencia maternal pre-natal', 'Reposo pre-parto', 42 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM tipo_licencia WHERE codigo = 'T4');

INSERT INTO tipo_licencia (id, codigo, nombre, descripcion, dias_maximos)
  SELECT seq_tipo_licencia.NEXTVAL, 'T5', 'Licencia maternal post-natal', 'Reposo post-parto', 84 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM tipo_licencia WHERE codigo = 'T5');

INSERT INTO tipo_licencia (id, codigo, nombre, descripcion, dias_maximos)
  SELECT seq_tipo_licencia.NEXTVAL, 'T6', 'Enfermedad grave del hijo menor', 'Cuidado de hijo menor con enfermedad grave', 30 FROM dual
  WHERE NOT EXISTS (SELECT 1 FROM tipo_licencia WHERE codigo = 'T6');
