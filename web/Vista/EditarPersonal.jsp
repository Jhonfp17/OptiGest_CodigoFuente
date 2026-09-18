<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Editar Personal - OptiGest</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctx}/Css/EditarPersonal.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
        <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
    </head>

    <body class="editar_personal">

        <div class="container d-flex justify-content-center align-items-center min-vh-100 py-5">
            <div class="card p-4 shadow registro-card w-100" style="max-width: 850px;">

                <div class="mb-4">
                    <h2 class="title">Editar Miembro del Personal</h2>
                    <p class="subtitle text-muted">
                        Actualice la información del miembro del personal y guarde los cambios.
                    </p>
                </div>

                <c:if test="${not empty mensaje}">
                    <div class="alert alert-danger">
                        ${mensaje}
                    </div>
                </c:if>

                <form action="${ctx}/EditarPersonal" method="post" data-validacion-personal>

                    <input type="hidden" name="idPersonal" value="${personal.idPersonal}">

                    <div class="row g-4">

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Nombre</label>
                            <input type="text" class="form-control" name="nombre"
                                   value="${personal.nombre}" required minlength="2" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+" title="Usa entre 2 y 45 letras.">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Apellidos</label>
                            <input type="text" class="form-control" name="apellidos"
                                   value="${personal.apellidos}" required minlength="2" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+" title="Usa entre 2 y 45 letras.">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Correo electrónico</label>
                            <input type="email" class="form-control" name="email"
                                   value="${personal.email}" required minlength="6" maxlength="45">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Teléfono</label>
                            <input type="text" class="form-control" name="telefono"
                                   value="${personal.telefono}" required inputmode="numeric" minlength="7" maxlength="15" pattern="\d{7,15}" title="Ingresa entre 7 y 15 dígitos.">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Dirección</label>
                            <input type="text" class="form-control" name="direccion"
                                   value="${personal.direccion}" required minlength="5" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ0-9#.,/ -]+">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">N. Documento</label>
                            <input type="text" class="form-control" name="identificacion"
                                   value="${personal.identificacion}" required inputmode="numeric" autocomplete="off">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Nueva clave</label>
                            <input type="password" class="form-control" name="clave" minlength="8" maxlength="20"
                                   autocomplete="new-password" placeholder="Deja este campo vacio para conservarla">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Fecha contratación</label>
                            <input type="date" class="form-control" name="fecha_contratacion"
                                   value="${personal.fecha_contratacion}" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Tipo de Documento</label>
                            <select class="form-select" name="Documento_id_documento" required>
                                <option value="" disabled>Seleccione tipo...</option>
                                <c:forEach var="d" items="${documentos}">
                                    <option value="${d.id_documento}" data-documento="${d.descripcion_doc}" ${d.id_documento == personal.documento_id_documento ? 'selected' : ''}>
                                        ${d.descripcion_doc}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Rol</label>
                            <c:choose>
                                <c:when test="${esAdministradorMaestro}">
                                    <input type="hidden" name="roles_idroles" value="1">
                                    <input type="text" class="form-control" value="Administrador maestro (protegido)" readonly>
                                </c:when>
                                <c:otherwise>
                                    <select class="form-select" name="roles_idroles" required>
                                        <option value="" disabled>Seleccione rol...</option>
                                        <c:forEach var="r" items="${roles}">
                                            <option value="${r.idRoles}" ${r.idRoles == personal.roles_idroles ? 'selected' : ''}>
                                                ${r.descripcion_roles}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Estado</label>
                            <select class="form-select" name="Estado_Personal_id_estado" required>
                                <option value="" disabled>Seleccione estado...</option>
                                <c:forEach var="e" items="${estadosPersonal}">
                                    <option value="${e.id_estado}" ${e.id_estado == personal.estado_Personal_id_estado ? 'selected' : ''}>
                                        ${e.descripcion_estado}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label fw-bold">Puede acceder</label>
                            <c:choose>
                                <c:when test="${esAdministradorMaestro}">
                                    <input type="hidden" name="puede_acceder" value="1">
                                    <input type="text" class="form-control" value="Sí — acceso protegido" readonly>
                                </c:when>
                                <c:otherwise>
                                    <select class="form-select" name="puede_acceder" required>
                                        <option value="1" ${personal.puede_acceder ? 'selected' : ''}>Si</option>
                                        <option value="0" ${!personal.puede_acceder ? 'selected' : ''}>No</option>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:if test="${esAdministradorMaestro}">
                            <div class="col-12">
                                <div class="alert alert-info mb-0">
                                    Esta cuenta es el administrador maestro. Su acceso y rol no pueden modificarse.
                                </div>
                            </div>
                        </c:if>

                        <div class="col-12">
                            <label class="form-label fw-bold">Observaciones</label>
                            <textarea class="form-control" name="observaciones" rows="3" maxlength="200">${personal.observaciones}</textarea>
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-bold" for="motivo">Motivo del cambio</label>
                            <textarea id="motivo" class="form-control" name="motivo" rows="3" maxlength="500" required placeholder="Explica por que se realiza este cambio"></textarea>
                        </div>
                    </div>

                    <div class="mt-5 d-flex justify-content-end gap-3">
                        <a href="${ctx}/GestionarPersonal" class="btn btn-outline-secondary px-4">
                            Cancelar
                        </a>

                        <button type="submit" class="btn btn-primary px-4">
                            Guardar cambios
                        </button>
                    </div>

                </form>
                <section class="mt-5 pt-4 border-top">
                    <h2 class="h5 mb-3">Historial del personal</h2>
                    <div class="table-responsive">
                        <table class="table table-sm table-striped align-middle">
                            <thead><tr><th>Fecha</th><th>Accion</th><th>Responsable</th><th>Motivo</th></tr></thead>
                            <tbody>
                                <c:forEach var="h" items="${historial}"><tr><td>${h.fecha}</td><td>${h.accion}</td><td>${h.usuarioNombre}</td><td>${h.motivo}</td></tr></c:forEach>
                                <c:if test="${empty historial}"><tr><td colspan="4" class="text-muted">Aun no hay cambios registrados para esta persona.</td></tr></c:if>
                            </tbody>
                        </table>
                    </div>
                </section>
            </div>
        </div>

        <script src="${ctx}/JavaScript/validacion_personal.js"></script>
        <script src="${ctx}/JavaScript/validacion_formularios.js?v=2"></script>
    </body>
</html>
