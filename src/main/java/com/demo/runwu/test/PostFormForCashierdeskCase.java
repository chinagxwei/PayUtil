package com.demo.runwu.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;


/**
 * 快捷支付收银台
 */
@Slf4j
public class PostFormForCashierdeskCase {
    public static void main(String[] args) throws Exception {
        String url = "http://202.100.182.26:8180/cashierdesk/mock/client/submit";
        String demoPrivateKey = "6DBBC9A95B64592BCFF1F26931D6E40148DF4C11A121371FC69E7BD4CF498F4B";

        Map<String,Object> params = new TreeMap<>();
        params.put("merchantMemberNo", "21070212563100000040");
        params.put("version", "1");
        params.put("serviceCode", "pay");
        params.put("body.customerMobileNo", "13233333333");
        params.put("body.merchantOrderNo", String.valueOf(System.currentTimeMillis()));
        params.put("body.amount", "1");
        params.put("body.channelType", "01");
        params.put("body.pageCallbackUrl", "http://www.baidu.com");
        params.put("body.notifyUrl", "http://127.0.0.1");
        params.put("signType", "sm2");


        CloseableHttpClient httpClient = null;

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLHostnameVerifier((hostName, sslSession) -> {
            return true; // 证书校验通过
        });
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 信任所有
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        httpClientBuilder.setSSLContext(sslContext);

        //创建自定义的httpclient对象
        httpClient = httpClientBuilder.build();

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(StandardCharsets.UTF_8);
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);


        HttpPost httpPost = new HttpPost(url);
        String data = MapUtil.mapToString(params);

        params.put("sign", CommonUtil.bytesToBase64(Sm2Util.sign(demoPrivateKey, data.getBytes(StandardCharsets.UTF_8))));
        Map<String, String> requestParam = new HashMap<>();

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key,value= (String) entry.getValue();
            if (entry.getKey().startsWith("body")){
                key = entry.getKey().substring(5);
            }else {
                key = entry.getKey();
            }
            requestParam.put(key, value);
            nameValuePairs.add(new NameValuePair() {
                @Override
                public String getName() {
                    return key;
                }

                @Override
                public String getValue() {
                    return value;
                }
            });
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8);
        httpPost.setEntity(entity);

        int connectTimeOut = 30000;
        int socketTimeOut = 30000;
        // 设置连接超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeOut)
                .setConnectionRequestTimeout(connectTimeOut).setSocketTimeout(socketTimeOut).build();
        httpPost.setConfig(requestConfig);
        // 执行提交
        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity responseEntity = response.getEntity();
        int status = response.getStatusLine().getStatusCode();
        log.info(String.valueOf(status));
        log.info(String.valueOf(requestParam));
        log.info(EntityUtils.toString(responseEntity, StandardCharsets.UTF_8));
        log.info(Arrays.toString(response.getAllHeaders()));
    }

}
