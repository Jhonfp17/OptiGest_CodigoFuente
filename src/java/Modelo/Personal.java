package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Personal {

    private int idPersonal;
    private String nombre;
    private String apellidos;
    private String identificacion;
    private String email;
    private String telefono;
    private String direccion;
    private String clave;
    private String observaciones;
    private boolean puede_acceder;
    private boolean debeCambiarClave;
    private LocalDateTime claveTemporalExpiraEn;
    private String fecha_contratacion;
    private int Documento_id_documento;
    private int roles_idroles;
    private String tipo_acceso;
    private List<Integer> rolesAsignados = new ArrayList<>();
    private int Estado_Personal_id_estado;

    public int getIdPersonal() {
        return idPersonal;
    }

    public void setIdPersonal(int idPersonal) {
        this.idPersonal = idPersonal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isPuede_acceder() {
        return puede_acceder;
    }

    public void setPuede_acceder(boolean puede_acceder) {
        this.puede_acceder = puede_acceder;
    }

    public boolean isDebeCambiarClave() { return debeCambiarClave; }
    public void setDebeCambiarClave(boolean debeCambiarClave) { this.debeCambiarClave = debeCambiarClave; }
    public LocalDateTime getClaveTemporalExpiraEn() { return claveTemporalExpiraEn; }
    public void setClaveTemporalExpiraEn(LocalDateTime claveTemporalExpiraEn) { this.claveTemporalExpiraEn = claveTemporalExpiraEn; }

    public String getFecha_contratacion() {
        return fecha_contratacion;
    }

    public void setFecha_contratacion(String fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }

    public int getDocumento_id_documento() {
        return Documento_id_documento;
    }

    public void setDocumento_id_documento(int Documento_id_documento) {
        this.Documento_id_documento = Documento_id_documento;
    }

    public int getRoles_idroles() {
        return roles_idroles;
    }

    public void setRoles_idroles(int roles_idroles) {
        this.roles_idroles = roles_idroles;
    }

    public String getTipo_acceso() {
        return tipo_acceso;
    }

    public void setTipo_acceso(String tipo_acceso) {
        this.tipo_acceso = tipo_acceso;
    }

    public List<Integer> getRolesAsignados() { return rolesAsignados; }
    public void setRolesAsignados(List<Integer> rolesAsignados) {
        this.rolesAsignados = rolesAsignados == null ? new ArrayList<>() : new ArrayList<>(rolesAsignados);
    }

    public int getEstado_Personal_id_estado() {
        return Estado_Personal_id_estado;
    }

    public void setEstado_Personal_id_estado(int Estado_Personal_id_estado) {
        this.Estado_Personal_id_estado = Estado_Personal_id_estado;
    }

    public boolean esAdministrador() {
        return "ADMINISTRADOR".equalsIgnoreCase(tipo_acceso) || roles_idroles == 1;
    }
}
