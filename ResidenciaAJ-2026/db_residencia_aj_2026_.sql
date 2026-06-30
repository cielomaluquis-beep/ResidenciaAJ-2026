CREATE DATABASE IF NOT EXISTS `db_residencia_aj_2026` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `db_residencia_aj_2026`;

SET FOREIGN_KEY_CHECKS=0;

-- ==========================================
-- MÓDULO DE SEGURIDAD Y ACCESO (RBAC)
-- ==========================================

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id_rol` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `descripcion` VARCHAR(150),
  `estado` CHAR(1) DEFAULT '1',
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `uq_rol_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `modulos`;
CREATE TABLE `modulos` (
  `id_modulo` INT NOT NULL AUTO_INCREMENT,
  `id_modulo_padre` INT DEFAULT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  `ruta` VARCHAR(100),
  `icono` VARCHAR(50),
  `orden` INT DEFAULT 0,
  PRIMARY KEY (`id_modulo`),
  KEY `fk_modulo_padre` (`id_modulo_padre`),
  CONSTRAINT `fk_modulo_padre` FOREIGN KEY (`id_modulo_padre`) REFERENCES `modulos` (`id_modulo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `permisos_rol_modulo`;
CREATE TABLE `permisos_rol_modulo` (
  `id_permiso` INT NOT NULL AUTO_INCREMENT,
  `ver_modulo` TINYINT(1) DEFAULT 0,
  `p_view` TINYINT(1) DEFAULT 0,
  `p_create` TINYINT(1) DEFAULT 0,
  `p_edit` TINYINT(1) DEFAULT 0,
  `p_delete` TINYINT(1) DEFAULT 0,
  `id_rol` INT NOT NULL,
  `id_modulo` INT NOT NULL,
  PRIMARY KEY (`id_permiso`),
  KEY `fk_permiso_rol` (`id_rol`),
  KEY `fk_permiso_modulo` (`id_modulo`),
  CONSTRAINT `fk_permiso_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`) ON DELETE CASCADE,
  CONSTRAINT `fk_permiso_modulo` FOREIGN KEY (`id_modulo`) REFERENCES `modulos` (`id_modulo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `contrasena` VARCHAR(255) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `habilitado` TINYINT(1) DEFAULT 1,
  `id_rol` INT NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `uq_usuario_email` (`email`),
  KEY `fk_usuario_rol` (`id_rol`),
  CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================================
-- MÓDULO DE NEGOCIO (RESIDENCIA/ALQUILERES)
-- ==========================================

DROP TABLE IF EXISTS `clientes`;
CREATE TABLE `clientes` (
  `id_cliente` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `ap_paterno` VARCHAR(50) NOT NULL,
  `ap_materno` VARCHAR(50),
  `nro_documento` VARCHAR(15) NOT NULL,
  `telefono` VARCHAR(15),
  `email` VARCHAR(100),
  `fecha_nac` DATE,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `uq_cliente_doc` (`nro_documento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `habitaciones`;
CREATE TABLE `habitaciones` (
  `id_habitacion` INT NOT NULL AUTO_INCREMENT,
  `numero` VARCHAR(10) NOT NULL,
  `tipo` VARCHAR(30) NOT NULL,
  `precio` DECIMAL(10,2) NOT NULL,
  `piso` INT,
  `img_habitacion` VARCHAR(255),
  `estado` CHAR(1) DEFAULT '1',
  PRIMARY KEY (`id_habitacion`),
  UNIQUE KEY `uq_habitacion_num` (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `tipo_contrato`;
CREATE TABLE `tipo_contrato` (
  `id_tipoContrato` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(30) NOT NULL,
  `dias_duracion` INT NOT NULL,
  `estado` CHAR(1) DEFAULT '1',
  PRIMARY KEY (`id_tipoContrato`),
  UNIQUE KEY `uq_tipocontrato_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `contrato`;
CREATE TABLE `contrato` (
  `id_contrato` INT NOT NULL AUTO_INCREMENT,
  `fecha_inicio` DATE NOT NULL,
  `fecha_fin` DATE,
  `estado` CHAR(1) DEFAULT '1',
  `id_tipoContrato` INT NOT NULL,
  `id_cliente` INT NOT NULL,
  `id_habitacion` INT NOT NULL,
  `id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_contrato`),
  KEY `fk_contrato_tipo` (`id_tipoContrato`),
  KEY `fk_contrato_cliente` (`id_cliente`),
  KEY `fk_contrato_habitacion` (`id_habitacion`),
  KEY `fk_contrato_usuario` (`id_usuario`),
  CONSTRAINT `fk_contrato_tipo` FOREIGN KEY (`id_tipoContrato`) REFERENCES `tipo_contrato` (`id_tipoContrato`),
  CONSTRAINT `fk_contrato_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  CONSTRAINT `fk_contrato_habitacion` FOREIGN KEY (`id_habitacion`) REFERENCES `habitaciones` (`id_habitacion`),
  CONSTRAINT `fk_contrato_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `reserva`;
CREATE TABLE `reserva` (
  `id_reserva` INT NOT NULL AUTO_INCREMENT,
  `fecha_reserva` DATE NOT NULL,
  `fecha_posibleIni` DATE,
  `estado` CHAR(1) DEFAULT '1',
  `id_cliente` INT NOT NULL,
  `id_habitacion` INT NOT NULL,
  `id_usuario` INT,
  PRIMARY KEY (`id_reserva`),
  KEY `fk_reserva_cliente` (`id_cliente`),
  KEY `fk_reserva_habitacion` (`id_habitacion`),
  KEY `fk_reserva_usuario` (`id_usuario`),
  CONSTRAINT `fk_reserva_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`),
  CONSTRAINT `fk_reserva_habitacion` FOREIGN KEY (`id_habitacion`) REFERENCES `habitaciones` (`id_habitacion`),
  CONSTRAINT `fk_reserva_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `pagos`;
CREATE TABLE `pagos` (
  `id_pago` INT NOT NULL AUTO_INCREMENT,
  `monto` DECIMAL(10,2) NOT NULL,
  `fecha` DATE NOT NULL,
  `metodo_pago` VARCHAR(50),
  `concepto` VARCHAR(100),
  `img_comprobante` VARCHAR(255),
  `estado` CHAR(1) DEFAULT '1',
  `id_reserva` INT DEFAULT NULL,
  `id_contrato` INT DEFAULT NULL,
  `id_usuario` INT NOT NULL,
  PRIMARY KEY (`id_pago`),
  KEY `fk_pago_reserva` (`id_reserva`),
  KEY `fk_pago_contrato` (`id_contrato`),
  KEY `fk_pago_usuario` (`id_usuario`),
  CONSTRAINT `fk_pago_reserva` FOREIGN KEY (`id_reserva`) REFERENCES `reserva` (`id_reserva`),
  CONSTRAINT `fk_pago_contrato` FOREIGN KEY (`id_contrato`) REFERENCES `contrato` (`id_contrato`),
  CONSTRAINT `fk_pago_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS=1;

-- INSERCIÓN DE DATOS BÁSICOS PARA PRUEBAS (Opcional pero recomendado)
INSERT INTO `roles` (`nombre`, `descripcion`) VALUES 
('ADMIN', 'Administrador total del sistema'),
('RECEPCIONISTA', 'Personal de recepción que gestiona clientes y cobros');

INSERT INTO `usuarios` (`email`, `contrasena`, `nombre`, `habilitado`, `id_rol`) VALUES 
('admin@residencia.com', '$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 'Administrador Principal', 1, 1);

INSERT INTO `tipo_contrato` (`nombre`, `dias_duracion`) VALUES 
('Quincenal', 15),
('Mensual', 30),
('Semestral', 180),
('Anual', 365);
