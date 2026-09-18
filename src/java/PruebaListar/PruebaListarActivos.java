package PruebaListar;

import Modelo.Activos;
import Controlador.ActivosDAO;
import java.util.Scanner;

public class PruebaListarActivos {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        ActivosDAO dao = new ActivosDAO();

        System.out.println("=== BUSCAR ACTIVO POR ID ===");

        System.out.print("Ingrese el ID del activo a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Activos a = dao.consultar(id);

        if (a != null) {
            System.out.println("ID: " + a.getId_activos()
                    + " | Codigo: " + a.getCodigo_act()
                    + " | Nombre: " + a.getNombre_activos()
                    + " | Valor: " + a.getValor()
                    + " | Fecha adquisicion: " + a.getFecha_adquma()
                    + " | Fecha devolucion: " + a.getFecha_devolucion()
                    + " | Vida util: " + a.getVida_util()
                    + " | Descripcion: " + a.getDescripcion()
                    + " | ID Estado: " + a.getEstado_Activo_idEstado_Activo()
                    + " | ID Categoria: " + a.getCategorias_idCategorias()
                    + " | ID Proveedor: " + a.getProveedores_idProveedores());
        } else {
            System.out.println("No se encontro activo con ID: " + id);
        }

        leer.close();
    }
}