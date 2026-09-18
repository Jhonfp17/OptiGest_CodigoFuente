<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Panel de Control - OptiGest</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">

        <link rel="stylesheet" href="${ctx}/Css/PanelControlActivosYPersonal.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
        <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
    </head>

    <body class="panel-page">

        <nav class="panel-navbar">

            <a class="panel-brand" href="${ctx}/PanelControl">
                <img class="panel-brand-logo" src="${ctx}/Imagenes/OptiGestLogo.jpg" alt="OptiGest">
            </a>

            <div class="panel-nav-actions">

                <a href="${ctx}/GestionContacto" class="panel-profile-link position-relative" title="Solicitudes de contacto">
                    <i class="bi bi-bell-fill"></i>
                    <span>Solicitudes</span>
                    <c:if test="${solicitudesPendientes > 0}">
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                            ${solicitudesPendientes}
                            <span class="visually-hidden">solicitudes pendientes</span>
                        </span>
                    </c:if>
                </a>

                <a href="${ctx}/VerPerfil?id=${usuarioLogueado.idPersonal}"
                   class="panel-profile-link">
                    <i class="bi bi-person-circle"></i>
                    <span>${nombreVisible}</span>
                </a>

                <a href="${ctx}/CerrarSesion" class="panel-logout-link" onclick="event.preventDefault(); const f=document.createElement('form'); f.method='post'; f.action=this.href; document.body.appendChild(f); f.submit();">
                    <i class="bi bi-box-arrow-right"></i>
                    <span>Cerrar Sesión</span>
                </a>

            </div>

        </nav>

        <main class="panel-main">

            <header class="panel-header">
                <h1>Panel Administrador</h1>

                <p>
                    Bienvenido, ${nombreVisible}.
                    Consulte y administre el personal registrado.
                </p>
            </header>

            <section class="panel-actions">

                <a href="${ctx}/GestionarPersonal" class="action-card">
                    <div class="action-icon">
                        <i class="bi bi-person-check"></i>
                    </div>
                    <h6>Gestor de Movimientos</h6>
                    <p>Consulte el Personal y los Activos</p>
                </a>

                <a href="${ctx}/Configuracion" class="action-card">
                    <div class="action-icon">
                        <i class="bi bi-gear-fill"></i>
                    </div>
                    <h6>Configuración del Sistema</h6>
                    <p>Administre Personal, Activos y Operaciones </p>
                </a>

            </section>

            <section class="panel-grid">

                <div class="panel-card panel-card-wide">

                    <div class="panel-card-body">

                        <h5>Últimas Asignaciones de Activo</h5>

                        <div class="table-responsive">

                            <table class="panel-table">

                                <thead>
                                    <tr>
                                        <th>Activo</th>
                                        <th>Personal</th>
                                        <th>Fecha Asignación</th>
                                        <th>Fecha Devolución</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty asignaciones}">
                                            <c:forEach var="a" items="${asignaciones}">
                                                <tr>
                                                    <td>${activosMap[a.activos_id_activos]}</td>
                                                    <td>${personalMap[a.personal_id_personal]}</td>
                                                    <td>${a.fecha_asignacion}</td>
                                                    <td>${a.fecha_devolucion}</td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>

                                        <c:otherwise>
                                            <tr>
                                                <td colspan="4" class="empty-cell">
                                                    No hay asignaciones registradas.
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>

                            </table>

                        </div>

                    </div>

                </div>

                <aside class="panel-card">

                    <div class="panel-card-body">

                        <h5>Resumen de Activos</h5>

                        <div class="summary-block">
                            <p>Total de Activos</p>
                            <strong>${totalActivos}</strong>
                        </div>

                        <div class="status-block">
                            <h6>Estado General</h6>

                            <div class="status-row">
                                <span class="status-dot disponible"></span>
                                <span>
                                    Ver detalle en
                                    <a href="${ctx}/ControladorActivo?accion=listar">
                                        Gestión Activos
                                    </a>
                                </span>
                            </div>
                        </div>

                    </div>

                </aside>

            </section>

            <section class="panel-card">

                <div class="panel-card-body">

                    <h5>Personal Registrado</h5>

                    <div class="table-responsive">

                        <table class="panel-table">

                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Identificación</th>
                                    <th>Puede Acceder</th>
                                    <th>Accion</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty listaPersonal}">
                                        <c:forEach var="per" items="${listaPersonal}">
                                            <tr>
                                                <td>
                                                    <strong>${per.nombre} ${per.apellidos}</strong>
                                                </td>

                                                <td>${per.identificacion}</td>

                                                <td>
                                                    <c:choose>
                                                        <c:when test="${per.puede_acceder}">
                                                            Si
                                                        </c:when>
                                                        <c:otherwise>
                                                            No
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td>
                                                    <a href="${ctx}/VerPerfil?id=${per.idPersonal}" class="table-action-btn">
                                                        Ver
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>

                                    <c:otherwise>
                                        <tr>
                                            <td colspan="4" class="empty-cell">
                                                No hay personal registrado.
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>

                        </table>

                    </div>

                </div>

            </section>

        </main>

        <script src="${ctx}/JavaScript/Panel.js"></script>
        <script src="${ctx}/JavaScript/bootstrap.bundle.min.js"></script>

    </body>

</html>
