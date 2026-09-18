package Servicio;

import Modelo.Activos;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/** Reglas de negocio para el registro y la edición de activos. */
public final class ValidacionActivo {
    private ValidacionActivo() { }

    public static String normalizarYValidar(Activos activo) {
        activo.setCodigo_act(limpiar(activo.getCodigo_act()).toUpperCase(Locale.ROOT));
        activo.setNombre_activos(NormalizadorTexto.mayusculas(activo.getNombre_activos()));
        activo.setDescripcion(NormalizadorTexto.mayusculas(activo.getDescripcion()));
        if (!activo.getCodigo_act().matches("[A-Z0-9-]{3,30}")) return "El código debe tener entre 3 y 30 caracteres: letras, números o guiones.";
        if (activo.getNombre_activos().length() < 3 || activo.getNombre_activos().length() > 30) return "El nombre del activo debe tener entre 3 y 30 caracteres.";
        try {
            if (new BigDecimal(limpiar(activo.getValor())).compareTo(BigDecimal.ZERO) <= 0) return "El valor del activo debe ser mayor que cero.";
        } catch (NumberFormatException e) { return "El valor del activo debe ser numérico."; }
        if (activo.getVida_util() < 1 || activo.getVida_util() > 20) return "La vida útil debe estar entre 1 y 20 años.";
        LocalDate adquisicion = fecha(activo.getFecha_adquma());
        if (adquisicion == null || adquisicion.isAfter(LocalDate.now())) return "La fecha de adquisición no puede ser futura.";
        if (!limpiar(activo.getFecha_devolucion()).isEmpty()) {
            LocalDate devolucion = fecha(activo.getFecha_devolucion());
            if (devolucion == null || devolucion.isBefore(adquisicion)) return "La fecha de devolución debe ser igual o posterior a la adquisición.";
        }
        if (activo.getDescripcion().length() > 200) return "La descripción no puede superar 200 caracteres.";
        return null;
    }

    private static LocalDate fecha(String valor) { try { return LocalDate.parse(limpiar(valor)); } catch (DateTimeParseException e) { return null; } }
    private static String limpiar(String valor) { return valor == null ? "" : valor.trim(); }
    private static String titulo(String valor) { String t=limpiar(valor).replaceAll("\\s+", " ").toLowerCase(Locale.forLanguageTag("es-CO")); return t.isEmpty()?t:t.substring(0,1).toUpperCase(Locale.forLanguageTag("es-CO"))+t.substring(1); }
}
