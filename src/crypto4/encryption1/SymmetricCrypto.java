package crypto4.encryption1;

import util.Util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class SymmetricCrypto {
    private static SecretKeySpec key;

    // I don't care about exceptions that won't show up because we use the standards
    public static void main(String[] args) throws Exception {
        setup();
        separator();

        blockCipher1();
        separator();
        blockCipher2();

        separator();
        padding1();

        separator();
        operationMode1();
        separator();
        operationMode2();
        separator();
        for (int i = 0; i < 5; i++) {
            operationMode2();
        }
        separator();
        operationMode2Decrypt();
        separator();
        operationMode2Decrypt2();

//        combined();
    }

    private static void setup() {
        // We don't care where they key comes from now, we'll only use it to show encryption.
        key = new SecretKeySpec(new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
                "AES");
        System.out.println(String.format("secret key from 0 input: %s", DatatypeConverter.printHexBinary(key.getEncoded())));

    }

    private static void blockCipher1() throws Exception {
        // Most ciphers we use are actually block ciphers: take data the size of the key's block size, and transform
        // it to another block of the same size.

        // Let's start with one full block
        byte[] data = "1234567890123456".getBytes(); // exactly 16 bytes or 128 bits long.
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data);
        System.out.println(String.format("Encrypted version of \"%s\" with AES key 0: %s",
                new String(data),
                Util.toHex(encrypted)));

        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(encrypted);

        System.out.println(String.format("Decrypted: %s", new String(decrypted)));
        // We can easily encrypt and decrypt this block
    }

    private static void blockCipher2() throws Exception {
        try {
            System.out.println("Trying to encrypt 15 bytes...");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipher.doFinal("123456789012345".getBytes()); // that's one byte to few
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace(System.out);
            // The system complains about "Input length not multiple of 16 bytes"; block
            // ciphers only work on full blocks
        }
    }

    private static void padding1() throws Exception{
        // We use PKCS5 padding to fill out our data to a full block size.
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] data = "123456789012345".getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data);
        System.out.println(String.format("Encrypted version of \"%s\" with AES key 0: %s",
                new String(data),
                Util.toHex(encrypted)));

        cipher.init(Cipher.DECRYPT_MODE, key);
        System.out.println(String.format("Decrypted :\n%s", new String(cipher.doFinal(encrypted))));

    }

    private static void operationMode1() throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        byte[] data = "1234567890123456123456789012345612345678901234561234567890123456"
                .getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data);
        System.out.println(String.format("Encrypted version of \"%s\" with AES key 0:\n%s",
                new String(data),
                breakStringAt(Util.toHex(encrypted), 32)));
        // When we try to encrypt more data, we notice that a block cipher encrypts each
        // block independently
    }

    private static void operationMode2() throws Exception {
        // So we introduce CBC (Cipher Block Chaining) as an operation mode
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        byte[] data = "1234567890123456123456789012345612345678901234561234567890123456"
                .getBytes(); // just four times the same text
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data);
        System.out.println(String.format("Encrypted version of \"%s\" CBC:\n%s",
                new String(data),
                breakStringAt(Util.toHex(encrypted), 32)));
        // Try running this method multiple times; you'll see that it results in
        // _different_ ciphertexts each time.
    }

    private static void operationMode2Decrypt() throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        byte[] data = "1234567890123456123456789012345612345678901234561234567890123456"
                .getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data);
        System.out.println(String.format("Encrypted version of \"%s\" CBC:\n%s",
                new String(data), breakStringAt(Util.toHex(encrypted), 32)));

        try {
            System.out.println("Decrypting...");
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch (InvalidKeyException e) {
            e.printStackTrace(System.out);
            // When decrypting, the init() methods complains about a "missing parameter."
            // That's an initialization vector; encryption chooses one at random, and
            // you can actually get it using cipher.getIV();
        }
    }

    private static void operationMode2Decrypt2() throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        byte[] data = "1234567890123456123456789012345612345678901234561234567890123456"
                .getBytes();

        IvParameterSpec iv = new IvParameterSpec(
                new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                            0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F});

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(data);
        System.out.println(String.format("Encrypted version of \"%s\" with AES key 0:\n%s",
                new String(data),
                breakStringAt(Util.toHex(encrypted), 32)));

        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        System.out.println(String.format("Decrypted :\n%s", new String(cipher.doFinal(encrypted))));
    }

    private static void combined() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        // generate or import key
        Key key = new SecretKeySpec(new byte[16], "AES");

        // Set up cipher and data; provide algorithm/mode/padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] data = "input".getBytes("UTF-8");

        // Set up initialization vector
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);

        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(data);
        // use update() for more data

        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decrypted = cipher.doFinal(encrypted);
        System.out.println(new String(decrypted));
    }

    private static void separator() {
        System.out.print("\n-----\n");
    }

    private static String breakStringAt(String input, int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= input.length() / size; i++) {
            sb.append(input.substring(i * size, Math.min((i + 1) * size, input.length()))).append("\n");
        }
        return sb.toString();
    }


}
