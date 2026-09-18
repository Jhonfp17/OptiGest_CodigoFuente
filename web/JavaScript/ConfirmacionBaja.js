(function () {
    'use strict';

    var formularioPendiente = null;
    var dialogo;
    var motivo;
    var bloqueMotivo;
    var mensajeError;

    function requiereMotivo(formulario) {
        return formulario.dataset.requiereMotivo !== 'false';
    }

    function crearDialogo() {
        dialogo = document.createElement('div');
        dialogo.className = 'confirmacion-baja';
        dialogo.setAttribute('role', 'dialog');
        dialogo.setAttribute('aria-modal', 'true');
        dialogo.setAttribute('aria-hidden', 'true');
        dialogo.innerHTML = ''
                + '<div class="confirmacion-baja__panel">'
                + '  <h2>Confirmar operacion</h2>'
                + '  <p>El registro se desactivara o anulara. La informacion y el historial se conservaran.</p>'
                + '  <div class="confirmacion-baja__motivo">'
                + '    <label for="motivoBaja">Motivo de la operacion</label>'
                + '    <textarea id="motivoBaja" rows="3" maxlength="500"></textarea>'
                + '  </div>'
                + '  <p class="confirmacion-baja__error" aria-live="polite"></p>'
                + '  <div class="confirmacion-baja__acciones">'
                + '    <button type="button" class="confirmacion-baja__cancelar">Cancelar</button>'
                + '    <button type="button" class="confirmacion-baja__aceptar">Confirmar</button>'
                + '  </div>'
                + '</div>';
        document.body.appendChild(dialogo);
        motivo = dialogo.querySelector('#motivoBaja');
        bloqueMotivo = dialogo.querySelector('.confirmacion-baja__motivo');
        mensajeError = dialogo.querySelector('.confirmacion-baja__error');
        dialogo.querySelector('.confirmacion-baja__cancelar').addEventListener('click', cerrar);
        dialogo.querySelector('.confirmacion-baja__aceptar').addEventListener('click', confirmar);
    }

    function abrir(formulario) {
        formularioPendiente = formulario;
        var pedirMotivo = requiereMotivo(formulario);
        bloqueMotivo.hidden = !pedirMotivo;
        motivo.value = '';
        mensajeError.textContent = '';
        dialogo.classList.add('confirmacion-baja--visible');
        dialogo.setAttribute('aria-hidden', 'false');
        if (pedirMotivo) motivo.focus();
    }

    function cerrar() {
        formularioPendiente = null;
        dialogo.classList.remove('confirmacion-baja--visible');
        dialogo.setAttribute('aria-hidden', 'true');
    }

    function confirmar() {
        if (!formularioPendiente) return;
        if (requiereMotivo(formularioPendiente) && !motivo.value.trim()) {
            mensajeError.textContent = 'Debes indicar el motivo de la operacion.';
            motivo.focus();
            return;
        }
        formularioPendiente.querySelector('[name="motivo"]').value = motivo.value.trim();
        formularioPendiente.dataset.confirmada = 'true';
        formularioPendiente.submit();
    }

    document.addEventListener('DOMContentLoaded', function () {
        crearDialogo();
        document.querySelectorAll('[data-cerrar-bloqueo]').forEach(function (boton) {
            boton.addEventListener('click', function () {
                var modal = boton.closest('.confirmacion-baja');
                if (modal) modal.remove();
            });
        });
        document.querySelectorAll('form.form-eliminar').forEach(function (formulario) {
            formulario.addEventListener('submit', function (evento) {
                if (formulario.dataset.confirmada === 'true') return;
                evento.preventDefault();
                abrir(formulario);
            });
        });
    });
}());
