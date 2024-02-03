package com.ginDriver.common.utils.http;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DueGin
 */
@Slf4j
public class HttpRequestUtil {

    private static String proxyHost = "proxy.ipidea.io";

    private static Integer proxyPort = 2336;

    private static String proxyPassword = "saluteai_2023";

    private static final Integer timeOut = 15 * 1000;


    /**
     * 发送 get 请求返回二进制数据
     *
     * @param url     请求地址
     * @param headers 请求头
     * @return 二进制数据
     */
    public static byte[] sendGetBackByte(String url, Map<String, String> headers) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        httpGet.setConfig(getConfig());

        // 设置请求头
        setHeaders(httpGet, headers);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String contentType = entity.getContentType().getValue();
                if (contentType.equals("application/octet-stream")) {

                    byte[] byteArray = EntityUtils.toByteArray(entity);
                    return byteArray;
                }
            }
        } catch (IOException e) {
            log.error("url: {}, msg: {}", url, e.getMessage());
        }
        return new byte[]{};
    }

    /**
     * 获取配置
     *
     * @return
     */
    private static RequestConfig getConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(timeOut)
                .setConnectionRequestTimeout(timeOut)
                .setSocketTimeout(timeOut)
                .build();
    }


    /**
     * 转换请求参数
     *
     * @param params
     * @return
     */
    public static List<NameValuePair> covertParamsToList(Map<String, String> params) {
        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
        }
        return pairs;
    }

    public static String sendGet(String url, Map<String, String> params, Map<String, String> headers) {

        HttpGet httpGet = null;
        try {
            String actuallyUrl = addParams(url, params);
            httpGet = new HttpGet(new URI(actuallyUrl));
            RequestConfig config = getConfig();
            httpGet.setConfig(config);
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }


        try (CloseableHttpClient httpClient = HttpClients.custom().build()) {
            setHeaders(httpGet, headers);

            HttpResponse response;

            response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 携带代理账号发送 get 请求
     *
     * @param url           请求地址
     * @param proxyHost     代理服务器地址
     * @param proxyPort     代理服务器端口
     * @param proxyAccount  代理账号
     * @param proxyPassword 代理帐号密码
     * @param headers       请求头
     * @return 响应二进制数据
     */
    public static byte[] sendGetWithProxyAccountBackByte(
            String url,
            String proxyHost,
            Integer proxyPort,
            String proxyAccount,
            String proxyPassword,
            Map<String, String> headers
    ) {
        HttpGet httpGet = new HttpGet(url);

        RequestConfig config = getConfig();

        httpGet.setConfig(config);

        // 配置代理主机和端口号
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(proxyAccount, proxyPassword));

        HttpClient httpClient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setProxy(proxy)
                .build();

        // 设置请求头
        setHeaders(httpGet, headers);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String contentType = entity.getContentType().getValue();
                if (contentType.equals("application/octet-stream")) {

                    byte[] byteArray = EntityUtils.toByteArray(entity);
                    return byteArray;
                }
            }
        } catch (IOException e) {
            log.error("url: {}, msg: {}", url, e.getMessage());
        }
        return new byte[]{};
    }


    /**
     * 设置请求头
     *
     * @param http    Http 请求方式
     * @param headers 请求头
     */
    public static void setHeaders(HttpRequestBase http, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                http.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 为 url 添加请求参数，拿到完整的url后，还需要new URI(newUrl)传入httpGet等请求方法
     *
     * @param url    请求地址
     * @param params 请求参数 map
     * @return 拼接了参数的 url
     */
    private static String addParams(String url, Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        sb.append(url).append("?");
        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 携带byte数组发请求
     *
     * @param url         请求地址
     * @param headers     请求头map
     * @param requestBody byte数组请求数据
     * @return 响应体内容
     * @throws IOException
     */
    public static String sendPostRequestWithByteBody(String url, Map<String, String> headers, byte[] requestBody) throws IOException {
        return sendPostRequestWithByteBody(url, headers, null, requestBody);
    }

    /**
     * 携带byte数组、请求参数发请求
     *
     * @param url         请求地址
     * @param headers     请求头
     * @param queryParams 请求参数
     * @param requestBody byte请求体
     * @return 响应体内容
     * @throws IOException
     */
    public static String sendPostRequestWithByteBody(String url, Map<String, String> headers, Map<String, String> queryParams, byte[] requestBody) throws IOException {
        // 创建http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 设置请求参数
        if (queryParams != null && queryParams.size() != 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(url).append("?");
            queryParams.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
            url = sb.toString();
        }

        // 创建连接
        HttpPost httpPost = new HttpPost(url);

        httpPost.setConfig(getConfig());

        // 设置请求头
        setHeaders(httpPost, headers);

        // 设置请求体（二进制数据）
        if (requestBody != null && requestBody.length > 0) {
            httpPost.setEntity(new ByteArrayEntity(requestBody));
        }

        // 发送请求并获取响应
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

        // 关闭连接
        httpClient.close();

        return responseBody;
    }

    /**
     * 读请求体
     *
     * @param request
     * @return 请求体字符串
     */
    public static String readBody(HttpServletRequest request) {
        StringBuffer json = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return json.toString();
    }

    /**
     * 读取byte数组
     *
     * @param request
     * @return byte数组
     * @throws IOException
     */
    public static byte[] readRequestBytes(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] byteArray = outputStream.toByteArray();

//        log.info("byte数组：");
//        for (byte b : byteArray) {
//            System.out.println(b);
//        }
//        log.info("byte数组长度: {}", byteArray.length);
        return byteArray;
    }

    /**
     * 获取请求参数
     *
     * @param request
     * @return 请求参数map
     */
    public static Map<String, String> readRequestParams(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        HashMap<String, String> res = new HashMap<>();
        parameterMap.forEach((k, v) -> res.put(k, v[0]));
        return res;
    }


    public static String sendPost(
            String url,
            Map<String, String> headers,
            Map<String, Object> body
    ) throws IOException {

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        RequestConfig config = getConfig();

        CloseableHttpClient httpClient = clientBuilder.build();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setConfig(config);

        // 设置请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // 设置请求体（二进制数据）
        if (body != null) {
            String bodyStr = JSON.toJSONString(body);
            httpPost.setEntity(new StringEntity(bodyStr, StandardCharsets.UTF_8));
        }

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String responseBody = EntityUtils.toString(responseEntity);
        log.info("responseBody: {}, 转换后: {}", responseEntity, responseBody);

        httpClient.close();

        return responseBody;
    }

    public static String sendPostWithProxy(
            String url, Map<String, String> headers,
            String proxyHost,
            Integer proxyPort,
            String proxyUsername,
            String proxyPassword,
            byte[] requestBody
    ) throws IOException {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(proxyUsername, proxyPassword));

        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setProxy(proxy);

        RequestConfig config = getConfig();

        CloseableHttpClient httpClient = clientBuilder.build();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setConfig(config);

        // 设置请求头
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }

        // 设置请求体（二进制数据）
        if (requestBody != null && requestBody.length > 0) {
            httpPost.setEntity(new ByteArrayEntity(requestBody));
        }

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String responseBody = EntityUtils.toString(responseEntity);

        httpClient.close();

        return responseBody;
    }
}
