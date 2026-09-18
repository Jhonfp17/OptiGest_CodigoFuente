<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>OptiGest</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
        <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">

        <link rel="stylesheet" href="${ctx}/Css/Index.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/ResponsiveBase.css?v=4">
    </head>

    <body class="index-page">

        <header class="index-header">

            <a href="${ctx}/Vista/InicioSesion.jsp" class="index-login-btn">
                Iniciar sesión
            </a>
        </header>

        <main>

            <section class="index-hero">
                <div class="container text-center">
                    <h1 class="index-title">
                        Optimiza tu PYME con <span>OptiGest</span>
                    </h1>

                    <p class="index-subtitle">
                        Sistema privado de gestión administrativa y control de activos para tu empresa.
                    </p>
                </div>
            </section>

            <section class="index-features">
                <div class="container text-center">

                    <h4 class="section-title">
                        Características
                    </h4>

                    <div class="row g-4">

                        <div class="col-md-3">
                            <div class="feature-card">
                                <i class="bi bi-diagram-3"></i>
                                <h6>Gestión Centralizada</h6>
                                <p>Controla todos tus activos desde un solo lugar.</p>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="feature-card">
                                <i class="bi bi-bar-chart"></i>
                                <h6>Control en Tiempo Real</h6>
                                <p>Consulta el estado de tus activos en cualquier momento.</p>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="feature-card">
                                <i class="bi bi-shield-check"></i>
                                <h6>Acceso Seguro</h6>
                                <p>Solo el personal autorizado puede acceder al sistema.</p>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="feature-card">
                                <i class="bi bi-stars"></i>
                                <h6>Interfaz Intuitiva</h6>
                                <p>Fácil de usar para todo tu equipo.</p>
                            </div>
                        </div>

                    </div>
                </div>
            </section>

            <section class="index-benefits">
                <div class="container">
                    <div class="benefits-box">
                        <h5>Beneficios</h5>

                        <ul>
                            <li>
                                <i class="bi bi-check-circle"></i>
                                Registro y seguimiento de activos de la empresa.
                            </li>
                            <li>
                                <i class="bi bi-check-circle"></i>
                                Control de asignaciones y devoluciones.
                            </li>
                            <li>
                                <i class="bi bi-check-circle"></i>
                                Historial de mantenimientos por activo.
                            </li>
                            <li>
                                <i class="bi bi-check-circle"></i>
                                Acceso desde cualquier dispositivo.
                            </li>
                        </ul>
                    </div>
                </div>
            </section>

            <section class="index-cta">
                <div class="container text-center">
                    <h4>¿Listo para gestionar tu empresa?</h4>
                    <p>Inicia sesión y toma el control de tus activos.</p>

                    <a href="${ctx}/Vista/InicioSesion.jsp" class="index-cta-btn">
                        Iniciar sesión
                    </a>
                </div>
            </section>

        </main>

        <footer class="index-footer">
            &copy; 2026 OptiGest PYME
        </footer>

    </body>
</html>
