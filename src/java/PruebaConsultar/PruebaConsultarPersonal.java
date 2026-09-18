package PruebaConsultar;

import Modelo.Personal;
import Controlador.PersonalDAO;
import java.util.List;

public class PruebaConsultarPersonal {

    public static void main(String[] args) {

        PersonalDAO dao = new PersonalDAO();

        System.out.println("=== LISTADO DE PERSONAL ===");

        List<Personal> lista = dao.consultarTodo();

        if (lista.isEmpty()) {
            System.out.println("No hay personal registrado.");
        } else {
            for (Personal p : lista) {
                System.out.println("ID: " + p.getIdPersonal()
                        + " | Identificacion: " + p.getIdentificacion()
                        + " | Nombre: " + p.getNombre()
                        + " | Apellidos: " + p.getApellidos()
                        + " | Email: " + p.getEmail()
                        + " | Telefono: " + p.getTelefono()
                        + " | Puede acceder: " + p.isPuede_acceder()
                        + " | ID Rol: " + p.getRoles_idroles()
                        + " | ID Estado: " + p.getEstado_Personal_id_estado());
            }
        }
    }
}