package PruebaConsultar;

import Modelo.Estado_Activo;
import Controlador.Estado_ActivoDAO;
import java.util.List;

public class PruebaConsultarEstado_Activo {

    public static void main(String[] args) {

        Estado_ActivoDAO dao = new Estado_ActivoDAO();

        System.out.println("=== LISTADO DE ESTADOS DE ACTIVO ===");

        List<Estado_Activo> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay estados de activo registrados.");
        } else {
            for (Estado_Activo ea : lista) {
                System.out.println("ID: " + ea.getIdEstado_Activo()
                        + " | Descripcion: " + ea.getDescripcion_activo());
            }
        }
    }
}