package Servicio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SmsService {

    private static final String VERIFY_URL = "https://verify.twilio.com/v2/Services/%s/Verifications";
    private static final String VERIFY_CHECK_URL = "https://verify.twilio.com/v2/Services/%s/VerificationCheck";

    public boolean enviarCodigo(String telefono) throws IOException {
        Configuracion configuracion = cargarConfiguracion();
        String destino = normalizarTelefono(telefono);
        String datos = "To=" + codificar(destino) + "&Channel=sms";
        HttpURLConnection conexion = abrirConexion(
                String.format(VERIFY_URL, configuracion.verifyServiceSid),
                configuracion
        );

        escribir(conexion, datos);

        int estado = conexion.getResponseCode();
        if (estado >= 200 && estado < 300) {
            return true;
        }

        throw new IOException("No se pudo enviar SMS. Estado " + estado + ": " + leerRespuesta(conexion));
    }

    public boolean verificarCodigo(String telefono, String codigo) throws IOException {
        Configuracion configuracion = cargarConfiguracion();
        String destino = normalizarTelefono(telefono);
        String datos = "To=" + codificar(destino) + "&Code=" + codificar(codigo);
        HttpURLConnection conexion = abrirConexion(
                String.format(VERIFY_CHECK_URL, configuracion.verifyServiceSid),
                configuracion
        );

        escribir(conexion, datos);

        int estado = conexion.getResponseCode();
        String respuesta = leerRespuesta(conexion);

        if (estado < 200 || estado >= 300) {
            throw new IOException("No se pudo verificar el codigo. Estado " + estado + ": " + respuesta);
        }

        return respuesta.contains("\"status\":\"approved\"");
    }

    public String normalizarTelefono(String telefono) {
        String limpio = telefono == null ? "" : telefono.replaceAll("[^0-9+]", "");

        if (limpio.startsWith("+")) {
            return limpio;
        }

        String digitos = limpio.replaceAll("[^0-9]", "");

        if (digitos.startsWith("00")) {
            return "+" + digitos.substring(2);
        }

        if (digitos.startsWith("57") && digitos.length() == 12) {
            return "+" + digitos;
        }

        if (digitos.length() == 10 && digitos.startsWith("3")) {
            return "+57" + digitos;
        }

        return "+" + digitos;
    }

    private Configuracion cargarConfiguracion() throws IOException {
        String accountSid = configurar("TWILIO_ACCOUNT_SID", "twilio.account.sid");
        String authToken = configurar("TWILIO_AUTH_TOKEN", "twilio.auth.token");
        String verifyServiceSid = configurar("TWILIO_VERIFY_SERVICE_SID", "twilio.verify.service.sid");

        if (vacio(accountSid) || vacio(authToken) || vacio(verifyServiceSid)
                || "TU_ACCOUNT_SID".equals(accountSid) || "TU_AUTH_TOKEN".equals(authToken)) {
            throw new IOException("Twilio Verify no esta configurado.");
        }

        return new Configuracion(accountSid, authToken, verifyServiceSid);
    }

    private HttpURLConnection abrirConexion(String urlServicio, Configuracion configuracion) throws IOException {
        URL url = new URL(urlServicio);
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("POST");
        conexion.setDoOutput(true);
        conexion.setConnectTimeout(10000);
        conexion.setReadTimeout(10000);
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conexion.setRequestProperty("Authorization",
                "Basic " + autenticar(configuracion.accountSid, configuracion.authToken));
        return conexion;
    }

    private void escribir(HttpURLConnection conexion, String datos) throws IOException {
        try (OutputStream salida = conexion.getOutputStream()) {
            salida.write(datos.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String configurar(String variableEntorno, String propiedad) {
        String valor = System.getenv(variableEntorno);
        if (!vacio(valor)) {
            return valor.trim();
        }

        valor = System.getProperty(propiedad);
        return valor == null ? "" : valor.trim();
    }

    private boolean vacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private String codificar(String valor) {
        return URLEncoder.encode(valor, StandardCharsets.UTF_8);
    }

    private String autenticar(String usuario, String clave) {
        String credenciales = usuario + ":" + clave;
        return Base64.getEncoder().encodeToString(credenciales.getBytes(StandardCharsets.UTF_8));
    }

    private String leerRespuesta(HttpURLConnection conexion) throws IOException {
        InputStream entrada = conexion.getErrorStream();
        if (entrada == null) {
            entrada = conexion.getInputStream();
        }

        try (BufferedReader lector = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) {
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = lector.readLine()) != null) {
                respuesta.append(linea);
            }
            return respuesta.toString();
        }
    }

    private static class Configuracion {

        private final String accountSid;
        private final String authToken;
        private final String verifyServiceSid;

        private Configuracion(String accountSid, String authToken, String verifyServiceSid) {
            this.accountSid = accountSid;
            this.authToken = authToken;
            this.verifyServiceSid = verifyServiceSid;
        }
    }
}
