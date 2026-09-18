package PruebaConsultar;

import Modelo.Documento;
import Controlador.DocumentoDAO;
import java.util.List;

public class PruebaConsultarDocumento {

    public static void main(String[] args) {

        DocumentoDAO dao = new DocumentoDAO();

        System.out.println("=== LISTADO DE DOCUMENTOS ===");

        List<Documento> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay documentos registrados.");
        } else {
            for (Documento d : lista) {
                System.out.println("ID: " + d.getId_documento()
                        + " | Descripcion: " + d.getDescripcion_doc());
            }
        }
    }
}