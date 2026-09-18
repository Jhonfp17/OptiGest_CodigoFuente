package PruebaListar;

import Modelo.Documento;
import Controlador.DocumentoDAO;
import java.util.Scanner;

public class PruebaListarDocumento {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        DocumentoDAO dao = new DocumentoDAO();

        System.out.println("=== BUSCAR DOCUMENTO POR ID ===");

        System.out.print("Ingrese el ID del documento a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Documento d = dao.consultar(id);

        if (d != null) {
            System.out.println("ID: " + d.getId_documento()
                    + " | Descripcion: " + d.getDescripcion_doc());
        } else {
            System.out.println("No se encontro documento con ID: " + id);
        }

        leer.close();
    }
}