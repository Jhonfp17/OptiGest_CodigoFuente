import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest {

    private static final String LOGIN_URL =
            "https://zooming-smile-production-a861.up.railway.app/Vista/InicioSesion.jsp";

    @Test
    public void loginDocumentoInexistente() throws Exception {

        String documento = "999999999999999";
        String password = "ClavePrueba123";

        String parametros =
                "usuario=" + URLEncoder.encode(documento, StandardCharsets.UTF_8)
                + "&pass=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        URL url = new URL(LOGIN_URL);

        HttpURLConnection conexion =
                (HttpURLConnection) url.openConnection();

        conexion.setRequestMethod("POST");
        conexion.setDoOutput(true);
        conexion.setInstanceFollowRedirects(false);

        conexion.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
        );

        try (OutputStream salida = conexion.getOutputStream()) {
            salida.write(parametros.getBytes(StandardCharsets.UTF_8));
        }

        int codigo = conexion.getResponseCode();

        InputStream flujo = codigo >= 400
                ? conexion.getErrorStream()
                : conexion.getInputStream();

        String respuesta = "";

        if (flujo != null) {
            try (BufferedReader lector = new BufferedReader(
                    new InputStreamReader(flujo, StandardCharsets.UTF_8))) {

                String linea;

                while ((linea = lector.readLine()) != null) {
                    respuesta += linea;
                }
            }
        }

        Assert.assertEquals(codigo, 200);

        Assert.assertTrue(
                respuesta.contains("El documento no existe"),
                "No apareció el mensaje esperado."
        );

        conexion.disconnect();
    }

    @Test
    public void loginClaveIncorrecta() throws Exception {

        String documento = "1000222333";
        String password = "ClaveIncorrecta123";

        String parametros =
                "usuario=" + URLEncoder.encode(documento, StandardCharsets.UTF_8)
                + "&pass=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        URL url = new URL(LOGIN_URL);

        HttpURLConnection conexion =
                (HttpURLConnection) url.openConnection();

        conexion.setRequestMethod("POST");
        conexion.setDoOutput(true);
        conexion.setInstanceFollowRedirects(false);

        conexion.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
        );

        try (OutputStream salida = conexion.getOutputStream()) {
            salida.write(parametros.getBytes(StandardCharsets.UTF_8));
        }

        int codigo = conexion.getResponseCode();

        InputStream flujo = codigo >= 400
                ? conexion.getErrorStream()
                : conexion.getInputStream();

        String respuesta = "";

        if (flujo != null) {
            try (BufferedReader lector = new BufferedReader(
                    new InputStreamReader(flujo, StandardCharsets.UTF_8))) {

                String linea;

                while ((linea = lector.readLine()) != null) {
                    respuesta += linea;
                }
            }
        }

        Assert.assertEquals(codigo, 200);

        Assert.assertTrue(
                respuesta.contains("Clave incorrecta"),
                "No apareció el mensaje de contraseña incorrecta."
        );

        conexion.disconnect();
    }

    @Test
    public void loginExitoso() throws Exception {

        String documento = "1000222333";
        String password = "12345";

        String parametros =
                "usuario=" + URLEncoder.encode(documento, StandardCharsets.UTF_8)
                + "&pass=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        URL url = new URL(LOGIN_URL);

        HttpURLConnection conexion =
                (HttpURLConnection) url.openConnection();

        conexion.setRequestMethod("POST");
        conexion.setDoOutput(true);
        conexion.setInstanceFollowRedirects(false);

        conexion.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
        );

        try (OutputStream salida = conexion.getOutputStream()) {
            salida.write(parametros.getBytes(StandardCharsets.UTF_8));
        }

        int codigo = conexion.getResponseCode();

        String ubicacion = conexion.getHeaderField("Location");

        System.out.println("Código HTTP: " + codigo);
        System.out.println("Redirección: " + ubicacion);

        Assert.assertEquals(
                codigo,
                302,
                "El login correcto no produjo una redirección."
        );

        Assert.assertNotNull(
                ubicacion,
                "No se recibió la dirección de redirección."
        );

        Assert.assertTrue(
                ubicacion.contains("Panel"),
                "No se redirigió a ningún panel."
        );

        conexion.disconnect();
    }
}
