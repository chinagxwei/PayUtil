package com.demo.runwu.services;

import com.alibaba.fastjson.JSON;
import com.lycheepay.gateway.client.GatewayClientException;
import com.lycheepay.gateway.client.InitiativePayService;
import com.lycheepay.gateway.client.dto.initiativepay.PassiveScanPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PassiveScanPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayReqDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayRespDTO;
import com.lycheepay.gateway.client.security.KeystoreSignProvider;
import com.lycheepay.gateway.client.security.SignProvider;

public class KFTPayService {

    private static InitiativePayService service;

    // 初始化KftService
    public static void init() throws Exception {
        // 初始化证书：证书类型、证书路径、证书密码、别名、证书容器密码
        SignProvider keystoreSignProvider = new KeystoreSignProvider("PKCS12","E:/keystore/pfx.pfx", "123456".toCharArray(), null,"123456".toCharArray());
        // 签名提供者、商户服务器IP(callerIp)、国际化、对账文件或批量回盘文件下载，商户本地保存路径
        service = new InitiativePayService(keystoreSignProvider, "10.36.160.37", "zh_CN","c:/zip");
        service.setHttpDomain("183.56.161.108"); // 测试环境交易请求Http地址，不设置默认为生产地址：merchant.kftpay.com.cn
    }

    // 被动扫码支付
    public void initiative_pay_bdsm() {
        PassiveScanPayReqDTO reqDTO = new PassiveScanPayReqDTO();
        reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
        reqDTO.setService("kpp_bdsm_pay");///接口名称，固定不变
        reqDTO.setVersion("1.0.0-IEST");//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
        reqDTO.setMerchantId("2024031400105169");//替换成快付通提供的商户ID，测试生产不一样
        //reqDTO.setSecMerchantId("2018102200097236");//二级商户ID 可空 平台商时必传，调用“二级商户入驻接口”生成。
        reqDTO.setOrderNo("KFT20180228149");//交易编号
        reqDTO.setAuthCode("135337911677890793");//扫码支付授权码，通过扫码枪等设备获取
        reqDTO.setAmount("1");//此次交易的具体金额,单位:分,不支持小数点,测试环境建议测试1分钱
        //reqDTO.setPlatMerchSubsidyAmt("");此次交易商户补贴金额,单位:分,不支持小数点
        reqDTO.setCurrency("CNY");//币种
        reqDTO.setTradeName("被扫交易");//商品描述,简要概括此次交易的内容.可能会在用户App上显示
        reqDTO.setRemark("日用品");//商品详情 可空
        reqDTO.setTradeTime("20181211100011");//商户方交易时间 注意此时间取值一般为商户方系统时间
        reqDTO.setBankNo("0000001");//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003
        //reqDTO.setOperatorId(""); //商户操作员编号 可空
        //reqDTO.setStoreId("");//商户门店编号 可空
        //reqDTO.setTerminalId("");//商户的机具终端编号 可空
        //reqDTO.setCouponFlag("0");//是否支持优惠 0：不支持，1：支持  仅银联支付支持 可空
        //reqDTO.setLimitCreditCard("");是否支持信用卡,0：不支持1：支持 仅微信支付支持，为空默认支持信用卡
        reqDTO.setNotifyUrl("http://10.36.160.61:8080/GateWay");//必须可直接访问的url，不能携带参数
        //reqDTO.setIsS0("0");//是否是S0支付是否是S0支付，1：是；0：否。默认否。如果是S0支付，金额会实时付给商户。需经快付通审核通过后才可开展此业务
        //reqDTO.setIsGuarantee("0");//是否担保交易,1:是，0:否
        //reqDTO.setIsSplit("0");//是否分账交易,1:是，0：否 ，
        //分账详情，如果是否分账交易为是，该字段为必填，格式如下:
        //reqDTO.setSplitInfo("[{\"merchantId\":\"2017072600081986\",\"amount\":\"1\",\"remark\":\"有线电视费\"},{\"merchantId\":\"2017073100082105\",\"amount\":\"1\",\"remark\":\"宽带费\"}]");
        try {
            PassiveScanPayRespDTO resp = service.passiveScanPay(reqDTO);
            System.err.println("被扫支付响应：" + JSON.toJSONString(resp));
        } catch (GatewayClientException e) {
            e.printStackTrace();
        }
    }

    //  主动扫码支付
    public void initiative_pay_publicno() {
        PublicNoPayReqDTO reqDTO = new PublicNoPayReqDTO();
        reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
        reqDTO.setService("kpp_pa_pay");//接口名称，固定不变
        reqDTO.setVersion("1.0.0-IEST");//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
        reqDTO.setMerchantId("2024031400105169");//替换成快付通提供的商户ID，测试生产不一样
        //reqDTO.setSecMerchantId("");//二级商户ID，可空， 商户为平台商时必填，调用“二级商户入驻接口”生成
        reqDTO.setOrderNo("KFT121");//交易编号
        reqDTO.setTerminalIp("127.0.0.1"); //APP和网页支付提交用户端ip，主扫支付填调用付API的机器IP
        reqDTO.setNotifyUrl("www.baidu.com");//必须可直接访问的url，不能携带参数
        reqDTO.setProductId("10000");//此id为二维码中包含的商品ID，商户自行定义
        reqDTO.setAmount("100");//此次交易的具体金额,单位:分,不支持小数点
        //reqDTO.setPlatMerchSubsidyAmt("");//此次交易商户补贴金额,单位:分,可空
        reqDTO.setCurrency("CNY");//币种
        reqDTO.setTradeName("hhhh");//商品描述,简要概括此次交易的内容.可能会在用户App上显示
        reqDTO.setRemark("ssss");//商品详情 可空
        reqDTO.setTradeTime("20190226185643");//商户方交易时间 注意此时间取值一般为商户方系统时间
        reqDTO.setSubAppId("151515");//商户公众号账号ID
        reqDTO.setUserOpenId("oZ29IwcKXzcRquJDimXwpgHasek0");//用户在支付渠道的ID,如微信的openId
        reqDTO.setBankNo("0000001");//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003
        //reqDTO.setLimitCreditCard("");是否支持信用卡,0：不支持，1：支持 仅微信支付支持，位空默认信用卡
        reqDTO.setIsS0("0");//是否是S0支付是否是S0支付，1：是；0：否。默认否。如果是S0支付，金额会实时付给商户。需经快付通审核通过后才可开展此业务
        reqDTO.setIsGuarantee("0");//是否担保交易,1:是，0:否
        reqDTO.setIsSplit("0");//是否分账交易,1:是，0：否 ，
        //分账详情，如果是否分账交易为是，该字段为必填，格式如下:
        //reqDTO.setSplitInfo("[{\"merchantId\":\"2017072600081986\",\"amount\":\"1\",\"remark\":\"有线电视费\"},{\"merchantId\":\"2017073100082105\",\"amount\":\"1\",\"remark\":\"宽带费\"}]");
        try {
            PublicNoPayRespDTO resp = service.publicNoPay(reqDTO);
            System.err.println("公众号支付响应：" + JSON.toJSONString(resp));
        } catch (GatewayClientException e) {
            e.printStackTrace();
        }
    }
}
