package PruebaInsertar;

import Modelo.Dias;
import Controlador.DiasDAO;
import java.util.Scanner;

public class PruebaInsertarDias {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        DiasDAO dao = new DiasDAO();
        Dias dia = new Dias();

        System.out.println("=== REGISTRO DE DIA ===");

        System.out.print("Descripcion del Dia (ej: Lunes): ");
        dia.setDescripcionDias(leer.nextLine());

        if (dao.insertar(dia)) {
            System.out.println("Dia guardado correctamente.");
        } else {
            System.out.println("Error al registrar dia.");
        }

        leer.close();
    }
}