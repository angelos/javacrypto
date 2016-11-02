package crypto2.hashes2;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleHash {
    public static void main(String[] args) {
        byte[][] data = new byte[][] {"Who lives in a pineapple under the sea?".getBytes(),
                                    "Absorbent and yellow and porous is he!".getBytes()};

        System.out.println(String.format("Input data:\n  %s\n  %s", DatatypeConverter.printHexBinary(data[0]), DatatypeConverter.printHexBinary(data[1])));

        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            // Won't happen. Mandatory hashing algorithms are,
            // - MD5
            // - SHA-1
            // - SHA-256
            return;
        }

        sha.update(data[0]);
        try {
            // Some hashes support getting intermediate results by cloning a hash. The only way
            // to find out whether cloning is supported is by catching the CloneNotSupportedException
            MessageDigest shaIntermediate = (MessageDigest) sha.clone();
            byte[] intermediateHash = shaIntermediate.digest();
            System.out.println(String.format("Intermediate hash:\n  %s", DatatypeConverter.printHexBinary(intermediateHash)));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        sha.update(data[1]);
        byte[] finalHash = sha.digest();
        System.out.println(String.format("Final hash:\n  %s", DatatypeConverter.printHexBinary(finalHash)));
        // Note that the outputs are always of the same length: 64 hex characters, or 256 bits

        System.out.println("-----");

        byte[][] smallData = new byte[][] {{0x00, 0x00, 0x00, 0x00},
                                           {0x00, 0x00, 0x00, 0x01}};

        sha.reset(); // These digests are heavy-weight objects, so they tend to stick around.
        // You can also feed data into the digest(byte[]) method. That's identical to calling update(byte[]), and then digest().
        System.out.println(String.format("Hash for %s: %s", DatatypeConverter.printHexBinary(smallData[0]), DatatypeConverter.printHexBinary(sha.digest(smallData[0]))));
        System.out.println(String.format("Hash for %s: %s", DatatypeConverter.printHexBinary(smallData[1]), DatatypeConverter.printHexBinary(sha.digest(smallData[1]))));
        // Notice that single-bit differences make a huge difference in the output hash.
    }
}
