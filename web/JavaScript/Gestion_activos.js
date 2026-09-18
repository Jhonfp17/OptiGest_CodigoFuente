document.addEventListener('DOMContentLoaded', function () {

    const searchInput = document.querySelector('.search-input');
    const filas       = document.querySelectorAll('.activos-tbl tbody tr');

    if (searchInput) {
        searchInput.addEventListener('keyup', function () {
            const texto = this.value.toLowerCase();

            filas.forEach(function (fila) {
                const contenido = fila.textContent.toLowerCase();
                if (contenido.includes(texto)) {
                    fila.style.display = '';
                } else {
                    fila.style.display = 'none';
                }
            });
        });
    }

    const main = document.querySelector('[data-estados-url]');
    const claseEstado = function (estado) {
        const valor = (estado || '').toUpperCase();
        if (valor === 'DISPONIBLE' || valor === 'ACTIVO') return 'estado-badge--success';
        if (valor === 'ASIGNADO') return 'estado-badge--info';
        if (valor === 'EN MANTENIMIENTO') return 'estado-badge--warning';
        if (valor === 'RETIRADO' || valor === 'DAÑADO' || valor === 'SUSPENDIDO') return 'estado-badge--danger';
        return 'estado-badge--neutral';
    };
    const aplicarClaseEstado = function (celda, estado) {
        celda.className = 'estado-badge ' + claseEstado(estado);
    };
    const actualizarEstados = function () {
        if (!main) return;
        fetch(main.dataset.estadosUrl, {credentials: 'same-origin'})
            .then(function (respuesta) { return respuesta.ok ? respuesta.json() : null; })
            .then(function (estados) {
                if (!estados) return;
                document.querySelectorAll('[data-estado-activo]').forEach(function (celda) {
                    const estado = estados[celda.dataset.estadoActivo];
                    if (estado) {
                        celda.textContent = estado;
                        aplicarClaseEstado(celda, estado);
                    }
                });
            })
            .catch(function () {
                // La vista mantiene el último estado conocido si falla la consulta.
            });
    };

    document.querySelectorAll('[data-estado-activo]').forEach(function (celda) {
        aplicarClaseEstado(celda, celda.textContent);
    });
    window.setInterval(actualizarEstados, 10000);
});
