package Modelo;

public class Roles {

    private int idRoles;
    private String descripcion_roles;
    private String tipo_acceso;

    public int getIdRoles() {
        return idRoles;
    }

    public void setIdRoles(int idRoles) {
        this.idRoles = idRoles;
    }

    public String getDescripcion_roles() {
        return descripcion_roles;
    }

    public void setDescripcion_roles(String descripcion_roles) {
        this.descripcion_roles = descripcion_roles;
    }

    public String getTipo_acceso() {
        return tipo_acceso;
    }

    public void setTipo_acceso(String tipo_acceso) {
        this.tipo_acceso = tipo_acceso;
    }
}