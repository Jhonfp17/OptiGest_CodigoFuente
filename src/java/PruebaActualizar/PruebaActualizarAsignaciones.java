package PruebaActualizar;

import Modelo.Asignaciones;
import Controlador.AsignacionesDAO;
import java.util.Scanner;

public class PruebaActualizarAsignaciones {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        AsignacionesDAO dao = new AsignacionesDAO();
        Asignaciones asig = new Asignaciones();

        System.out.println("=== ACTUALIZAR ASIGNACION ===");

        System.out.print("Ingrese el ID de la asignacion a modificar: ");
        asig.setId_asignaciones(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva fecha asignacion (YYYY-MM-DD): ");
        asig.setFecha_asignacion(leer.nextLine());

        System.out.print("Nueva fecha devolucion (YYYY-MM-DD o vacio si no aplica): ");
        asig.setFecha_devolucion(leer.nextLine());

        System.out.print("Nuevas observaciones: ");
        asig.setObservaciones(leer.nextLine());

        System.out.print("Nuevo ID Personal (Debe existir): ");
        asig.setPersonal_id_personal(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo ID Activo (Debe existir): ");
        asig.setActivos_id_activos(Integer.parseInt(leer.nextLine()));

        if (dao.actualizar(asig)) {
            System.out.println("Asignacion actualizada correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID de la asignacion.");
        }

        leer.close();
    }
}