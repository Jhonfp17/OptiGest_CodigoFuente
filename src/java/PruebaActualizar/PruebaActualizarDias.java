package PruebaActualizar;

import Modelo.Dias;
import Controlador.DiasDAO;
import java.util.Scanner;

public class PruebaActualizarDias {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        DiasDAO dao = new DiasDAO();
        Dias dia = new Dias();

        System.out.println("=== ACTUALIZAR DIA ===");

        System.out.print("Ingrese el ID del dia a modificar: ");
        dia.setIdDias(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva descripcion del dia: ");
        dia.setDescripcionDias(leer.nextLine());

        if (dao.actualizar(dia)) {
            System.out.println("Dia actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID del dia.");
        }

        leer.close();
    }
}