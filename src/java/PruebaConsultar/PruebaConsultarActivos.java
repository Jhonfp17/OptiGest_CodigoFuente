package PruebaConsultar;

import Modelo.Activos;
import Controlador.ActivosDAO;
import java.util.List;

public class PruebaConsultarActivos {

    public static void main(String[] args) {

        ActivosDAO dao = new ActivosDAO();

        System.out.println("=== LISTADO DE ACTIVOS ===");

        List<Activos> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay activos registrados.");
        } else {
            for (Activos a : lista) {
                System.out.println("ID: " + a.getId_activos()
                        + " | Codigo: " + a.getCodigo_act()
                        + " | Nombre: " + a.getNombre_activos()
                        + " | Valor: " + a.getValor()
                        + " | Vida util: " + a.getVida_util()
                        + " | ID Estado: " + a.getEstado_Activo_idEstado_Activo()
                        + " | ID Categoria: " + a.getCategorias_idCategorias()
                        + " | ID Proveedor: " + a.getProveedores_idProveedores());
            }
        }
    }
}