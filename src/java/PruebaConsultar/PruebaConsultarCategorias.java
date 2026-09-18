package PruebaConsultar;

import Modelo.Categorias;
import Controlador.CategoriasDAO;
import java.util.List;

public class PruebaConsultarCategorias {

    public static void main(String[] args) {

        CategoriasDAO dao = new CategoriasDAO();

        System.out.println("=== LISTADO DE CATEGORIAS ===");

        List<Categorias> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay categorias registradas.");
        } else {
            for (Categorias c : lista) {
                System.out.println("ID: " + c.getIdCategorias()
                        + " | Descripcion: " + c.getDescripcionCategoria());
            }
        }
    }
}