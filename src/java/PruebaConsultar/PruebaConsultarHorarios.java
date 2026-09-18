package PruebaConsultar;

import Modelo.Horarios;
import Controlador.HorariosDAO;
import java.util.List;

public class PruebaConsultarHorarios {

    public static void main(String[] args) {

        HorariosDAO dao = new HorariosDAO();

        System.out.println("=== LISTADO DE HORARIOS ===");

        List<Horarios> lista = dao.consultarTodo();

        if (lista.isEmpty()) {
            System.out.println("No hay horarios registrados.");
        } else {
            for (Horarios h : lista) {
                System.out.println("ID: " + h.getId_horarios()
                        + " | Descripcion: " + h.getDescripcion()
                        + " | Hora ingreso: " + h.getHora_ingreso()
                        + " | Hora salida: " + h.getHora_salida());
            }
        }
    }
}