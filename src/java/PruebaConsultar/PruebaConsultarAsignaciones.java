package PruebaConsultar;

import Modelo.Asignaciones;
import Controlador.AsignacionesDAO;
import java.util.List;

public class PruebaConsultarAsignaciones {

    public static void main(String[] args) {

        AsignacionesDAO dao = new AsignacionesDAO();

        System.out.println("=== LISTADO DE ASIGNACIONES ===");

        List<Asignaciones> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay asignaciones registradas.");
        } else {
            for (Asignaciones a : lista) {
                System.out.println("ID: " + a.getId_asignaciones()
                        + " | Fecha asignacion: " + a.getFecha_asignacion()
                        + " | Fecha devolucion: " + a.getFecha_devolucion()
                        + " | Observaciones: " + a.getObservaciones()
                        + " | ID Personal: " + a.getPersonal_id_personal()
                        + " | ID Activo: " + a.getActivos_id_activos());
            }
        }
    }
}