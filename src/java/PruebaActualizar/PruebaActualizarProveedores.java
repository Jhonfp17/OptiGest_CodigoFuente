package PruebaActualizar;

import Modelo.Proveedores;
import Controlador.ProveedoresDAO;
import java.util.Scanner;

public class PruebaActualizarProveedores {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        ProveedoresDAO dao = new ProveedoresDAO();
        Proveedores prov = new Proveedores();

        System.out.println("=== ACTUALIZAR PROVEEDOR ===");

        System.out.print("Ingrese el ID del proveedor a modificar: ");
        prov.setIdProveedores(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo nombre: ");
        prov.setNombre(leer.nextLine());

        System.out.print("Nuevo telefono: ");
        prov.setTelefono(leer.nextLine());

        System.out.print("Nueva direccion: ");
        prov.setDireccion(leer.nextLine());

        System.out.print("Nuevo email: ");
        prov.setEmail(leer.nextLine());

        if (dao.actualizar(prov)) {
            System.out.println("Proveedor actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID del proveedor.");
        }

        leer.close();
    }
}