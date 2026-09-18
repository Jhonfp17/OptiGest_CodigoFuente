package Modelo;

public class Horarios {

    private int id_horarios;
    private String descripcion;
    private String hora_ingreso;
    private String hora_salida;

    public int getId_horarios() {
        return id_horarios;
    }

    public void setId_horarios(int id_horarios) {
        this.id_horarios = id_horarios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora_ingreso() {
        return hora_ingreso;
    }

    public void setHora_ingreso(String hora_ingreso) {
        this.hora_ingreso = hora_ingreso;
    }

    public String getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }
}