package Servicio;

import Modelo.Personal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/** Reglas de negocio para los datos de una persona. */
public final class ValidacionPersonal {

    private static final String SOLO_LETRAS = "[\\p{L} ]+";

    private ValidacionPersonal() { }

    public static void normalizar(Personal persona) {
        persona.setNombre(NormalizadorTexto.mayusculas(persona.getNombre()));
        persona.setApellidos(NormalizadorTexto.mayusculas(persona.getApellidos()));
        persona.setDireccion(NormalizadorTexto.mayusculas(persona.getDireccion()));
        persona.setEmail(persona.getEmail() == null ? "" : persona.getEmail());
        persona.setTelefono(limpio(persona.getTelefono()));
        persona.setIdentificacion(limpio(persona.getIdentificacion()));
        persona.setObservaciones(NormalizadorTexto.mayusculas(persona.getObservaciones()));
    }

    public static String validar(Personal persona, String tipoDocumento, boolean validarClave) {
        String nombre = limpio(persona.getNombre());
        String apellidos = limpio(persona.getApellidos());
        String email = limpio(persona.getEmail());
        String telefono = limpio(persona.getTelefono());
        String direccion = limpio(persona.getDireccion());
        String identificacion = limpio(persona.getIdentificacion());

        if (!nombre.matches(SOLO_LETRAS) || nombre.length() < 2 || nombre.length() > 45) {
            return "El nombre debe tener entre 2 y 45 letras.";
        }
        if (!apellidos.matches(SOLO_LETRAS) || apellidos.length() < 2 || apellidos.length() > 45) {
            return "Los apellidos deben tener entre 2 y 45 letras.";
        }
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$") || email.length() < 6 || email.length() > 45) {
            return "Ingresa un correo electrónico válido entre 6 y 45 caracteres.";
        }
        if (!telefono.matches("\\d{7,15}")) {
            return "El teléfono debe contener entre 7 y 15 dígitos.";
        }
        if (!direccion.matches("[\\p{L}0-9#.,/\\- ]+") || direccion.length() < 5 || direccion.length() > 45) {
            return "La dirección debe tener entre 5 y 45 caracteres.";
        }
        String errorDocumento = validarDocumento(identificacion, tipoDocumento);
        if (errorDocumento != null) return errorDocumento;
        if (!fechaValida(persona.getFecha_contratacion())) {
            return "Ingresa una fecha de contratación válida que no sea futura.";
        }
        if (limpio(persona.getObservaciones()).length() > 200) {
            return "Las observaciones no pueden superar 200 caracteres.";
        }
        String clave = persona.getClave();
        if (validarClave && (clave == null || clave.length() < 8 || clave.length() > 20)) {
            return "La clave debe tener entre 8 y 20 caracteres.";
        }
        return null;
    }

    private static String validarDocumento(String numero, String tipoDocumento) {
        if (limpio(tipoDocumento).isEmpty()) return "Selecciona un tipo de documento válido.";
        if (numero.isEmpty()) return "Ingresa el número de documento.";
        String tipo = normalizar(tipoDocumento);
        if (tipo.contains("tarjeta") || tipo.matches(".*\\bti\\b.*")) {
            return numero.matches("\\d{10,11}") ? null : "La tarjeta de identidad debe tener 10 u 11 dígitos.";
        }
        if (tipo.contains("extranjer") || tipo.matches(".*\\bce\\b.*")) {
            return numero.matches("\\d{6,10}") ? null : "La cédula de extranjería debe tener entre 6 y 10 dígitos.";
        }
        if (tipo.contains("nit")) {
            return numero.matches("\\d{9,10}") ? null : "El NIT debe tener 9 o 10 dígitos.";
        }
        if (tipo.contains("cedula") || tipo.matches(".*\\bcc\\b.*")) {
            return numero.matches("\\d{6,10}") ? null : "La cédula debe tener entre 6 y 10 dígitos.";
        }
        return numero.matches("\\d{5,20}") ? null : "El documento debe contener solo números y tener entre 5 y 20 dígitos.";
    }

    private static boolean fechaValida(String fecha) {
        try {
            return !LocalDate.parse(limpio(fecha)).isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static String limpio(String valor) { return valor == null ? "" : valor.trim(); }

    private static String titulo(String valor) {
        String texto = limpio(valor).replaceAll("\\s+", " ").toLowerCase(Locale.forLanguageTag("es-CO"));
        if (texto.isEmpty()) return texto;
        String[] palabras = texto.split(" ");
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < palabras.length; i++) {
            String palabra = palabras[i];
            if (i > 0 && (palabra.equals("de") || palabra.equals("del") || palabra.equals("la")
                    || palabra.equals("las") || palabra.equals("los") || palabra.equals("y"))) {
                // Conectores en minúscula para conservar una presentación natural.
            } else if (!palabra.isEmpty()) {
                palabra = palabra.substring(0, 1).toUpperCase(Locale.forLanguageTag("es-CO")) + palabra.substring(1);
            }
            if (i > 0) resultado.append(' ');
            resultado.append(palabra);
        }
        return resultado.toString();
    }

    private static String normalizar(String valor) {
        return Normalizer.normalize(limpio(valor), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
    }
}
