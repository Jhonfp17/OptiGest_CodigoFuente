package PruebaActualizar;

import Modelo.Documento;
import Controlador.DocumentoDAO;
import java.util.Scanner;

public class PruebaActualizarDocumento {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        DocumentoDAO dao = new DocumentoDAO();
        Documento doc = new Documento();

        System.out.println("=== ACTUALIZAR DOCUMENTO ===");

        System.out.print("Ingrese el ID del documento a modificar: ");
        doc.setId_documento(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva descripcion del documento: ");
        doc.setDescripcion_doc(leer.nextLine());

        if (dao.actualizar(doc)) {
            System.out.println("Documento actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID del documento.");
        }

        leer.close();
    }
}