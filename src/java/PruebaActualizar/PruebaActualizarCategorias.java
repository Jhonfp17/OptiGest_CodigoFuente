package PruebaActualizar;

import Modelo.Categorias;
import Controlador.CategoriasDAO;
import java.util.Scanner;

public class PruebaActualizarCategorias {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        CategoriasDAO dao = new CategoriasDAO();
        Categorias cat = new Categorias();

        System.out.println("=== ACTUALIZAR CATEGORIA ===");

        System.out.print("Ingrese el ID de la categoria a modificar: ");
        cat.setIdCategorias(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva descripcion de la categoria: ");
        cat.setDescripcionCategoria(leer.nextLine());

        if (dao.actualizar(cat)) {
            System.out.println("Categoria actualizada correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID de la categoria.");
        }

        leer.close();
    }
}