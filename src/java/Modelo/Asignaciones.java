package Modelo;

public class Asignaciones {

    private int id_asignaciones;
    private String fecha_asignacion;
    private String fecha_devolucion;
    private String observaciones;
    private int Personal_id_personal;
    private int Activos_id_activos;

    public int getId_asignaciones() {
        return id_asignaciones;
    }

    public void setId_asignaciones(int id_asignaciones) {
        this.id_asignaciones = id_asignaciones;
    }

    public String getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(String fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }

    public String getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(String fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getPersonal_id_personal() {
        return Personal_id_personal;
    }

    public void setPersonal_id_personal(int Personal_id_personal) {
        this.Personal_id_personal = Personal_id_personal;
    }

    public int getActivos_id_activos() {
        return Activos_id_activos;
    }

    public void setActivos_id_activos(int Activos_id_activos) {
        this.Activos_id_activos = Activos_id_activos;
    }
}