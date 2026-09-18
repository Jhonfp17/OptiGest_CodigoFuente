<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<fmt:setLocale value="es_CO" />

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />

        <title>Gestión de Activos OptiGest</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">

        <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">


        <link rel="stylesheet" href="${ctx}/Css/GestionActivos.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
        <link rel="stylesheet" href="${ctx}/Css/ConfirmacionBaja.css">

        <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
    </head>

    <body class="gestion-activos-page">

        <div class="shell">

            <aside class="sidebar" id="sidebar">

                <div class="sidebar-brand">
                    <img class="brand-logo-image" src="${ctx}/Imagenes/OptiGestLogo.jpg" alt="OptiGest">


                </div>


                <nav class="sidebar-nav">
                    <a href="${ctx}/PanelControl" class="sn-item">
                        <i class="bi bi-grid-1x2-fill"></i>
                        <span>Inicio</span>
                    </a>
                    <a href="${ctx}/ControladorActivo?accion=listar" class="sn-item active">
                        <i class="bi bi-box-seam-fill"></i>
                        <span>Activos</span>
                    </a>
                    <a href="${ctx}/GestionarPersonal" class="sn-item">
                        <i class="bi bi-person-badge-fill"></i>
                        <span>Personal</span>
                    </a>
                    <a href="${ctx}/GestionarAsignaciones" class="sn-item">
                        <i class="bi bi-clipboard-check-fill"></i>
                        <span>Asignaciones</span>
                    </a>
                    <a href="${ctx}/CerrarSesion" class="sn-item" onclick="event.preventDefault(); const f = document.createElement('form'); f.method = 'post'; f.action = this.href; document.body.appendChild(f); f.submit();">
                        <i class="bi bi-box-arrow-right"></i>
                        <span>Cerrar sesión</span>
                    </a>
                </nav>

            </aside>

            <main class="main" id="main" data-estados-url="${ctx}/ControladorActivo?accion=estados">

                <header class="topbar">
                    <div class="page-header-text">
                        <h1 class="page-title">Gestión de Activos</h1>
                        <p class="page-sub">Vista general de activos registrados.</p>
                    </div>
                </header>

                <div class="page-head">
                    <div class="search-wrap">
                        <i class="bi bi-search search-icon"></i>
                        <input class="search-input" type="text" placeholder="Buscar activo..." />
                    </div>
                    <a href="${ctx}/GuardarActivo" class="btn-registrar">
                        <i class="bi bi-plus-lg"></i>
                        Registrar Activo
                    </a>
                </div>

                <c:if test="${param.guardado == '1'}">
                    <div class="alert alert-success">
                        Activo registrado correctamente.
                    </div>
                </c:if>

                <c:if test="${param.actualizado == '1'}">
                    <div class="alert alert-success">
                        Activo actualizado correctamente.
                    </div>
                </c:if>

                <c:if test="${param.eliminado == '1'}">
                    <div class="alert alert-success">
                        Activo retirado correctamente.
                    </div>
                </c:if>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger">
                        No se pudo completar la operación. Verifica los datos.
                    </div>
                </c:if>

                <c:if test="${not empty param.bloqueo}">
                    <div id="modalBajaBloqueada" class="confirmacion-baja confirmacion-baja--visible" role="dialog" aria-modal="true" aria-labelledby="tituloBajaBloqueada">
                        <div class="confirmacion-baja__panel">
                            <h2 id="tituloBajaBloqueada">No se puede dar de baja el activo</h2>
                            <p>
                                <c:choose>
                                    <c:when test="${param.bloqueo == 'mantenimiento'}">El activo está en mantenimiento.</c:when>
                                    <c:when test="${param.bloqueo == 'asignado'}">El activo está asignado actualmente.</c:when>
                                    <c:when test="${param.bloqueo == 'activo_retirado'}">El activo ya fue dado de baja totalmente en el módulo de Activos.</c:when>
                                    <c:otherwise>No fue posible validar el estado del activo.</c:otherwise>
                                </c:choose>
                            </p>
                            <div class="confirmacion-baja__acciones">
                                <button type="button" class="confirmacion-baja__cancelar" data-cerrar-bloqueo>Entendido</button>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="table-card">
                    <div class="table-wrap">
                        <table class="activos-tbl">
                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Código</th>
                                    <th>Valor</th>
                                    <th>Proveedor</th>
                                    <th>Estado</th>
                                    <th>Fecha Adquisición</th>
                                    <th>Fecha Devolución</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty activos}">
                                        <tr>
                                            <td colspan="8" class="sin-datos">
                                                No hay activos registrados.
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="a" items="${activos}">
                                            <tr>
                                                <td class="td-name">${a.nombre_activos}</td>
                                                <td>${a.codigo_act}</td>
                                                <td class="td-qty">$<fmt:formatNumber value="${a.valor}" type="number" groupingUsed="true" minFractionDigits="0" maxFractionDigits="0" /></td>
                                                <td>${proveedoresMap[a.proveedores_idProveedores]}</td>
                                                <td><span class="estado-badge" data-estado-activo="${a.id_activos}">${a.estadoActual}</span></td>
                                                <td>${a.fecha_adquma}</td>
                                                <td>${empty a.fecha_devolucion ? 'N/A' : a.fecha_devolucion}</td>
                                                <td class="acciones">
                                                    <a class="tbl-btn tbl-edit"
                                                       href="${ctx}/EditarActivo?id=${a.id_activos}"
                                                       title="Editar activo">
                                                        <i class="bi bi-pencil-fill"></i>
                                                    </a>
                                                    <form action="${ctx}/ControladorActivo" method="post" class="form-eliminar">
                                                        <input type="hidden" name="accion" value="eliminar">
                                                        <input type="hidden" name="id" value="${a.id_activos}">
                                                        <input type="hidden" name="motivo" value="">
                                                        <button type="submit" class="btn-eliminar" title="Inactivar activo">
                                                            <i class="bi bi-archive-fill"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <details class="table-card mt-4">
                    <summary class="p-3 fw-bold">Activos inactivos o retirados</summary>
                    <div class="table-wrap">
                        <table class="activos-tbl">
                            <thead>
                                <tr><th>Nombre</th><th>Código</th><th>Proveedor</th><th>Estado</th><th>Fecha de baja</th><th>Consulta</th></tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty activosInactivos}">
                                        <tr><td colspan="6" class="sin-datos">No hay activos retirados o inactivos.</td></tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="a" items="${activosInactivos}">
                                            <tr>
                                                <td class="td-name">${a.nombre_activos}</td>
                                                <td>${a.codigo_act}</td>
                                                <td>${proveedoresMap[a.proveedores_idProveedores]}</td>
                                                <td><span class="estado-badge estado-badge--danger">${a.estadoActual}</span></td>
                                                <td>Registro conservado</td>
                                                <td><a class="tbl-btn tbl-edit" href="${ctx}/HistorialAdministrativo?entidad=Activos&buscar=${a.nombre_activos}" title="Ver historial"><i class="bi bi-clock-history"></i></a></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </details>

            </main>

        </div>

        <div class="sidebar-overlay" id="overlay"></div>

        <button class="menu-toggle d-lg-none" id="menuToggle">
            <i class="bi bi-list"></i>
        </button>

        <script src="${ctx}/JavaScript/bootstrap.bundle.min.js"></script>
        <script src="${ctx}/JavaScript/Panel.js"></script>
        <script src="${ctx}/JavaScript/Gestion_activos.js"></script>
        <script src="${ctx}/JavaScript/ConfirmacionBaja.js"></script>
    </body>

</html>
