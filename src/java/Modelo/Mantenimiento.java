package Modelo;

public class Mantenimiento {

    private int id_mantenimiento;
    private String fecha_mante;
    private String costo;
    private String descripcion;
    private int Activos_id_activos;
    private int Proveedores_idProveedores;

    public int getId_mantenimiento() {
        return id_mantenimiento;
    }

    public void setId_mantenimiento(int id_mantenimiento) {
        this.id_mantenimiento = id_mantenimiento;
    }

    public String getFecha_mante() {
        return fecha_mante;
    }

    public void setFecha_mante(String fecha_mante) {
        this.fecha_mante = fecha_mante;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getActivos_id_activos() {
        return Activos_id_activos;
    }

    public void setActivos_id_activos(int Activos_id_activos) {
        this.Activos_id_activos = Activos_id_activos;
    }

    public int getProveedores_idProveedores() {
        return Proveedores_idProveedores;
    }

    public void setProveedores_idProveedores(int Proveedores_idProveedores) {
        this.Proveedores_idProveedores = Proveedores_idProveedores;
    }
}