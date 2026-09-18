package PruebaConsultar;

import Modelo.Mantenimiento;
import Controlador.MantenimientoDAO;
import java.util.List;

public class PruebaConsultarMantenimiento {

    public static void main(String[] args) {

        MantenimientoDAO dao = new MantenimientoDAO();

        System.out.println("=== LISTADO DE MANTENIMIENTOS ===");

        List<Mantenimiento> lista = dao.consultar();

        if (lista.isEmpty()) {
            System.out.println("No hay mantenimientos registrados.");
        } else {
            for (Mantenimiento m : lista) {
                System.out.println("ID: " + m.getId_mantenimiento()
                        + " | Fecha: " + m.getFecha_mante()
                        + " | Costo: " + m.getCosto()
                        + " | Descripcion: " + m.getDescripcion()
                        + " | ID Activo: " + m.getActivos_id_activos()
                        + " | ID Proveedor: " + m.getProveedores_idProveedores());
            }
        }
    }
}