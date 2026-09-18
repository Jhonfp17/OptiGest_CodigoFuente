package PruebaConsultar;

import Modelo.Estado_Personal;
import Controlador.Estado_PersonalDAO;
import java.util.List;

public class PruebaConsultarEstado_Personal {

    public static void main(String[] args) {

        Estado_PersonalDAO dao = new Estado_PersonalDAO();

        System.out.println("=== LISTADO DE ESTADOS DE PERSONAL ===");

        List<Estado_Personal> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay estados de personal registrados.");
        } else {
            for (Estado_Personal ep : lista) {
                System.out.println("ID: " + ep.getId_estado()
                        + " | Descripcion: " + ep.getDescripcion_estado());
            }
        }
    }
}