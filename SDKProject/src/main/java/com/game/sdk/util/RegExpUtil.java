package com.game.sdk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class RegExpUtil {

    public static boolean isMatchAccount(String data){
        Pattern pattern = Pattern.compile("^[A-Za-z\\d_]{6,16}$");
//        Pattern pattern = Pattern.compile("[0-9A-Za-z]{6,16}");
//        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        return pattern.matcher(data).matches();
    }
    public static boolean isMatchPassword(String data){
        Pattern pattern = Pattern.compile("^\\S{8,16}$");
//        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~@#$^*()\\-=[{}]\\\\|:;\"',.?/!<>%&_+]{8,16}$");
        return pattern.matcher(data).matches();
    }
    /**
     * 判断是否是手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^((1[0-9][0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isIdCardNumber(String IdCard){
        Pattern p = Pattern.compile("^([1-6][1-9]|50)\\d{4}(19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
        Matcher m = p.matcher(IdCard);
        return m.matches();
    }

    //根据指定长度生成字母和数字的随机数
    //0~9的ASCII为48~57
    //A~Z的ASCII为65~90
    //a~z的ASCII为97~122
    public static String createRandomCharData1(int length)
    {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();//随机用以下三个随机生成器
        Random randdata=new Random();
        int data=0;
        for(int i=0;i<length;i++)
        {
            int index=rand.nextInt(3);
            //目的是随机选择生成数字，大小写字母
            switch(index) {
                case 0:
                    data=randdata.nextInt(10);//仅仅会生成0~9
                    sb.append(data);
                    break;
                case 1:
                    data=randdata.nextInt(26)+65;//保证只会产生65~90之间的整数
                    sb.append((char)data);
                    break;
                case 2:
                    data=randdata.nextInt(26)+97;//保证只会产生97~122之间的整数
                    sb.append((char)data);
                    break;
            }
        }
        String result=sb.toString();
        return result;
    }

    public static String createRandomCharData(int length) {
        StringBuffer valSb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 字符串
//                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;  // 取得大写字母还是小写字母
                int choice = 97;  // 取得小写字母
                valSb.append((char) (choice + random.nextInt(26)));

            } else if ("num".equalsIgnoreCase(charOrNum)) {
                // 数字
                valSb.append(String.valueOf(random.nextInt(10)));
            }
        }
        return valSb.toString();
    }

    public static String RandomCharAndNum(){
        char c=(char)(int)(Math.random()*26+97);
//        Random ran = new Random();
//        int getInt = ran.nextInt(2);
//        if (getInt == 0){
        //Android 为手字母+数字
            return c+ RandomUnRepeatNum();
//        }else {
//            return RandomUnRepeatNum() + c;
//        }


    }

    public static String RandomUnRepeatNum(){
        String strings = createRandomStrings(6).toString(); //生成随机数字字符串
        String[] numbers = createArraysByStrings(strings); //通过字符串拿到数组
        if (checkRepetition(numbers)){
            strings = RandomCharAndNum();
        }
        return strings;
    }

    public static StringBuffer createRandomStrings(int numLength){
        StringBuffer strings = new StringBuffer();
        Random ran = new Random();
        for (int i=0; i<numLength; i++){
            strings.append(ran.nextInt(10) );
        }
        return strings;
    }

    public static String[] createArraysByStrings(String strings){
        String[] numbers = new String[strings.length()];
        for (int i=0; i<strings.length(); i++){
            numbers[i] = getCharByIndex(strings, i);
        }
        return numbers;
    }

    public static String getCharByIndex(String strings, int index){
        return  String.valueOf(strings.charAt(index));
    }

    public static boolean checkRepetition(String[] number) {
        String count = "0";
        for (int i = 0; i < number.length; i++) {
            if (i == 0) {
                count = number[i];
            } else {
                if (!number[i].equals(count)) {
                    return false;
                }
            }
        }
        return true;
    }
    public static String getStarMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile)) {
            if (mobile.length() >=11)
                return mobile.substring(0,3) +"****" + mobile.substring(7, mobile.length());
        }else {
            return "";
        }
        return mobile;
    }

    //获取当前版本号
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public static int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density; //当前屏幕密度因子
        return (int) (dp * scale + 0.5f);
    }



}
