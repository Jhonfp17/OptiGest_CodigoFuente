package PruebaInsertar;

import Modelo.Documento;
import Controlador.DocumentoDAO;
import java.util.Scanner;

public class PruebaInsertarDocumento {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        DocumentoDAO dao = new DocumentoDAO();
        Documento doc = new Documento();

        System.out.println("=== REGISTRO DE TIPO DE DOCUMENTO ===");

        System.out.print("Descripcion (ej: Cedula, Pasaporte): ");
        doc.setDescripcion_doc(leer.nextLine());

        if (dao.insertar(doc)) {
            System.out.println("Tipo de documento guardado correctamente.");
        } else {
            System.out.println("Error al registrar tipo de documento.");
        }

        leer.close();
    }
}