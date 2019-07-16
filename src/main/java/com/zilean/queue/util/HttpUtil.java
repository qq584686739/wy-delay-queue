package com.zilean.queue.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ Author     ：XJH.
 * @ Date       ：Created in 22:46 2018/1/14/013.
 * @ Description：Http封装类
 * @ Modified By：
 */
public class HttpUtil {

    private static final String TAG = "HttpUtils";

    private static final boolean DEBUG = false;

    public final static String REQUEST_GET = "GET";

    public final static String REQUEST_POST = "POST";

    private String mRequestUrl = "";

    private String mEncode = "utf-8";

    /**
     * 是否允许自动跳转 返回码为302的时候
     */
    private boolean mAllowAutoJump = false;

    /**
     * 是否取消了异步执行回调
     */
    private boolean isCancel = false;

    /**
     * 请求方式
     */
    private String mRequestMethod = REQUEST_GET;

    /**
     * 响应超时时间
     */
    private int mTimeout = 1000 * 3;

    /**
     * 请求头参数
     */
    private Map<String, String> mHeadProperty = new HashMap<String, String>();

    /**
     * 请求参数
     */
    private Map<String, String> mRequestProperty = new HashMap<String, String>();

    public HttpUtil(String requestUrl) {
        this(requestUrl, null);
    }

    public HttpUtil(String requestUrl, Map<String, String> requestProperty) {
        this(requestUrl, null, requestProperty);
    }

    public HttpUtil(String requestUrl, Map<String, String> headProperty, Map<String, String> requestProperty) {
        this.mRequestUrl = requestUrl;
        this.mHeadProperty = headProperty;
        this.mRequestProperty = requestProperty;
    }

    /**
     * 设置请求的URL
     */
    public HttpUtil setRequestUrl(String url) {
        this.mRequestUrl = url;
        return this;
    }

    /**
     * 设置请求模式
     *
     * @param requestMethod {@link #REQUEST_GET}  {{@link #REQUEST_POST}}
     */
    public HttpUtil setRequestMethod(String requestMethod) {
        this.mRequestMethod = requestMethod;
        return this;
    }

    /**
     * 设置是否自动跳转，自动响应302等
     *
     * @param allow
     * @return
     */
    public HttpUtil setAllowAutoJump(boolean allow) {
        mAllowAutoJump = allow;
        return this;
    }

    /**
     * 设置请求头
     */
    public HttpUtil setHeadProperty(Map<String, String> headProperty) {
        this.mHeadProperty = headProperty;
        return this;
    }

    /**
     * 添加请求头参数
     */
    public HttpUtil addHeadProperty(String key, String value) {
        this.mHeadProperty.put(key, value);
        return this;
    }

    /**
     * 设置请求参数
     */
    public HttpUtil setRequestProperty(Map<String, String> requestProperty) {
        this.mRequestProperty = requestProperty;
        return this;
    }

    /**
     * 添加请求参数
     */
    public HttpUtil addRequestProperty(String key, String value) {
        this.mRequestProperty.put(key, value);
        return this;
    }

    /**
     * 设置超时时间
     */
    public HttpUtil setTimeout(int timeout) {
        this.mTimeout = timeout;
        return this;
    }


    /**
     * 设置参数编码格式
     *
     * @param encoder 默认编码为utf-8
     * @return
     */
    public HttpUtil setRequestEncoder(String encoder) {
        mEncode = encoder;
        return this;
    }

    /**
     * 同步请求
     */
    public HttpResult request() {
        return doRequest();
    }

    /**
     * 取消异步请求
     */
    public void cancelSync() {
        isCancel = true;
    }

