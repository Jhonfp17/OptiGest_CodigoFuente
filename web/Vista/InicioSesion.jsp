<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - OptiGest</title>

    <link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/Css/bootstrap-icons.css">

    <link rel="stylesheet" href="${ctx}/Css/InicioSesion.css">
    <link rel="stylesheet" href="${ctx}/Css/OptiGestUI.css">
    <link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
</head>

<body class="login-page">

    <main class="login-wrapper">

        <section class="login-card">

            <a href="${ctx}/index.jsp" class="login-back-button">
                <i class="bi bi-arrow-left" aria-hidden="true"></i>
                Regresar
            </a>

            <div class="login-header">
                <div class="login-icon">
                    <img src="${ctx}/Imagenes/OptiGestLogo.jpg" alt="OptiGest">
                </div>

                <h3>Accede a tu cuenta</h3>

                <p>
                    Acceso exclusivo para personal autorizado.
                </p>
            </div>

            <c:if test="${not empty mensaje}">
                <div class="login-alert">
                    ${mensaje}
                </div>
            </c:if>

            <form action="${ctx}/Iniciar" method="post" class="login-form">

                <div class="form-group">
                    <input type="text"
                           class="form-control"
                           name="Usuario"
                            placeholder="NÚMERO DE DOCUMENTO"
                           inputmode="numeric" minlength="6" maxlength="10" pattern="\d{6,10}"
                           required>
                </div>

                <div class="form-group">
                    <input type="password"
                           class="form-control"
                           name="pass"
                            placeholder="CONTRASEÑA"
                           required>
                </div>

             

                <button type="submit" class="login-button">
                    Iniciar sesión
                </button>

            </form>

            <div class="login-footer">
                <a href="${ctx}/Contacto" class="login-link">
                    Contáctanos
                </a>
            </div>

        </section>

</main>

<script src="${ctx}/JavaScript/validacion_formularios.js?v=2"></script>

</body>
</html>
