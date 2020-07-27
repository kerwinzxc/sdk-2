package com.game.sdk.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.game.sdk.SdkConstant;
import com.game.sdk.domain.DeviceBean;
import com.game.sdk.domain.NotProguard;
import com.game.sdk.log.L;
import com.game.sdk.log.T;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.game.sdk.ui.YunLoginActivity.REQUEST_PHONE_STATE;


@NotProguard
public class DeviceUtil {
    public static DeviceBean getDeviceBean(Context context) {
        String deviceId = getDeviceId(context);
        String mac = getLocalMac(context);
        if (TextUtils.isEmpty(mac)) {
            mac = "null";
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "null";
        }
        DeviceBean deviceBean = new DeviceBean();
        deviceBean.setUserua(getUserUa(context));
        deviceBean.setLocalIp(getHostIP());
        deviceBean.setMac(mac);
        deviceBean.setDeviceId(deviceId);
        //设备信息: 电话号码，用户系统版本，MAC地址，机器码，机型，运营商
        StringBuffer deviceInfoSb = new StringBuffer();
//        deviceInfoSb.append(getPhoneNum(context)).append("||android").
//                append(Build.VERSION.RELEASE).append("||").
//                append(mac).append("||").
//                append(deviceId).append("||").
//                append(getPhoneModel()).append("||").
//                append(getOperators(context));

        //设备信息: 手机品牌，手机型号，用户系统版本
        deviceInfoSb.append(getBrandModel()).append("||android").append(Build.VERSION.RELEASE);

        //getSystemModel
        deviceBean.setDeviceinfo(deviceInfoSb.toString());
        return deviceBean;
    }

//    public static String getPhoneNum(Context context) {
//        TelephonyManager telephonyManager = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        String phoneNum = telephonyManager.getLine1Number();
//        if (TextUtils.isEmpty(phoneNum)) {
//            return "null";
//        } else {
//            return phoneNum;
//        }
//    }

    /**
     * 获取SIM卡运营商
     *
     * @param context
     * @return
     */
//    public static String getOperators(Context context) {
//        TelephonyManager tm = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//        String operator = null;
//        String IMSI = tm.getSubscriberId();
//        if (IMSI == null || IMSI.equals("")) {
//            return operator;
//        }
//        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
//            operator = "中国移动";
//        } else if (IMSI.startsWith("46001")) {
//            operator = "中国联通";
//        } else if (IMSI.startsWith("46003")) {
//            operator = "中国电信";
//        }
//        if (TextUtils.isEmpty(operator)) {
//            return "null";
//        } else {
//            return operator;
//        }
//    }

    /**
     * 手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        if (TextUtils.isEmpty(Build.MODEL)) {
            return "null";
        } else {
            return Build.MODEL;
        }
    }

    /**
     * 获取ua信息
     *
     * @throws UnsupportedEncodingException
     */
    public static String getUserUa(Context context) {
        WebView webview = new WebView(context);
        webview.layout(0, 0, 0, 0);
        String str = webview.getSettings().getUserAgentString();
        return str;
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIp;
    }

    // IMEI码
    public static String getIMIEStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String deviceId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                deviceId = tm.getDeviceId();
            } else {
                T.s(context, "获取IMEI失败，读取手机状态权限被禁止");
            }
        } else {
            deviceId = tm.getDeviceId();
        }

        return deviceId;
    }


    public static String getPhoneStateManifest(Activity mContext) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // toast("需要动态获取权限");
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        } else {
            // toast("不需要动态获取权限");
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            return tm.getDeviceId();
        }
        return "";
    }

    // Mac地址
    private static String getLocalMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    // Android Id
