package com.demo.runwu.test;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class CodePay {

    public static void main(String[] args) {

        String url = "http://202.100.182.26:8180/transapi/open/trans/onlinepay";
        String privateKey = "6DBBC9A95B64592BCFF1F26931D6E40148DF4C11A121371FC69E7BD4CF498F4B";
        String publicKey = "048292F0AE2301E3A8F8C41EAB9A12C9CEC094244FADE88FF5122F3D09927FD04341CAB4C183AB0F1454B2C6020C6BAC9FAEB93608510F50C6E5E2D07740A1349D";

        //参数
        String version = "1";
        String serviceCode = "gateway.pay.b2c";
        String merchantMemberNo = "21070212563100000040";
        String signType = "SM2";
        String sign = "";

        String productCode = "gateway.pay.b2c";
        String merchantOrderNo = String.valueOf(System.currentTimeMillis());
        long amount = 100;
        String clientIp = "127.0.0.1";
        String productName = "百事";
        String notifyUrl = "http://127.0.0.1/api/notifytest/notify";
        String pageCallbackUrl= "http://wwww.baidu.com";
        String institutionalId = "W3IS0000001";
        String orgProductType="org_product_type_default";
        String customerMobileNo="13201569405";
        String upchannelType="01";
        String currency= "CNY";

        //拼接参数
        Map<String, Object> request_map = new HashMap<>();
        request_map.put("version", version);
        request_map.put("serviceCode", serviceCode);
        request_map.put("merchantMemberNo", merchantMemberNo);
        request_map.put("signType", signType);
        request_map.put("sign", "");

        //计算 sign
        TreeMap<String, Object> sign_map = new TreeMap<String, Object>();
        sign_map.put("version", version);
        sign_map.put("serviceCode", serviceCode);
        sign_map.put("merchantMemberNo", merchantMemberNo);
        sign_map.put("signType", signType);

        sign_map.put("body.productCode", productCode);
        sign_map.put("body.merchantOrderNo", merchantOrderNo);
        sign_map.put("body.amount", amount);
        sign_map.put("body.clientIp", clientIp);
        sign_map.put("body.productName", productName);
        sign_map.put("body.notifyUrl", notifyUrl);
        sign_map.put("body.pageCallbackUrl", pageCallbackUrl);
        sign_map.put("body.institutionalId", institutionalId);
        sign_map.put("body.orgProductType", orgProductType);
        sign_map.put("body.customerMobileNo", customerMobileNo);
        sign_map.put("body.upchannelType", upchannelType);
        sign_map.put("body.currency", currency);

        String needSign = MapUtil.mapToString(sign_map);
        log.info(needSign);

        sign = CommonUtil.bytesToBase64(Sm2Util.sign(privateKey, needSign.getBytes(StandardCharsets.UTF_8)));
        //计算 sign 结束

        request_map.put("sign", sign);

        Map<String, Object> request_body_map = new HashMap<>();
        for (String key: sign_map.keySet()){
            if (key.startsWith("body")){
                request_body_map.put(key.substring(5), sign_map.get(key));
            }
        }

        request_map.put("body", request_body_map);
        //

        //
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String request_content = gson.toJson(request_map);

        //发送请求
        log.info(url);
        log.info(request_content);
        String response_content = HttpUtil.post(url, request_content);
        log.info(response_content);

        // code=
        // ORDERED 下单成功
        // FAIL
        Map<String, Object> map = JSONUtil.toBean(response_content,Map.class);
        String toVerifySign = (String) map.get("sign");
        map.remove("sign");
        TreeMap<String, Object> treeMap = new TreeMap<>();
        for (String key: map.keySet()){
            if ("body".equals(key)){
                if (!"null".equals(map.get("body").toString())) {
                    Map<String, Object> body = (Map<String, Object>) map.get("body");
                    for (String subKey : body.keySet()) {
                        treeMap.put("body." + subKey, body.get(subKey));
                    }
                }
            }else {
                treeMap.put(key, map.get(key));
            }
        }
        String verifyStr = MapUtil.mapToString(treeMap);
        log.info("sign: {}",toVerifySign);
        log.info("verifyDataStr: {}",verifyStr);
        boolean verify = Sm2Util.verify(publicKey, CommonUtil.base64ToBytes(toVerifySign), verifyStr.getBytes(StandardCharsets.UTF_8));
        log.info("验签结果: {}", verify);
    }

}
