/* Reglas visuales compartidas para todos los formularios de OptiGest. */
document.addEventListener('DOMContentLoaded', function () {
    const hoy = new Date().toISOString().slice(0, 10);
    const mayusculas = function (texto) { return texto.toLocaleUpperCase('es-CO'); };
    document.querySelectorAll('form').forEach(function (formulario) {
        const esFormularioContacto = formulario.classList.contains('contacto-form');
        const tipoBusqueda = formulario.querySelector('[name="tipoBusqueda"]');
        const valorBusqueda = formulario.querySelector('[name="valorBusqueda"]');
        if (tipoBusqueda && valorBusqueda) {
            // Conserva el dato consultado al volver desde el historial.
            // Así el resultado y el filtro visible siempre corresponden.
            const buscadoEnUrl = new URLSearchParams(window.location.search).get('valorBusqueda');
            if (!valorBusqueda.value && buscadoEnUrl) {
                valorBusqueda.value = buscadoEnUrl;
            }
            const actualizarBusqueda = function () {
                const documento = tipoBusqueda.value === 'documento';
                valorBusqueda.inputMode = documento ? 'numeric' : 'text';
                // Buscar no equivale a registrar: el nombre de un activo puede ser largo.
                valorBusqueda.maxLength = documento ? 10 : 100;
                if (documento) {
                    valorBusqueda.pattern = '\\d{6,10}';
                } else {
                    valorBusqueda.removeAttribute('pattern');
                }
                valorBusqueda.placeholder = documento ? 'Número de documento' : 'Nombre o código del activo';
                valorBusqueda.setCustomValidity('');
            };
            tipoBusqueda.addEventListener('change', actualizarBusqueda);
            valorBusqueda.addEventListener('input', function () { if (tipoBusqueda.value === 'documento') this.value=this.value.replace(/\D/g, '').slice(0,10); });
            actualizarBusqueda();
        }
        formulario.querySelectorAll('[name="nombre"], [name="apellidos"], [name="nombre_activos"], [name="nombreActivo"], [name="direccion"], [name="asunto"]').forEach(function (campo) {
            /* Cada vista define sus propios límites. Solo se establece un valor seguro
               si el campo todavía no tiene una regla declarada. */
            if (campo.name === 'nombre' || campo.name === 'apellidos') {
                if (!campo.hasAttribute('minlength')) campo.minLength = 2;
                if (!campo.hasAttribute('maxlength')) campo.maxLength = 45;
            } else if (campo.name === 'direccion') {
                if (!campo.hasAttribute('minlength')) campo.minLength = 5;
                if (!campo.hasAttribute('maxlength')) campo.maxLength = 45;
            } else if (campo.name === 'asunto') {
                if (!campo.hasAttribute('maxlength')) campo.maxLength = 100;
            } else {
                if (!campo.hasAttribute('minlength')) campo.minLength = 3;
                if (!campo.hasAttribute('maxlength')) campo.maxLength = 30;
            }
            if (!esFormularioContacto) {
                campo.addEventListener('input', function () { this.value = mayusculas(this.value); });
            }
        });
        formulario.querySelectorAll('[name="email"]').forEach(function (campo) {
            campo.type = 'email';
            if (!campo.hasAttribute('minlength')) campo.minLength = 6;
            if (!campo.hasAttribute('maxlength')) campo.maxLength = 45;
            campo.addEventListener('input', function () { this.value = this.value.replace(/\s/g, ''); });
        });
        formulario.querySelectorAll('[name="telefono"], [name="telefonoVerificacion"]').forEach(function (campo) {
            campo.inputMode = 'numeric';
            if (!campo.hasAttribute('maxlength')) campo.maxLength = 15;
            if (!campo.hasAttribute('pattern')) campo.pattern = '\\d{7,15}';
            campo.addEventListener('input', function () { this.value = this.value.replace(/\D/g, '').slice(0, 15); });
        });
        const selectorDocumento = formulario.querySelector('[name="Documento_id_documento"], [name="documentoId"], [name="tipoDocumentoId"]');
        const identificacion = formulario.querySelector('[name="identificacion"], [name="documento"]');
        formulario.querySelectorAll('[name="Usuario"]').forEach(function (campo) {
            campo.inputMode = 'numeric'; campo.maxLength = 10; campo.pattern = '\\d{6,10}';
            campo.addEventListener('input', function () { this.value = this.value.replace(/\D/g, '').slice(0, 10); });
        });
        if (!selectorDocumento) formulario.querySelectorAll('[name="documento"]').forEach(function (campo) {
            campo.inputMode = 'numeric'; campo.maxLength = 10; campo.pattern = '\\d{6,10}';
            campo.addEventListener('input', function () { this.value = this.value.replace(/\D/g, '').slice(0, 10); });
        });
        if (selectorDocumento && identificacion) {
            const obtenerReglaDocumento = function () {
                const opcion = selectorDocumento.options[selectorDocumento.selectedIndex];
                const tipo = (opcion ? (opcion.dataset.documento || opcion.textContent || '') : '').normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase().trim();
                if (!selectorDocumento.value) return { patron: '\\d{5,20}', minimo: 5, maximo: 20, texto: 'SELECCIONE PRIMERO EL TIPO DE DOCUMENTO' };
                if (tipo.includes('tarjeta') || /(^|[^a-z])ti([^a-z]|$)/.test(tipo)) return { patron: '\\d{10,11}', minimo: 10, maximo: 11, texto: 'TARJETA DE IDENTIDAD: 10 U 11 DIGITOS' };
                if (tipo.includes('extranjer') || /(^|[^a-z])ce([^a-z]|$)/.test(tipo)) return { patron: '\\d{6,10}', minimo: 6, maximo: 10, texto: 'CEDULA DE EXTRANJERIA: 6 A 10 DIGITOS' };
                if (tipo.includes('nit')) return { patron: '\\d{9,10}', minimo: 9, maximo: 10, texto: 'NIT: 9 O 10 DIGITOS' };
                if (tipo.includes('cedula') || /(^|[^a-z])cc([^a-z]|$)/.test(tipo)) return { patron: '\\d{6,10}', minimo: 6, maximo: 10, texto: 'CEDULA: 6 A 10 DIGITOS' };
                return { patron: '\\d{5,20}', minimo: 5, maximo: 20, texto: 'DOCUMENTO: 5 A 20 DIGITOS' };
            };
            const actualizarReglaDocumento = function () {
                const regla = obtenerReglaDocumento();
                identificacion.inputMode = 'numeric'; identificacion.pattern = regla.patron;
                identificacion.minLength = regla.minimo; identificacion.maxLength = regla.maximo;
                identificacion.title = regla.texto;
                if (!identificacion.value) identificacion.placeholder = regla.texto;
                identificacion.setCustomValidity('');
            };
            selectorDocumento.addEventListener('change', actualizarReglaDocumento);
            identificacion.addEventListener('input', function () {
                const regla = obtenerReglaDocumento();
                this.value = this.value.replace(/\D/g, '').slice(0, regla.maximo);
                this.setCustomValidity(this.value && !new RegExp('^(?:' + regla.patron + ')$').test(this.value) ? regla.texto : '');
            });
            actualizarReglaDocumento();
        }
        formulario.querySelectorAll('[name="codigo_act"], [name="codigoAct"]').forEach(function (campo) {
            if (!campo.hasAttribute('maxlength')) campo.maxLength = 30;
            if (!campo.hasAttribute('pattern')) campo.pattern = '[A-Za-z0-9-]{3,30}';
            campo.addEventListener('input', function () { this.value = this.value.toUpperCase().replace(/[^A-Z0-9-]/g, ''); });
        });
        formulario.querySelectorAll('[name="valor"], [name="costo"]').forEach(function (campo) {
            campo.inputMode = 'decimal'; campo.pattern = '\\d+(\\.\\d{1,2})?';
            campo.addEventListener('input', function () { this.value = this.value.replace(/[^0-9.]/g, ''); });
        });
        formulario.querySelectorAll('[name="vida_util"], [name="vidaUtil"]').forEach(function (campo) { campo.min = 1; campo.max = 20; });
        formulario.querySelectorAll('[name="fecha_contratacion"], [name="fechaContratacion"], [name="fecha_adquma"], [name="fechaAdquma"], [name="fechaMante"], [name="fechaAsignacion"]').forEach(function (campo) { campo.max = hoy; });
        formulario.querySelectorAll('textarea').forEach(function (campo) {
            campo.maxLength = Math.min(campo.maxLength || 200, 200);
            if (!esFormularioContacto && campo.name !== 'clave' && campo.name !== 'claveActual' && campo.name !== 'nuevaClave' && campo.name !== 'confirmarClave') {
                campo.addEventListener('input', function () { this.value = mayusculas(this.value); });
            }
        });
        formulario.addEventListener('submit', function (evento) {
            const adquisicion = formulario.querySelector('[name="fecha_adquma"], [name="fechaAdquma"]');
            const devolucion = formulario.querySelector('[name="fecha_devolucion"], [name="fechaDevolucion"]');
            if (adquisicion && devolucion && devolucion.value && devolucion.value < adquisicion.value) {
                devolucion.setCustomValidity('La fecha de devolución no puede ser anterior a la fecha de adquisición.');
                evento.preventDefault(); devolucion.reportValidity();
            } else if (devolucion) devolucion.setCustomValidity('');
            const desde = formulario.querySelector('[name="fechaDesde"]');
            const hasta = formulario.querySelector('[name="fechaHasta"]');
            if (desde && hasta && hasta.value && hasta.value < desde.value) {
                hasta.setCustomValidity('La fecha hasta no puede ser anterior a la fecha desde.');
                evento.preventDefault(); hasta.reportValidity();
            } else if (hasta) hasta.setCustomValidity('');
        });
    });
});

