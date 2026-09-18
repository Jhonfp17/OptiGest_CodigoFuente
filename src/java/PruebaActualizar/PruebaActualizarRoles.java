package PruebaActualizar;

import Modelo.Roles;
import Controlador.RolesDAO;
import java.util.Scanner;

public class PruebaActualizarRoles {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        RolesDAO dao = new RolesDAO();
        Roles rol = new Roles();

        System.out.println("=== ACTUALIZAR ROL ===");

        System.out.print("Ingrese el ID del rol a modificar: ");
        rol.setIdRoles(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva descripcion del rol: ");
        rol.setDescripcion_roles(leer.nextLine());

        System.out.print("Nuevo tipo de acceso (ADMINISTRADOR, PERSONAL_FIJO, TEMPORAL): ");
        rol.setTipo_acceso(leer.nextLine());

        if (dao.actualizar(rol)) {
            System.out.println("Rol actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID del rol.");
        }

        leer.close();
    }
}