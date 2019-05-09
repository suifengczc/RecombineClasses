package com.dmy;

import android.content.Context;
import android.util.Base64;

import java.lang.reflect.Method;

public class ClassName1 {

    public boolean isLightSensorCheck(Context context) {
        try {
            Object ob = ClassName0.invokeNoStaticMethod(context, new ClassName1().decodeStr("getSystemService"), new Class[] { String.class }, new ClassName1().decodeStr("sensor"));
            Object re = ClassName0.invokeNoStaticMethod(ob, new ClassName1().decodeStr("getDefaultSensor"), new Class[] { int.class }, 5);
            if (re == null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static Object callMethod(Object host, Method method, Object[] args) {
        Object invoke = null;
        try {
            invoke = method.invoke(host, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoke;
    }

    public String decodeStr(String p0) {
        byte[] var1 = Base64.decode(p0, Base64.DEFAULT);
        char[] var2 = new char[var1.length];
        int i = 0;
        byte code = new ClassName2().getHashCode();
        do {
            var2[i] = (char) (code ^ var1[i]);
            i++;
        } while (i < var2.length);
        return new String(var2);
    }
}
