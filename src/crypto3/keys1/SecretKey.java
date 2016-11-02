package crypto3.keys1;

import util.Util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class SecretKey {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Keys come in many formats:
        random();

        fromMaterial();

        fromPassword();
    }

    private static void random() throws NoSuchAlgorithmException {
        KeyGenerator aesGenerator = KeyGenerator.getInstance("AES");

        javax.crypto.SecretKey key = aesGenerator.generateKey();

        System.out.println(String.format("Generated secret key: %s", DatatypeConverter.printHexBinary(key.getEncoded())));
        // That gives us a 32 character, so 16 byte = 128 bit sysmmetric key.

    }

    private static void fromMaterial() {
        Key key = new SecretKeySpec(
                new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
                "AES");
        System.out.println(String.format("secret key from 0 input: %s", Util.toHex(key.getEncoded())));

    }

    private static void fromPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA1" );

        PBEKeySpec spec = new PBEKeySpec( "nauticalnonsense".toCharArray(), // the password
                new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08}, // a salt
                50, // iteration count, hardness of the function
                128 ); // key length, should match the kind of encryption you're trying to do
        Key key = skf.generateSecret(spec);
        System.out.println(String.format("secret key from \"nauticalnonsense\" using PBKDF2 with HmacSHA256: %s", Util.toHex(key.getEncoded())));

    }


}
