document.addEventListener('DOMContentLoaded', function () {

    const searchInput = document.querySelector('.search-input');
    const filas       = document.querySelectorAll('.personal-tbl tbody tr');

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
});