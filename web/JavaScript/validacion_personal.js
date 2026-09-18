document.addEventListener('DOMContentLoaded', function () {
    const formulario = document.querySelector('form[data-validacion-personal]');
    if (!formulario) return;

    const tipoDocumento = formulario.querySelector('[name="Documento_id_documento"]');
    const identificacion = formulario.querySelector('[name="identificacion"]');
    const telefono = formulario.querySelector('[name="telefono"]');
    const fechaContratacion = formulario.querySelector('[name="fecha_contratacion"]');
    const patronNombre = /^[\p{L} ]{2,45}$/u;
    const patronDireccion = /^[\p{L}0-9#.,/\- ]{5,45}$/u;

    function reglaDocumento() {
        const opcion = tipoDocumento.options[tipoDocumento.selectedIndex];
        const tipo = opcion ? (opcion.dataset.documento || '').normalize('NFD')
                .replace(/[\u0300-\u036f]/g, '').toLowerCase() : '';
        if (tipo.includes('tarjeta') || /(^|[^a-z])ti([^a-z]|$)/.test(tipo)) return { patron: '\\d{10,11}', minimo: 10, maximo: 11, texto: 'La tarjeta de identidad debe tener 10 u 11 dígitos.' };
        if (tipo.includes('extranjer') || /(^|[^a-z])ce([^a-z]|$)/.test(tipo)) return { patron: '\\d{6,10}', minimo: 6, maximo: 10, texto: 'La cédula de extranjería debe tener entre 6 y 10 dígitos.' };
        if (tipo.includes('nit')) return { patron: '\\d{9,10}', minimo: 9, maximo: 10, texto: 'El NIT debe tener 9 o 10 dígitos.' };
        if (tipo.includes('cedula') || /(^|[^a-z])cc([^a-z]|$)/.test(tipo)) return { patron: '\\d{6,10}', minimo: 6, maximo: 10, texto: 'La cédula debe tener entre 6 y 10 dígitos.' };
        return { patron: '\\d{5,20}', minimo: 5, maximo: 20, texto: 'El documento debe contener solo números y tener entre 5 y 20 dígitos.' };
    }

    function actualizarDocumento() {
        const regla = reglaDocumento();
        identificacion.pattern = regla.patron;
        identificacion.minLength = regla.minimo;
        identificacion.maxLength = regla.maximo;
        identificacion.inputMode = 'numeric';
        identificacion.title = regla.texto;
        identificacion.placeholder = regla.texto;
        identificacion.setCustomValidity('');
    }

    tipoDocumento.addEventListener('change', actualizarDocumento);
    identificacion.addEventListener('input', function () {
        const regla = reglaDocumento();
        this.value = this.value.replace(/\D/g, '');
        this.setCustomValidity(this.value && !(new RegExp('^(?:' + regla.patron + ')$')).test(this.value)
                ? regla.texto : '');
    });
    if (telefono) {
        telefono.inputMode = 'numeric';
        telefono.maxLength = 15;
        telefono.pattern = '\\d{7,15}';
        telefono.title = 'Ingresa solo números: entre 7 y 15 dígitos.';
        telefono.addEventListener('input', function () {
            this.value = this.value.replace(/\D/g, '').slice(0, 15);
            this.setCustomValidity(this.value && !/^\d{7,15}$/.test(this.value)
                    ? 'El teléfono debe tener entre 7 y 15 dígitos.' : '');
        });
    }
    formulario.querySelectorAll('[name="nombre"], [name="apellidos"]').forEach(function (campo) {
        campo.addEventListener('input', function () {
            this.value = this.value.replace(/[^\p{L} ]/gu, '');
            this.setCustomValidity(this.value && !patronNombre.test(this.value)
                    ? 'Usa solo letras y espacios; mínimo 2 caracteres.' : '');
        });
    });
    const direccion = formulario.querySelector('[name="direccion"]');
    if (direccion) {
        direccion.addEventListener('input', function () {
            this.value = this.value.replace(/[^\p{L}0-9#.,/\- ]/gu, '');
            this.setCustomValidity(this.value && !patronDireccion.test(this.value)
                    ? 'La dirección debe tener entre 5 y 45 caracteres válidos.' : '');
        });
    }
    formulario.querySelectorAll('[name="email"]').forEach(function (campo) {
        campo.addEventListener('input', function () {
            this.value = this.value.replace(/\s/g, '').toLowerCase();
        });
    });
    if (fechaContratacion) {
        fechaContratacion.max = new Date().toISOString().slice(0, 10);
    }
    actualizarDocumento();
});
