<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Personal - OptiGest</title>

    <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">

    <link rel="stylesheet" href="${ctx}/Css/RegistroPersonal.css">
    <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
    <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
</head>

<body class="registro-personal-page">

    <main class="registro-wrapper">

        <section class="registro-card">

            <header class="registro-header">
                <h2>Registrar Personal</h2>
                <p>Ingrese los datos del nuevo empleado.</p>
            </header>

            <c:if test="${not empty mensaje}">
                <div class="registro-alert">
                    ${mensaje}
                </div>
            </c:if>

            <form action="${ctx}/GuardarPersonal?origen=${param.origen}" method="post" class="registro-form" data-validacion-personal>
                <input type="hidden" name="origen" value="${param.origen}">
                <c:if test="${not empty solicitudId}">
                    <input type="hidden" name="solicitudId" value="${solicitudId}">
                </c:if>

                <div class="form-grid">

                    <div class="form-field">
                        <label>Nombre</label>
                        <input type="text" name="nombre" value="${nombre}" required minlength="2" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+" title="Usa entre 2 y 45 letras.">
                    </div>

                    <div class="form-field">
                        <label>Apellidos</label>
                        <input type="text" name="apellidos" value="${apellidos}" required minlength="2" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+" title="Usa entre 2 y 45 letras.">
                    </div>

                    <div class="form-field">
                        <label>Correo</label>
                        <input type="email" name="email" value="${email}" required minlength="6" maxlength="45">
                    </div>

                    <div class="form-field">
                        <label>Teléfono</label>
                        <input type="tel" name="telefono" value="${telefono}" required inputmode="numeric" minlength="7" maxlength="15" pattern="\d{7,15}" title="Ingresa entre 7 y 15 dígitos.">
                    </div>

                    <div class="form-field form-field-full">
                        <label>Dirección</label>
                        <input type="text" name="direccion" value="${direccion}" placeholder="Ej.: Calle 10 # 20-30" required minlength="5" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ0-9#.,/ -]+">
                    </div>

                    <div class="form-field">
                        <label>Tipo de Documento</label>
                        <select name="Documento_id_documento" required>
                            <option value="">Seleccione</option>

                            <c:forEach var="doc" items="${documentos}">
                                <option value="${doc.id_documento}" data-documento="${doc.descripcion_doc}" ${doc.descripcion_doc == documentoSolicitud ? 'selected' : ''}>
                                    ${doc.descripcion_doc}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-field">
                        <label>Número de Documento</label>
                        <input type="text" name="identificacion" value="${identificacion}" required inputmode="numeric" autocomplete="off">
                    </div>

                    <div class="form-field">
                        <label>Clave</label>
                        <input id="clave" type="password" name="clave" minlength="8" maxlength="20" placeholder="No es necesaria con activación por recuperación">
                    </div>

                    <div class="form-field">
                        <label>Activación de cuenta</label>
                        <select id="claveProvisional" name="clave_provisional">
                            <option value="0">No</option>
                            <option value="1">El usuario define su clave con Olvidé mi contraseña</option>
                        </select>
                    </div>

                    <div class="form-field">
                        <label>Fecha de Contratación</label>
                        <input type="date" name="fecha_contratacion" required>
                    </div>

                    <div class="form-field">
                        <label>Rol</label>
                        <select name="roles_idroles" required>
                            <option value="">Seleccione</option>

                            <c:forEach var="rol" items="${roles}">
                                <option value="${rol.idRoles}">
                                    ${rol.descripcion_roles}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-field">
                        <label>Estado</label>
                        <select name="Estado_Personal_id_estado" required>
                            <option value="">Seleccione</option>

                            <c:forEach var="estado" items="${estadosPersonal}">
                                <option value="${estado.id_estado}">
                                    ${estado.descripcion_estado}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-field form-field-full">
                        <label>¿Puede acceder al sistema?</label>
                        <select name="puede_acceder" required>
                            <option value="">Seleccione</option>
                            <option value="1">Sí</option>
                            <option value="0">No</option>
                        </select>
                    </div>

                    <div class="form-field form-field-full">
                        <label>Observaciones</label>
                        <textarea name="observaciones" rows="3" maxlength="200"></textarea>
                    </div>

                </div>

                <div class="form-actions">
                    <c:choose>
                        <c:when test="${param.origen == 'configuracion'}">
                            <a href="${ctx}/GestionCatalogo?tipo=personal" class="btn-cancelar">Cancelar</a>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn-cancelar" onclick="history.back()">Cancelar</button>
                        </c:otherwise>
                    </c:choose>

                    <button type="submit" class="btn-guardar">
                        Registrar
                    </button>
                </div>

            </form>

        </section>

    </main>

<script src="${ctx}/JavaScript/validacion_personal.js"></script>
<script src="${ctx}/JavaScript/configuracion_clave.js"></script>
<script src="${ctx}/JavaScript/validacion_formularios.js?v=2"></script>
</body>
</html>

