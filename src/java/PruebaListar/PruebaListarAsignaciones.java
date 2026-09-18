package PruebaListar;

import Modelo.Asignaciones;
import Controlador.AsignacionesDAO;
import java.util.Scanner;

public class PruebaListarAsignaciones {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        AsignacionesDAO dao = new AsignacionesDAO();

        System.out.println("=== BUSCAR ASIGNACION POR ID ===");

        System.out.print("Ingrese el ID de la asignacion a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Asignaciones a = dao.consultar(id);

        if (a != null) {
            System.out.println("ID: " + a.getId_asignaciones()
                    + " | Fecha asignacion: " + a.getFecha_asignacion()
                    + " | Fecha devolucion: " + a.getFecha_devolucion()
                    + " | Observaciones: " + a.getObservaciones()
                    + " | ID Personal: " + a.getPersonal_id_personal()
                    + " | ID Activo: " + a.getActivos_id_activos());
        } else {
            System.out.println("No se encontro asignacion con ID: " + id);
        }

        leer.close();
    }
}