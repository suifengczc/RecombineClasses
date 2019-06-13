package com.dmy;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ClassName0 {

    public String getCurrentProcessName() {
        try {
            String classStr = ClassName2.decodeStr("android.app.ActivityThread");
            Class<?> cl = Class.forName(classStr);
            Object var2 = ClassName1.invokeStaticMethod(cl, ClassName2.decodeStr("currentActivityThread"), null);
            return (String) new ClassName4().invokeNoStaticMethod(var2, ClassName2.decodeStr("getProcessName"), null);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static String toHex(byte[] data) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < data.length; ++i) {
            byte b = data[i];
            buffer.append(String.format("%x", b));
            if (i != data.length - 1) {
                buffer.append(":");
            }
        }
        return buffer.toString();
    }

    public static void decodeFile(InputStream p0, FileOutputStream p1) throws IOException {
        byte[] encCode = ClassName2.getCodeByte(p0);
        int num = encCode.length;
        int var3 = 0;
        byte[] var4 = new byte[8 * 1024];
        int var5;
        for (; (var5 = p0.read(var4)) > 0; ) {
            int i = 0;
            for (byte tt : var4) {
                if (i < var5) {
                    tt = (byte) ~(tt ^ encCode[var3 % num]);
                    int value_1 = (tt & 0xff) >>> 4;
                    int value_2 = (tt & 0xff) << 4;
                    var4[i] = ClassName4.orOperation(value_1, value_2);
                    i++;
                    var3++;
                }
            }
            p1.write(var4, 0, var5);
        }
    }

    public Object callMethod(Object host, Method method, Object[] args) {
        Object invoke = null;
        try {
            invoke = method.invoke(host, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoke;
    }

    public static boolean checkHasProximitySensor(Context context) {
        try {
            Object ob = new ClassName4().invokeNoStaticMethod(context, ClassName2.decodeStr("getSystemService"), new Class[] { String.class }, ClassName2.decodeStr("mssensorms").substring(2, 8));
            Object re = new ClassName4().invokeNoStaticMethod(ob, ClassName2.decodeStr("getDefaultSensor"), new Class[] { int.class }, 8);
            if (re == null) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }
}
