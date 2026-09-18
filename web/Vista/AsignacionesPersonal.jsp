<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Historial de asignaciones - OptiGest</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">
                <link rel="stylesheet" href="${ctx}/Css/GestionActivos.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
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
                    <a href="${ctx}/ControladorActivo?accion=listar" class="sn-item">
                        <i class="bi bi-box-seam-fill"></i>
                        <span>Activos</span>
                    </a>
                    <a href="${ctx}/GestionarPersonal" class="sn-item">
                        <i class="bi bi-person-badge-fill"></i>
                        <span>Personal</span>
                    </a>
                    <a href="${ctx}/AsignacionesPersonal" class="sn-item active">
                        <i class="bi bi-clipboard-check-fill"></i>
                        <span>Historial</span>
                    </a>
                    <a href="${ctx}/CerrarSesion" class="sn-item" onclick="event.preventDefault(); const f=document.createElement('form'); f.method='post'; f.action=this.href; document.body.appendChild(f); f.submit();">
                        <i class="bi bi-box-arrow-right"></i>
                        <span>Cerrar sesión</span>
                    </a>
                </nav>
            </aside>

            <main class="main" id="main">
                <header class="topbar">
                    <div class="page-header-text">
                        <h1 class="page-title">Historial de asignaciones</h1>
                        <p class="page-sub">Vista general de las asignaciones y su estado actual.</p>
                    </div>
                </header>

                    <form class="table-card p-4 mb-4" action="${ctx}/AsignacionesPersonal" method="get">
                        <div class="row g-3 align-items-end">
                            <div class="col-lg-4">
                                <label class="form-label">Consultar por</label>
                                <select class="form-select" name="tipoBusqueda" required>
                                    <option value="documento" ${tipoBusqueda == 'documento' ? 'selected' : ''}>Documento</option>
                                    <option value="activo" ${tipoBusqueda == 'activo' ? 'selected' : ''}>Activo</option>
                                </select>
                            </div>
                            <div class="col-lg-6">
                                <label class="form-label">Dato a consultar</label>
                                <input type="text"
                                       class="form-control"
                                       name="valorBusqueda"
                                       value="${valorBusqueda}"
                                       list="activosBusquedaList"
                                       placeholder="Documento, nombre o código"
                                       required>
                                <datalist id="activosBusquedaList">
                                    <c:forEach var="activo" items="${activosBusqueda}">
                                        <option value="${activo.nombre_activos}">
                                            ${activo.codigo_act}
                                        </option>
                                    </c:forEach>
                                </datalist>
                            </div>
                            <div class="col-lg-2 d-grid">
                                <button type="submit" class="btn-registrar">
                                    <i class="bi bi-search"></i>
                                    Consultar
                                </button>
                            </div>
                        </div>
                    </form>

                    <script src="${ctx}/JavaScript/validacion_formularios.js?v=2"></script>

                    <div class="table-card">
                        <c:if test="${busquedaRealizada || not empty asignacionesConsulta}">
                            <div class="px-4 pt-3 text-muted small">
                                Historial asignaciones.
                            </div>
                        </c:if>
                        <div class="table-wrap">
                            <table class="activos-tbl">
                                <thead>
                                    <tr>
                                        <th>Persona</th>
                                        <th>Documento</th>
                                        <th>Activo</th>
                                        <th>Código</th>
                                        <th>Estado activo</th>
                                        <th>Fecha asignación</th>
                                        <th>Fecha devolución</th>
                                        <th>Observaciones</th>
                                        <th>Estado</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty asignacionesConsulta}">
                                            <c:forEach var="a" items="${asignacionesConsulta}">
                                                <c:set var="asignacionAnulada" value="${fn:contains(a.observaciones, '[ANULADA - registro conservado]')}" />
                                                <c:set var="asignacionPendiente" value="${not asignacionAnulada && a.fechaAsignacion gt fechaActual}" />
                                                <c:set var="asignacionVigente" value="${not asignacionAnulada && not asignacionPendiente && (empty a.fechaDevolucion || a.fechaDevolucion ge fechaActual)}" />
                                                <c:set var="estadoClase" value="${asignacionAnulada ? 'anulada' : asignacionPendiente ? 'pendiente' : asignacionVigente ? 'vigente' : 'finalizada'}" />
                                                <c:set var="estadoTexto" value="${asignacionAnulada ? 'Anulada (conservada)' : asignacionPendiente ? 'Pendiente' : asignacionVigente ? 'Vigente' : 'Finalizada'}" />
                                                <tr>
                                                    <td class="td-name">${a.nombreCompleto}</td>
                                                    <td>${a.identificacion}</td>
                                                    <td>${a.nombreActivo}</td>
                                                    <td>${a.codigoActivo}</td>
                                                    <td>${empty a.estadoActivo ? 'NO DISPONIBLE' : a.estadoActivo}</td>
                                                    <td>
                                                        <span class="estado-fecha ${estadoClase}">
                                                            ${a.fechaAsignacion}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <span class="estado-fecha ${estadoClase}">
                                                            ${empty a.fechaDevolucion ? 'Vigente' : a.fechaDevolucion}
                                                        </span>
                                                    </td>
                                                    <td>${empty a.observaciones ? 'Sin observaciones' : a.observaciones}</td>
                                                    <td><span class="estado-fecha ${estadoClase}">${estadoTexto}</span></td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${busquedaRealizada}">
                                            <tr>
                                                <td colspan="9" class="sin-datos">
                                                    No se encontraron asignaciones para esos filtros.
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="9" class="sin-datos">
                                                    No hay asignaciones registradas.
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
            </main>
        </div>

        <div class="sidebar-overlay" id="overlay"></div>

        <button class="menu-toggle d-lg-none" id="menuToggle">
            <i class="bi bi-list"></i>
        </button>

        <script src="${ctx}/JavaScript/bootstrap.bundle.min.js"></script>
        <script src="${ctx}/JavaScript/Panel.js"></script>
    </body>
</html>
