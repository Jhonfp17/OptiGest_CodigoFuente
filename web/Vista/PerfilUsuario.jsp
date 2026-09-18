<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.Personal"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de Empleado - OptiGest</title>

    <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">
    
    <link rel="stylesheet" href="${ctx}/Css/PerfilUsuario.css">
    <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
    <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
</head>

<body class="perfil-page">

    <%
        Personal p = (Personal) request.getAttribute("personal");
        Personal usuarioSesion = (Personal) session.getAttribute("usuarioLogueado");
        boolean esAdminLogueado = usuarioSesion != null && usuarioSesion.esAdministrador();
        boolean esPerfilPropio = usuarioSesion != null && p != null
                && usuarioSesion.getIdPersonal() == p.getIdPersonal();

        if (p == null) {
            response.sendRedirect(request.getContextPath() + "/GestionarPersonal");
            return;
        }
    %>

    <div class="perfil-shell">

        <aside class="perfil-sidebar" id="sidebar">

            <div class="perfil-logo">
                <img class="perfil-logo-image" src="${ctx}/Imagenes/OptiGestLogo.jpg" alt="OptiGest">
            </div>

            <nav class="perfil-nav">

                <a href="${ctx}/PanelControl" class="perfil-nav-item">
                    <i class="bi bi-grid-1x2-fill"></i>
                    <span>Inicio</span>
                </a>

                <% if (esAdminLogueado) { %>

                <a href="${ctx}/ControladorActivo?accion=listar" class="perfil-nav-item">
                    <i class="bi bi-card-list"></i>
                    <span>Activos</span>
                </a>

                <a href="${ctx}/GestionarPersonal" class="perfil-nav-item">
                    <i class="bi bi-person-badge-fill"></i>
                    <span>Personal</span>
                </a>

                <a href="${ctx}/AsignacionesPersonal" class="perfil-nav-item">
                    <i class="bi bi-clipboard-check-fill"></i>
                    <span>Asignaciones</span>
                </a>

                <% } %>

                <a href="${ctx}/CerrarSesion" class="perfil-nav-item" onclick="event.preventDefault(); const f=document.createElement('form'); f.method='post'; f.action=this.href; document.body.appendChild(f); f.submit();">
                    <i class="bi bi-box-arrow-right"></i>
                    <span>Cerrar sesión</span>
                </a>

            </nav>

        </aside>

        <main class="perfil-main" id="main">

            <header class="perfil-page-head">
                <h1>Información de Empleado</h1>

                <button class="btn-regresar" type="button" onclick="history.back()">
                    <i class="bi bi-arrow-left"></i>
                    Regresar
                </button>
            </header>

            <c:if test="${param.claveOk == '1'}">
                <div class="alert alert-success perfil-alert">
                    Contraseña actualizada correctamente.
                </div>
            </c:if>

            <c:if test="${param.claveError == 'campos'}">
                <div class="alert alert-danger perfil-alert">
                    Completa todos los campos para cambiar la contraseña.
                </div>
            </c:if>

            <c:if test="${param.claveError == 'confirmacion'}">
                <div class="alert alert-danger perfil-alert">
                    La nueva contraseña y la confirmación no coinciden.
                </div>
            </c:if>

            <c:if test="${param.claveError == 'longitud'}">
                <div class="alert alert-danger perfil-alert">
                    La nueva contraseña debe tener al menos 4 caracteres.
                </div>
            </c:if>

            <c:if test="${param.claveError == 'actual'}">
                <div class="alert alert-danger perfil-alert">
                    La contraseña actual no es correcta.
                </div>
            </c:if>

            <c:if test="${param.claveError == 'guardar'}">
                <div class="alert alert-danger perfil-alert">
                    No se pudo actualizar la contraseña. Intenta nuevamente.
                </div>
            </c:if>

            <section class="perfil-card">

                <div class="perfil-header">

                    <div class="perfil-identity">
                        <h2><%= p.getNombre() %> <%= p.getApellidos() %></h2>
                        <p>Estado:
                            <c:choose>
                                <c:when test="${estadoPersonal.descripcion_estado == 'ACTIVO'}">
                                    <span class="badge-estado badge-activo">ACTIVO</span>
                                </c:when>
                                <c:when test="${estadoPersonal.descripcion_estado == 'RETIRADO'}">
                                    <span class="badge-estado badge-retirado">RETIRADO</span>
                                </c:when>
                                <c:when test="${estadoPersonal.descripcion_estado == 'VACACIONES'}">
                                    <span class="badge-estado badge-vacaciones">VACACIONES</span>
                                </c:when>
                                <c:when test="${not empty estadoPersonal}">
                                    <span class="badge-estado badge-vacaciones"><c:out value="${estadoPersonal.descripcion_estado}" /></span>
                                </c:when>
                                <c:otherwise><span class="badge-estado badge-vacaciones">NO DISPONIBLE</span></c:otherwise>
                            </c:choose>
                        </p>
                    </div>

                    <% if (esAdminLogueado) { %>
                    <a href="${ctx}/EditarPersonal?id=<%= p.getIdPersonal() %>" class="btn-editar">
                        Editar
                    </a>
                    <% } %>

                </div>

                <div class="tabs-wrap">
                    <button class="tab-btn active" type="button" data-tab="general">
                        Información General
                    </button>
                </div>

                <div class="tab-panel active" id="tab-general">

                    <div class="info-grid">

                        <div class="info-field">
                            <span class="field-lbl">Identificación</span>
                            <span class="field-val"><%= p.getIdentificacion() %></span>
                        </div>

                        <div class="info-field">
                            <span class="field-lbl">Correo Electrónico</span>
                            <span class="field-val"><%= p.getEmail() %></span>
                        </div>

                        <div class="info-field">
                            <span class="field-lbl">Teléfono</span>
                            <span class="field-val"><%= p.getTelefono() %></span>
                        </div>

                        <div class="info-field">
                            <span class="field-lbl">Dirección</span>
                            <span class="field-val"><%= p.getDireccion() %></span>
                        </div>

                        <div class="info-field">
                            <span class="field-lbl">Fecha de Contratación</span>
                            <span class="field-val"><%= p.getFecha_contratacion() %></span>
                        </div>

                        <div class="info-field">
                            <span class="field-lbl">Puede acceder</span>
                            <span class="field-val">
                                <%= p.isPuede_acceder() ? "Sí" : "No" %>
                            </span>
                        </div>

                        <div class="info-field info-field-full">
                            <span class="field-lbl">Observaciones</span>
                            <span class="field-val"><%= p.getObservaciones() %></span>
                        </div>

                    </div>

                </div>

            </section>

            <section class="perfil-card">

                <div class="tabs-wrap">
                    <button class="tab-btn active" type="button" data-tab="activos">
                        Activos Asignados
                    </button>
                </div>

                <div class="tab-panel active" id="tab-activos">

                    <c:choose>
                        <c:when test="${not empty activosAsignados}">
                            <div class="activos-asignados-grid">
                                <c:forEach var="asig" items="${activosAsignados}">
                                    <div class="activo-asignado-card">
                                        <div class="activo-asignado-nombre"><c:out value="${asig.nombreActivo}" /></div>
                                        <div class="info-field">
                                            <span class="field-lbl">Código</span>
                                            <span class="field-val"><c:out value="${asig.codigoActivo}" /></span>
                                        </div>
                                        <div class="info-field">
                                            <span class="field-lbl">Fecha de Asignación</span>
                                            <span class="field-val"><c:out value="${asig.fechaAsignacion}" /></span>
                                        </div>
                                        <c:if test="${not empty asig.observaciones}">
                                            <div class="info-field info-field-full">
                                                <span class="field-lbl">Observaciones</span>
                                                <span class="field-val"><c:out value="${asig.observaciones}" /></span>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="activos-asignados-vacio">Actualmente no tiene activos asignados.</p>
                        </c:otherwise>
                    </c:choose>

                </div>

            </section>

            <% if (esPerfilPropio) { %>
            <% if (p.isDebeCambiarClave()) { %>
            <div class="cambio-clave-obligatorio" role="alert">
                <div>
                    <strong>Cambio de contraseña obligatorio</strong>
                    <p>Estás usando una contraseña temporal. Debes crear una nueva para proteger tu cuenta.</p>
                </div>
                <a href="#cambiar-clave">Cambiar ahora</a>
            </div>
            <% } %>
            <section class="perfil-card password-card" id="cambiar-clave">
                <div class="password-head">
                    <div>
                        <h2>Cambiar contraseña</h2>
                        <p>Actualiza tu clave de acceso usando tu contraseña actual.</p>
                    </div>
                    <i class="bi bi-shield-lock"></i>
                </div>

                <form class="password-form" action="${ctx}/CambiarContrasena" method="post">
                    <div class="password-grid">
                        <div class="password-field">
                            <label for="claveActual">Contraseña actual</label>
                            <input id="claveActual" name="claveActual" type="password" required>
                        </div>

                        <div class="password-field">
                            <label for="nuevaClave">Nueva contraseña</label>
                            <input id="nuevaClave" name="nuevaClave" type="password" minlength="8" required>
                        </div>

                        <div class="password-field">
                            <label for="confirmarClave">Confirmar nueva contraseña</label>
                            <input id="confirmarClave" name="confirmarClave" type="password" minlength="8" required>
                        </div>
                    </div>

                    <button class="btn-cambiar-clave" type="submit">
                        <i class="bi bi-key-fill"></i>
                        Guardar nueva contraseña
                    </button>
                </form>
            </section>
            <% } %>

        </main>

    </div>

    <div class="sidebar-overlay" id="overlay"></div>

    <button class="menu-toggle d-lg-none" id="menuToggle" type="button">
        <i class="bi bi-list"></i>
    </button>

    <script src="${ctx}/JavaScript/bootstrap.bundle.min.js"></script>
    <script src="${ctx}/JavaScript/perfil_usuario.js"></script>
    <script src="${ctx}/JavaScript/Panel.js"></script>
    <script src="${ctx}/JavaScript/validacion_formularios.js?v=2"></script>

</body>
</html>
