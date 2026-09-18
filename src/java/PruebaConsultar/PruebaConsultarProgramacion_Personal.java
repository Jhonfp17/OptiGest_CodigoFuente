package PruebaConsultar;

import Modelo.Programacion_Personal;
import Controlador.Programacion_PersonalDAO;
import java.util.List;

public class PruebaConsultarProgramacion_Personal {

    public static void main(String[] args) {

        Programacion_PersonalDAO dao = new Programacion_PersonalDAO();

        System.out.println("=== LISTADO DE PROGRAMACION DE PERSONAL ===");

        List<Programacion_Personal> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay programaciones registradas.");
        } else {
            for (Programacion_Personal p : lista) {
                System.out.println("ID: " + p.getIdProgramacion_Personal()
                        + " | Descripcion: " + p.getDescripcion_programacion()
                        + " | Fecha desde: " + p.getFecha_desde()
                        + " | Fecha hasta: " + p.getFecha_hasta()
                        + " | ID Dia: " + p.getDias_idDias()
                        + " | ID Personal: " + p.getPersonal_id_personal()
                        + " | ID Horario: " + p.getHorarios_id_horarios());
            }
        }
    }
}