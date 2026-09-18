package PruebaListar;

import Modelo.Horarios;
import Controlador.HorariosDAO;
import java.util.Scanner;

public class PruebaListarHorarios {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        HorariosDAO dao = new HorariosDAO();

        System.out.println("=== BUSCAR HORARIO POR ID ===");

        System.out.print("Ingrese el ID del horario a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Horarios h = dao.consultarHorario(id);

        if (h != null) {
            System.out.println("ID: " + h.getId_horarios()
                    + " | Descripcion: " + h.getDescripcion()
                    + " | Hora ingreso: " + h.getHora_ingreso()
                    + " | Hora salida: " + h.getHora_salida());
        } else {
            System.out.println("No se encontro horario con ID: " + id);
        }

        leer.close();
    }
}