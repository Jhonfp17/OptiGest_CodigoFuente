package PruebaInsertar;

import Modelo.Categorias;
import Controlador.CategoriasDAO;
import java.util.Scanner;

public class PruebaInsertarCategorias {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        CategoriasDAO dao = new CategoriasDAO();
        Categorias cat = new Categorias();

        System.out.println("=== REGISTRO DE CATEGORIA ===");

        System.out.print("Descripcion de la Categoria: ");
        cat.setDescripcionCategoria(leer.nextLine());

        if (dao.insertar(cat)) {
            System.out.println("Categoria guardada correctamente.");
        } else {
            System.out.println("Error al registrar categoria.");
        }

        leer.close();
    }
}