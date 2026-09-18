package Modelo;

public class Programacion_Personal {

    private int idProgramacion_Personal;
    private String descripcion_programacion;
    private String fecha_desde;
    private String fecha_hasta;
    private int Dias_idDias;
    private int Personal_id_personal;
    private int Horarios_id_horarios;

    public int getIdProgramacion_Personal() {
        return idProgramacion_Personal;
    }

    public void setIdProgramacion_Personal(int idProgramacion_Personal) {
        this.idProgramacion_Personal = idProgramacion_Personal;
    }

    public String getDescripcion_programacion() {
        return descripcion_programacion;
    }

    public void setDescripcion_programacion(String descripcion_programacion) {
        this.descripcion_programacion = descripcion_programacion;
    }

    public String getFecha_desde() {
        return fecha_desde;
    }

    public void setFecha_desde(String fecha_desde) {
        this.fecha_desde = fecha_desde;
    }

    public String getFecha_hasta() {
        return fecha_hasta;
    }

    public void setFecha_hasta(String fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }

    public int getDias_idDias() {
        return Dias_idDias;
    }

    public void setDias_idDias(int Dias_idDias) {
        this.Dias_idDias = Dias_idDias;
    }

    public int getPersonal_id_personal() {
        return Personal_id_personal;
    }

    public void setPersonal_id_personal(int Personal_id_personal) {
        this.Personal_id_personal = Personal_id_personal;
    }

    public int getHorarios_id_horarios() {
        return Horarios_id_horarios;
    }

    public void setHorarios_id_horarios(int Horarios_id_horarios) {
        this.Horarios_id_horarios = Horarios_id_horarios;
    }
}