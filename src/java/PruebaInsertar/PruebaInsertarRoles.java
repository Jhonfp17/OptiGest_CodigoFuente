package PruebaInsertar;

import Modelo.Roles;
import Controlador.RolesDAO;
import java.util.Scanner;

public class PruebaInsertarRoles {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        RolesDAO dao = new RolesDAO();
        Roles rol = new Roles();

        System.out.println("=== REGISTRO DE ROL ===");

        System.out.print("Descripcion del rol: ");
        rol.setDescripcion_roles(leer.nextLine());

        System.out.print("Tipo de acceso (ADMINISTRADOR, PERSONAL_FIJO, TEMPORAL): ");
        rol.setTipo_acceso(leer.nextLine());

        if (dao.insertar(rol)) {
            System.out.println("Rol guardado correctamente.");
        } else {
            System.out.println("Error al registrar rol.");
        }

        leer.close();
    }
}