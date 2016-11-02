package crypto2.hashes1;

import util.Util;

import javax.xml.bind.DatatypeConverter;
import java.util.zip.CRC32;

public class Checksum {
    public static void main(String[] args) {
        byte[][] data = new byte[][]{"Who lives in a pineapple under the sea?".getBytes(),
                "Absorbent and yellow and porous is he!".getBytes()};

        System.out.println(String.format("Input data:\n  %s\n  %s", Util.toHex(data[0]), Util.toHex(data[1])));

        // Most hashes in Java a (1) created, (2) updated, and (3) asked for a result.
        CRC32 crc = new CRC32();
        crc.update(data[0]);
        crc.update(data[1]);
        long checksum = crc.getValue();

        System.out.println(String.format("Checksum:\n  %s", Long.toHexString(checksum)));

        // this is a small hash, and collissions are possible: http://preshing.com/20110504/hash-collision-probabilities/
        // However, a checksum is intended to protect against _accidental bit flips_, not _intentional tampering_.
    }
}
