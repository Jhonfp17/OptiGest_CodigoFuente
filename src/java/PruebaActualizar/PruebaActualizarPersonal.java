package PruebaActualizar;

import Modelo.Personal;
import Controlador.PersonalDAO;
import java.util.Scanner;

public class PruebaActualizarPersonal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        PersonalDAO dao = new PersonalDAO();
        Personal per = new Personal();

        System.out.println("=== ACTUALIZAR PERSONAL ===");

        System.out.print("Ingrese la identificacion del personal a modificar: ");
        per.setIdentificacion(leer.nextLine());

        System.out.print("Nuevo nombre: ");
        per.setNombre(leer.nextLine());

        System.out.print("Nuevos apellidos: ");
        per.setApellidos(leer.nextLine());

        System.out.print("Nuevo email: ");
        per.setEmail(leer.nextLine());

        System.out.print("Nuevo telefono: ");
        per.setTelefono(leer.nextLine());

        System.out.print("Nueva direccion: ");
        per.setDireccion(leer.nextLine());

        System.out.print("Nueva clave: ");
        per.setClave(leer.nextLine());

        System.out.print("Nuevas observaciones: ");
        per.setObservaciones(leer.nextLine());

        System.out.print("Puede acceder? (1=Si, 0=No): ");
        per.setPuede_acceder(Integer.parseInt(leer.nextLine()) == 1);

        System.out.print("Nueva fecha contratacion (YYYY-MM-DD): ");
        per.setFecha_contratacion(leer.nextLine());

        System.out.print("Nuevo ID Documento (Debe existir): ");
        per.setDocumento_id_documento(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo ID Rol (Debe existir): ");
        per.setRoles_idroles(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo ID Estado Personal (Debe existir): ");
        per.setEstado_Personal_id_estado(Integer.parseInt(leer.nextLine()));

        if (dao.actualizar(per)) {
            System.out.println("Personal actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica la identificacion del personal.");
        }

        leer.close();
    }
}