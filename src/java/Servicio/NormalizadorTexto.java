package Servicio;

import java.util.Locale;

/** Normaliza los textos no sensibles que se muestran en el sistema. */
public final class NormalizadorTexto {
    private NormalizadorTexto() { }

    public static String mayusculas(String valor) {
        return valor == null ? "" : valor.trim().replaceAll("\\s+", " ").toUpperCase(Locale.forLanguageTag("es-CO"));
    }
}
