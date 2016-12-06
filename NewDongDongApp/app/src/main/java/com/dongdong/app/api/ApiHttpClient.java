package com.dongdong.app.api;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.text.TextUtils;

import com.dongdong.app.AppConfig;
import com.dongdong.app.AppContext;
import com.dongdong.app.util.LogUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiHttpClient {

    private final static String HOST = "www.dd121.com";
    private static String API_URL = "http://www.dd121.com/%s";
    // public final static String HOST = "192.168.1.101";
    // private static String API_URL = "http://192.168.1.101/%s";
    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    private static AsyncHttpClient client;

    // 正式服务器
//     private static String API_KEY = "P4VenrKw5aam6sREotky";
//     private static String SECRET_KEY = "1bvNOGryv3IPq0fwICBH";
    // 测试服务器(55)
    private static String API_KEY = "po8ujkvNjIm5hcoxtbp4";
    private static String SECRET_KEY = "UspRzPUcEytfcwR3N56k";

    private static final String ALLOW_CIRCULAR_REDIRECTS = "http.protocol.allow-circular-redirects";

    public ApiHttpClient() {
    }

    public static AsyncHttpClient getHttpClient() {
        return client;
    }

    public static void setHttpClient(AsyncHttpClient c) {
        client = c;
        client.addHeader("Accept-Language", Locale.getDefault().toString());
        client.addHeader("Host", HOST);
        client.addHeader("Connection", "Keep-Alive");
        client.getHttpClient().getParams()
                .setParameter(ALLOW_CIRCULAR_REDIRECTS, true);

        setUserAgent(ApiClientHelper.getUserAgent(AppContext.getInstance()));
    }

    public static void cancelAll(Context context) {
        client.cancelRequests(context, true);
    }

    public static void clearUserCookies(Context context) {
        // (new HttpClientCookieStore(context)).a();
    }

    public static void delete(String partUrl, AsyncHttpResponseHandler handler) {
        client.delete(getAbsoluteApiUrl(partUrl), handler);
        log(new StringBuilder("DELETE ").append(partUrl).toString());
    }

    public static void get(String partUrl, AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteApiUrl(partUrl), handler);
        log(new StringBuilder("GET ").append(partUrl).toString());
    }

    public static void get(String partUrl, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        client.get(getAbsoluteApiUrl(partUrl), params, handler);
        log(new StringBuilder("GET ").append(partUrl).append("&")
                .append(params).toString());
    }

    private static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = String.format(API_URL, partUrl);
        }
        LogUtils.d("ApiHttpClient.clazz--->>>getAbsoluteApiUrl request url:"
                + url);
        return url;
    }

    public static String getApiUrl() {
        return API_URL;
    }

    public static void getDirect(String url, AsyncHttpResponseHandler handler) {
        client.get(url, handler);
        log(new StringBuilder("GET ").append(url).toString());
    }

    public static void log(String log) {
        LogUtils.d("BaseApi", "ApiHttpClient.clazz--->>>" + log);
    }

    public static void post(String partUrl, AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(partUrl), handler);
        log(new StringBuilder("POST ").append(partUrl).toString());
    }

    public static void post(String partUrl, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        client.post(getAbsoluteApiUrl(partUrl), params, handler);
        log(new StringBuilder("POST ").append(partUrl).append("&")
                .append(params).toString());
    }

    public static void postDirect(String url, RequestParams params,
                                  AsyncHttpResponseHandler handler) {
        client.post(url, params, handler);
        log(new StringBuilder("POST ").append(url).append(params).toString());
    }

    public static void put(String partUrl, AsyncHttpResponseHandler handler) {
        client.put(getAbsoluteApiUrl(partUrl), handler);
        log(new StringBuilder("PUT ").append(partUrl).toString());
    }

    public static void put(String partUrl, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        client.put(getAbsoluteApiUrl(partUrl), params, handler);
        log(new StringBuilder("PUT ").append(partUrl).append("&")
                .append(params).toString());
    }

    public static void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }

    private static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }

    public static void setCookie(String cookie) {
        client.addHeader("Cookie", cookie);
    }

    private static String appCookie;

    public static void cleanCookie() {
        appCookie = "";
    }

    public static String getCookie(AppConfig config) {
        if (TextUtils.isEmpty(appCookie)) {
            appCookie = (String) config.getConfigValue(
                    AppConfig.DONG_CONFIG_SHARE_PREF_NAME, "cookie", "");
        }
        return appCookie;
    }

    /***************************************************************************************/
    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    // 使用 Map按key进行排序
    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    // 获取签名
    private static String getSign(String url, Map<String, String> params,
                                  String encode) {
        //String baseStr = "POST" + url;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("POST").append(url);
        Map<String, String> sortparams = sortMapByKey(params);
        try {
            for (Map.Entry<String, String> entry : sortparams.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode));
            }
            stringBuffer.append(SECRET_KEY);
            return getMd5(URLEncoder.encode(stringBuffer.toString(), encode));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // md5加密
    private static String getMd5(String plainText) {
        try {
            StringBuffer buf = new StringBuffer("");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            for (int i : md.digest()) {
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString();
            // 16位加密
            // return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
     * Fuction:获取开门记录的参数：
     */
    public static RequestParams getUnlockRecords3(
            String url, int user_id, int device_id, int start_index, int end_index) {
        Calendar calendar = Calendar.getInstance();
        RequestParams params = new RequestParams();
        params.put("apikey", API_KEY);
        params.put("timestamp", "" + calendar.getTimeInMillis() / 1000);
        params.put("id", "435");
        params.put("method", "getUnlockRecords3");
        params.put("userid", user_id);
        params.put("deviceid", device_id);
        params.put("startindex", start_index);
        params.put("endindex", end_index);

        Map<String, String> map = new HashMap<>();
        map.put("apikey", API_KEY);
        map.put("timestamp", "" + calendar.getTimeInMillis() / 1000);
        map.put("id", "435");
        map.put("method", "getUnlockRecords3");
        map.put("userid", String.valueOf(user_id));
        map.put("deviceid", String.valueOf(device_id));
        map.put("startindex", String.valueOf(start_index));
        map.put("endindex", String.valueOf(end_index));

        String sign = getSign(url, map, "utf-8");
        params.put("sign", sign);

        System.out.println("开门记录sign>>>>>>>>>>" + sign);
        return params;
    }
}
