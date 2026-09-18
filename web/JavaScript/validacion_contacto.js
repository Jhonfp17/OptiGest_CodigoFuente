/* Reglas específicas del formulario público de contacto. */
document.addEventListener('DOMContentLoaded', function () {
    const formulario = document.querySelector('form.contacto-form');
    if (!formulario) return;

    const tipoSolicitud = formulario.querySelector('[name="tipoSolicitud"]');
    const usuarioDeseado = formulario.querySelector('[name="usuarioDeseado"]');
    if (!tipoSolicitud || !usuarioDeseado) return;

    function actualizarUsuarioDeseado() {
        const crearCuenta = tipoSolicitud.value === 'Crear cuenta';
        usuarioDeseado.required = crearCuenta;
        usuarioDeseado.setCustomValidity('');
        usuarioDeseado.placeholder = crearCuenta
                ? 'Entre 3 y 45 caracteres: letras, números, punto, guion o guion bajo'
                : 'Opcional: letras, números, punto, guion o guion bajo';
    }

    tipoSolicitud.addEventListener('change', actualizarUsuarioDeseado);
    usuarioDeseado.addEventListener('input', function () {
        this.value = this.value.replace(/[^\p{L}0-9._-]/gu, '');
        this.setCustomValidity(this.value && !/^[\p{L}0-9._-]{3,45}$/u.test(this.value)
                ? 'Usa entre 3 y 45 caracteres válidos.' : '');
    });
    actualizarUsuarioDeseado();
});
