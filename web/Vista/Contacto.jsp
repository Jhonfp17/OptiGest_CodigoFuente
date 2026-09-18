<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Contacto - OptiGest</title>
<link rel="stylesheet" href="${ctx}/Css/bootstrap.min.css">
<link rel="stylesheet" href="${ctx}/Css/Contacto.css?v=2">
<link rel="stylesheet" href="${ctx}/Css/ResponsiveBase.css?v=4">
</head>
<body class="contacto-page">
<main class="container py-5 contacto-wrap">
<section class="card contacto-card">
<header class="contacto-head">
<a href="${ctx}/Vista/InicioSesion.jsp" class="contacto-back">← Volver al inicio</a>
<h1 class="h2 contacto-title">Solicita ayuda o una cuenta</h1>
<p class="contacto-copy">Completa la información para que el administrador revise tu solicitud y se comunique contigo.</p>
</header>
<c:if test="${param.enviado == '1'}">
<div class="alert alert-success m-4 mb-0">Solicitud enviada correctamente. Te contactaremos cuando sea revisada.</div>
</c:if>
<c:if test="${not empty param.error}">
<div class="alert alert-danger m-4 mb-0">Completa todos los campos obligatorios y acepta el tratamiento de datos.</div>
</c:if>
<form action="${ctx}/Contacto" method="post" class="row g-3 contacto-form">
<div class="col-md-6">
<label class="form-label">Nombre</label>
<input class="form-control" name="nombre" minlength="2" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+" required>
</div>
<div class="col-md-6">
<label class="form-label">Apellidos</label>
<input class="form-control" name="apellidos" minlength="2" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+" required>
</div>
<div class="col-md-6">
<label class="form-label">Tipo de documento</label>
<select class="form-select" name="tipoDocumentoId" required>
<option value="">Seleccione el tipo de documento</option>
<c:forEach var="documento" items="${documentos}">
<option value="${documento.id_documento}" data-documento="${documento.descripcion_doc}">${documento.descripcion_doc}</option>
</c:forEach>
</select>
</div>
<div class="col-md-6">
<label class="form-label">Número de documento</label>
<input class="form-control" name="documento" inputmode="numeric" required>
</div>
<div class="col-md-6">
<label class="form-label">Teléfono</label>
<input class="form-control" name="telefono" inputmode="numeric" minlength="7" maxlength="15" pattern="\d{7,15}" required>
</div>
<div class="col-12">
<label class="form-label">Dirección</label>
<input class="form-control" name="direccion" minlength="5" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ0-9#.,/ -]+" autocomplete="street-address" required>
</div>
<div class="col-12">
<label class="form-label">Correo electrónico</label>
<input class="form-control" name="email" type="email" minlength="6" maxlength="45" required>
</div>
<div class="col-md-6">
<label class="form-label">Tipo de solicitud</label>
<select class="form-select" name="tipoSolicitud" required>
<option value="">Seleccione una opción</option>
<option>Crear cuenta</option>
<option>Recuperar acceso</option>
<option>Actualizar datos</option>
<option>Otro</option>
</select>
</div>
<div class="col-md-6">
<label class="form-label">Nombre deseado para la cuenta</label>
<input class="form-control" name="usuarioDeseado" minlength="3" maxlength="45" pattern="[A-Za-zÁÉÍÓÚÜÑáéíóúüñ0-9._-]{3,45}" placeholder="Escribe el nombre que deseas usar">
</div>
<div class="col-12">
<label class="form-label">Asunto</label>
<input class="form-control" name="asunto" minlength="3" maxlength="100" required>
</div>
<div class="col-12">
<label class="form-label">Cuéntanos cómo podemos ayudarte</label>
<textarea class="form-control" name="mensaje" rows="4" maxlength="200" required>
</textarea>
</div>
<div class="col-12">
<div class="form-check contacto-privacy">
<input class="form-check-input" type="checkbox" name="aceptaDatos" id="aceptaDatos" required>
<label class="form-check-label" for="aceptaDatos">Autorizo el tratamiento de estos datos únicamente para atender mi solicitud.</label>
</div>
</div>
<div class="col-12 pt-2">
<button class="btn btn-primary contacto-submit" type="submit">Enviar solicitud</button>
</div>
</form>
</section>
</main>
<script src="${ctx}/JavaScript/validacion_formularios.js?v=2"></script>
<script src="${ctx}/JavaScript/validacion_contacto.js?v=1"></script>
</body>
</html>
