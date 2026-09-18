package PruebaInsertar;

import Modelo.Horarios;
import Controlador.HorariosDAO;
import java.util.Scanner;

public class PruebaInsertarHorarios {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        HorariosDAO dao = new HorariosDAO();
        Horarios horario = new Horarios();

        System.out.println("=== REGISTRO DE HORARIO ===");

        System.out.print("Descripcion del horario: ");
        horario.setDescripcion(leer.nextLine());

        System.out.print("Hora de ingreso (HH:MM:SS): ");
        horario.setHora_ingreso(leer.nextLine());

        System.out.print("Hora de salida (HH:MM:SS): ");
        horario.setHora_salida(leer.nextLine());

        if (dao.insertar(horario)) {
            System.out.println("Horario guardado correctamente.");
        } else {
            System.out.println("Error al registrar horario.");
        }

        leer.close();
    }
}