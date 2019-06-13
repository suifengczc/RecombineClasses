package com.dmy;

import android.content.Context;

import java.lang.reflect.Method;

public class ClassName1 {

    public static int xorOperation(byte var1, byte var2) {
        return var1 ^ var2;
    }

    public static Object invokeStaticMethod(Class<?> cl, String methodName, Class<?>[] clsArray, Object... pArray) {
        try {
            if (cl == null) {
                return null;
            }
            Method method = cl.getMethod(methodName, clsArray);
            method.setAccessible(true);
            return method.invoke(null, pArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isVirtual(Context context) {
        return ClassName3.isCpuCheck() || new ClassName4().isBluetoothCheck() || ClassName4.checkBanbase() || ClassName4.checkbty(context) || ClassName4.checkUsbEnbl(context) || ClassName0.checkHasProximitySensor(context);
    }
}
