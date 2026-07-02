-- Script para establecer los permisos exactos según rol (ADMIN, GERENTE, CLIENTE)
-- Asegúrate de correr este script en tu base de datos para que la matriz de seguridad funcione correctamente.

-- 1. Limpiamos permisos anteriores para evitar duplicados
DELETE FROM permisos_rol_modulo;

-- 2. Aseguramos que los roles existan con los IDs correctos (1=ADMIN, 2=GERENTE, 3=CLIENTE)
-- (Si ya existen con otros IDs, deberás ajustar los números en los INSERTS de abajo)
INSERT IGNORE INTO roles (id_rol, nombre, descripcion) VALUES (1, 'ADMIN', 'Administrador Total');
INSERT IGNORE INTO roles (id_rol, nombre, descripcion) VALUES (2, 'GERENTE', 'Gerente General');
INSERT IGNORE INTO roles (id_rol, nombre, descripcion) VALUES (3, 'CLIENTE', 'Estudiante / Residente');

-- 3. Aseguramos que los módulos existan
-- Módulos típicos según la imagen: Clientes (1), Contratos (2), Dashboard (3), Habitaciones (4), Notificaciones (5), Pagos (6), Reservas (7), Seguridad (8)
INSERT IGNORE INTO modulos (id_modulo, nombre) VALUES (1, 'Clientes');
INSERT IGNORE INTO modulos (id_modulo, nombre) VALUES (2, 'Contratos');
INSERT IGNORE INTO modulos (id_modulo, nombre) VALUES (3, 'Dashboard');
INSERT IGNORE INTO modulos (id_modulo, nombre) VALUES (4, 'Habitaciones');
INSERT IGNORE INTO modulos (id_modulo, nombre) VALUES (5, 'Notificaciones');
INSERT IGNORE INTO modulos (id_modulo, nombre) VALUES (6, 'Pagos');
INSERT IGNORE INTO modulos (id_modulo, nombre) VALUES (7, 'Reservas');
INSERT IGNORE INTO modulos (id_modulo, nombre) VALUES (8, 'Seguridad');

-- ==========================================
-- A) PERMISOS ROL: ADMIN (id_rol = 1)
-- Tiene Acceso, Ver, Crear, Editar y Eliminar en TODOS los módulos
-- ==========================================
INSERT INTO permisos_rol_modulo (ver_modulo, p_view, p_create, p_edit, p_delete, id_rol, id_modulo) VALUES 
(1, 1, 1, 1, 1, 1, 1), -- Clientes
(1, 1, 1, 1, 1, 1, 2), -- Contratos
(1, 1, 1, 1, 1, 1, 3), -- Dashboard
(1, 1, 1, 1, 1, 1, 4), -- Habitaciones
(1, 1, 1, 1, 1, 1, 5), -- Notificaciones
(1, 1, 1, 1, 1, 1, 6), -- Pagos
(1, 1, 1, 1, 1, 1, 7), -- Reservas
(1, 1, 1, 1, 1, 1, 8); -- Seguridad

-- ==========================================
-- B) PERMISOS ROL: GERENTE (id_rol = 2)
-- Acceso de Solo Lectura (Ver) a los módulos operativos, pero acceso total al Dashboard y Reportes
-- No puede crear, editar ni eliminar registros operativos (eso lo hace el Admin).
-- ==========================================
INSERT INTO permisos_rol_modulo (ver_modulo, p_view, p_create, p_edit, p_delete, id_rol, id_modulo) VALUES 
(1, 1, 0, 0, 0, 2, 1), -- Clientes (Solo ver)
(1, 1, 0, 0, 0, 2, 2), -- Contratos (Solo ver)
(1, 1, 1, 1, 1, 2, 3), -- Dashboard (Total acceso)
(1, 1, 0, 0, 0, 2, 4), -- Habitaciones (Solo ver)
(1, 1, 0, 0, 0, 2, 5), -- Notificaciones (Solo ver)
(1, 1, 0, 0, 0, 2, 6), -- Pagos (Solo ver)
(1, 1, 0, 0, 0, 2, 7), -- Reservas (Solo ver)
(0, 0, 0, 0, 0, 2, 8); -- Seguridad (Sin acceso)

-- ==========================================
-- C) PERMISOS ROL: CLIENTE (id_rol = 3)
-- Solo puede ver y crear (pagos/reservas), pero NO puede editar ni eliminar.
-- NO tiene acceso a Clientes, Contratos, Dashboard ni Seguridad.
-- ==========================================
INSERT INTO permisos_rol_modulo (ver_modulo, p_view, p_create, p_edit, p_delete, id_rol, id_modulo) VALUES 
(0, 0, 0, 0, 0, 3, 1), -- Clientes (Sin acceso)
(0, 0, 0, 0, 0, 3, 2), -- Contratos (Sin acceso)
(0, 0, 0, 0, 0, 3, 3), -- Dashboard (Sin acceso)
(1, 1, 0, 0, 0, 3, 4), -- Habitaciones (Ver Catálogo)
(1, 1, 1, 0, 0, 3, 5), -- Notificaciones (Ver Alertas y Crear Incidencias)
(1, 1, 1, 0, 0, 3, 6), -- Pagos (Ver sus pagos y Crear/Abonar)
(1, 1, 1, 0, 0, 3, 7), -- Reservas (Ver sus reservas y Crear solicitud)
(0, 0, 0, 0, 0, 3, 8); -- Seguridad (Sin acceso)
