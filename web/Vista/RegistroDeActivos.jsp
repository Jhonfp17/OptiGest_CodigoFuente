<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar Activo - OptiGest</title>

    <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">

    <link rel="stylesheet" href="${ctx}/Css/RegistroDeActivos.css">
    <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
    <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
</head>

<body class="registro-activos-page">

    <main class="registro-wrapper">

        <section class="registro-card">

            <header class="registro-header">
                <h2>Registrar Nuevo Activo</h2>
                <p>Complete los siguientes campos para añadir un nuevo activo al sistema.</p>
            </header>

            <c:if test="${not empty mensaje}">
                <div class="registro-alert">
                    ${mensaje}
                </div>
            </c:if>

            <form class="registro-form" action="${ctx}/GuardarActivo" method="post">
                <input type="hidden" name="origen" value="${param.origen}">

                <div class="form-grid">

                    <div class="form-field">
                        <label>Nombre del activo</label>
                        <input type="text"
                               name="nombre_activos"
                               placeholder="Ej: Laptop HP EliteBook 840 G8"
                               required minlength="3" maxlength="30">
                    </div>

                    <div class="form-field">
                        <label>Código del activo</label>
                        <input type="text"
                               name="codigo_act"
                               placeholder="Ej: LAP-00123"
                               required minlength="3" maxlength="30" pattern="[A-Za-z0-9-]{3,30}">
                    </div>

                    <div class="form-field">
                        <label>Valor (COP)</label>
                        <input type="number"
                               name="valor"
                               placeholder="Ej: 500000"
                               min="1" step="0.01" required>
                    </div>

                    <div class="form-field">
                        <label>Vida útil (años)</label>
                        <input type="number"
                               name="vida_util"
                               min="1" max="20"
                               step="1"
                               placeholder="Ej: 5"
                               required>
                    </div>

                    <div class="form-field">
                        <label>Fecha de adquisición</label>
                        <input type="date"
                               name="fecha_adquma"
                               required>
                    </div>

                    <div class="form-field">
                        <label>Estado del activo</label>
                        <select name="Estado_Activo_idEstado_Activo" required>
                            <option value="" selected disabled>Seleccione estado...</option>

                            <c:forEach var="estado" items="${estadosActivo}">
                                <option value="${estado.idEstado_Activo}">
                                    ${estado.descripcion_activo}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-field">
                        <label>Categoría</label>
                        <select name="Categorias_idCategorias" required>
                            <option value="" selected disabled>Seleccione categoría...</option>

                            <c:forEach var="categoria" items="${categorias}">
                                <option value="${categoria.idCategorias}">
                                    ${categoria.descripcionCategoria}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-field">
                        <label>Proveedor</label>
                        <select name="Proveedores_idProveedores" required>
                            <option value="" selected disabled>Seleccione proveedor...</option>

                            <c:forEach var="proveedor" items="${proveedores}">
                                <option value="${proveedor.idProveedores}">
                                    ${proveedor.nombre}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-field form-field-full">
                        <label>Descripción</label>
                        <textarea name="descripcion"
                                  rows="3"
                                  maxlength="200"
                                  placeholder="Descripción física o detalles relevantes..."></textarea>
                    </div>

                </div>

                <div class="form-actions">
                    <button type="button" class="btn-cancelar" onclick="history.back()">
                        Cancelar
                    </button>

                    <button type="submit" class="btn-guardar">
                        Guardar Activo
                    </button>
                </div>

            </form>

        </section>

    </main>

    <script src="${ctx}/JavaScript/bootstrap.bundle.min.js"></script>
    <script src="${ctx}/JavaScript/validacion_formularios.js?v=2"></script>

</body>
</html>

