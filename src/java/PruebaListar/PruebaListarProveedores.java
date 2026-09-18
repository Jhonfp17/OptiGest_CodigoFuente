package PruebaListar;

import Modelo.Proveedores;
import Controlador.ProveedoresDAO;
import java.util.Scanner;

public class PruebaListarProveedores {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        ProveedoresDAO dao = new ProveedoresDAO();

        System.out.println("=== BUSCAR PROVEEDOR POR ID ===");

        System.out.print("Ingrese el ID del proveedor a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Proveedores p = dao.consultar(id);

        if (p != null) {
            System.out.println("ID: " + p.getIdProveedores()
                    + " | Nombre: " + p.getNombre()
                    + " | Telefono: " + p.getTelefono()
                    + " | Direccion: " + p.getDireccion()
                    + " | Email: " + p.getEmail());
        } else {
            System.out.println("No se encontro proveedor con ID: " + id);
        }

        leer.close();
    }
}