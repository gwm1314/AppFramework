package com.gwm.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *      获取手机信息的工具类
 *
 *  <!--获取mac地址权限 -->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <!--获取手机信息权限 -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 */
public class MobileUtil {
    /**
     * 获取手机的IP地址（IPV4）
     */
    public static String GetHostIp(){
        try {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();en.hasMoreElements();){
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address){
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取android当前可用内存大小
     */
    public static String getAvailMemory(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }
    /**
     * 获得系统总内存
     */
    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
            return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得手机屏幕宽高
     * @return
     */
    public static int[] getHeightAndWidth(WindowManager wm){
        int width=wm.getDefaultDisplay().getWidth();
        int heigth=wm.getDefaultDisplay().getHeight();
        return new int[]{width,heigth};
    }
    /**
     * 获取IMEI号，IESI号，手机型号
     */
    public static String[] getInfo(Context context) {
        TelephonyManager mTm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL; // 手机型号
        String mtyb= android.os.Build.BRAND;//手机品牌
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        Log.i("text", "手机IMEI号："+imei+"手机IESI号："+imsi+"手机型号："+mtype+"手机品牌："+mtyb+"手机号码"+numer);
        return new String[]{imei,imsi,mtype,mtyb,numer};
    }
    /**
     * 获取手机MAC地址
     * 只有手机开启wifi才能获取到mac地址
     */
    public static String getMacAddress(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }
    /**
     * 手机CPU信息
     */
    public static String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("text", "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return cpuInfo;
    }

    /**
     * 检查手机的SDCard的状态  返回可用的存储空间大小
     * @return  -1：未发现SDCard  0: 没有可用的存储空间
     */
    public static long checkExternalStorageAvailable() {
        boolean bAvailable = Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
        if (bAvailable) {
            StatFs statfs = new StatFs(Environment
                    .getExternalStorageDirectory().getPath());
            if ((long) statfs.getAvailableBlocks()
                    * (long) statfs.getBlockSize() <= 0) {
                return 0;
            }else {
                return (long) statfs.getAvailableBlocks()
                        * (long) statfs.getBlockSize();
            }
        } else {
            return -1;
        }
    }

}
