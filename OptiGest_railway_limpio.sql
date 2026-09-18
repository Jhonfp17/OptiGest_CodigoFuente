-- OptiGest: instalación limpia para Railway.
-- ATENCIÓN: borra por completo la base de datos OptiGest antes de crearla.
-- Crea la estructura actual, sus disparadores y deja como único usuario a LUIS MORA.

DROP DATABASE IF EXISTS `OptiGest`;
CREATE DATABASE `OptiGest` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `OptiGest`;

CREATE TABLE `Documento` (
  `id_documento` INT NOT NULL AUTO_INCREMENT,
  `descripcion_doc` VARCHAR(60) NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`id_documento`), UNIQUE KEY `uq_descripcion_doc` (`descripcion_doc`)
) ENGINE=InnoDB;

CREATE TABLE `Roles` (
  `idroles` INT NOT NULL AUTO_INCREMENT,
  `descripcion_roles` VARCHAR(60) NOT NULL,
  `tipo_acceso` ENUM('ADMINISTRADOR','PERSONAL_FIJO','TEMPORAL') NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`idroles`)
) ENGINE=InnoDB;

CREATE TABLE `Estado_Personal` (
  `id_estado` INT NOT NULL AUTO_INCREMENT,
  `descripcion_estado` VARCHAR(60) NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`id_estado`), UNIQUE KEY `uq_estado_personal` (`descripcion_estado`)
) ENGINE=InnoDB;

CREATE TABLE `Estado_Activo` (
  `idEstado_Activo` INT NOT NULL AUTO_INCREMENT,
  `descripcion_activo` VARCHAR(60) NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`idEstado_Activo`), UNIQUE KEY `uq_estado_activo` (`descripcion_activo`)
) ENGINE=InnoDB;

CREATE TABLE `Categorias` (
  `idCategorias` INT NOT NULL AUTO_INCREMENT,
  `descripcionCategoria` VARCHAR(60) NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`idCategorias`), UNIQUE KEY `uq_categoria` (`descripcionCategoria`)
) ENGINE=InnoDB;

CREATE TABLE `Dias` (
  `idDias` INT NOT NULL AUTO_INCREMENT,
  `descripcionDias` VARCHAR(20) NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`idDias`), UNIQUE KEY `uq_dia` (`descripcionDias`)
) ENGINE=InnoDB;

CREATE TABLE `Horarios` (
  `id_horarios` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(60) NOT NULL,
  `hora_ingreso` TIME NOT NULL, `hora_salida` TIME NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`id_horarios`)
) ENGINE=InnoDB;

CREATE TABLE `Proveedores` (
  `idProveedores` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL, `telefono` VARCHAR(20) NOT NULL,
  `direccion` VARCHAR(200) NOT NULL, `email` VARCHAR(100) NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`idProveedores`)
) ENGINE=InnoDB;

