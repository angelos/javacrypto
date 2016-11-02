package crypto2.hashes3;

import util.Util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class KeyedHash {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[][] data = new byte[][]{"Who lives in a pineapple under the sea?".getBytes(),
                                     "Absorbent and yellow and porous is he!".getBytes()};
        System.out.println(String.format("Input data:\n  %s\n  %s",
                Util.toHex(data[0]), Util.toHex(data[1])));

        Mac mac = Mac.getInstance("HmacSHA256");

        Key secret = new SecretKeySpec("nauticalnonsense".getBytes(), "HmacSHA256");
        mac.init(secret);

        mac.update(data[0]);
        byte[] digest = mac.doFinal(data[1]);

        System.out.println(String.format("Hash for password \"nauticalnonsense\":\n  %s",
                Util.toHex(digest)));

        // Like SimpleHashes, you can reset Macs. Let's try with a different key
        mac.reset();

        secret = new SecretKeySpec("floplikeafish".getBytes(), "HmacSHA256");
        try {
            mac.init(secret);
        } catch (InvalidKeyException e) {
            // We ended up with key that's not valid for this kind of MAC. Won't happen in our case.
            return;
        }

        mac.update(data[0]);
        digest = mac.doFinal(data[1]);

        System.out.println(String.format("Hash for password \"floplikeafish\":\n  %s", Util.toHex(digest)));

    }

}
