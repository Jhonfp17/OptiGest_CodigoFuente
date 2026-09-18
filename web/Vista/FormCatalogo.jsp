<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${modo == 'editar' ? 'Editar' : 'Nuevo'} ${titulo} - OptiGest</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctx}/Css/RegistroPersonal.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
        <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
    </head>

    <body class="registro_personal">
        <div class="container d-flex justify-content-center align-items-center min-vh-100 py-5">
            <div class="card p-4 shadow registro-card w-100" style="max-width: 760px;">

                <div class="mb-4">
                    <h2 class="title">${modo == 'editar' ? 'Editar' : 'Nuevo'} ${titulo}</h2>
                    <p class="subtitle text-muted">Complete los datos y guarde el registro.</p>
                </div>

                <form action="${ctx}/${modo == 'editar' ? 'EditarCatalogo' : 'GuardarCatalogo'}?origen=${origen}" method="post" class="registro-form">
                    <input type="hidden" name="tipo" value="${tipo}">
                    <input type="hidden" name="origen" value="${origen}">
                    <c:if test="${modo == 'editar'}">
                        <input type="hidden" name="id" value="${id}">
                    </c:if>

                    <c:choose>
                        <c:when test="${tipo == 'proveedores'}">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Nombre</label>
                                <input type="text" name="nombre" class="form-control" value="${nombre}" minlength="3" maxlength="60" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Teléfono</label>
                                <input type="text" name="telefono" class="form-control" value="${telefono}" inputmode="numeric" minlength="7" maxlength="15" pattern="\d{7,15}" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Dirección</label>
                                <input type="text" name="direccion" class="form-control" value="${direccion}" minlength="5" maxlength="40" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ0-9#.,/ -]+" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Email</label>
                                <input type="email" name="email" class="form-control" value="${email}" minlength="15" maxlength="20" required>
                            </div>
                        </c:when>

                        <c:when test="${tipo == 'horarios'}">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Descripción</label>
                                <input type="text" name="descripcion" class="form-control" value="${descripcion}" minlength="3" maxlength="60" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Hora ingreso</label>
                                <input type="time" name="horaIngreso" class="form-control" value="${horaIngreso}" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Hora salida</label>
                                <input type="time" name="horaSalida" class="form-control" value="${horaSalida}" required>
                            </div>
                        </c:when>

                        <c:when test="${tipo == 'mantenimiento'}">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Fecha mantenimiento</label>
                                <input type="date" name="fechaMante" class="form-control" value="${fechaMante}" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Costo</label>
                                <input type="number" name="costo" class="form-control" value="${costo}" min="0.01" step="0.01" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Descripción</label>
                                <textarea name="descripcion" class="form-control" rows="3" maxlength="200" required>${descripcion}</textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Activo</label>
                                <select name="activoId" class="form-control" required>
                                    <option value="">Seleccione un activo</option>
                                    <c:forEach var="activo" items="${opcionesActivos}">
                                        <option value="${activo.id_activos}" <c:if test="${activo.id_activos == activoId}">selected</c:if>>
                                            ${activo.nombre_activos}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Proveedor</label>
                                <select name="proveedorId" class="form-control" required>
                                    <option value="">Seleccione un proveedor</option>
                                    <c:forEach var="proveedor" items="${opcionesProveedores}">
                                        <option value="${proveedor.idProveedores}" <c:if test="${proveedor.idProveedores == proveedorId}">selected</c:if>>
                                            ${proveedor.nombre}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:when>

                        <c:when test="${tipo == 'personal'}">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Nombre</label>
                                    <input type="text" name="nombre" class="form-control" value="${nombre}" minlength="2" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Apellidos</label>
                                    <input type="text" name="apellidos" class="form-control" value="${apellidos}" minlength="2" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+" required>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Identificación</label>
                                    <input type="text" name="identificacion" class="form-control" value="${identificacion}" inputmode="numeric" minlength="6" maxlength="10" pattern="\d{6,10}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Email</label>
                                    <input type="email" name="email" class="form-control" value="${email}" minlength="6" maxlength="45" required>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Teléfono</label>
                                    <input type="text" name="telefono" class="form-control" value="${telefono}" inputmode="numeric" minlength="7" maxlength="15" pattern="\d{7,15}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Dirección</label>
                                    <input type="text" name="direccion" class="form-control" value="${direccion}" minlength="5" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ0-9#.,/ -]+" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">${modo == 'editar' ? 'Nueva clave' : 'Clave'}</label>
                                <input type="password" name="clave" class="form-control" value="${clave}"
                                       autocomplete="new-password" placeholder="${modo == 'editar' ? 'Deja este campo vacio para conservarla' : ''}"
                                        minlength="8" maxlength="20" <c:if test="${modo != 'editar'}">required</c:if>>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Observaciones</label>
                                <textarea name="observaciones" class="form-control" rows="3" maxlength="200">${observaciones}</textarea>
                            </div>
                            <div class="form-check mb-3">
                                <c:choose>
                                    <c:when test="${esAdministradorMaestro}">
                                        <input type="hidden" name="puedeAcceder" value="on">
                                        <input class="form-check-input" type="checkbox" id="puedeAcceder" checked disabled>
                                        <label class="form-check-label fw-bold" for="puedeAcceder">Acceso protegido para el administrador maestro</label>
                                    </c:when>
                                    <c:otherwise>
                                        <input class="form-check-input" type="checkbox" name="puedeAcceder" id="puedeAcceder" <c:if test="${puedeAcceder}">checked</c:if>>
                                        <label class="form-check-label fw-bold" for="puedeAcceder">Puede acceder</label>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Fecha contratacion</label>
                                    <input type="date" name="fechaContratacion" class="form-control" value="${fechaContratacion}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Documento</label>
                                    <select name="documentoId" class="form-control" required>
                                        <option value="">Seleccione un documento</option>
                                        <c:forEach var="documento" items="${opcionesDocumentos}">
                                            <option value="${documento.id_documento}" <c:if test="${documento.id_documento == documentoId}">selected</c:if>>
                                                ${documento.descripcion_doc}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Rol</label>
                                    <c:choose>
                                        <c:when test="${esAdministradorMaestro}">
                                            <input type="hidden" name="rolId" value="1">
                                            <input type="text" class="form-control" value="Administrador maestro (protegido)" readonly>
                                        </c:when>
                                        <c:otherwise>
                                            <select name="rolId" class="form-control" required>
                                                <option value="">Seleccione un rol</option>
                                                <c:forEach var="rol" items="${opcionesRoles}">
                                                    <option value="${rol.idRoles}" <c:if test="${rol.idRoles == rolId}">selected</c:if>>
                                                        ${rol.descripcion_roles}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Estado Personal</label>
                                    <select name="estadoPersonalId" class="form-control" required>
                                        <option value="">Seleccione un estado</option>
                                        <c:forEach var="estado" items="${opcionesEstadoPersonal}">
                                            <option value="${estado.id_estado}" <c:if test="${estado.id_estado == estadoPersonalId}">selected</c:if>>
                                                ${estado.descripcion_estado}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </c:when>

                        <c:when test="${tipo == 'activos'}">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Código activo</label>
                                    <input type="text" name="codigoAct" class="form-control" value="${codigoAct}" minlength="3" maxlength="30" pattern="[A-Za-z0-9-]{3,30}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Nombre activo</label>
                                    <input type="text" name="nombreActivo" class="form-control" value="${nombreActivo}" minlength="3" maxlength="30" required>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Valor</label>
                                    <input type="text" name="valor" class="form-control" value="${valor}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Vida util</label>
                <input type="number" name="vidaUtil" class="form-control" value="${vidaUtil}" min="1" max="20" step="1" required>
                                </div>
                            </div>
                            <div class="row"><div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Fecha adquisicion</label>
                                <input type="date" name="fechaAdquma" class="form-control" value="${fechaAdquma}" required>
                            </div></div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Descripción</label>
                                <textarea name="descripcion" class="form-control" rows="3" maxlength="200" required>${descripcion}</textarea>
                            </div>
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-bold">Estado Activo</label>
                                    <select name="estadoActivoId" class="form-control" required>
                                        <option value="">Seleccione un estado</option>
                                        <c:forEach var="estado" items="${opcionesEstadoActivo}">
                                            <option value="${estado.idEstado_Activo}" <c:if test="${estado.idEstado_Activo == estadoActivoId}">selected</c:if>>
                                                ${estado.descripcion_activo}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-bold">Categoria</label>
                                    <select name="categoriaId" class="form-control" required>
                                        <option value="">Seleccione una categoria</option>
                                        <c:forEach var="categoria" items="${opcionesCategorias}">
                                            <option value="${categoria.idCategorias}" <c:if test="${categoria.idCategorias == categoriaId}">selected</c:if>>
                                                ${categoria.descripcionCategoria}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-bold">Proveedor</label>
                                    <select name="proveedorId" class="form-control" required>
                                        <option value="">Seleccione un proveedor</option>
                                        <c:forEach var="proveedor" items="${opcionesProveedores}">
                                            <option value="${proveedor.idProveedores}" <c:if test="${proveedor.idProveedores == proveedorId}">selected</c:if>>
                                                ${proveedor.nombre}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </c:when>

                        <c:when test="${tipo == 'asignaciones'}">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Fecha asignación</label>
                                    <input type="date" name="fechaAsignacion" class="form-control" value="${fechaAsignacion}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Fecha devolución</label>
                                    <input type="date" name="fechaDevolucion" class="form-control" value="${fechaDevolucion}">
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Observaciones</label>
                                <textarea name="observaciones" class="form-control" rows="3" maxlength="200">${observaciones}</textarea>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Personal</label>
                                    <select name="personalId" class="form-control" required>
                                        <option value="">Seleccione una persona</option>
                                        <c:forEach var="persona" items="${opcionesPersonal}">
                                            <option value="${persona.idPersonal}" <c:if test="${persona.idPersonal == personalId}">selected</c:if>>
                                                ${persona.nombre} ${persona.apellidos}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Activo</label>
                                    <select name="activoId" class="form-control" required>
                                        <option value="">Seleccione un activo</option>
                                        <c:forEach var="activo" items="${opcionesActivos}">
                                            <option value="${activo.id_activos}" <c:if test="${activo.id_activos == activoId}">selected</c:if>>
                                                ${activo.nombre_activos}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </c:when>

                        <c:when test="${tipo == 'programacionPersonal'}">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Descripción</label>
                                <input type="text" name="descripcion" class="form-control" value="${descripcion}" minlength="3" maxlength="100" required>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Fecha desde</label>
                                    <input type="date" name="fechaDesde" class="form-control" value="${fechaDesde}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Fecha hasta</label>
                                    <input type="date" name="fechaHasta" class="form-control" value="${fechaHasta}" required>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-bold">Dia</label>
                                    <select name="diaId" class="form-control" required>
                                        <option value="">Seleccione un dia</option>
                                        <c:forEach var="dia" items="${opcionesDias}">
                                            <option value="${dia.idDias}" <c:if test="${dia.idDias == diaId}">selected</c:if>>
                                                ${dia.descripcionDias}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-bold">Personal</label>
                                    <select name="personalId" class="form-control" required>
                                        <option value="">Seleccione una persona</option>
                                        <c:forEach var="persona" items="${opcionesPersonal}">
                                            <option value="${persona.idPersonal}" <c:if test="${persona.idPersonal == personalId}">selected</c:if>>
                                                ${persona.nombre} ${persona.apellidos}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label class="form-label fw-bold">Horario</label>
                                    <select name="horarioId" class="form-control" required>
                                        <option value="">Seleccione un horario</option>
                                        <c:forEach var="horario" items="${opcionesHorarios}">
                                            <option value="${horario.id_horarios}" <c:if test="${horario.id_horarios == horarioId}">selected</c:if>>
                                                ${horario.descripcion}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="mb-3">
                                <label class="form-label fw-bold">Descripción</label>
                                <input type="text" name="descripcion" class="form-control" value="${descripcion}" minlength="3" maxlength="60" required>
                            </div>

                            <c:if test="${tipo == 'roles'}">
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Tipo de acceso</label>
                                    <select name="tipoAcceso" class="form-control" required>
                                        <option value="">Seleccione un tipo</option>
                                        <option value="ADMINISTRADOR" <c:if test="${tipoAcceso == 'ADMINISTRADOR'}">selected</c:if>>ADMINISTRADOR</option>
                                        <option value="PERSONAL_FIJO" <c:if test="${tipoAcceso == 'PERSONAL_FIJO'}">selected</c:if>>PERSONAL_FIJO</option>
                                        <option value="TEMPORAL" <c:if test="${tipoAcceso == 'TEMPORAL'}">selected</c:if>>TEMPORAL</option>
                                    </select>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>

                    <c:if test="${modo == 'editar' && (tipo == 'activos' || tipo == 'personal' || tipo == 'asignaciones' || tipo == 'mantenimiento' || tipo == 'proveedores')}">
                        <div class="alert alert-info">
                            <strong>Historial de ${titulo}.</strong> Al guardar, este cambio se conservará en el historial del registro junto con su motivo y responsable.
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold" for="motivo">Motivo del cambio</label>
                            <textarea id="motivo" name="motivo" class="form-control" rows="3" maxlength="200" required placeholder="Explica por que se realiza este cambio"></textarea>
                        </div>
                    </c:if>

                    <div class="mt-4 d-flex justify-content-end gap-3">
                        <c:choose>
                            <c:when test="${tipo == 'asignaciones' && origen == 'movimientos'}">
                                <a href="${ctx}/GestionarAsignaciones" class="btn btn-outline-secondary px-4">Cancelar</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${ctx}/GestionCatalogo?tipo=${tipo}" class="btn btn-outline-secondary px-4">Cancelar</a>
                            </c:otherwise>
                        </c:choose>
                        <button type="submit" class="btn btn-primary px-4">Guardar</button>
                    </div>
                </form>
                <c:if test="${modo == 'editar' && (tipo == 'activos' || tipo == 'personal' || tipo == 'asignaciones' || tipo == 'mantenimiento' || tipo == 'proveedores')}">
                    <section class="mt-5 pt-4 border-top">
                        <h2 class="h5 mb-3">Historial del registro</h2>
                        <div class="table-responsive">
                            <table class="table table-sm table-striped align-middle">
                                <thead><tr><th>Fecha</th><th>Accion</th><th>Responsable</th><th>Motivo</th></tr></thead>
                                <tbody>
                                    <c:forEach var="h" items="${historial}"><tr><td>${h.fecha}</td><td>${h.accion}</td><td>${h.usuarioNombre}</td><td>${h.motivo}</td></tr></c:forEach>
                                    <c:if test="${empty historial}"><tr><td colspan="4" class="text-muted">Aun no hay cambios registrados para este registro.</td></tr></c:if>
                                </tbody>
                            </table>
                        </div>
                    </section>
                </c:if>
            </div>
        </div>
        <script src="${ctx}/JavaScript/validacion_formularios.js?v=2"></script>
    </body>
</html>
