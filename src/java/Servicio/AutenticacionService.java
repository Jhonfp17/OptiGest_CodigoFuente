package Servicio;

import Controlador.PersonalDAO;
import Modelo.Personal;
import Seguridad.PasswordHasher;
import java.time.LocalDateTime;

/**
 * Centraliza las reglas de autenticacion utilizadas por la web y por la API.
 * Las consultas permanecen en PersonalDAO.
 */
public class AutenticacionService {

    private final PersonalDAO dao;

    public AutenticacionService() {
        this(new PersonalDAO());
    }

    AutenticacionService(PersonalDAO dao) {
        this.dao = dao;
    }

    public ResultadoAutenticacion autenticar(String identificacion, String clave) {
        if (estaVacio(identificacion) || estaVacio(clave)) {
            return ResultadoAutenticacion.camposIncompletos();
        }

        Personal personal = dao.consultarPorIdentificacion(identificacion.trim());
        if (personal == null) {
            return ResultadoAutenticacion.usuarioNoEncontrado();
        }
        if (!personal.isPuede_acceder()) {
            return ResultadoAutenticacion.sinPermiso();
        }
        if (!PasswordHasher.verificar(clave.trim(), personal.getClave())) {
            return ResultadoAutenticacion.claveIncorrecta();
        }
        if (personal.isDebeCambiarClave() && personal.getClaveTemporalExpiraEn() != null
                && !personal.getClaveTemporalExpiraEn().isAfter(LocalDateTime.now())) {
            return ResultadoAutenticacion.claveTemporalVencida();
        }

        // Conserva la migracion de claves antiguas que ya hacia InicioSesion.
        if (!PasswordHasher.esHash(personal.getClave())) {
            dao.actualizarClavePorId(personal.getIdPersonal(), clave.trim());
        }

        personal.setClave(null);
        return ResultadoAutenticacion.exitoso(personal);
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public static final class ResultadoAutenticacion {

        public enum Estado {
            EXITOSO,
            CAMPOS_INCOMPLETOS,
            USUARIO_NO_ENCONTRADO,
            SIN_PERMISO,
            CLAVE_INCORRECTA,
            CLAVE_TEMPORAL_VENCIDA
        }

        private final Estado estado;
        private final Personal personal;

        private ResultadoAutenticacion(Estado estado, Personal personal) {
            this.estado = estado;
            this.personal = personal;
        }

        public static ResultadoAutenticacion exitoso(Personal personal) {
            return new ResultadoAutenticacion(Estado.EXITOSO, personal);
        }

        public static ResultadoAutenticacion camposIncompletos() {
            return new ResultadoAutenticacion(Estado.CAMPOS_INCOMPLETOS, null);
        }

        public static ResultadoAutenticacion usuarioNoEncontrado() {
            return new ResultadoAutenticacion(Estado.USUARIO_NO_ENCONTRADO, null);
        }

        public static ResultadoAutenticacion sinPermiso() {
            return new ResultadoAutenticacion(Estado.SIN_PERMISO, null);
        }

        public static ResultadoAutenticacion claveIncorrecta() {
            return new ResultadoAutenticacion(Estado.CLAVE_INCORRECTA, null);
        }

        public static ResultadoAutenticacion claveTemporalVencida() {
            return new ResultadoAutenticacion(Estado.CLAVE_TEMPORAL_VENCIDA, null);
        }

        public Estado getEstado() {
            return estado;
        }

        public Personal getPersonal() {
            return personal;
        }

        public boolean esExitoso() {
            return estado == Estado.EXITOSO;
        }
    }
}
