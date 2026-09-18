package PruebaActualizar;

import Modelo.Activos;
import Controlador.ActivosDAO;
import java.util.Scanner;

public class PruebaActualizarActivos {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        ActivosDAO dao = new ActivosDAO();
        Activos act = new Activos();

        System.out.println("=== ACTUALIZAR ACTIVO ===");

        System.out.print("Ingrese el ID del activo a modificar: ");
        act.setId_activos(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo codigo activo: ");
        act.setCodigo_act(leer.nextLine());

        System.out.print("Nuevo nombre activo: ");
        act.setNombre_activos(leer.nextLine());

        System.out.print("Nuevo valor: ");
        act.setValor(leer.nextLine());

        System.out.print("Nueva fecha adquisicion (YYYY-MM-DD): ");
        act.setFecha_adquma(leer.nextLine());

        System.out.print("Nueva fecha devolucion (YYYY-MM-DD o vacio si no aplica): ");
        act.setFecha_devolucion(leer.nextLine());

        System.out.print("Nueva vida util en anos: ");
        act.setVida_util(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva descripcion: ");
        act.setDescripcion(leer.nextLine());

        System.out.print("Nuevo ID Estado Activo (Debe existir): ");
        act.setEstado_Activo_idEstado_Activo(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo ID Categoria (Debe existir): ");
        act.setCategorias_idCategorias(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo ID Proveedor (Debe existir): ");
        act.setProveedores_idProveedores(Integer.parseInt(leer.nextLine()));

        if (dao.actualizar(act)) {
            System.out.println("Activo actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID del activo.");
        }

        leer.close();
    }
}