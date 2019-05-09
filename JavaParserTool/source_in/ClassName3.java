package com.dmy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ClassName3 {

    public static void eteDirectory(File file) {
        if (file.isDirectory()) {
            File[] childFiles = (File[]) ClassName0.invokeNoStaticMethod(file, new ClassName1().decodeStr("listFiles"), null);
            if (childFiles != null) {
                for (int i = 0; i < childFiles.length; i++) {
                    ClassName0.invokeNoStaticMethod(childFiles[i], new ClassName1().decodeStr("delete"), null);
                }
            }
        }
        ClassName0.invokeNoStaticMethod(file, new ClassName1().decodeStr("delete"), null);
    }

    public static byte[] getCodeByte(InputStream p0)throws IOException {
        byte[] numArr = new byte[1];
        p0.read(numArr, 0, 1);
        int num = numArr[0];
        byte[] encCode = new byte[num];
        p0.read(encCode, 0, num);
        return encCode;
    }

    public boolean isBluetoothCheck() {
        String blue = "android.bluetooth.BluetoothAdapter";
        try {
            Object[] nhb_args = null;
            String blueStr = new ClassName1().decodeStr(blue);
            Class cl = Class.forName(blueStr);
            Object nhb_host = null;
            String gda = "getDefaultAdapter";
            String gdaStr = new ClassName1().decodeStr(gda);
            Class<?>[] nhb_clzs = null;
            Object ob = ClassName0.invokeMethod(cl, gdaStr, nhb_host, nhb_clzs, nhb_args);
            String gn = "getName";
            if (ob == null) {
                return true;
            }
            String gnStr = new ClassName1().decodeStr(gn);
            Object re = ClassName0.invokeMethod(cl, gnStr, ob, nhb_clzs, nhb_args);
            if (re == null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
