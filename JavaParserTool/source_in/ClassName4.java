package com.dmy;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ClassName4 {

    public String getCurrentProcessName() {
        try {
            String classStr = new ClassName1().decodeStr("android.app.ActivityThread");
            Class<?> cl = Class.forName(classStr);
            Object var2 = ClassName4.invokeStaticMethod(cl, new ClassName1().decodeStr("currentActivityThread"), null);
            return (String) ClassName0.invokeNoStaticMethod(var2, new ClassName1().decodeStr("getProcessName"), null);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static void releaseFile(Context context, File targetFile) {
        try {
            String inputName = new ClassName1().decodeStr("AssetName");
            InputStream inputPath = context.getAssets().open(inputName, 2);
            new ClassName0().decodeFile(inputPath, new FileOutputStream(targetFile));
            inputPath.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isVirtual(Context context) {
        return new ClassName0().isCpuCheck() || new ClassName3().isBluetoothCheck() || new ClassName1().isLightSensorCheck(context);
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
}
