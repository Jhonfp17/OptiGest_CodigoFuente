package PruebaInsertar;

import Modelo.Asignaciones;
import Controlador.AsignacionesDAO;
import java.util.Scanner;

public class PruebaInsertarAsignaciones {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        AsignacionesDAO dao = new AsignacionesDAO();
        Asignaciones asig = new Asignaciones();

        System.out.println("=== REGISTRO DE ASIGNACION ===");

        System.out.print("Fecha Asignacion (YYYY-MM-DD): ");
        asig.setFecha_asignacion(leer.nextLine());

        System.out.print("Fecha Devolucion (YYYY-MM-DD o vacio si no aplica): ");
        asig.setFecha_devolucion(leer.nextLine());

        System.out.print("Observaciones: ");
        asig.setObservaciones(leer.nextLine());

        System.out.print("ID Personal (Debe existir): ");
        asig.setPersonal_id_personal(Integer.parseInt(leer.nextLine()));

        System.out.print("ID Activo (Debe existir): ");
        asig.setActivos_id_activos(Integer.parseInt(leer.nextLine()));

        if (dao.insertar(asig)) {
            System.out.println("Asignacion guardada correctamente.");
        } else {
            System.out.println("Error: Verifica los IDs de Personal y Activo.");
        }

        leer.close();
    }
}