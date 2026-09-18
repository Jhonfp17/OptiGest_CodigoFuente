-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema OptiGest
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema OptiGest
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `OptiGest` DEFAULT CHARACTER SET utf8 ;
USE `OptiGest` ;

-- -----------------------------------------------------
-- Table `OptiGest`.`Documento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Documento` (
  `id_documento` INT NOT NULL AUTO_INCREMENT,
  `descripcion_doc` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_documento`),
  UNIQUE INDEX `descripcion_doc_UNIQUE` (`descripcion_doc` ) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Roles` (
  `idroles` INT NOT NULL AUTO_INCREMENT,
  `descripcion_roles` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idroles`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Estado_Personal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Estado_Personal` (
  `id_estado` INT NOT NULL AUTO_INCREMENT,
  `descripcion_estado` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_estado`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Personal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Personal` (
  `id_personal` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellidos` VARCHAR(45) NOT NULL,
  `identificacion` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `telefono` VARCHAR(45) NOT NULL,
  `direccion` VARCHAR(45) NOT NULL,
  `clave` VARCHAR(100) NOT NULL,
  `observaciones` VARCHAR(200) NULL,
  `Documento_id_documento` INT NOT NULL,
  `roles_idroles` INT NOT NULL,
  `Estado_Personal_id_estado` INT NOT NULL,
  PRIMARY KEY (`id_personal`),
  INDEX `idx_personal_identificacion` (`identificacion` ) ,
  INDEX `idx_personal_email` (`email` ) ,
  INDEX `fk_Personal_Documento1_idx` (`Documento_id_documento` ) ,
  INDEX `fk_Personal_roles1_idx` (`roles_idroles` ) ,
  INDEX `fk_Personal_Estado_Personal1_idx` (`Estado_Personal_id_estado` ) ,
  CONSTRAINT `fk_Personal_Documento1`
    FOREIGN KEY (`Documento_id_documento`)
    REFERENCES `OptiGest`.`Documento` (`id_documento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Personal_roles1`
    FOREIGN KEY (`roles_idroles`)
    REFERENCES `OptiGest`.`Roles` (`idroles`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Personal_Estado_Personal1`
    FOREIGN KEY (`Estado_Personal_id_estado`)
    REFERENCES `OptiGest`.`Estado_Personal` (`id_estado`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Estado_Activo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Estado_Activo` (
  `idEstado_Activo` INT NOT NULL AUTO_INCREMENT,
  `descripcion_activo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEstado_Activo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Categorias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Categorias` (
  `idCategorias` INT NOT NULL AUTO_INCREMENT,
  `descripcionCategoria` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCategorias`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Proveedores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Proveedores` (
  `idProveedores` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `telefono` VARCHAR(45) NOT NULL,
  `direccion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idProveedores`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Activos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Activos` (
  `id_activos` INT NOT NULL AUTO_INCREMENT,
  `codigo_act` VARCHAR(45) NOT NULL,
  `nombre_activos` VARCHAR(45) NOT NULL,
  `valor` VARCHAR(45) NOT NULL,
  `fecha_adquma` VARCHAR(45) NOT NULL,
  `fecha_devolucion` VARCHAR(45) NOT NULL,
  `vida_util` VARCHAR(45) NOT NULL,
  `Estado_Activo_idEstado_Activo` INT NOT NULL,
  `Categorias_idCategorias` INT NOT NULL,
  `Proveedores_idProveedores` INT NOT NULL,
  PRIMARY KEY (`id_activos`),
  UNIQUE INDEX `fecha_adquma_UNIQUE` (`fecha_adquma` ) ,
  UNIQUE INDEX `fecha_devolucion_UNIQUE` (`fecha_devolucion` ) ,
  UNIQUE INDEX `codigo_act_UNIQUE` (`codigo_act` ) ,
  INDEX `fk_Activos_Estado_Activo1_idx` (`Estado_Activo_idEstado_Activo` ) ,
  INDEX `fk_Activos_Categorias1_idx` (`Categorias_idCategorias` ) ,
  INDEX `fk_Activos_Proveedores1_idx` (`Proveedores_idProveedores` ) ,
  CONSTRAINT `fk_Activos_Estado_Activo1`
    FOREIGN KEY (`Estado_Activo_idEstado_Activo`)
    REFERENCES `OptiGest`.`Estado_Activo` (`idEstado_Activo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Activos_Categorias1`
    FOREIGN KEY (`Categorias_idCategorias`)
    REFERENCES `OptiGest`.`Categorias` (`idCategorias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Activos_Proveedores1`
    FOREIGN KEY (`Proveedores_idProveedores`)
    REFERENCES `OptiGest`.`Proveedores` (`idProveedores`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.` Asignaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Asignaciones` (
  `id_asignaciones` INT NOT NULL,
  `fecha_asignaciones` VARCHAR(45) NOT NULL,
  `fecha_devolucion` VARCHAR(45) NOT NULL,
  `observaciones` VARCHAR(45) NOT NULL,
  `cantidad` VARCHAR(45) NOT NULL,
  `Personal_id_personal` INT NOT NULL,
  `Activos_id_activos` INT NOT NULL,
  PRIMARY KEY (`id_asignaciones`),
  UNIQUE INDEX `id Asignaciones_UNIQUE` (`id_asignaciones` ) ,
  UNIQUE INDEX `fecha_asignaciones_UNIQUE` (`fecha_asignaciones` ) ,
  UNIQUE INDEX `fecha_devolucion_UNIQUE` (`fecha_devolucion` ) ,
  UNIQUE INDEX `observaciones_UNIQUE` (`observaciones` ) ,
  INDEX `fk_ Asignaciones_Personal_idx` (`Personal_id_personal` ) ,
  INDEX `fk_ Asignaciones_Activos1_idx` (`Activos_id_activos` ) ,
  CONSTRAINT `fk_ Asignaciones_Personal`
    FOREIGN KEY (`Personal_id_personal`)
    REFERENCES `OptiGest`.`Personal` (`id_personal`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ Asignaciones_Activos1`
    FOREIGN KEY (`Activos_id_activos`)
    REFERENCES `OptiGest`.`Activos` (`id_activos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Mantenimiento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Mantenimiento` (
  `id_mantenimiento` INT NOT NULL AUTO_INCREMENT,
  `fecha_mante` VARCHAR(45) NOT NULL,
  `costo` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(45) NOT NULL,
  `Activos_id_activos` INT NOT NULL,
  PRIMARY KEY (`id_mantenimiento`),
  UNIQUE INDEX `ecffsf_UNIQUE` (`fecha_mante` ) ,
  UNIQUE INDEX `idMantenimiento_UNIQUE` (`id_mantenimiento` ) ,
  UNIQUE INDEX `costo_UNIQUE` (`costo` ) ,
  UNIQUE INDEX `descripcion_UNIQUE` (`descripcion` ) ,
  INDEX `fk_Mantenimiento_Activos1_idx` (`Activos_id_activos` ) ,
  CONSTRAINT `fk_Mantenimiento_Activos1`
    FOREIGN KEY (`Activos_id_activos`)
    REFERENCES `OptiGest`.`Activos` (`id_activos`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Horarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Horarios` (
  `id_horarios` INT NOT NULL AUTO_INCREMENT,
  `fecha_ingreso` VARCHAR(45) NOT NULL,
  `fecha_salida` VARCHAR(45) NOT NULL,
  `Horarioscod` INT NOT NULL,
  PRIMARY KEY (`id_horarios`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Dias`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Dias` (
  `idDias` INT NOT NULL AUTO_INCREMENT,
  `descripcionDias` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDias`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `OptiGest`.`Programacion_Personal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `OptiGest`.`Programacion_Personal` (
  `idProgramacion_Personal` INT NOT NULL AUTO_INCREMENT,
  `descripcion_progracion` VARCHAR(45) NOT NULL,
  `Dias_idDias` INT NOT NULL,
  `Personal_id_personal` INT NOT NULL,
  `Horarios_id_horarios` INT NOT NULL,
  PRIMARY KEY (`idProgramacion_Personal`),
  INDEX `fk_Programacion_Personal_Dias1_idx` (`Dias_idDias` ) ,
  INDEX `fk_Programacion_Personal_Personal1_idx` (`Personal_id_personal` ) ,
  INDEX `fk_Programacion_Personal_Horarios1_idx` (`Horarios_id_horarios` ) ,
  CONSTRAINT `fk_Programacion_Personal_Dias1`
    FOREIGN KEY (`Dias_idDias`)
    REFERENCES `OptiGest`.`Dias` (`idDias`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Programacion_Personal_Personal1`
    FOREIGN KEY (`Personal_id_personal`)
    REFERENCES `OptiGest`.`Personal` (`id_personal`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Programacion_Personal_Horarios1`
    FOREIGN KEY (`Horarios_id_horarios`)
    REFERENCES `OptiGest`.`Horarios` (`id_horarios`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Ajustes finales para la aplicación web OptiGest
-- Incluye las migraciones históricas para instalaciones nuevas y existentes.
-- El historial se limita a Personal, Activos y Asignaciones.
-- Requiere MySQL 8.
-- -----------------------------------------------------
USE `OptiGest`;

-- Roles.tipo_acceso ya era usado por Modelo/Roles.java, RolesDAO.java y las
-- consultas de PersonalDAO (SELECT p.*, r.tipo_acceso ...) pero nunca se habia
-- agregado a la tabla. Sin esta columna, las consultas de PersonalDAO fallaban
-- con "Unknown column 'r.tipo_acceso'", lo que impedia iniciar sesion.
ALTER TABLE `Roles`
    ADD COLUMN IF NOT EXISTS `tipo_acceso` VARCHAR(45) NULL;

-- Un empleado puede tener varios roles. Se conserva roles_idroles como rol
-- principal para compatibilidad con los flujos existentes de autenticacion.
CREATE TABLE IF NOT EXISTS `Personal_Roles` (
    `id_personal` INT NOT NULL,
    `id_rol` INT NOT NULL,
    PRIMARY KEY (`id_personal`, `id_rol`),
    CONSTRAINT `fk_Personal_Roles_personal` FOREIGN KEY (`id_personal`)
        REFERENCES `Personal` (`id_personal`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_Personal_Roles_rol` FOREIGN KEY (`id_rol`)
        REFERENCES `Roles` (`idroles`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

INSERT IGNORE INTO `Personal_Roles` (`id_personal`, `id_rol`)
SELECT `id_personal`, `roles_idroles` FROM `Personal`;

ALTER TABLE `Personal`
    ADD COLUMN IF NOT EXISTS `activo` BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS `fecha_baja` DATETIME NULL,
    ADD COLUMN IF NOT EXISTS `puede_acceder` BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS `debe_cambiar_clave` BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS `clave_temporal_expira_en` DATETIME NULL,
    ADD COLUMN IF NOT EXISTS `fecha_contratacion` DATE NULL;
ALTER TABLE `Personal`
    MODIFY `clave` VARCHAR(100) NOT NULL,
    MODIFY `observaciones` VARCHAR(200) NULL;

-- El historial de personal se conserva como filas independientes. Por eso la
-- identificacion y el correo no pueden ser unicos globalmente: un retirado
-- puede reingresar con un nuevo registro activo.
DELIMITER $$
DROP PROCEDURE IF EXISTS `normalizar_indices_personal_optigest`$$
CREATE PROCEDURE `normalizar_indices_personal_optigest`()
BEGIN
    DECLARE existe_indice INT DEFAULT 0;

    SELECT COUNT(*) INTO existe_indice
    FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'Personal' AND index_name = 'identificacion_UNIQUE';
    IF existe_indice = 1 THEN ALTER TABLE `Personal` DROP INDEX `identificacion_UNIQUE`; END IF;

    SELECT COUNT(*) INTO existe_indice
    FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'Personal' AND index_name = 'email_UNIQUE';
    IF existe_indice = 1 THEN ALTER TABLE `Personal` DROP INDEX `email_UNIQUE`; END IF;

    SELECT COUNT(*) INTO existe_indice
    FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'Personal' AND index_name = 'uq_identificacion';
    IF existe_indice = 1 THEN ALTER TABLE `Personal` DROP INDEX `uq_identificacion`; END IF;

    SELECT COUNT(*) INTO existe_indice
    FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'Personal' AND index_name = 'uq_email';
    IF existe_indice = 1 THEN ALTER TABLE `Personal` DROP INDEX `uq_email`; END IF;

    SELECT COUNT(*) INTO existe_indice
    FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'Personal' AND index_name = 'idx_personal_identificacion';
    IF existe_indice = 0 THEN ALTER TABLE `Personal` ADD INDEX `idx_personal_identificacion` (`identificacion`); END IF;

    SELECT COUNT(*) INTO existe_indice
    FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'Personal' AND index_name = 'idx_personal_email';
    IF existe_indice = 0 THEN ALTER TABLE `Personal` ADD INDEX `idx_personal_email` (`email`); END IF;
END$$
CALL `normalizar_indices_personal_optigest`()$$
DROP PROCEDURE IF EXISTS `normalizar_indices_personal_optigest`$$
DELIMITER ;

ALTER TABLE `Activos`
    ADD COLUMN IF NOT EXISTS `activo` BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN IF NOT EXISTS `fecha_baja` DATETIME NULL,
    ADD COLUMN IF NOT EXISTS `descripcion` VARCHAR(255) NULL;
UPDATE `Activos` SET `fecha_devolucion` = NULL
WHERE CAST(`fecha_devolucion` AS CHAR) = '';
ALTER TABLE `Activos` MODIFY `fecha_devolucion` DATE NULL;

-- En versiones anteriores el campo se llamaba fecha_asignaciones.
DELIMITER $$
DROP PROCEDURE IF EXISTS `normalizar_asignaciones_optigest`$$
CREATE PROCEDURE `normalizar_asignaciones_optigest`()
BEGIN
    DECLARE existe_fecha_actual INT DEFAULT 0;
    DECLARE existe_fecha_anterior INT DEFAULT 0;
    DECLARE existe_cantidad INT DEFAULT 0;
    DECLARE existe_indice INT DEFAULT 0;

    SELECT COUNT(*) INTO existe_fecha_actual
    FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'Asignaciones' AND column_name = 'fecha_asignacion';
    SELECT COUNT(*) INTO existe_fecha_anterior
    FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'Asignaciones' AND column_name = 'fecha_asignaciones';

    IF existe_fecha_actual = 0 AND existe_fecha_anterior = 1 THEN
        ALTER TABLE `Asignaciones` RENAME COLUMN `fecha_asignaciones` TO `fecha_asignacion`;
    ELSEIF existe_fecha_actual = 0 THEN
        ALTER TABLE `Asignaciones` ADD COLUMN `fecha_asignacion` DATE NULL;
    END IF;

    UPDATE `Asignaciones` SET `fecha_asignacion` = NULL
    WHERE CAST(`fecha_asignacion` AS CHAR) = '';
    UPDATE `Asignaciones` SET `fecha_devolucion` = NULL
    WHERE CAST(`fecha_devolucion` AS CHAR) = '';

    ALTER TABLE `Asignaciones`
        MODIFY `id_asignaciones` INT NOT NULL AUTO_INCREMENT,
        MODIFY `fecha_asignacion` DATE NULL,
        MODIFY `fecha_devolucion` DATE NULL,
        MODIFY `observaciones` VARCHAR(200) NULL;

    SELECT COUNT(*) INTO existe_cantidad
    FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'Asignaciones' AND column_name = 'cantidad';
    IF existe_cantidad = 1 THEN
        ALTER TABLE `Asignaciones` MODIFY `cantidad` VARCHAR(45) NULL;
    END IF;

    ALTER TABLE `Asignaciones` ADD COLUMN IF NOT EXISTS `anulado` BOOLEAN NOT NULL DEFAULT FALSE;

    SELECT COUNT(*) INTO existe_indice FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'Asignaciones' AND index_name = 'fecha_asignaciones_UNIQUE';
    IF existe_indice = 1 THEN ALTER TABLE `Asignaciones` DROP INDEX `fecha_asignaciones_UNIQUE`; END IF;
    SELECT COUNT(*) INTO existe_indice FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'Asignaciones' AND index_name = 'fecha_devolucion_UNIQUE';
    IF existe_indice = 1 THEN ALTER TABLE `Asignaciones` DROP INDEX `fecha_devolucion_UNIQUE`; END IF;
    SELECT COUNT(*) INTO existe_indice FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'Asignaciones' AND index_name = 'observaciones_UNIQUE';
    IF existe_indice = 1 THEN ALTER TABLE `Asignaciones` DROP INDEX `observaciones_UNIQUE`; END IF;
END$$
CALL `normalizar_asignaciones_optigest`()$$
DROP PROCEDURE `normalizar_asignaciones_optigest`$$
DELIMITER ;

CREATE TABLE IF NOT EXISTS `HistorialCambios` (
    `id_historial` BIGINT NOT NULL AUTO_INCREMENT,
    `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `entidad` VARCHAR(60) NOT NULL,
    `id_registro` INT NOT NULL,
    `accion` VARCHAR(30) NOT NULL,
    `datos_antes` JSON NULL,
    `datos_despues` JSON NULL,
    `usuario_id` INT NULL,
    `usuario_nombre` VARCHAR(200) NOT NULL,
    `motivo` VARCHAR(500) NULL,
    `usuario_bd` VARCHAR(150) NOT NULL,
    PRIMARY KEY (`id_historial`),
    INDEX `idx_historial_entidad_registro` (`entidad`, `id_registro`),
    INDEX `idx_historial_fecha` (`fecha`)
);

-- Solicitudes enviadas desde la vista pública "Contáctanos".
CREATE TABLE IF NOT EXISTS `MensajesContacto` (
    `id_mensaje` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(45) NOT NULL,
    `apellidos` VARCHAR(45) NOT NULL,
    `documento` VARCHAR(20) NOT NULL,
    `tipo_documento` VARCHAR(45) NOT NULL DEFAULT '',
    `email` VARCHAR(45) NOT NULL,
    `telefono` VARCHAR(15) NOT NULL,
    `direccion` VARCHAR(45) NOT NULL,
    `tipo_solicitud` VARCHAR(30) NOT NULL,
    `usuario_deseado` VARCHAR(45) NULL,
    `asunto` VARCHAR(100) NOT NULL,
    `mensaje` VARCHAR(200) NOT NULL,
    `fecha` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `atendido` BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`id_mensaje`),
    INDEX `idx_mensajes_contacto_estado_fecha` (`atendido`, `fecha`)
);

-- Compatibilidad con instalaciones creadas antes de que el formulario
-- público solicitara la dirección de contacto.
ALTER TABLE `MensajesContacto`
    ADD COLUMN IF NOT EXISTS `direccion` VARCHAR(150) NOT NULL DEFAULT '' AFTER `telefono`;

ALTER TABLE `MensajesContacto`
    ADD COLUMN IF NOT EXISTS `tipo_documento` VARCHAR(45) NOT NULL DEFAULT '' AFTER `documento`;

ALTER TABLE `MensajesContacto`
    ADD COLUMN IF NOT EXISTS `Personal_id_personal` INT NULL,
    ADD INDEX IF NOT EXISTS `idx_mensajes_personal` (`Personal_id_personal`);

ALTER TABLE `MensajesContacto`
    ADD COLUMN IF NOT EXISTS `fecha_atencion` DATETIME NULL;

-- Normalización histórica: no se modifican correo, usuario/apodo, contraseñas,
-- identificaciones, teléfonos, fechas ni valores numéricos.
UPDATE `Personal` SET nombre = UPPER(nombre), apellidos = UPPER(apellidos), direccion = UPPER(direccion), observaciones = UPPER(observaciones);
UPDATE `Activos` SET codigo_act = UPPER(codigo_act), nombre_activos = UPPER(nombre_activos), descripcion = UPPER(descripcion);
UPDATE `Asignaciones` SET observaciones = UPPER(observaciones);
UPDATE `Proveedores` SET nombre = UPPER(nombre), direccion = UPPER(direccion);
UPDATE `Mantenimiento` SET descripcion = UPPER(descripcion);
UPDATE `Horarios` SET descripcion = UPPER(descripcion);
UPDATE `Programacion_Personal` SET descripcion_programacion = UPPER(descripcion_programacion);
UPDATE `Categorias` SET descripcionCategoria = UPPER(descripcionCategoria);
UPDATE `Documento` SET descripcion_doc = UPPER(descripcion_doc);
UPDATE `Dias` SET descripcionDias = UPPER(descripcionDias);
UPDATE `Estado_Activo` SET descripcion_activo = UPPER(descripcion_activo);
UPDATE `Estado_Personal` SET descripcion_estado = UPPER(descripcion_estado);
UPDATE `Roles` SET descripcion_roles = UPPER(descripcion_roles), tipo_acceso = UPPER(tipo_acceso);
UPDATE `MensajesContacto` SET nombre = UPPER(nombre), apellidos = UPPER(apellidos), direccion = UPPER(direccion), tipo_solicitud = UPPER(tipo_solicitud), asunto = UPPER(asunto), mensaje = UPPER(mensaje);

DROP TRIGGER IF EXISTS `ai_historial_personal`;
DROP TRIGGER IF EXISTS `au_historial_personal`;
DROP TRIGGER IF EXISTS `ai_historial_activos`;
DROP TRIGGER IF EXISTS `au_historial_activos`;
DROP TRIGGER IF EXISTS `ai_historial_asignaciones`;
DROP TRIGGER IF EXISTS `au_historial_asignaciones`;
DROP TRIGGER IF EXISTS `ai_historial_mantenimiento`;
DROP TRIGGER IF EXISTS `au_historial_mantenimiento`;
DROP TRIGGER IF EXISTS `ai_historial_proveedores`;
DROP TRIGGER IF EXISTS `au_historial_proveedores`;

DELIMITER $$
CREATE TRIGGER `ai_historial_personal` AFTER INSERT ON `Personal` FOR EACH ROW
BEGIN
    INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
    VALUES ('Personal', NEW.`id_personal`, 'CREADO',
        JSON_OBJECT('identificacion', NEW.`identificacion`, 'nombre', NEW.`nombre`, 'apellidos', NEW.`apellidos`),
        @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'),
        COALESCE(NULLIF(@aud_motivo, ''), 'Registro de personal'), CURRENT_USER());
END$$

CREATE TRIGGER `au_historial_personal` AFTER UPDATE ON `Personal` FOR EACH ROW
BEGIN
    IF NOT(OLD.`identificacion` <=> NEW.`identificacion`)
       OR NOT(OLD.`nombre` <=> NEW.`nombre`)
       OR NOT(OLD.`apellidos` <=> NEW.`apellidos`)
       OR NOT(OLD.`email` <=> NEW.`email`)
       OR NOT(OLD.`telefono` <=> NEW.`telefono`)
       OR NOT(OLD.`direccion` <=> NEW.`direccion`)
       OR NOT(OLD.`clave` <=> NEW.`clave`)
       OR NOT(OLD.`Documento_id_documento` <=> NEW.`Documento_id_documento`)
       OR NOT(OLD.`roles_idroles` <=> NEW.`roles_idroles`)
       OR NOT(OLD.`Estado_Personal_id_estado` <=> NEW.`Estado_Personal_id_estado`)
       OR NOT(OLD.`puede_acceder` <=> NEW.`puede_acceder`)
       OR NOT(OLD.`activo` <=> NEW.`activo`) THEN
        INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_antes`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
        VALUES ('Personal', NEW.`id_personal`,
            IF(NEW.`activo` = 0 AND OLD.`activo` = 1, 'DESACTIVADO', 'ACTUALIZADO'),
            JSON_OBJECT('nombre', OLD.`nombre`, 'apellidos', OLD.`apellidos`, 'correo', OLD.`email`, 'acceso', OLD.`puede_acceder`, 'activo', OLD.`activo`),
            JSON_OBJECT('nombre', NEW.`nombre`, 'apellidos', NEW.`apellidos`, 'correo', NEW.`email`, 'acceso', NEW.`puede_acceder`, 'activo', NEW.`activo`, 'clave_actualizada', NOT(OLD.`clave` <=> NEW.`clave`)),
            @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'), COALESCE(NULLIF(@aud_motivo, ''), 'Sin motivo registrado'), CURRENT_USER());
    END IF;
END$$

CREATE TRIGGER `ai_historial_activos` AFTER INSERT ON `Activos` FOR EACH ROW
BEGIN
    INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
    VALUES ('Activos', NEW.`id_activos`, 'CREADO',
        JSON_OBJECT('codigo', NEW.`codigo_act`, 'nombre', NEW.`nombre_activos`, 'valor', NEW.`valor`),
        @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'),
        COALESCE(NULLIF(@aud_motivo, ''), 'Registro de activo'), CURRENT_USER());
END$$

CREATE TRIGGER `au_historial_activos` AFTER UPDATE ON `Activos` FOR EACH ROW
BEGIN
    IF NOT(OLD.`codigo_act` <=> NEW.`codigo_act`)
       OR NOT(OLD.`nombre_activos` <=> NEW.`nombre_activos`)
       OR NOT(OLD.`valor` <=> NEW.`valor`)
       OR NOT(OLD.`fecha_adquma` <=> NEW.`fecha_adquma`)
       OR NOT(OLD.`fecha_devolucion` <=> NEW.`fecha_devolucion`)
       OR NOT(OLD.`vida_util` <=> NEW.`vida_util`)
       OR NOT(OLD.`descripcion` <=> NEW.`descripcion`)
       OR NOT(OLD.`Estado_Activo_idEstado_Activo` <=> NEW.`Estado_Activo_idEstado_Activo`)
       OR NOT(OLD.`Categorias_idCategorias` <=> NEW.`Categorias_idCategorias`)
       OR NOT(OLD.`Proveedores_idProveedores` <=> NEW.`Proveedores_idProveedores`)
       OR NOT(OLD.`activo` <=> NEW.`activo`) THEN
        INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_antes`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
        VALUES ('Activos', NEW.`id_activos`,
            IF(NEW.`activo` = 0 AND OLD.`activo` = 1, 'RETIRADO', 'ACTUALIZADO'),
            JSON_OBJECT('codigo', OLD.`codigo_act`, 'nombre', OLD.`nombre_activos`, 'valor', OLD.`valor`, 'activo', OLD.`activo`),
            JSON_OBJECT('codigo', NEW.`codigo_act`, 'nombre', NEW.`nombre_activos`, 'valor', NEW.`valor`, 'activo', NEW.`activo`),
            @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'), COALESCE(NULLIF(@aud_motivo, ''), 'Sin motivo registrado'), CURRENT_USER());
    END IF;
END$$

CREATE TRIGGER `ai_historial_asignaciones` AFTER INSERT ON `Asignaciones` FOR EACH ROW
BEGIN
    INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
    VALUES ('Asignaciones', NEW.`id_asignaciones`, 'CREADO',
        JSON_OBJECT('personal_id', NEW.`Personal_id_personal`, 'activo_id', NEW.`Activos_id_activos`, 'fecha_asignacion', NEW.`fecha_asignacion`, 'fecha_devolucion', NEW.`fecha_devolucion`),
        @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'),
        COALESCE(NULLIF(@aud_motivo, ''), 'Registro de asignación'), CURRENT_USER());
END$$

CREATE TRIGGER `au_historial_asignaciones` AFTER UPDATE ON `Asignaciones` FOR EACH ROW
BEGIN
    IF NOT(OLD.`fecha_asignacion` <=> NEW.`fecha_asignacion`)
       OR NOT(OLD.`fecha_devolucion` <=> NEW.`fecha_devolucion`)
       OR NOT(OLD.`observaciones` <=> NEW.`observaciones`)
       OR NOT(OLD.`Personal_id_personal` <=> NEW.`Personal_id_personal`)
       OR NOT(OLD.`Activos_id_activos` <=> NEW.`Activos_id_activos`)
       OR NOT(OLD.`anulado` <=> NEW.`anulado`) THEN
        INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_antes`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
        VALUES ('Asignaciones', NEW.`id_asignaciones`,
            IF(NEW.`anulado` = 1 AND OLD.`anulado` = 0, 'ANULADO', IF(OLD.`fecha_devolucion` IS NULL AND NEW.`fecha_devolucion` IS NOT NULL, 'DEVUELTO', 'ACTUALIZADO')),
            JSON_OBJECT('personal_id', OLD.`Personal_id_personal`, 'activo_id', OLD.`Activos_id_activos`, 'fecha_asignacion', OLD.`fecha_asignacion`, 'fecha_devolucion', OLD.`fecha_devolucion`, 'anulado', OLD.`anulado`),
            JSON_OBJECT('personal_id', NEW.`Personal_id_personal`, 'activo_id', NEW.`Activos_id_activos`, 'fecha_asignacion', NEW.`fecha_asignacion`, 'fecha_devolucion', NEW.`fecha_devolucion`, 'anulado', NEW.`anulado`),
            @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'), COALESCE(NULLIF(@aud_motivo, ''), 'Sin motivo registrado'), CURRENT_USER());
    END IF;
END$$

CREATE TRIGGER `ai_historial_mantenimiento` AFTER INSERT ON `Mantenimiento` FOR EACH ROW
BEGIN
    INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
    VALUES ('Mantenimiento', NEW.`id_mantenimiento`, 'CREADO',
        JSON_OBJECT('fecha', NEW.`fecha_mante`, 'costo', NEW.`costo`, 'descripcion', NEW.`descripcion`,
                    'activo_id', NEW.`Activos_id_activos`, 'proveedor_id', NEW.`Proveedores_idProveedores`),
        @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'),
        COALESCE(NULLIF(@aud_motivo, ''), 'Registro de mantenimiento'), CURRENT_USER());
END$$

CREATE TRIGGER `au_historial_mantenimiento` AFTER UPDATE ON `Mantenimiento` FOR EACH ROW
BEGIN
    IF NOT(OLD.`fecha_mante` <=> NEW.`fecha_mante`)
       OR NOT(OLD.`costo` <=> NEW.`costo`)
       OR NOT(OLD.`descripcion` <=> NEW.`descripcion`)
       OR NOT(OLD.`Activos_id_activos` <=> NEW.`Activos_id_activos`)
       OR NOT(OLD.`Proveedores_idProveedores` <=> NEW.`Proveedores_idProveedores`)
       OR NOT(OLD.`activo` <=> NEW.`activo`) THEN
        INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_antes`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
        VALUES ('Mantenimiento', NEW.`id_mantenimiento`,
            IF(NEW.`activo` = 0 AND OLD.`activo` = 1, 'DESACTIVADO', 'ACTUALIZADO'),
            JSON_OBJECT('fecha', OLD.`fecha_mante`, 'costo', OLD.`costo`, 'descripcion', OLD.`descripcion`,
                        'activo_id', OLD.`Activos_id_activos`, 'proveedor_id', OLD.`Proveedores_idProveedores`, 'activo', OLD.`activo`),
            JSON_OBJECT('fecha', NEW.`fecha_mante`, 'costo', NEW.`costo`, 'descripcion', NEW.`descripcion`,
                        'activo_id', NEW.`Activos_id_activos`, 'proveedor_id', NEW.`Proveedores_idProveedores`, 'activo', NEW.`activo`),
            @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'), COALESCE(NULLIF(@aud_motivo, ''), 'Sin motivo registrado'), CURRENT_USER());
    END IF;
END$$

CREATE TRIGGER `ai_historial_proveedores` AFTER INSERT ON `Proveedores` FOR EACH ROW
BEGIN
    INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
    VALUES ('Proveedores', NEW.`idProveedores`, 'CREADO',
        JSON_OBJECT('nombre', NEW.`nombre`, 'telefono', NEW.`telefono`, 'direccion', NEW.`direccion`, 'correo', NEW.`email`),
        @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'),
        COALESCE(NULLIF(@aud_motivo, ''), 'Registro de proveedor'), CURRENT_USER());
END$$

CREATE TRIGGER `au_historial_proveedores` AFTER UPDATE ON `Proveedores` FOR EACH ROW
BEGIN
    IF NOT(OLD.`nombre` <=> NEW.`nombre`)
       OR NOT(OLD.`telefono` <=> NEW.`telefono`)
       OR NOT(OLD.`direccion` <=> NEW.`direccion`)
       OR NOT(OLD.`email` <=> NEW.`email`)
       OR NOT(OLD.`activo` <=> NEW.`activo`) THEN
        INSERT INTO `HistorialCambios` (`entidad`, `id_registro`, `accion`, `datos_antes`, `datos_despues`, `usuario_id`, `usuario_nombre`, `motivo`, `usuario_bd`)
        VALUES ('Proveedores', NEW.`idProveedores`,
            IF(NEW.`activo` = 0 AND OLD.`activo` = 1, 'DESACTIVADO', 'ACTUALIZADO'),
            JSON_OBJECT('nombre', OLD.`nombre`, 'telefono', OLD.`telefono`, 'direccion', OLD.`direccion`, 'correo', OLD.`email`, 'activo', OLD.`activo`),
            JSON_OBJECT('nombre', NEW.`nombre`, 'telefono', NEW.`telefono`, 'direccion', NEW.`direccion`, 'correo', NEW.`email`, 'activo', NEW.`activo`),
            @aud_usuario_id, COALESCE(@aud_usuario_nombre, 'Sistema'), COALESCE(NULLIF(@aud_motivo, ''), 'Sin motivo registrado'), CURRENT_USER());
    END IF;
END$$
DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;



