<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Editar Activo - OptiGest</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">
        <link rel="stylesheet" href="${ctx}/Css/EditarActivo.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
        <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
    </head>

    <body class="editar-activo-page">
        <div class="container py-5">
            <div class="card p-4 shadow w-100" style="max-width: 760px; margin: auto;">

                <h2>Editar Activo</h2>
                <p class="text-muted">Modifique la información del activo y guarde los cambios.</p>

                <c:if test="${not empty mensaje}">
                    <div class="alert alert-info">${mensaje}</div>
                </c:if>

                <form action="${ctx}/EditarActivo" method="post">
                    <input type="hidden" name="id_activos" value="${activo.id_activos}">

                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label">Nombre del activo</label>
                            <input type="text" class="form-control" name="nombre_activos"
                                   value="${activo.nombre_activos}" required minlength="3" maxlength="30">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Código del activo</label>
                            <input type="text" class="form-control" name="codigo_act"
                                   value="${activo.codigo_act}" required minlength="3" maxlength="30" pattern="[A-Za-z0-9-]{3,30}">
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Valor (COP)</label>
                            <input type="text" class="form-control" name="valor"
                                   value="${activo.valor}" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Vida util (anios)</label>
                            <input type="number" class="form-control" name="vida_util"
                                   min="1" max="20" step="1"
                                   value="${activo.vida_util}" required>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label">Fecha de adquisicion</label>
                            <input type="date" class="form-control" name="fecha_adquma"
                                   value="${activo.fecha_adquma}" required>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label">Estado</label>
                            <select class="form-select" name="Estado_Activo_idEstado_Activo" required>
                                <option value="" disabled>Seleccione estado...</option>
                                <c:forEach var="estado" items="${estadosActivo}">
                                    <option value="${estado.idEstado_Activo}"
                                            ${estado.idEstado_Activo == activo.estado_Activo_idEstado_Activo ? 'selected' : ''}>
                                        ${estado.descripcion_activo}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label">Categoria</label>
                            <select class="form-select" name="Categorias_idCategorias" required>
                                <option value="" disabled>Seleccione categoria...</option>
                                <c:forEach var="categoria" items="${categorias}">
                                    <option value="${categoria.idCategorias}"
                                            ${categoria.idCategorias == activo.categorias_idCategorias ? 'selected' : ''}>
                                        ${categoria.descripcionCategoria}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label class="form-label">Proveedor</label>
                            <select class="form-select" name="Proveedores_idProveedores" required>
                                <option value="" disabled>Seleccione proveedor...</option>
                                <c:forEach var="proveedor" items="${proveedores}">
                                    <option value="${proveedor.idProveedores}"
                                            ${proveedor.idProveedores == activo.proveedores_idProveedores ? 'selected' : ''}>
                                        ${proveedor.nombre}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-12">
                            <label class="form-label">Descripción</label>
                            <textarea class="form-control" name="descripcion" rows="3" maxlength="200">${activo.descripcion}</textarea>
                        </div>
                        <div class="col-12">
                            <label class="form-label" for="motivo">Motivo del cambio</label>
                            <textarea id="motivo" class="form-control" name="motivo" rows="3" maxlength="500" required placeholder="Explica por que se realiza este cambio"></textarea>
                        </div>
                    </div>

                    <div class="mt-5 d-flex justify-content-end gap-3">
                        <a href="${ctx}/ControladorActivo?accion=listar" class="btn btn-outline-secondary px-4">
                            Cancelar
                        </a>
                        <button type="submit" class="btn btn-primary px-4">
                            Guardar cambios
                        </button>
                    </div>
                </form>
                <section class="mt-5 pt-4 border-top">
                    <h2 class="h5 mb-3">Historial del activo</h2>
                    <div class="table-responsive">
                        <table class="table table-sm table-striped align-middle">
                            <thead><tr><th>Fecha</th><th>Accion</th><th>Responsable</th><th>Motivo</th></tr></thead>
                            <tbody>
                                <c:forEach var="h" items="${historial}"><tr><td>${h.fecha}</td><td>${h.accion}</td><td>${h.usuarioNombre}</td><td>${h.motivo}</td></tr></c:forEach>
                                <c:if test="${empty historial}"><tr><td colspan="4" class="text-muted">Aun no hay cambios registrados para este activo.</td></tr></c:if>
                            </tbody>
                        </table>
                    </div>
                </section>
            </div>
        </div>
        <script src="${ctx}/JavaScript/validacion_formularios.js?v=2"></script>
    </body>
</html>
