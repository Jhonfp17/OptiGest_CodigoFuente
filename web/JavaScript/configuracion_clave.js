document.addEventListener('DOMContentLoaded', function () {
    const selector = document.getElementById('claveProvisional');
    const clave = document.getElementById('clave');
    if (!selector || !clave) return;

    selector.name = 'modo_clave';
    selector.replaceChildren(
        new Option('Ingresar contraseña', 'manual'),
        new Option('Generar contraseña segura', 'generada')
    );

    const campoModo = selector.closest('.form-field');
    const etiquetaModo = campoModo ? campoModo.querySelector('label') : null;
    const campoClave = clave.closest('.form-field');
    const etiquetaClave = campoClave ? campoClave.querySelector('label') : null;
    if (etiquetaModo) etiquetaModo.textContent = 'Configuración de contraseña';
    if (etiquetaClave) etiquetaClave.textContent = 'Contraseña inicial';

    const ayuda = document.createElement('small');
    ayuda.id = 'ayudaClave';
    campoModo.appendChild(ayuda);

    function actualizar() {
        const generada = selector.value === 'generada';
        clave.required = !generada;
        clave.disabled = generada;
        clave.minLength = generada ? 0 : 8;
        if (generada) clave.value = '';
        clave.placeholder = generada
                ? 'La contraseña se generará al registrar'
                : 'Ingresa una contraseña de mínimo 8 caracteres';
        ayuda.textContent = generada
                ? 'El sistema generará una contraseña segura. Se mostrará una sola vez después de registrar para entregársela al empleado.'
                : 'Define la contraseña inicial que recibirá el empleado.';
    }

    selector.addEventListener('change', actualizar);
    const formulario = selector.closest('form');
    if (formulario) {
        formulario.addEventListener('submit', function () {
            if (selector.value === 'manual' && !clave.value.trim()) {
                clave.setCustomValidity('Ingresa una contraseña inicial o selecciona “Generar contraseña segura”.');
            } else {
                clave.setCustomValidity('');
            }
        });
        clave.addEventListener('input', function () { clave.setCustomValidity(''); });
    }
    actualizar();
});