CREATE TABLE `HistorialCambios` (
  `id_historial` BIGINT NOT NULL AUTO_INCREMENT,
  `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `entidad` VARCHAR(60) NOT NULL, `id_registro` INT NOT NULL, `accion` VARCHAR(30) NOT NULL,
  `datos_antes` JSON NULL, `datos_despues` JSON NULL, `usuario_id` INT NULL,
  `usuario_nombre` VARCHAR(200) NOT NULL, `motivo` VARCHAR(500) NULL, `usuario_bd` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`id_historial`), KEY `idx_historial_entidad_registro` (`entidad`,`id_registro`), KEY `idx_historial_fecha` (`fecha`)
) ENGINE=InnoDB;

CREATE TABLE `Personal` (
  `id_personal` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(80) NOT NULL, `apellidos` VARCHAR(80) NOT NULL,
  `identificacion` VARCHAR(20) NOT NULL, `email` VARCHAR(100) NOT NULL,
  `telefono` VARCHAR(20) NOT NULL, `direccion` VARCHAR(200) NOT NULL,
  `clave` VARCHAR(255) NOT NULL, `observaciones` VARCHAR(500) NULL,
  `puede_acceder` TINYINT(1) NOT NULL DEFAULT 0,
  `debe_cambiar_clave` TINYINT(1) NOT NULL DEFAULT 0,
  `fecha_contratacion` DATE NULL,
  `Documento_id_documento` INT NOT NULL, `roles_idroles` INT NOT NULL,
  `Estado_Personal_id_estado` INT NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  `clave_temporal_expira_en` DATETIME NULL,
  PRIMARY KEY (`id_personal`), KEY `idx_personal_identificacion` (`identificacion`), KEY `idx_personal_email` (`email`),
  KEY `fk_personal_documento_idx` (`Documento_id_documento`), KEY `fk_personal_roles_idx` (`roles_idroles`), KEY `fk_personal_estado_idx` (`Estado_Personal_id_estado`),
  CONSTRAINT `fk_personal_documento` FOREIGN KEY (`Documento_id_documento`) REFERENCES `Documento` (`id_documento`) ON UPDATE CASCADE,
  CONSTRAINT `fk_personal_roles` FOREIGN KEY (`roles_idroles`) REFERENCES `Roles` (`idroles`) ON UPDATE CASCADE,
  CONSTRAINT `fk_personal_estado` FOREIGN KEY (`Estado_Personal_id_estado`) REFERENCES `Estado_Personal` (`id_estado`) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `Activos` (
  `id_activos` INT NOT NULL AUTO_INCREMENT,
  `codigo_act` VARCHAR(20) NOT NULL, `nombre_activos` VARCHAR(100) NOT NULL,
  `valor` DECIMAL(15,2) NOT NULL, `fecha_adquma` DATE NOT NULL,
  `fecha_devolucion` DATE NULL, `vida_util` INT NULL, `descripcion` VARCHAR(500) NULL,
  `Estado_Activo_idEstado_Activo` INT NOT NULL, `Categorias_idCategorias` INT NOT NULL, `Proveedores_idProveedores` INT NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`id_activos`), UNIQUE KEY `uq_codigo_act` (`codigo_act`),
  KEY `fk_activos_estado_idx` (`Estado_Activo_idEstado_Activo`), KEY `fk_activos_categoria_idx` (`Categorias_idCategorias`), KEY `fk_activos_proveedor_idx` (`Proveedores_idProveedores`),
  CONSTRAINT `fk_activos_estado` FOREIGN KEY (`Estado_Activo_idEstado_Activo`) REFERENCES `Estado_Activo` (`idEstado_Activo`) ON UPDATE CASCADE,
  CONSTRAINT `fk_activos_categoria` FOREIGN KEY (`Categorias_idCategorias`) REFERENCES `Categorias` (`idCategorias`) ON UPDATE CASCADE,
  CONSTRAINT `fk_activos_proveedor` FOREIGN KEY (`Proveedores_idProveedores`) REFERENCES `Proveedores` (`idProveedores`) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `Asignaciones` (
  `id_asignaciones` INT NOT NULL AUTO_INCREMENT,
  `fecha_asignacion` DATE NOT NULL, `fecha_devolucion` DATE NULL, `observaciones` VARCHAR(500) NULL,
  `Personal_id_personal` INT NOT NULL, `Activos_id_activos` INT NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL, `anulado` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_asignaciones`), KEY `fk_asignaciones_personal_idx` (`Personal_id_personal`), KEY `fk_asignaciones_activos_idx` (`Activos_id_activos`),
  CONSTRAINT `fk_asignaciones_personal` FOREIGN KEY (`Personal_id_personal`) REFERENCES `Personal` (`id_personal`) ON UPDATE CASCADE,
  CONSTRAINT `fk_asignaciones_activos` FOREIGN KEY (`Activos_id_activos`) REFERENCES `Activos` (`id_activos`) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `Mantenimiento` (
  `id_mantenimiento` INT NOT NULL AUTO_INCREMENT,
  `fecha_mante` DATE NOT NULL, `costo` DECIMAL(15,2) NULL, `descripcion` VARCHAR(500) NULL,
  `Activos_id_activos` INT NOT NULL, `Proveedores_idProveedores` INT NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`id_mantenimiento`), KEY `fk_mantenimiento_activos_idx` (`Activos_id_activos`), KEY `fk_mantenimiento_proveedores_idx` (`Proveedores_idProveedores`),
  CONSTRAINT `fk_mantenimiento_activos` FOREIGN KEY (`Activos_id_activos`) REFERENCES `Activos` (`id_activos`) ON UPDATE CASCADE,
  CONSTRAINT `fk_mantenimiento_proveedores` FOREIGN KEY (`Proveedores_idProveedores`) REFERENCES `Proveedores` (`idProveedores`) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `Programacion_Personal` (
  `idProgramacion_Personal` INT NOT NULL AUTO_INCREMENT,
  `descripcion_programacion` VARCHAR(200) NULL, `fecha_desde` DATE NULL, `fecha_hasta` DATE NULL,
  `Dias_idDias` INT NOT NULL, `Personal_id_personal` INT NOT NULL, `Horarios_id_horarios` INT NOT NULL,
  `activo` TINYINT(1) NOT NULL DEFAULT 1, `fecha_baja` DATETIME NULL,
  PRIMARY KEY (`idProgramacion_Personal`), KEY `fk_programacion_dias_idx` (`Dias_idDias`), KEY `fk_programacion_personal_idx` (`Personal_id_personal`), KEY `fk_programacion_horarios_idx` (`Horarios_id_horarios`),
  CONSTRAINT `fk_programacion_dias` FOREIGN KEY (`Dias_idDias`) REFERENCES `Dias` (`idDias`) ON UPDATE CASCADE,
  CONSTRAINT `fk_programacion_personal` FOREIGN KEY (`Personal_id_personal`) REFERENCES `Personal` (`id_personal`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_programacion_horarios` FOREIGN KEY (`Horarios_id_horarios`) REFERENCES `Horarios` (`id_horarios`) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `MensajesContacto` (
  `id_mensaje` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NOT NULL, `apellidos` VARCHAR(150) NOT NULL DEFAULT '', `documento` VARCHAR(45) NOT NULL DEFAULT '', `tipo_documento` VARCHAR(45) NOT NULL DEFAULT '',
  `email` VARCHAR(150) NOT NULL, `telefono` VARCHAR(45) NOT NULL DEFAULT '', `direccion` VARCHAR(150) NOT NULL DEFAULT '',
  `tipo_solicitud` VARCHAR(45) NOT NULL DEFAULT 'OTRO', `usuario_deseado` VARCHAR(45) NOT NULL DEFAULT '',
  `asunto` VARCHAR(150) NOT NULL, `mensaje` TEXT NOT NULL, `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `atendido` TINYINT(1) NOT NULL DEFAULT 0, `Personal_id_personal` INT NULL, `fecha_atencion` DATETIME NULL,
  PRIMARY KEY (`id_mensaje`), KEY `idx_mensajes_fecha` (`fecha`), KEY `idx_mensajes_personal` (`Personal_id_personal`)
) ENGINE=InnoDB;

-- Catálogos mínimos requeridos por los formularios. No son registros operativos.
INSERT INTO `Documento` (`id_documento`,`descripcion_doc`) VALUES (1,'CÉDULA'),(2,'PASAPORTE'),(3,'CÉDULA EXTRANJERÍA');
INSERT INTO `Roles` (`idroles`,`descripcion_roles`,`tipo_acceso`) VALUES (1,'ADMINISTRADOR','ADMINISTRADOR'),(2,'PERSONAL FIJO','PERSONAL_FIJO'),(3,'TEMPORAL','TEMPORAL');
INSERT INTO `Estado_Personal` (`id_estado`,`descripcion_estado`) VALUES (1,'ACTIVO'),(2,'RETIRADO'),(3,'VACACIONES');
INSERT INTO `Estado_Activo` (`idEstado_Activo`,`descripcion_activo`) VALUES (1,'DISPONIBLE'),(2,'NO DISPONIBLE'),(3,'EN MANTENIMIENTO');
INSERT INTO `Categorias` (`idCategorias`,`descripcionCategoria`) VALUES (1,'ELECTRÓNICOS'),(2,'HERRAMIENTAS');
INSERT INTO `Dias` (`idDias`,`descripcionDias`) VALUES (1,'LUNES'),(2,'MARTES'),(3,'MIÉRCOLES'),(4,'JUEVES'),(5,'VIERNES'),(6,'SÁBADO'),(7,'DOMINGO');
INSERT INTO `Horarios` (`id_horarios`,`descripcion`,`hora_ingreso`,`hora_salida`) VALUES (1,'ADMINISTRATIVO','08:00:00','17:00:00'),(2,'PERSONAL FIJO','08:00:00','17:00:00'),(3,'TEMPORAL','07:00:00','16:30:00');
INSERT INTO `Proveedores` (`idProveedores`,`nombre`,`telefono`,`direccion`,`email`) VALUES (1,'PROVEEDOR POR DEFINIR','0000000000','POR DEFINIR',NULL);

-- Único usuario operativo: administrador maestro LUIS MORA.
-- La clave inicial es 12345. Cámbiala inmediatamente al iniciar sesión.
INSERT INTO `Personal` (`id_personal`,`nombre`,`apellidos`,`identificacion`,`email`,`telefono`,`direccion`,`clave`,`observaciones`,`puede_acceder`,`debe_cambiar_clave`,`fecha_contratacion`,`Documento_id_documento`,`roles_idroles`,`Estado_Personal_id_estado`,`activo`)
VALUES (3,'LUIS','MORA','1000222333','LUIZ@GMAIL.COM','3219087654','CLL 13S #20-01','pbkdf2_sha256$120000$EfpBildmjHhLRATQmcL23A==$HvWx9oADYsLeniOEwCSz7gGqFc8xrMnrme7sNPiNAzE=','ADMINISTRADOR MAESTRO',1,0,CURDATE(),1,1,1,1);

DELIMITER //
CREATE TRIGGER `ai_historial_activos` AFTER INSERT ON `Activos` FOR EACH ROW BEGIN
  INSERT INTO HistorialCambios(entidad,id_registro,accion,datos_despues,usuario_id,usuario_nombre,motivo,usuario_bd) VALUES ('Activos',NEW.id_activos,'CREADO',JSON_OBJECT('codigo',NEW.codigo_act,'nombre',NEW.nombre_activos),@aud_usuario_id,COALESCE(@aud_usuario_nombre,'SISTEMA'),COALESCE(NULLIF(@aud_motivo,''),'REGISTRO DE ACTIVO'),CURRENT_USER());
END//
CREATE TRIGGER `au_historial_activos` AFTER UPDATE ON `Activos` FOR EACH ROW BEGIN
  IF NOT(OLD.codigo_act <=> NEW.codigo_act) OR NOT(OLD.nombre_activos <=> NEW.nombre_activos) OR NOT(OLD.valor <=> NEW.valor) OR NOT(OLD.Estado_Activo_idEstado_Activo <=> NEW.Estado_Activo_idEstado_Activo) OR NOT(OLD.activo <=> NEW.activo) THEN
    INSERT INTO HistorialCambios(entidad,id_registro,accion,datos_antes,datos_despues,usuario_id,usuario_nombre,motivo,usuario_bd) VALUES ('Activos',NEW.id_activos,IF(NEW.activo=0 AND OLD.activo=1,'RETIRADO','ACTUALIZADO'),JSON_OBJECT('codigo',OLD.codigo_act,'nombre',OLD.nombre_activos,'activo',OLD.activo),JSON_OBJECT('codigo',NEW.codigo_act,'nombre',NEW.nombre_activos,'activo',NEW.activo),@aud_usuario_id,COALESCE(@aud_usuario_nombre,'SISTEMA'),COALESCE(NULLIF(@aud_motivo,''),'SIN MOTIVO REGISTRADO'),CURRENT_USER());
  END IF;
END//
CREATE TRIGGER `ai_historial_personal` AFTER INSERT ON `Personal` FOR EACH ROW BEGIN
  INSERT INTO HistorialCambios(entidad,id_registro,accion,datos_despues,usuario_id,usuario_nombre,motivo,usuario_bd) VALUES ('Personal',NEW.id_personal,'CREADO',JSON_OBJECT('identificacion',NEW.identificacion,'nombre',NEW.nombre),@aud_usuario_id,COALESCE(@aud_usuario_nombre,'SISTEMA'),COALESCE(NULLIF(@aud_motivo,''),'REGISTRO DE PERSONAL'),CURRENT_USER());
END//
CREATE TRIGGER `au_historial_personal` AFTER UPDATE ON `Personal` FOR EACH ROW BEGIN
  IF NOT(OLD.identificacion <=> NEW.identificacion) OR NOT(OLD.nombre <=> NEW.nombre) OR NOT(OLD.apellidos <=> NEW.apellidos) OR NOT(OLD.email <=> NEW.email) OR NOT(OLD.clave <=> NEW.clave) OR NOT(OLD.roles_idroles <=> NEW.roles_idroles) OR NOT(OLD.Estado_Personal_id_estado <=> NEW.Estado_Personal_id_estado) OR NOT(OLD.puede_acceder <=> NEW.puede_acceder) OR NOT(OLD.activo <=> NEW.activo) THEN
    INSERT INTO HistorialCambios(entidad,id_registro,accion,datos_antes,datos_despues,usuario_id,usuario_nombre,motivo,usuario_bd) VALUES ('Personal',NEW.id_personal,IF(NEW.activo=0 AND OLD.activo=1,'DESACTIVADO','ACTUALIZADO'),JSON_OBJECT('nombre',OLD.nombre,'activo',OLD.activo),JSON_OBJECT('nombre',NEW.nombre,'activo',NEW.activo),@aud_usuario_id,COALESCE(@aud_usuario_nombre,'SISTEMA'),COALESCE(NULLIF(@aud_motivo,''),'SIN MOTIVO REGISTRADO'),CURRENT_USER());
  END IF;
END//
CREATE TRIGGER `au_historial_asignaciones` AFTER UPDATE ON `Asignaciones` FOR EACH ROW BEGIN
  IF NOT(OLD.fecha_asignacion <=> NEW.fecha_asignacion) OR NOT(OLD.fecha_devolucion <=> NEW.fecha_devolucion) OR NOT(OLD.observaciones <=> NEW.observaciones) OR NOT(OLD.anulado <=> NEW.anulado) THEN
    INSERT INTO HistorialCambios(entidad,id_registro,accion,usuario_id,usuario_nombre,motivo,usuario_bd) VALUES ('Asignaciones',NEW.id_asignaciones,IF(NEW.anulado=1 AND OLD.anulado=0,'ANULADO',IF(OLD.fecha_devolucion IS NULL AND NEW.fecha_devolucion IS NOT NULL,'DEVUELTO','ACTUALIZADO')),@aud_usuario_id,COALESCE(@aud_usuario_nombre,'SISTEMA'),COALESCE(NULLIF(@aud_motivo,''),'SIN MOTIVO REGISTRADO'),CURRENT_USER());
  END IF;
END//
CREATE TRIGGER `au_historial_mantenimiento` AFTER UPDATE ON `Mantenimiento` FOR EACH ROW BEGIN
  INSERT INTO HistorialCambios(entidad,id_registro,accion,usuario_id,usuario_nombre,motivo,usuario_bd) VALUES ('Mantenimiento',NEW.id_mantenimiento,IF(NEW.activo=0 AND OLD.activo=1,'DESACTIVADO','ACTUALIZADO'),@aud_usuario_id,COALESCE(@aud_usuario_nombre,'SISTEMA'),COALESCE(NULLIF(@aud_motivo,''),'SIN MOTIVO REGISTRADO'),CURRENT_USER());
END//
CREATE TRIGGER `au_historial_programacion` AFTER UPDATE ON `Programacion_Personal` FOR EACH ROW BEGIN
  INSERT INTO HistorialCambios(entidad,id_registro,accion,usuario_id,usuario_nombre,motivo,usuario_bd) VALUES ('Programacion_Personal',NEW.idProgramacion_Personal,IF(NEW.activo=0 AND OLD.activo=1,'DESACTIVADO','ACTUALIZADO'),@aud_usuario_id,COALESCE(@aud_usuario_nombre,'SISTEMA'),COALESCE(NULLIF(@aud_motivo,''),'SIN MOTIVO REGISTRADO'),CURRENT_USER());
END//
CREATE TRIGGER `au_historial_roles` AFTER UPDATE ON `Roles` FOR EACH ROW BEGIN
  INSERT INTO HistorialCambios(entidad,id_registro,accion,usuario_id,usuario_nombre,motivo,usuario_bd) VALUES ('Roles',NEW.idroles,IF(NEW.activo=0 AND OLD.activo=1,'DESACTIVADO','ACTUALIZADO'),@aud_usuario_id,COALESCE(@aud_usuario_nombre,'SISTEMA'),COALESCE(NULLIF(@aud_motivo,''),'SIN MOTIVO REGISTRADO'),CURRENT_USER());
END//
CREATE TRIGGER `au_historial_proveedores` AFTER UPDATE ON `Proveedores` FOR EACH ROW BEGIN
  INSERT INTO HistorialCambios(entidad,id_registro,accion,usuario_id,usuario_nombre,motivo,usuario_bd) VALUES ('Proveedores',NEW.idProveedores,IF(NEW.activo=0 AND OLD.activo=1,'DESACTIVADO','ACTUALIZADO'),@aud_usuario_id,COALESCE(@aud_usuario_nombre,'SISTEMA'),COALESCE(NULLIF(@aud_motivo,''),'SIN MOTIVO REGISTRADO'),CURRENT_USER());
END//
DELIMITER ;

-- Verificación esperada: un único registro operativo en Personal y ninguno en Activos o MensajesContacto.
SELECT id_personal, nombre, apellidos, identificacion FROM Personal;
SELECT COUNT(*) AS total_activos FROM Activos;
SELECT COUNT(*) AS total_solicitudes FROM MensajesContacto;
