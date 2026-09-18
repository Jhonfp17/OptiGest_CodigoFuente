package PruebaActualizar;

import Modelo.Horarios;
import Controlador.HorariosDAO;
import java.util.Scanner;

public class PruebaActualizarHorarios {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        HorariosDAO dao = new HorariosDAO();
        Horarios horario = new Horarios();

        System.out.println("=== ACTUALIZAR HORARIO ===");

        System.out.print("Ingrese el ID del horario a modificar: ");
        horario.setId_horarios(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva descripcion del horario: ");
        horario.setDescripcion(leer.nextLine());

        System.out.print("Nueva hora de ingreso (HH:MM:SS): ");
        horario.setHora_ingreso(leer.nextLine());

        System.out.print("Nueva hora de salida (HH:MM:SS): ");
        horario.setHora_salida(leer.nextLine());

        if (dao.actualizar(horario)) {
            System.out.println("Horario actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID del horario.");
        }

        leer.close();
    }
}