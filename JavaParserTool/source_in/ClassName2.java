package com.dmy;

import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;

public class ClassName2 {

    public static String decodeStr(String p0) {
        byte[] var1 = Base64.decode(p0, Base64.DEFAULT);
        char[] var2 = new char[var1.length];
        int i = 0;
        byte code = ClassName4.getHashCode();
        do {
            var2[i] = (char) (ClassName1.xorOperation(code, var1[i]));
            i++;
        } while (i < var2.length);
        return new String(var2);
    }

    public static byte[] getCodeByte(InputStream p0) throws IOException {
        byte[] numArr = new byte[1];
        ClassName4.invokeMethod(p0.getClass(), ClassName2.decodeStr("msreadms").substring(2, 6), p0, new Class[] { byte[].class, int.class, int.class }, new Object[] { numArr, 0, 1 });
        int num = numArr[0];
        byte[] encCode = new byte[num];
        ClassName4.invokeMethod(p0.getClass(), ClassName2.decodeStr("msreadms").substring(2, 6), p0, new Class[] { byte[].class, int.class, int.class }, new Object[] { encCode, 0, num });
        return encCode;
    }
}
