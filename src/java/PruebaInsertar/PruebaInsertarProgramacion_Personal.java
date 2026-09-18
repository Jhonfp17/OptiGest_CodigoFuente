package PruebaInsertar;

import Modelo.Programacion_Personal;
import Controlador.Programacion_PersonalDAO;
import java.util.Scanner;

public class PruebaInsertarProgramacion_Personal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Programacion_PersonalDAO dao = new Programacion_PersonalDAO();
        Programacion_Personal prog = new Programacion_Personal();

        System.out.println("=== REGISTRO DE PROGRAMACION DE PERSONAL ===");

        System.out.print("Descripcion de la programacion: ");
        prog.setDescripcion_programacion(leer.nextLine());

        System.out.print("Fecha desde (YYYY-MM-DD): ");
        prog.setFecha_desde(leer.nextLine());

        System.out.print("Fecha hasta (YYYY-MM-DD): ");
        prog.setFecha_hasta(leer.nextLine());

        System.out.print("ID Dia (Debe existir): ");
        prog.setDias_idDias(Integer.parseInt(leer.nextLine()));

        System.out.print("ID Personal (Debe existir): ");
        prog.setPersonal_id_personal(Integer.parseInt(leer.nextLine()));

        System.out.print("ID Horario (Debe existir): ");
        prog.setHorarios_id_horarios(Integer.parseInt(leer.nextLine()));

        if (dao.insertar(prog)) {
            System.out.println("Programacion guardada correctamente.");
        } else {
            System.out.println("Error: Verifica los IDs de Dia, Personal y Horario.");
        }

        leer.close();
    }
}