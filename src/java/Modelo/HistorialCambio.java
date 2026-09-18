package Modelo;

/** Entrada de auditoria inmutable creada por la base de datos. */
public class HistorialCambio {
    private int id;
    private String fecha;
    private String entidad;
    private int idRegistro;
    private String accion;
    private String datosAntes;
    private String datosDespues;
    private String usuarioBaseDatos;
    private String usuarioNombre;
    private String motivo;
    private String registroDescripcion;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }
    public int getIdRegistro() { return idRegistro; }
    public void setIdRegistro(int idRegistro) { this.idRegistro = idRegistro; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getDatosAntes() { return datosAntes; }
    public void setDatosAntes(String datosAntes) { this.datosAntes = datosAntes; }
    public String getDatosDespues() { return datosDespues; }
    public void setDatosDespues(String datosDespues) { this.datosDespues = datosDespues; }
    public String getUsuarioBaseDatos() { return usuarioBaseDatos; }
    public void setUsuarioBaseDatos(String usuarioBaseDatos) { this.usuarioBaseDatos = usuarioBaseDatos; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getRegistroDescripcion() { return registroDescripcion; }
    public void setRegistroDescripcion(String registroDescripcion) { this.registroDescripcion = registroDescripcion; }
}
