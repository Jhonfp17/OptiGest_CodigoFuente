package PruebaConsultar;

import Modelo.Proveedores;
import Controlador.ProveedoresDAO;
import java.util.List;

public class PruebaConsultarProveedores {

    public static void main(String[] args) {

        ProveedoresDAO dao = new ProveedoresDAO();

        System.out.println("=== LISTADO DE PROVEEDORES ===");

        List<Proveedores> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay proveedores registrados.");
        } else {
            for (Proveedores p : lista) {
                System.out.println("ID: " + p.getIdProveedores()
                        + " | Nombre: " + p.getNombre()
                        + " | Telefono: " + p.getTelefono()
                        + " | Direccion: " + p.getDireccion()
                        + " | Email: " + p.getEmail());
            }
        }
    }
}