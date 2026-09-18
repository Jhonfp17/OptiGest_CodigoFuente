<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${titulo} - Configuración</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctx}/Css/GestionCatalogo.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
        <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
    </head>

    <body class="catalogo-page">

        <div class="catalogo-container">

            <div class="catalogo-header">
                <div>
                    <h1>${titulo}</h1>
                    <p>Administración de ${titulo}</p>
                </div>

                <div class="catalogo-actions">
                    <a href="${ctx}/Configuracion" class="btn-volver">Volver</a>
                    <c:choose>
                        <c:when test="${tipo == 'activos'}">
                            <a href="${ctx}/GuardarActivo?origen=configuracion" class="btn-nuevo">Nuevo Registro</a>
                        </c:when>
                        <c:when test="${tipo == 'personal'}">
                            <a href="${ctx}/GuardarPersonal?origen=configuracion" class="btn-nuevo">Nuevo Registro</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${ctx}/GuardarCatalogo?tipo=${tipo}" class="btn-nuevo">Nuevo Registro</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="catalogo-card">

                <c:if test="${not empty mensaje}">
                    <div class="alert alert-info">${mensaje}</div>
                </c:if>

                <c:if test="${param.guardado == '1'}">
                    <div class="alert alert-success">Registro guardado correctamente.</div>
                </c:if>

                <c:if test="${param.actualizado == '1'}">
                    <div class="alert alert-success">Registro actualizado correctamente.</div>
                </c:if>

                <c:if test="${param.eliminado == '1'}">
                    <div class="alert alert-success">Registro desactivado correctamente.</div>
                </c:if>

                <c:if test="${param.conservado == '1'}">
                    <div class="alert alert-success">El registro fue desactivado o anulado.</div>
                </c:if>

                <c:if test="${param.error == 'eliminar' && tipo == 'documentos'}">
                    <div class="alert alert-warning">Los tipos de documento no se pueden desactivar; el catálogo se conserva completo.</div>
                </c:if>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger">No se pudo completar la operación. Verifica los datos.</div>
                </c:if>

                <c:if test="${param.error == 'activo_no_disponible'}"><div class="alert alert-warning">No se puede asignar: el activo no esta disponible.</div></c:if>
                <c:if test="${param.error == 'activo_ya_asignado'}"><div class="alert alert-warning">No se puede asignar: el activo ya tiene una asignacion vigente.</div></c:if>
                <c:if test="${param.error == 'personal_retirado'}"><div class="alert alert-warning">No se puede modificar ni asignar a una persona retirada.</div></c:if>
                <c:if test="${param.error == 'asignacion_cerrada'}"><div class="alert alert-warning">Una asignacion devuelta o cerrada se conserva como historial y no se puede modificar.</div></c:if>
                <c:if test="${param.error == 'activo_duplicado'}"><div class="alert alert-warning">No se puede guardar: ya existe un activo con ese codigo.</div></c:if>

                <div class="table-responsive">

                    <table class="catalogo-table">
                        <thead>
                            <tr>
                                <th>Descripción</th>
                                <th>Detalle</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="d" items="${datos}">
                                <c:set var="idRegistro" value="" />
                                <c:set var="descripcionRegistro" value="" />
                                <c:set var="detalleRegistro" value="" />

                                <c:choose>
                                    <c:when test="${tipo == 'roles'}">
                                        <c:set var="idRegistro" value="${d.idRoles}" />
                                        <c:set var="descripcionRegistro" value="${d.descripcion_roles}" />
                                        <c:set var="detalleRegistro" value="${d.tipo_acceso}" />
                                    </c:when>

                                    <c:when test="${tipo == 'documentos'}">
                                        <c:set var="idRegistro" value="${d.id_documento}" />
                                        <c:set var="descripcionRegistro" value="${d.descripcion_doc}" />
                                    </c:when>

                                    <c:when test="${tipo == 'categorias'}">
                                        <c:set var="idRegistro" value="${d.idCategorias}" />
                                        <c:set var="descripcionRegistro" value="${d.descripcionCategoria}" />
                                    </c:when>

                                    <c:when test="${tipo == 'horarios'}">
                                        <c:set var="idRegistro" value="${d.id_horarios}" />
                                        <c:set var="descripcionRegistro" value="${d.descripcion}" />
                                        <c:set var="detalleRegistro" value="${d.hora_ingreso} - ${d.hora_salida}" />
                                    </c:when>

                                    <c:when test="${tipo == 'dias'}">
                                        <c:set var="idRegistro" value="${d.idDias}" />
                                        <c:set var="descripcionRegistro" value="${d.descripcionDias}" />
                                    </c:when>

                                    <c:when test="${tipo == 'estadoActivo'}">
                                        <c:set var="idRegistro" value="${d.idEstado_Activo}" />
                                        <c:set var="descripcionRegistro" value="${d.descripcion_activo}" />
                                    </c:when>

                                    <c:when test="${tipo == 'estadoPersonal'}">
                                        <c:set var="idRegistro" value="${d.id_estado}" />
                                        <c:set var="descripcionRegistro" value="${d.descripcion_estado}" />
                                    </c:when>

                                    <c:when test="${tipo == 'proveedores'}">
                                        <c:set var="idRegistro" value="${d.idProveedores}" />
                                        <c:set var="descripcionRegistro" value="${d.nombre}" />
                                        <c:set var="detalleRegistro" value="${d.telefono} - ${d.email}" />
                                    </c:when>

                                    <c:when test="${tipo == 'mantenimiento'}">
                                        <c:set var="idRegistro" value="${d.id_mantenimiento}" />
                                        <c:set var="descripcionRegistro" value="${d.descripcion}" />
                                        <c:set var="detalleRegistro" value="${d.fecha_mante} - ${d.costo} - ${activosMap[d.activos_id_activos]} - ${proveedoresMap[d.proveedores_idProveedores]}" />
                                    </c:when>

                                    <c:when test="${tipo == 'personal'}">
                                        <c:set var="idRegistro" value="${d.idPersonal}" />
                                        <c:set var="descripcionRegistro" value="${d.nombre} ${d.apellidos}" />
                                        <c:set var="detalleRegistro" value="${d.identificacion} - ${rolesMap[d.roles_idroles]} - ${estadoPersonalMap[d.estado_Personal_id_estado]}" />
                                    </c:when>

                                    <c:when test="${tipo == 'activos'}">
                                        <c:set var="idRegistro" value="${d.id_activos}" />
                                        <c:set var="descripcionRegistro" value="${d.nombre_activos}" />
                                        <c:set var="detalleRegistro" value="${d.codigo_act} - ${d.valor} - ${categoriasMap[d.categorias_idCategorias]} - ${estadoActivoMap[d.estado_Activo_idEstado_Activo]} - ${proveedoresMap[d.proveedores_idProveedores]}" />
                                    </c:when>

                                    <c:when test="${tipo == 'asignaciones'}">
                                        <c:set var="idRegistro" value="${d.id_asignaciones}" />
                                        <c:set var="descripcionRegistro" value="${d.fecha_asignacion}" />
                                        <c:set var="detalleRegistro" value="${personalMap[d.personal_id_personal]} - ${activosMap[d.activos_id_activos]}" />
                                    </c:when>

                                    <c:when test="${tipo == 'programacionPersonal'}">
                                        <c:set var="idRegistro" value="${d.idProgramacion_Personal}" />
                                        <c:set var="descripcionRegistro" value="${d.descripcion_programacion}" />
                                        <c:set var="detalleRegistro" value="${d.fecha_desde} - ${d.fecha_hasta} - ${diasMap[d.dias_idDias]} - ${personalMap[d.personal_id_personal]} - ${horariosMap[d.horarios_id_horarios]}" />
                                    </c:when>
                                </c:choose>

                                <tr>
                                    <td>${descripcionRegistro}</td>
                                    <td>${detalleRegistro}</td>
                                    <td>
                                        <div class="acciones">
                                            <a class="btn-editar"
                                               href="${ctx}/EditarCatalogo?tipo=${tipo}&id=${idRegistro}">
                                                 Editar
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
