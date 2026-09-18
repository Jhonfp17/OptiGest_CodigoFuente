package PruebaInsertar;

import Modelo.Activos;
import Controlador.ActivosDAO;
import java.util.Scanner;

public class PruebaInsertarActivos {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        ActivosDAO dao = new ActivosDAO();
        Activos act = new Activos();

        System.out.println("=== REGISTRO DE ACTIVOS - OPTIGEST ===");

        System.out.print("Codigo del Activo: ");
        act.setCodigo_act(leer.nextLine());
        
        System.out.print("Nombre del Activo: ");
        act.setNombre_activos(leer.nextLine());

        System.out.print("Valor: ");
        act.setValor(leer.nextLine());

        System.out.print("Fecha Adquisicion (YYYY-MM-DD): ");
        act.setFecha_adquma(leer.nextLine());

        System.out.print("Fecha Devolucion (YYYY-MM-DD o vacio si no aplica): ");
        act.setFecha_devolucion(leer.nextLine());

        System.out.print("Vida Util en años: ");
        act.setVida_util(Integer.parseInt(leer.nextLine()));

        System.out.print("Descripcion: ");
        act.setDescripcion(leer.nextLine());

        System.out.println("\n--- DATOS DE RELACION (DEBEN EXISTIR EN BD) ---");

        System.out.print("ID Estado del Activo: ");
        act.setEstado_Activo_idEstado_Activo(Integer.parseInt(leer.nextLine()));

        System.out.print("ID Categoria: ");
        act.setCategorias_idCategorias(Integer.parseInt(leer.nextLine()));

        System.out.print("ID Proveedor: ");
        act.setProveedores_idProveedores(Integer.parseInt(leer.nextLine()));

        System.out.println("\nIntentando guardar...");

        if (dao.insertar(act)) {
            System.out.println("Activo guardado con exito.");
        } else {
            System.out.println("Error: No se pudo guardar el activo.");
            System.out.println("Verifica que los IDs de Estado, Categoria y Proveedor sean correctos.");
        }

        leer.close();
    }
}