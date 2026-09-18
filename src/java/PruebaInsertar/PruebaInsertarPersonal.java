package PruebaInsertar;

import Modelo.Personal;
import Controlador.PersonalDAO;
import java.util.Scanner;

public class PruebaInsertarPersonal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        PersonalDAO dao = new PersonalDAO();
        Personal per = new Personal();

        System.out.println("=== REGISTRO DE PERSONAL ===");

        System.out.print("Nombre: ");
        per.setNombre(leer.nextLine());

        System.out.print("Apellidos: ");
        per.setApellidos(leer.nextLine());

        System.out.print("Identificacion: ");
        per.setIdentificacion(leer.nextLine());

        System.out.print("Email: ");
        per.setEmail(leer.nextLine());

        System.out.print("Telefono: ");
        per.setTelefono(leer.nextLine());

        System.out.print("Direccion: ");
        per.setDireccion(leer.nextLine());

        System.out.print("Clave: ");
        per.setClave(leer.nextLine());

        System.out.print("Observaciones: ");
        per.setObservaciones(leer.nextLine());

        System.out.print("Puede acceder? (1=Si, 0=No): ");
        per.setPuede_acceder(Integer.parseInt(leer.nextLine()) == 1);

        System.out.print("Fecha contratacion (YYYY-MM-DD): ");
        per.setFecha_contratacion(leer.nextLine());

        System.out.print("ID Documento (Debe existir): ");
        per.setDocumento_id_documento(Integer.parseInt(leer.nextLine()));

        System.out.print("ID Rol (Debe existir): ");
        per.setRoles_idroles(Integer.parseInt(leer.nextLine()));

        System.out.print("ID Estado Personal (Debe existir): ");
        per.setEstado_Personal_id_estado(Integer.parseInt(leer.nextLine()));

        if (dao.insertar(per)) {
            System.out.println("Personal guardado correctamente.");
        } else {
            System.out.println("Error: Verifica los IDs de Documento, Rol y Estado.");
        }

        leer.close();
    }
}