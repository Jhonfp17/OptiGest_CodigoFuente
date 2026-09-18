package PruebaConsultar;

import Modelo.Dias;
import Controlador.DiasDAO;
import java.util.List;

public class PruebaConsultarDias {

    public static void main(String[] args) {

        DiasDAO dao = new DiasDAO();

        System.out.println("=== LISTADO DE DIAS ===");

        List<Dias> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay dias registrados.");
        } else {
            for (Dias d : lista) {
                System.out.println("ID: " + d.getIdDias()
                        + " | Descripcion: " + d.getDescripcionDias());
            }
        }
    }
}