    /**
     * 发出异步请求
     */
    public void requestSync(final HttpCallBack callBack) {
        isCancel = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpResult httpResult = doRequest();
                //如果已经被取消掉了或者回调设置为空，则直接退出
                if (isCancel || callBack == null) {
                    return;
                }
                if (httpResult.getException() != null) {
                    callBack.onError(httpResult.getException());
                } else {
                    callBack.onSuccess(httpResult);
                }
                callBack.onComplete();
            }
        }).start();
    }

    /**
     * 在请求之前允许对请求进行处理
     *
     * @param headProperty    设置的请求头 可能为null
     * @param requestProperty 设置的参数 可能为null
     */
    protected void perRequest(Map<String, String> headProperty, Map<String, String> requestProperty) {

    }

    /**
     * 允许对响应结果进行处理
     */
    protected void afterRequest(HttpResult httpResult) {

    }

    /**
     * 真正执行网络请求的位置
     */
    private HttpResult doRequest() {
        HttpResult httpResult = new HttpResult();
        try {
            //去掉网址结尾的 /分号
            mRequestUrl.replaceAll("/*$", "");
            perRequest(mHeadProperty, mRequestProperty);
            URL url;
            //请求参数格式化结果
            String requestString = formatProperty(mRequestProperty);
            if (REQUEST_GET.equals(mRequestMethod)) {
                if ("".equals(requestString)) {
                    url = new URL(mRequestUrl);
                } else {
                    url = new URL(mRequestUrl + "?" + requestString);
                }
            } else {
                url = new URL(mRequestUrl);
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(mRequestMethod);
            conn.setConnectTimeout(mTimeout);
            conn.setReadTimeout(mTimeout);
            conn.setInstanceFollowRedirects(mAllowAutoJump);
//            conn.setAllowUserInteraction(mAllowAutoJump);
            //设置请求头
            if (mHeadProperty != null) {
                Set<Map.Entry<String, String>> entries = mHeadProperty.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    conn.setRequestProperty(key, value);
                }
            }
            //设置参数
            if (REQUEST_POST.equals(mRequestMethod)) {
                conn.setDoOutput(true);
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(requestString.getBytes(mEncode));
            }

            InputStream in = conn.getInputStream();
            byte[] responseBytes = inputStream2Bytes(in);
            httpResult.setResponseCode(conn.getResponseCode());
            httpResult.setResponseHead(conn.getHeaderFields());
            httpResult.setResponseBody(responseBytes);
        } catch (Exception e) {
            httpResult.setException(e);
        }
        afterRequest(httpResult);
        return httpResult;
    }

    /**
     * 格式化参数
     * <p>
     * 类似于pws=123&uid=123的形式
     */
    private String formatProperty(Map<String, String> property) {
        StringBuilder formatResult = new StringBuilder();
        if (property == null) {
            return formatResult.toString();
        }
        Set<Map.Entry<String, String>> entries = property.entrySet();
        int begin = 0;
        //拼接所有参数
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (begin == 0) {
                begin++;
            } else {
                formatResult.append("&");
            }
            formatResult.append(key);
            formatResult.append("=");
            formatResult.append(value);
        }
        return formatResult.toString();
    }

    /**
     * 将输出流读取为字符串
     *
     * @param in          输出流
     * @param charsetName 读取的编码格式
     * @return
     * @throws IOException
     */
    private String inputStream2String(InputStream in, String charsetName) throws IOException {
        StringBuffer result = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charsetName));
        String temp;
        while ((temp = reader.readLine()) != null) {
            result.append(temp);
        }
        in.close();
        return result.toString();
    }


    /**
     * 将输入流装换为byte数组
     *
     * @param in 输入流
     * @return
     * @throws IOException
     */
    private byte[] inputStream2Bytes(InputStream in) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        in.close();
        return outStream.toByteArray();
    }

    /**
     * Http请求响应结果包装类
     */
    public static final class HttpResult {

        private int mResponseCode;

        private Map<String, List<String>> mResponseHead;

        private byte[] mResponseBody;

        private Exception exception;

        public int getResponseCode() {
            return mResponseCode;
        }

        public void setResponseCode(int responseCode) {
            this.mResponseCode = responseCode;
        }

        public Map<String, List<String>> getResponseHead() {
            return mResponseHead;
        }

        public void setResponseHead(Map<String, List<String>> responseHead) {
            this.mResponseHead = responseHead;
        }

        public byte[] getResponseBody() {
            return mResponseBody;
        }

        public String getResponseBodyString(String charset) {
            try {
                return new String(getResponseBody(), charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }

        public void setResponseBody(byte[] responseBody) {
            this.mResponseBody = responseBody;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        @Override
        public String toString() {
            return "HttpResult{" +
                    "mResponseCode=" + mResponseCode +
                    ", mResponseHead=" + mResponseHead +
                    ", mResponseBody=" + Arrays.toString(mResponseBody) +
                    ", exception=" + exception +
                    '}';
        }
    }

    /**
     * Http响应回调
     */
    public interface HttpCallBack {

        void onError(Exception e);

        void onSuccess(HttpResult httpResult);

        void onComplete();
    }
}