/* Ayudas y mensajes junto a cada control para que el usuario sepa qué escribir. */
document.addEventListener('DOMContentLoaded', function () {
    const ayudas = {
        nombre: 'Solo letras y espacios, entre 10 y 15 caracteres. Ejemplo: Carlos Alberto.',
        apellidos: 'Solo letras y espacios, entre 10 y 15 caracteres. Ejemplo: Rodriguez Perez.',
        email: 'Correo electrónico válido, entre 15 y 20 caracteres. Ejemplo: nombre@correo.com.',
        telefono: 'Solo números, entre 7 y 15 dígitos. Ejemplo: 3001234567.',
        telefonoVerificacion: 'Solo números, entre 7 y 15 dígitos. Ejemplo: 3001234567.',
        direccion: 'Letras, números y signos #, -, / o punto. Máximo 40 caracteres. Ejemplo: Calle 10 # 20-30.',
        identificacion: 'Seleccione el tipo de documento para conocer la cantidad de dígitos requerida.',
        documento: 'Solo números, entre 6 y 10 dígitos. Ejemplo: 1234567890.',
        Usuario: 'Ingrese su número de documento: solo números, entre 6 y 10 dígitos.',
        clave: 'Entre 8 y 20 caracteres. No comparta esta contraseña.',
        nuevaClave: 'Entre 8 y 20 caracteres. Use una contraseña segura que pueda recordar.',
        confirmarClave: 'Escriba exactamente la misma contraseña anterior.',
        claveActual: 'Escriba su contraseña actual.',
        codigo_act: 'De 3 a 20 caracteres: letras, números y guion. Ejemplo: ACT-001.',
        codigoAct: 'De 3 a 20 caracteres: letras, números y guion. Ejemplo: ACT-001.',
        nombre_activos: 'Nombre del activo: máximo 30 caracteres. Ejemplo: Monitor Dell 24.',
        nombreActivo: 'Nombre del activo: máximo 30 caracteres. Ejemplo: Monitor Dell 24.',
        valor: 'Ingrese un valor numérico mayor que cero. Ejemplo: 1250000.',
        costo: 'Ingrese un valor numérico mayor que cero. Ejemplo: 1250000.',
        vida_util: 'Ingrese años completos entre 1 y 20. Ejemplo: 5.',
        vidaUtil: 'Ingrese años completos entre 1 y 20. Ejemplo: 5.',
        asunto: 'Máximo 100 caracteres. Ejemplo: Solicitud de mantenimiento.',
        descripcion: 'Describa la información solicitada. Máximo 200 caracteres.',
        observaciones: 'Información adicional opcional. Máximo 200 caracteres.',
        mensaje: 'Explique su solicitud de forma clara. Máximo 200 caracteres.',
        motivo: 'Explique brevemente el motivo del cambio o la acción.',
        fecha_contratacion: 'Seleccione una fecha válida que no sea futura.',
        fechaContratacion: 'Seleccione una fecha válida que no sea futura.',
        fecha_adquma: 'Seleccione una fecha válida que no sea futura.',
        fechaAdquma: 'Seleccione una fecha válida que no sea futura.',
        fecha_devolucion: 'Opcional. No puede ser anterior a la fecha de adquisición.',
        fechaDevolucion: 'Opcional. No puede ser anterior a la fecha inicial.',
        fechaDesde: 'Seleccione la fecha de inicio.',
        fechaHasta: 'Debe ser igual o posterior a la fecha de inicio.',
        horaIngreso: 'Seleccione la hora de ingreso en formato de 24 horas.',
        horaSalida: 'Seleccione una hora posterior a la de ingreso.',
        fechaMante: 'Seleccione una fecha válida de mantenimiento.',
        usuarioDeseado: 'Nombre de usuario opcional. Máximo 45 caracteres. Ejemplo: carlos.garzon.',
        buscar: 'Escriba un nombre, documento o código para filtrar los resultados.'
    };

    function textoAyuda(campo) {
        if (campo.name === 'descripcion') return 'Describa la información solicitada. Máximo ' + campo.maxLength + ' caracteres. Ejemplo: Mantenimiento preventivo.';
        if (campo.name === 'mensaje') return 'Explique su solicitud de forma clara. Máximo ' + campo.maxLength + ' caracteres.';
        if (ayudas[campo.name]) return ayudas[campo.name];
        if (campo.tagName === 'SELECT') return 'Seleccione una opción de la lista.';
        if (campo.type === 'date') return 'Seleccione una fecha válida.';
        if (campo.type === 'time') return 'Seleccione una hora válida.';
        if (campo.type === 'checkbox') return 'Marque esta casilla para continuar.';
        if (campo.maxLength > 0) return 'Máximo ' + campo.maxLength + ' caracteres.';
        return '';
    }

    function ejemplo(campo) {
        const ejemplos = {
            nombre: 'Ej.: Carlos Alberto', apellidos: 'Ej.: Rodriguez Perez',
            email: 'Ej.: nombre@correo.com', telefono: 'Ej.: 3001234567',
            telefonoVerificacion: 'Ej.: 3001234567',
            Usuario: 'NÚMERO DE DOCUMENTO',
            direccion: 'Ej.: Calle 10 # 20-30', codigo_act: 'Ej.: ACT-001',
            codigoAct: 'Ej.: ACT-001', nombre_activos: 'Ej.: Monitor Dell 24',
            nombreActivo: 'Ej.: Monitor Dell 24', valor: 'Ej.: 1250000',
            costo: 'Ej.: 1250000', vida_util: 'Ej.: 5', vidaUtil: 'Ej.: 5',
            asunto: 'Ej.: Solicitud de mantenimiento', descripcion: 'Ej.: Mantenimiento preventivo',
            observaciones: 'Ej.: Equipo entregado en buen estado',
            mensaje: 'Ej.: Solicito información sobre...', motivo: 'Ej.: Actualización de datos',
            usuarioDeseado: 'Ej.: carlos.garzon', buscar: 'Nombre, documento o código'
        };
        if (campo.name === 'clave' || campo.name === 'nuevaClave') return 'De 8 a 20 caracteres';
        if (campo.name === 'confirmarClave') return 'Repita la contraseña';
        return ejemplos[campo.name] || '';
    }

    function textoError(campo) {
        const v = campo.validity;
        if (v.valueMissing) return 'Este campo es obligatorio.';
        if (v.typeMismatch) return campo.type === 'email' ? 'Ingrese un correo electrónico válido. Ejemplo: nombre@correo.com.' : 'El formato ingresado no es válido.';
        if (v.tooShort) return 'Debe tener al menos ' + campo.minLength + ' caracteres.';
        if (v.tooLong) return 'No puede superar ' + campo.maxLength + ' caracteres.';
        if (v.patternMismatch) return campo.title || textoAyuda(campo) || 'El formato ingresado no es válido.';
        if (v.rangeUnderflow) return 'El valor mínimo permitido es ' + campo.min + '.';
        if (v.rangeOverflow) return 'El valor máximo permitido es ' + campo.max + '.';
        if (v.badInput) return 'Ingrese un valor válido.';
        return campo.validationMessage || 'Revise la información ingresada.';
    }

    function crearMensajes(campo) {
        if (campo.type === 'hidden' || campo.type === 'submit' || campo.type === 'button' || campo.dataset.ayudaLista === 'si') return;
        const contenedor = campo.closest('.form-field, .mb-3, .col, [class*="col-"], .form-group, .form-check') || campo.parentElement;
        if (!contenedor) return;
        const id = campo.id || ('campo-' + campo.name + '-' + Math.random().toString(36).slice(2, 7));
        campo.id = id;
        let error;
        function obtenerError() {
            if (error) return error;
            error = document.createElement('small');
            error.className = 'field-error';
            error.id = id + '-error';
            error.hidden = true;
            contenedor.append(error);
            campo.setAttribute('aria-describedby', error.id);
            return error;
        }
        if (campo.tagName !== 'SELECT' && campo.type !== 'checkbox' && campo.type !== 'radio'
                && campo.type !== 'date' && campo.type !== 'time' && !campo.value) {
            const textoEjemplo = ejemplo(campo);
            if (textoEjemplo) campo.placeholder = textoEjemplo;
        }
        campo.dataset.ayudaLista = 'si';

        function mostrarError() {
            if (campo.validity.valid) {
                if (error) error.hidden = true;
                campo.removeAttribute('aria-invalid');
                campo.classList.remove('is-invalid');
            } else {
                const mensaje = obtenerError();
                mensaje.textContent = textoError(campo);
                mensaje.hidden = false;
                campo.setAttribute('aria-invalid', 'true');
                campo.classList.add('is-invalid');
            }
        }
        campo.addEventListener('blur', mostrarError);
        campo.addEventListener('input', mostrarError);
        campo.addEventListener('change', mostrarError);
        campo.addEventListener('invalid', mostrarError);
    }

    function bloque(campo) {
        return campo && campo.closest('.form-field, .col-md-6, .col-md-4, .mb-3, .form-check');
    }

    function moverAntes(origen, destino) {
        const origenBloque = bloque(origen);
        const destinoBloque = bloque(destino);
        if (!origenBloque || !destinoBloque || origenBloque === destinoBloque) return;
        destinoBloque.parentElement.insertBefore(origenBloque, destinoBloque);
    }

    function organizarJerarquia(formulario) {
        /* El tipo de documento determina las reglas del número de identificación. */
        const tipoDocumento = formulario.querySelector('[name="Documento_id_documento"], [name="documentoId"], [name="tipoDocumentoId"]');
        const identificacion = formulario.querySelector('[name="identificacion"]');
        moverAntes(tipoDocumento, identificacion);

        /* La modalidad de acceso determina si se solicita o se genera una clave. */
        moverAntes(formulario.querySelector('[name="clave_provisional"]'), formulario.querySelector('[name="clave"]'));

        /* En mantenimiento se elige primero el activo y su proveedor. */
        const activo = formulario.querySelector('[name="activoId"]');
        const proveedor = formulario.querySelector('[name="proveedorId"]');
        const fechaMantenimiento = formulario.querySelector('[name="fechaMante"]');
        if (activo && proveedor && fechaMantenimiento) {
            moverAntes(proveedor, fechaMantenimiento);
            moverAntes(activo, proveedor);
        }

        /* En asignaciones, la persona y el activo se definen antes de las fechas. */
        const personal = formulario.querySelector('[name="personalId"]');
        const fechaAsignacion = formulario.querySelector('[name="fechaAsignacion"]');
        if (personal && activo && fechaAsignacion) {
            const filaSeleccion = personal.closest('.row');
            const filaFecha = fechaAsignacion.closest('.row');
            if (filaSeleccion && filaFecha && filaSeleccion !== filaFecha) {
                filaFecha.parentElement.insertBefore(filaSeleccion, filaFecha);
            }
        }

        /* Una programación empieza por la persona y el horario que se le asignará. */
        const horario = formulario.querySelector('[name="horarioId"]');
        const descripcion = formulario.querySelector('[name="descripcion"]');
        if (personal && horario && descripcion) {
            const filaProgramacion = personal.closest('.row');
            const bloqueDescripcion = bloque(descripcion);
            if (filaProgramacion && bloqueDescripcion && filaProgramacion !== bloqueDescripcion) {
                bloqueDescripcion.parentElement.insertBefore(filaProgramacion, bloqueDescripcion);
            }
        }
    }

    document.querySelectorAll('form').forEach(function (formulario) {
        organizarJerarquia(formulario);
        formulario.querySelectorAll('input, select, textarea').forEach(crearMensajes);
        formulario.addEventListener('submit', function () {
            formulario.querySelectorAll('input, select, textarea').forEach(function (campo) {
                if (campo.dataset.ayudaLista === 'si' && !campo.validity.valid) campo.dispatchEvent(new Event('invalid', {cancelable: true}));
            });
        });
    });
});
