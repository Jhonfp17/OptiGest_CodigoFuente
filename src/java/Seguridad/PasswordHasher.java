package Seguridad;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Utilidad para almacenar y validar claves sin conservar el texto original. */
public final class PasswordHasher {
    private static final String PREFIJO = "pbkdf2_sha256";
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACIONES = 120000;
    private static final int TAMANO_SAL = 16;
    private static final int TAMANO_HASH_BITS = 256;

    private PasswordHasher() { }

    public static String proteger(String clave) {
        if (clave == null || clave.isEmpty()) {
            throw new IllegalArgumentException("La clave no puede estar vacia.");
        }
        byte[] sal = new byte[TAMANO_SAL];
        new SecureRandom().nextBytes(sal);
        byte[] hash = derivar(clave.toCharArray(), sal, ITERACIONES);
        return PREFIJO + "$" + ITERACIONES + "$"
                + Base64.getEncoder().encodeToString(sal) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String clave, String almacenada) {
        if (clave == null || almacenada == null || almacenada.isEmpty()) return false;
        if (!esHash(almacenada)) {
            return MessageDigest.isEqual(clave.getBytes(StandardCharsets.UTF_8), almacenada.getBytes(StandardCharsets.UTF_8));
        }
        try {
            String[] partes = almacenada.split("\\$", -1);
            int iteraciones = Integer.parseInt(partes[1]);
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            return MessageDigest.isEqual(derivar(clave.toCharArray(), sal, iteraciones), esperado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean esHash(String valor) {
        return valor != null && valor.startsWith(PREFIJO + "$") && valor.split("\\$", -1).length == 4;
    }

    private static byte[] derivar(char[] clave, byte[] sal, int iteraciones) {
        PBEKeySpec especificacion = new PBEKeySpec(clave, sal, iteraciones, TAMANO_HASH_BITS);
        try {
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(especificacion).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No fue posible proteger la clave.", e);
        } finally {
            especificacion.clearPassword();
        }
    }
}
