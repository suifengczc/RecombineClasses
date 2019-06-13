package com.dmy;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ClassName3 {

    public static boolean isMacBad() {
        String macString = ClassName4.getMacAddress();
        if (TextUtils.isEmpty(macString)) {
            return false;
        } else if (ClassName2.decodeStr("2:0:0:0:0:0").equals(macString)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCpuCheck() {
        String aa = "";
        String istr = ClassName2.decodeStr("intel");
        String pci = ClassName2.decodeStr("/proc/cpuinfo");
        try {
            InputStream is = new FileInputStream(pci);
            StringBuffer sb = null;
            int len = -1;
            while (true) {
                len = (int) ClassName4.invokeMethod(is.getClass(), ClassName2.decodeStr("msreadms").substring(2, 6), is, (Class<?>[]) null, (Object[]) null);
                if (len == -1) {
                    break;
                }
                sb.append((char) len);
            }
            aa = sb.toString();
            is.close();
        } catch (Exception var7) {
        }
        String aStr = ClassName2.decodeStr("amd");
        return aa.contains(istr) || aa.contains(aStr);
    }

    public static void createFile(File p0) {
        if (!p0.exists()) {
            File parentFile = (File) new ClassName4().invokeNoStaticMethod(p0, ClassName2.decodeStr("msgetParentFilems").substring(2, 15), null);
            new ClassName4().invokeNoStaticMethod(parentFile, ClassName2.decodeStr("mkdirs"), null);
            new ClassName4().invokeNoStaticMethod(p0, ClassName2.decodeStr("createNewFile"), null);
        }
    }
}
