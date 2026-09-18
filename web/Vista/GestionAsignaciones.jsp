<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestión de Asignaciones - OptiGest</title>
        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">
        <link rel="stylesheet" href="${ctx}/Css/GestionPersonal.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
        <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
    </head>

    <body class="gestion-personal-page gestion-asignaciones-page">
        <div class="shell">
            <aside class="sidebar" id="sidebar">
                <div class="sidebar-brand"><img class="brand-logo-image" src="${ctx}/Imagenes/OptiGestLogo.jpg" alt="OptiGest"></div>
                <nav class="sidebar-nav">
                    <a href="${ctx}/PanelControl" class="sn-item"><i class="bi bi-grid-1x2-fill"></i><span>Inicio</span></a>
                    <a href="${ctx}/ControladorActivo?accion=listar" class="sn-item"><i class="bi bi-box-seam-fill"></i><span>Activos</span></a>
                    <a href="${ctx}/GestionarPersonal" class="sn-item"><i class="bi bi-person-badge-fill"></i><span>Personal</span></a>
                    <a href="${ctx}/GestionarAsignaciones" class="sn-item active"><i class="bi bi-clipboard-check-fill"></i><span>Asignaciones</span></a>
                    <a href="${ctx}/CerrarSesion" class="sn-item" onclick="event.preventDefault(); const f=document.createElement('form'); f.method='post'; f.action=this.href; document.body.appendChild(f); f.submit();"><i class="bi bi-box-arrow-right"></i><span>Cerrar sesión</span></a>
                </nav>
            </aside>

            <main class="main" id="main">
                <header class="topbar"><div class="page-header-text"><h1 class="page-title">Gestión de Asignaciones</h1><p class="page-sub">Vista general de las asignaciones registradas.</p></div></header>
                <div class="page-head">
                    <div class="search-wrap"><i class="bi bi-search search-icon"></i><input class="search-input" type="search" placeholder="Buscar asignación..."></div>
                    <a href="${ctx}/GuardarCatalogo?tipo=asignaciones&origen=movimientos" class="btn-registrar"><i class="bi bi-plus-lg"></i>Nueva asignación</a>
                </div>

                <c:if test="${param.guardado == '1'}"><div class="alert alert-success">Asignación registrada correctamente.</div></c:if>
                <c:if test="${param.actualizado == '1'}"><div class="alert alert-success">Asignación actualizada correctamente.</div></c:if>
                <c:if test="${param.error == 'asignacion_no_disponible'}"><div class="alert alert-warning">La asignación ya no está disponible para edición.</div></c:if>
                <c:if test="${not empty param.error && param.error != 'asignacion_no_disponible'}"><div class="alert alert-danger">No se pudo completar la operación. Verifica los datos.</div></c:if>

                <div class="table-card"><div class="table-wrap"><table class="personal-tbl asignaciones-tbl">
                    <thead><tr><th>Persona</th><th>Documento</th><th>Activo</th><th>Código</th><th>Estado activo</th><th>Fecha asignación</th><th>Fecha devolución</th><th>Observaciones</th><th>Estado</th><th>Acciones</th></tr></thead>
                    <tbody><c:choose><c:when test="${not empty asignaciones}"><c:forEach var="a" items="${asignaciones}" varStatus="estadoFila">
                        <c:set var="anulada" value="${fn:contains(a.observaciones, '[ANULADA - registro conservado]')}" />
                        <tr class="asignacion-fila${estadoFila.index >= 5 ? ' asignacion-fila-oculta' : ''}" data-asignacion-extra="${estadoFila.index >= 5}">
                            <td>${a.nombrePersona} ${a.apellidosPersona}</td><td>${a.identificacion}</td><td>${a.nombreActivo}</td><td>${a.codigoActivo}</td><td>${empty a.estadoActivo ? 'NO DISPONIBLE' : a.estadoActivo}</td><td>${a.fechaAsignacion}</td>
                            <c:set var="vigente" value="${empty a.fechaDevolucion || a.fechaDevolucion >= fechaActual}" />
                            <td><c:choose><c:when test="${vigente}">Vigente</c:when><c:otherwise>${a.fechaDevolucion}</c:otherwise></c:choose></td><td>${a.observaciones}</td>
                            <td><c:choose><c:when test="${anulada}"><span class="badge bg-secondary">Anulada</span></c:when><c:when test="${vigente}"><span class="badge bg-success">Vigente</span></c:when><c:otherwise><span class="badge bg-secondary">Finalizada</span></c:otherwise></c:choose></td>
                            <td class="acciones"><a class="btn-ver" href="${ctx}/HistorialAdministrativo?entidad=Asignaciones&buscar=${a.codigoActivo}" title="Ver historial de asignaciones del activo"><i class="bi bi-clock-history"></i></a><c:if test="${not anulada && vigente}"><a class="btn-editar" href="${ctx}/EditarCatalogo?tipo=asignaciones&id=${a.idAsignacion}&origen=movimientos" title="Editar asignación"><i class="bi bi-pencil-square"></i></a></c:if></td>
                        </tr>
                    </c:forEach></c:when><c:otherwise><tr><td colspan="10" class="sin-datos">No hay asignaciones registradas.</td></tr></c:otherwise></c:choose></tbody>
                </table></div>
                <c:if test="${fn:length(asignaciones) > 5}">
                    <div class="asignaciones-expandir-wrap">
                        <button type="button" class="btn-ver-mas-asignaciones" aria-expanded="false">Ver todas las asignaciones</button>
                    </div>
                </c:if>
                </div>
            </main>
        </div>
        <div class="sidebar-overlay" id="overlay"></div>
        <button class="menu-toggle d-lg-none" id="menuToggle"><i class="bi bi-list"></i></button>
        <script src="${ctx}/JavaScript/Panel.js"></script>
        <script src="${ctx}/JavaScript/Gestion_personal.js"></script>
        <script src="${ctx}/JavaScript/Gestion_asignaciones.js"></script>
    </body>
</html>