//    public static String getAndroidId(Context context) {
//        String androidId = Settings.Secure.getString(
//                context.getContentResolver(), Settings.Secure.ANDROID_ID);
//        return androidId;
//    }
    public static String getAndroidId(Context context) {
        String android_id = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取设备ID
     * @param context
     * @return
     * Android Q 已弃用
     */
//    public static String getDeviceId(Context context) {
//        String deviceId = "";
//        if (isPhone(context)) {//是通信设备使用设备id
//            TelephonyManager telephony = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
////            String imei1 = null;
////            try{
////                Method method = telephony.getClass().getMethod("getImei",int.class);
////                imei1 = (String) method.invoke(telephony,0);
////            }catch (Exception e) {
////                e.printStackTrace();
////            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
////                    deviceId = telephony.getImei(1);
////                    deviceId = imei1;
//                    deviceId = telephony.getDeviceId();
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
//                    T.s(context, "获取IMEI失败，读取手机状态权限被禁止");
//                }
//            } else {
////                deviceId = imei1;
//                deviceId = telephony.getDeviceId();
//            }
//        } else {//使用android_id
//            deviceId = Settings.Secure.getString(context.getContentResolver(), "android_id");
//        }
//        if (!TextUtils.isEmpty(deviceId)) {
//            return deviceId;
//        }
//        //使用mac地址
//        try {
//            deviceId = getLocalMac(context).replace(":", "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (!TextUtils.isEmpty(deviceId)) {
//            return deviceId;
//        }
//        //使用UUID
//        UUID uuid = UUID.randomUUID();
//        deviceId = uuid.toString().replace("-", "");
//        return deviceId;
//    }

//    public static String getDeviceId(Context context) {
//        String serial = null;
//        String m_szDevIDShort = "35" +
//                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
//                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
//                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
//                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
//                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
//                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
//                Build.USER.length() % 10; //13 位
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                    serial = android.os.Build.getSerial();
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PHONE_STATE);
////                    T.s(context, "获取设备信息失败，读取手机状态权限被禁止");
//                }
//            } else {  serial = Build.SERIAL; }
//            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();//API>=9 使用serial号
//        } catch (Exception exception) {
//            serial = "serial"; //serial需要一个初始化 随便一个初始化
//        }
//        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString(); //使用硬件信息拼凑出来的15位号码
//    }

    public static String getDeviceId(Context context) {
        return  "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10 +
                "-" + Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID); //13 位
    }

    /**
     * 打开权限设置界面
     */
    public static void openSettingPermission(Context context) {
        try {
            if (MiuiDeviceUtil.isMiui()) {
                MiuiDeviceUtil.openMiuiPermissionActivity(context);
            } else {
                Intent intent1 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent1.setData(uri);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int getAppVersionCode(Context context) {
        int version = 1;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getClientInfo(Context context, String name) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            L.e("PackageManager.NameNotFoundException" + e);
        }

        String msg = "";

        if (name.equals(SdkConstant.YZ_CLIENTKEY_NAME)) {
            msg = appInfo.metaData.getInt(name) + "";
        }
//        else if (name.equals(SdkConstant.YZ_VERSION_NAME)) {
//            msg = appInfo.metaData.getFloat(name, 1.0f) + "";
//        }
        else {
            msg = appInfo.metaData.getString(name);
        }

        L.e("name = " + msg);

        return msg;
    }

//    public static String getClientInfo(Context context) {
//
//        try {
//            //Return an AssetManager instance for your application's package
//            InputStream is = context.getAssets().open("config.txt");
//            int size = is.available();
//            // Read the entire asset into a local byte buffer.
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            // Convert the buffer into a string.
//            String text = new String(buffer, "utf-8");
//            // Finally stick the string into the text view.
//            L.e("config.txt-----" + text);
//            return text;
//        } catch (IOException e) {
//            // Should never happen!
////            throw new RuntimeException(e);
//            e.printStackTrace();
//        }
//        return "读取错误，请检查文件名";
//    }

    public static int countStr(String str1, String str2) {
        int counter = 0;
        if (str1.indexOf(str2) == -1) {
            return 0;
        } else if (str1.indexOf(str2) != -1) {
            counter++;
            countStr(str1.substring(str1.indexOf(str2) +
                    str2.length()), str2);
            return counter;
        }
        return 0;
    }

    /**
     * 判断是魅族操作系统
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/18,9:43
     * <h3>UpdateTime</h3> 2016/6/18,9:43
     * <h3>CreateAuthor</h3> vera
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @return true 为魅族系统 否则不是
     */
    public static boolean isMeizuFlymeOS() {
/* 获取魅族系统操作版本标识*/
        String meizuFlymeOSFlag = getSystemProperty("ro.build.display.id", "");
        if (TextUtils.isEmpty(meizuFlymeOSFlag)) {
            return false;
        } else if (meizuFlymeOSFlag.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取系统属性
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/18,9:35
     * <h3>UpdateTime</h3> 2016/6/18,9:35
     * <h3>CreateAuthor</h3> vera
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param key          ro.build.display.id
     * @param defaultValue 默认值
     * @return 系统操作版本标识
     */
    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getBrandModel() {
        return android.os.Build.BRAND + " " + android.os.Build.MODEL;
    }

    public static boolean isPortrait(Context context){
        boolean isPortrait = false;
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            isPortrait = true;
        }
        return isPortrait;
    }
}
