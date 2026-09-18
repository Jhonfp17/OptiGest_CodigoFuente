package PruebaListar;

import Modelo.Programacion_Personal;
import Controlador.Programacion_PersonalDAO;
import java.util.Scanner;

public class PruebaListarProgramacion_Personal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Programacion_PersonalDAO dao = new Programacion_PersonalDAO();

        System.out.println("=== BUSCAR PROGRAMACION DE PERSONAL POR ID ===");

        System.out.print("Ingrese el ID de la programacion a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Programacion_Personal p = dao.consultar(id);

        if (p != null) {
            System.out.println("ID: " + p.getIdProgramacion_Personal()
                    + " | Descripcion: " + p.getDescripcion_programacion()
                    + " | Fecha desde: " + p.getFecha_desde()
                    + " | Fecha hasta: " + p.getFecha_hasta()
                    + " | ID Dia: " + p.getDias_idDias()
                    + " | ID Personal: " + p.getPersonal_id_personal()
                    + " | ID Horario: " + p.getHorarios_id_horarios());
        } else {
            System.out.println("No se encontro programacion con ID: " + id);
        }

        leer.close();
    }
}