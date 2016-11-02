package util;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by angelos on 01/11/2016.
 */
public class Util {

    public static String toHex(byte[] in) {
        return DatatypeConverter.printHexBinary(in);
    }
}
