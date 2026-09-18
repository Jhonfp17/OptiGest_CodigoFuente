<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Configuración - OptiGest</title>

        <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">

        <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">

        <link rel="stylesheet" href="${ctx}/Css/Configuracion.css">
        <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">

        <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
    </head>

    <body class="config-page">

        <div class="container py-5">

            <div class="mb-4">
                <a href="${ctx}/PanelControl" class="btn btn-outline-primary">
                    <i class="bi bi-arrow-left"></i>
                    Volver al Panel Administrador
                </a>
            </div>

            <div class="text-center mb-5">
                <h1>Configuración del Sistema</h1>
                <p class="text-muted">
                    Administre las tablas maestras de OptiGest
                </p>
            </div>

            <h3 class="section-title">
                <i class="bi bi-people-fill"></i>
                Personal
            </h3>

            <div class="container">
                <div class="row g-4">
                    <div class="col-md-3">
                        <a href="${ctx}/GestionCatalogo?tipo=personal" class="config-card">
                            <i class="bi bi-person-lines-fill"></i>
                            <h5>Personal</h5>
                        </a>
                    </div>

                    <div class="col-md-3">
                        <a href="${ctx}/GestionCatalogo?tipo=roles" class="config-card">
                            <i class="bi bi-person-gear"></i>
                            <h5>Roles</h5>
                        </a>
                    </div>

                    <div class="col-md-3">
                        <a href="${ctx}/GestionCatalogo?tipo=documentos" class="config-card">
                            <i class="bi bi-file-earmark-text"></i>
                            <h5>Documentos</h5>
                        </a>
                    </div>

                    <div class="col-md-3">
                        <a href="${ctx}/GestionCatalogo?tipo=estadoPersonal" class="config-card">
                            <i class="bi bi-person-check"></i>
                            <h5>Estado Personal</h5>
                        </a>
                    </div>
                </div>

                <h3 class="section-title">
                    <i class="bi bi-box-seam"></i>
                    Activos
                </h3>

                <div class="container py-2">
                    <div class="row g-4">

                        <div class="col-md-3">
                            <a href="${ctx}/GestionCatalogo?tipo=activos" class="config-card">
                                <i class="bi bi-pc-display"></i>
                                <h5>Activos</h5>
                            </a>
                        </div>

                        <div class="col-md-3">
                            <a href="${ctx}/GestionCatalogo?tipo=categorias" class="config-card">
                                <i class="bi bi-tags"></i>
                                <h5>Categorías</h5>
                            </a>
                        </div>

                        <div class="col-md-3">
                            <a href="${ctx}/GestionCatalogo?tipo=estadoActivo" class="config-card">
                                <i class="bi bi-check2-circle"></i>
                                <h5>Estado Activo</h5>
                            </a>
                        </div>

                        <div class="col-md-3">
                            <a href="${ctx}/GestionCatalogo?tipo=proveedores" class="config-card">
                                <i class="bi bi-truck"></i>
                                <h5>Proveedores</h5>
                            </a>
                        </div>
                    </div>

                </div>

                <h3 class="section-title">
                    <i class="bi bi-gear-fill"></i>
                    Operación
                </h3>

                <div class="container text-center">
                    <div class="row g-4">

                        <div class="col-sm-4">
                            <a href="${ctx}/GestionCatalogo?tipo=asignaciones" class="config-card">
                                <i class="bi bi-arrow-left-right"></i>
                                <h5>Asignaciones</h5>
                            </a>
                        </div>

                        <div class="col-sm-4">
                            <a href="${ctx}/GestionCatalogo?tipo=programacionPersonal" class="config-card">
                                <i class="bi bi-calendar2-check"></i>
                                <h5>Programacion Personal</h5>
                            </a>
                        </div>

                        <div class="col-sm-4">
                            <a href="${ctx}/GestionCatalogo?tipo=horarios" class="config-card">
                                <i class="bi bi-clock"></i>
                                <h5>Horarios</h5>
                            </a>

                        </div>
                    </div>

                    <div class="row g-4">

                        <div class="col-sm-4">
                            <a href="${ctx}/GestionCatalogo?tipo=dias" class="config-card">
                                <i class="bi bi-calendar-week"></i>
                                <h5>Días</h5>
                            </a>
                        </div>

                        <div class="col-sm-4">
                            <a href="${ctx}/GestionCatalogo?tipo=mantenimiento" class="config-card">
                                <i class="bi bi-tools"></i>
                                <h5>Mantenimiento</h5>
                            </a>
                        </div>

                        <div class="col-sm-4">
                            <a href="${ctx}/GestionContacto" class="config-card">
                                <i class="bi bi-envelope-paper"></i>
                                <h5>Mensajes de contacto</h5>
                            </a>
                        </div>
                    </div>

                </div>

                <div class="text-center mt-5">

                    <a href="${ctx}/PanelControl"
                       class="btn btn-primary">

                        Volver al Panel

                    </a>

                </div>

            </div>

    </body>
</html>
