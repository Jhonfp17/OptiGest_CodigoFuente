document.addEventListener('DOMContentLoaded', function () {
    var boton = document.querySelector('.btn-ver-mas-asignaciones');
    var extras = Array.prototype.slice.call(document.querySelectorAll('[data-asignacion-extra="true"]'));
    var buscador = document.querySelector('.search-input');
    var expandido = false;

    function actualizarVista() {
        extras.forEach(function (fila) {
            fila.classList.toggle('asignacion-fila-oculta', !expandido);
        });
        if (boton) {
            boton.textContent = expandido ? 'Mostrar menos' : 'Ver todas las asignaciones';
            boton.setAttribute('aria-expanded', String(expandido));
        }
    }

    if (boton) {
        boton.addEventListener('click', function () {
            expandido = !expandido;
            actualizarVista();
        });
    }

    if (buscador) {
        buscador.addEventListener('input', function () {
            if (this.value.trim() && !expandido) {
                expandido = true;
                actualizarVista();
            }
            var texto = this.value.toLowerCase().trim();
            document.querySelectorAll('.asignaciones-tbl tbody tr').forEach(function (fila) {
                fila.style.display = !texto || fila.textContent.toLowerCase().includes(texto) ? '' : 'none';
            });
        });
    }
});
