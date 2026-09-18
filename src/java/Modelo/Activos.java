package Modelo;

public class Activos {

    private int id_activos;
    private String codigo_act;
    private String nombre_activos;
    private String valor;
    private String fecha_adquma;
    private String fecha_devolucion;
    private int vida_util;
    private String descripcion;
    private int Estado_Activo_idEstado_Activo;
    private int Categorias_idCategorias;
    private int Proveedores_idProveedores;
    private String estadoActual;

    public int getId_activos() {
        return id_activos;
    }

    public void setId_activos(int id_activos) {
        this.id_activos = id_activos;
    }

    public String getCodigo_act() {
        return codigo_act;
    }

    public void setCodigo_act(String codigo_act) {
        this.codigo_act = codigo_act;
    }

    public String getNombre_activos() {
        return nombre_activos;
    }

    public void setNombre_activos(String nombre_activos) {
        this.nombre_activos = nombre_activos;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getFecha_adquma() {
        return fecha_adquma;
    }

    public void setFecha_adquma(String fecha_adquma) {
        this.fecha_adquma = fecha_adquma;
    }

    public String getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(String fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public int getVida_util() {
        return vida_util;
    }

    public void setVida_util(int vida_util) {
        this.vida_util = Math.max(0, vida_util);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado_Activo_idEstado_Activo() {
        return Estado_Activo_idEstado_Activo;
    }

    public void setEstado_Activo_idEstado_Activo(int Estado_Activo_idEstado_Activo) {
        this.Estado_Activo_idEstado_Activo = Estado_Activo_idEstado_Activo;
    }

    public int getCategorias_idCategorias() {
        return Categorias_idCategorias;
    }

    public void setCategorias_idCategorias(int Categorias_idCategorias) {
        this.Categorias_idCategorias = Categorias_idCategorias;
    }

    public int getProveedores_idProveedores() {
        return Proveedores_idProveedores;
    }

    public void setProveedores_idProveedores(int Proveedores_idProveedores) {
        this.Proveedores_idProveedores = Proveedores_idProveedores;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }
}
