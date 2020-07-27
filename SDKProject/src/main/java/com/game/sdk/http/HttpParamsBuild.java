package com.game.sdk.http;

import android.util.Log;

import com.game.sdk.SdkConstant;
import com.game.sdk.domain.NotProguard;
import com.game.sdk.log.L;
import com.game.sdk.util.AuthCodeUtil;
import com.game.sdk.util.MD5;
import com.game.sdk.util.RSAUtils;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.toolbox.HttpParamsEntry;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;


public class HttpParamsBuild {

    private static final String TAG = HttpParamsBuild.class.getSimpleName();
    private static String randChDict = "qwertyuiopasdfghjklzxcvbnm123456789QWERTYUIOPASDFGHJKLZXCVBNM";
    private String jsonParam = "";
    private String authkey;
    private final ArrayList<HttpParamsEntry> mHeaders = new ArrayList<HttpParamsEntry>(4);
    private HttpParams httpParams;

    public HttpParamsBuild(String jsonParam) {
        this.jsonParam = jsonParam;
//        encodeData();
        encodeJson();
    }

    private void encodeJson() {
        httpParams = new HttpParams();

        HashMap<String, String> map = toMap(jsonParam);

        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            Log.d("ZDC", "key= " + entry.getKey() + "    value= " + entry.getValue());
            httpParams.put(entry.getKey(), entry.getValue());
        }

        try {
            String sign = MD5.getSignature(map, SdkConstant.YZ_CLIENTSECRET);
            httpParams.put("sign", sign);

            Log.d("ZDC", "jsonParam = " + jsonParam.substring(0, jsonParam.length() - 1) +
                    ",\"sign\":\"" + sign + "\"}");

        } catch (IOException e) {
            Log.d("ZDC", "IOException = " + e);
        }
    }

    public static HashMap<String, String> toMap(Object object) {
        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(object.toString());

        Iterator ite = jsonObject.keys();
        // 遍历jsonObject数据,添加到Map对象
        while (ite.hasNext()) {
            String key = ite.next().toString();
            String value = jsonObject.getString(key);
            data.put(key, value);
        }
        // 或者直接将 jsonObject赋值给Map
        // data = jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

//    private void encodeData() {
//        httpParams = new HttpParams();
//        String randCh = getRandCh(16);
//        //生成key
////        client_id 与 时间戳 与 rand16 使用下划线(_)连接，得到 rsakey
//        long time = System.currentTimeMillis() + SdkConstant.SERVER_TIME_INTERVAL;
//        StringBuffer keyBuffer = new StringBuffer(SdkConstant.HS_CLIENTID).append("_")
//                .append(time).append("_").append(randCh);
//        L.e(TAG, "key加密前：" + keyBuffer);
//        String key = null;
//        try {
//            key = new String(RSAUtils.encryptByPublicKey(keyBuffer.toString().getBytes(), SdkConstant.RSA_PUBLIC_KEY), "utf-8");
//            L.e(TAG, "key公钥加密后：" + key);
//            //生成key
//            httpParams.put("key", key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //生成加密数据
////        6、2中的rand16与client_key组成对称加密参数 authkey ([client_key]_rand16)
////        7、将requestdata与 authkey 对称加密并 URLencoding 得到请求参数  `data`
//        StringBuffer dataKeyBuffer = new StringBuffer(SdkConstant.HS_CLIENTKEY).append(randCh);
//        this.authkey = dataKeyBuffer.toString();
//        L.e(TAG, "data加密前1：" + jsonParam);
//        String data = AuthCodeUtil.authcodeEncode(jsonParam, authkey);
//        L.e(TAG, "key:" + key + "\ndata:" + data);
//        httpParams.put("data", data);
//        for (HttpParamsEntry httpParamsEntry : mHeaders) {
//            httpParams.putHeaders(httpParamsEntry.k, httpParamsEntry.v);
//        }
//    }

    @NotProguard
    public HttpParams getHttpParams() {

        return httpParams;
    }

    @NotProguard
    public String getAuthkey() {
        return authkey;
    }

    /**
     * 随机从randChDict字典里获取length长度的字符串
     *
     * @param length
     * @return
     */
    @NotProguard
    public static String getRandCh(int length) {
        int dictLength = randChDict.length();
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(randChDict.charAt(random.nextInt(dictLength)));
        }
        return buffer.toString();
    }
}
