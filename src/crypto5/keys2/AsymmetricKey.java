package crypto5.keys2;

import util.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class AsymmetricKey {
    private static KeyPair keyPair;


    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        generateKeyPair();

        encrypt();

        decrypt();
    }

    private static void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048); // Key size

        keyPair = kpg.generateKeyPair();

        System.out.println(String.format("Generated a private key: %s",
                Util.toHex(keyPair.getPrivate().getEncoded())));
        System.out.println(String.format("Generated a public  key: %s",
                Util.toHex(keyPair.getPublic().getEncoded())));
    }

    private static void encrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] data = "1234567890123456".getBytes();

        Cipher rsa = Cipher.getInstance("RSA");

        rsa.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] encrypted = rsa.doFinal(data);

        System.out.println(String.format("Encrypted version of \"%s\" with generated RSA key:\n%s",
                new String(data),
                breakStringAt(Util.toHex(encrypted), 64)));
    }

    private static void decrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] data = "1234567890123456".getBytes();

        Cipher rsa = Cipher.getInstance("RSA");

        rsa.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] encrypted = rsa.doFinal(data);

        System.out.println(String.format("Encrypted version of \"%s\" with generated RSA key:\n%s",
                new String(data),
                breakStringAt(Util.toHex(encrypted), 64)));

        rsa.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] decrypted = rsa.doFinal(encrypted);
        System.out.println(String.format("Decrypted: %s",
                new String(decrypted)));

    }

    private static void combined() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        // Load or generate keypair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        keyPair = kpg.generateKeyPair();

        byte[] data = "input".getBytes("UTF-8");

        // Set up cipher for encryption
        Cipher rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        byte[] encrypted = rsa.doFinal(data);

        // Set up cipher for decryption
        rsa.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] decrypted = rsa.doFinal(encrypted);
        System.out.println(String.format("Decrypted: %s",
                new String(decrypted)));

    }


    private static String breakStringAt(String input, int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= input.length() / size; i++) {
            sb.append(input.substring(i * size, Math.min((i + 1) * size, input.length()))).append("\n");
        }
        return sb.toString();
    }



}
