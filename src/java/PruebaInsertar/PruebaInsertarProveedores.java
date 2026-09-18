package PruebaInsertar;

import Modelo.Proveedores;
import Controlador.ProveedoresDAO;
import java.util.Scanner;

public class PruebaInsertarProveedores {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        ProveedoresDAO dao = new ProveedoresDAO();
        Proveedores prov = new Proveedores();

        System.out.println("=== REGISTRO DE PROVEEDOR ===");

        System.out.print("Nombre: ");
        prov.setNombre(leer.nextLine());

        System.out.print("Telefono: ");
        prov.setTelefono(leer.nextLine());

        System.out.print("Direccion: ");
        prov.setDireccion(leer.nextLine());

        System.out.print("Email: ");
        prov.setEmail(leer.nextLine());

        if (dao.insertar(prov)) {
            System.out.println("Proveedor guardado correctamente.");
        } else {
            System.out.println("Error al registrar proveedor.");
        }

        leer.close();
    }
}
