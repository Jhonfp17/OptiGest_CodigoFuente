<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="Modelo.Personal"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>

<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestión de Personal - OptiGest</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">
        
        <link rel="stylesheet" href="${ctx}/Css/GestionPersonal.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
        <link rel="stylesheet" href="${ctx}/Css/ConfirmacionBaja.css">
        <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">

    </head>

    <body class="gestion-personal-page">

        <%
        Personal admin = (Personal) session.getAttribute("usuarioLogueado");

        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/Vista/InicioSesion.jsp");
            return;
        }

        if (!admin.esAdministrador()) {
            response.sendRedirect(request.getContextPath() + "/Vista/PerfilUsuario.jsp");
            return;
        }

        %>

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

                    <a href="${ctx}/GestionarPersonal" class="sn-item active">
                        <i class="bi bi-person-badge-fill"></i>
                        <span>Personal</span>
                    </a>

                    <a href="${ctx}/GestionarAsignaciones" class="sn-item">
                        <i class="bi bi-clipboard-check-fill"></i>
                        <span>Asignaciones</span>
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
                        <h1 class="page-title">Gestión de Personal</h1>
                        <p class="page-sub">Vista general del personal registrado.</p>
                    </div>
                </header>

                <div class="page-head">

                    <div class="search-wrap">
                        <i class="bi bi-search search-icon"></i>
                        <input class="search-input" type="text" placeholder="Buscar personal...">
                    </div>

                    <a href="${ctx}/GuardarPersonal" class="btn-registrar">
                        <i class="bi bi-plus-lg"></i>
                        Registrar Personal
                    </a>

                </div>

                <%
                    String mensaje = (String) request.getAttribute("mensaje");
                    if (mensaje != null) {
                %>
                <div class="alert alert-info">
                    <%= mensaje %>
                </div>
                <%
                    }
                %>

                <c:if test="${param.actualizado == '1'}">
                    <div class="alert alert-success">
                        Personal actualizado correctamente.
                    </div>
                </c:if>

                <c:if test="${param.guardado == '1'}">
                    <div class="alert alert-success">
                        Personal registrado correctamente.
                    </div>
                </c:if>

                <c:if test="${param.activacion == '1'}">
                    <div class="alert alert-info">
                        Cuenta creada sin compartir contraseña. Informa a la persona que debe usar “Olvidé mi contraseña” con su correo, documento y teléfono para definir su clave inicial.
                    </div>
                </c:if>

                <c:if test="${not empty claveGenerada}">
                    <div class="alert alert-warning clave-generada-panel" role="alert">
                        <div>
                            <strong>Contraseña temporal generada</strong><br>
                            Entrégala al empleado por un medio seguro. Se muestra una única vez y deberá cambiarse al iniciar sesión.
                        </div>
                        <div class="clave-generada-valor">
                            <code id="claveGenerada">${claveGenerada}</code>
                            <button type="button" data-copiar-clave>Copiar</button>
                        </div>
                    </div>
                </c:if>

                <c:if test="${param.desactivado == '1'}">
                    <div class="alert alert-success">
                        Personal desactivado correctamente.
                    </div>
                </c:if>

                <c:if test="${not empty param.error}">
                    <div class="alert alert-danger">
                        No se pudo completar la operación. Verifica los datos.
                    </div>
                </c:if>

                <c:if test="${param.bloqueo == 'asignaciones_activas'}">
                    <div id="modalPersonalConActivos" class="confirmacion-baja confirmacion-baja--visible" role="dialog" aria-modal="true" aria-labelledby="tituloPersonalConActivos">
                        <div class="confirmacion-baja__panel">
                            <h2 id="tituloPersonalConActivos">No se puede eliminar el empleado</h2>
                            <p>El empleado tiene activos asignados. Debes eliminar manualmente las asignaciones antes de continuar.</p>
                            <div class="confirmacion-baja__acciones">
                                <button type="button" class="confirmacion-baja__cancelar" data-cerrar-bloqueo>Entendido</button>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="table-card">
                    <div class="table-wrap">

                        <table class="personal-tbl">

                            <thead>
                                <tr>
                                    <th>Nombre</th>
                                    <th>Apellidos</th>
                                    <th>Identificación</th>
                                    <th>Email</th>
                                    <th>Teléfono</th>
                                    <th>Fecha Contratación</th>
                                    <th>Acceso</th>
                                    <th>Rol</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>

                            <tbody>

                                <c:choose>

                                    <c:when test="${not empty listaPersonal}">

                                        <c:forEach var="p" items="${listaPersonal}">
                                            <tr>
                                                <td>${p.nombre}</td>
                                                <td>${p.apellidos}</td>
                                                <td>${p.identificacion}</td>
                                                <td>${p.email}</td>
                                                <td>${p.telefono}</td>
                                                <td>${p.fecha_contratacion}</td>
                                                <td>${p.puede_acceder ? 'Sí' : 'No'}</td>
                                                <td>${rolesMap[p.roles_idroles]}</td>
                                                <c:set var="estadoPersonal" value="${estadoPersonalMap[p.estado_Personal_id_estado]}" />
                                                <td><span class="estado-badge
                                                    <c:choose>
                                                        <c:when test="${estadoPersonal == 'ACTIVO'}"> estado-badge--success</c:when>
                                                        <c:when test="${estadoPersonal == 'VACACIONES'}"> estado-badge--warning</c:when>
                                                        <c:when test="${estadoPersonal == 'INCAPACIDAD'}"> estado-badge--neutral</c:when>
                                                        <c:otherwise> estado-badge--danger</c:otherwise>
                                                    </c:choose>
                                                ">${empty estadoPersonal ? 'SIN ESTADO' : estadoPersonal}</span></td>

                                                <td class="acciones">

                                                    <a class="btn-ver"
                                                       href="${ctx}/VerPerfil?id=${p.idPersonal}"
                                                       title="Ver personal">
                                                        <i class="bi bi-eye"></i>
                                                    </a>

                                                    <c:choose>
                                                        <c:when test="${p.idPersonal == 3}">
                                                            <span class="text-success" title="Administrador maestro protegido">
                                                                <i class="bi bi-shield-lock-fill"></i>
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <form action="${ctx}/EliminarPersonal" method="post" class="form-eliminar">
                                                                <input type="hidden" name="id" value="${p.idPersonal}">
                                                                <input type="hidden" name="motivo" value="">
                                                                <button type="submit" class="btn-eliminar" title="Desactivar personal">
                                                                    <i class="bi bi-person-x-fill"></i>
                                                                </button>
                                                            </form>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </td>
                                            </tr>
                                        </c:forEach>

                                    </c:when>

                                    <c:otherwise>

                                        <tr>
                                            <td colspan="10" class="sin-datos">
                                                No hay personal registrado.
                                            </td>
                                        </tr>

                                    </c:otherwise>

                                </c:choose>

                            </tbody>

                        </table>

                    </div>
                </div>

                <details class="table-card mt-4">
                    <summary class="p-3 fw-bold">Personal retirado</summary>
                    <div class="table-wrap">
                        <table class="personal-tbl">
                            <thead><tr><th>Nombre</th><th>Identificación</th><th>Email</th><th>Teléfono</th><th>Acción</th></tr></thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${empty listaPersonalRetirado}"><tr><td colspan="5" class="sin-datos">No hay personal inactivo o retirado.</td></tr></c:when>
                                    <c:otherwise><c:forEach var="p" items="${listaPersonalRetirado}">
                                        <tr>
                                            <td>${p.nombre} ${p.apellidos}</td><td>${p.identificacion}</td><td>${p.email}</td><td>${p.telefono}</td>
                                            <td><a class="btn-ver" href="${ctx}/HistorialAdministrativo?entidad=Personal&buscar=${p.nombre}%20${p.apellidos}" title="Ver historial"><i class="bi bi-clock-history"></i></a></td>
                                        </tr>
                                    </c:forEach></c:otherwise>
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

        <script src="${ctx}/JavaScript/Gestion_personal.js"></script>
        <script src="${ctx}/JavaScript/ConfirmacionBaja.js"></script>
        <script>
            document.addEventListener('click', function (evento) {
                if (!evento.target.matches('[data-copiar-clave]')) return;
                navigator.clipboard.writeText(document.getElementById('claveGenerada').textContent)
                        .then(function () { evento.target.textContent = 'Copiada'; });
            });
        </script>

    </body>
</html>
