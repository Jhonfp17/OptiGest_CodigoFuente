package PruebaActualizar;

import Modelo.Programacion_Personal;
import Controlador.Programacion_PersonalDAO;
import java.util.Scanner;

public class PruebaActualizarProgramacion_Personal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Programacion_PersonalDAO dao = new Programacion_PersonalDAO();
        Programacion_Personal prog = new Programacion_Personal();

        System.out.println("=== ACTUALIZAR PROGRAMACION DE PERSONAL ===");

        System.out.print("Ingrese el ID de la programacion a modificar: ");
        prog.setIdProgramacion_Personal(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva descripcion: ");
        prog.setDescripcion_programacion(leer.nextLine());

        System.out.print("Nueva fecha desde (YYYY-MM-DD): ");
        prog.setFecha_desde(leer.nextLine());

        System.out.print("Nueva fecha hasta (YYYY-MM-DD): ");
        prog.setFecha_hasta(leer.nextLine());

        System.out.print("Nuevo ID Dia (Debe existir): ");
        prog.setDias_idDias(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo ID Personal (Debe existir): ");
        prog.setPersonal_id_personal(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo ID Horario (Debe existir): ");
        prog.setHorarios_id_horarios(Integer.parseInt(leer.nextLine()));

        if (dao.actualizar(prog)) {
            System.out.println("Programacion actualizada correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID de la programacion.");
        }

        leer.close();
    }
}