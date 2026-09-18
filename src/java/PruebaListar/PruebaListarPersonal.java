package PruebaListar;

import Modelo.Personal;
import Controlador.PersonalDAO;
import java.util.Scanner;

public class PruebaListarPersonal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        PersonalDAO dao = new PersonalDAO();

        System.out.println("=== BUSCAR PERSONAL POR IDENTIFICACION ===");

        System.out.print("Ingrese la identificacion del personal a buscar: ");
        String identificacion = leer.nextLine();

        Personal p = dao.consultarPorIdentificacion(identificacion);

        if (p != null) {
            System.out.println("ID: " + p.getIdPersonal()
                    + " | Identificacion: " + p.getIdentificacion()
                    + " | Nombre: " + p.getNombre()
                    + " | Apellidos: " + p.getApellidos()
                    + " | Email: " + p.getEmail()
                    + " | Telefono: " + p.getTelefono()
                    + " | Direccion: " + p.getDireccion()
                    + " | Puede acceder: " + p.isPuede_acceder()
                    + " | Fecha contratacion: " + p.getFecha_contratacion()
                    + " | ID Documento: " + p.getDocumento_id_documento()
                    + " | ID Rol: " + p.getRoles_idroles()
                    + " | ID Estado: " + p.getEstado_Personal_id_estado());
        } else {
            System.out.println("No se encontro personal con identificacion: " + identificacion);
        }

        leer.close();
    }
}