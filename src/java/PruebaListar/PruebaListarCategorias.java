package PruebaListar;

import Modelo.Categorias;
import Controlador.CategoriasDAO;
import java.util.Scanner;

public class PruebaListarCategorias {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        CategoriasDAO dao = new CategoriasDAO();

        System.out.println("=== BUSCAR CATEGORIA POR ID ===");

        System.out.print("Ingrese el ID de la categoria a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Categorias c = dao.consultar(id);

        if (c != null) {
            System.out.println("ID: " + c.getIdCategorias()
                    + " | Descripcion: " + c.getDescripcionCategoria());
        } else {
            System.out.println("No se encontro categoria con ID: " + id);
        }

        leer.close();
    }
}