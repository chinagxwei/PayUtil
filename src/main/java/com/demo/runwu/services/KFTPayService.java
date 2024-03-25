package com.demo.runwu.services;

import com.demo.runwu.models.KFTPayPublicno;
import com.demo.runwu.models.KFTPayQuery;
import com.demo.runwu.models.KFTPayZDSM;
import com.demo.runwu.utils.KFTConfig;
import com.lycheepay.gateway.client.GatewayClientException;
import com.lycheepay.gateway.client.InitiativePayService;
import com.lycheepay.gateway.client.dto.initiativepay.*;
import com.lycheepay.gateway.client.security.KeystoreSignProvider;
import com.lycheepay.gateway.client.security.SignProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KFTPayService {

    public final static String WECHAT_PAY = "0000001";

    public final static String ALIPAY = "0000002";

    private static InitiativePayService service;

    @Autowired
    private KFTConfig kftConfig;

    public KFTPayService init() throws Exception {
        // 初始化证书：证书类型、证书路径、证书密码、别名、证书容器密码
        SignProvider keystoreSignProvider = new KeystoreSignProvider("PKCS12", kftConfig.keyStorePath, kftConfig.keyStorePassword.toCharArray(), null, kftConfig.keyPassword.toCharArray());
        // 签名提供者、商户服务器IP(callerIp)、国际化、对账文件或批量回盘文件下载，商户本地保存路径
        service = new InitiativePayService(keystoreSignProvider, kftConfig.merchantIp, "zh_CN", kftConfig.tempZipFilePath);
        service.setHttpDomain(kftConfig.httpDomain); // 测试环境交易请求Http地址，不设置默认为生产地址：merchant.kftpay.com.cn
        return this;
    }

    public ActiveScanPayRespDTO initiative_pay_zdsm(KFTPayZDSM kftPayZDSM) throws GatewayClientException {
        ActiveScanPayReqDTO reqDTO = new ActiveScanPayReqDTO();
        reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
        reqDTO.setService("kpp_zdsm_pay");//接口名称，固定不变
        reqDTO.setVersion(kftConfig.version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
        reqDTO.setMerchantId(kftConfig.merchantId);//替换成快付通提供的商户ID，测试生产不一样
        reqDTO.setSecMerchantId(kftPayZDSM.secMerchantId);//商户为平台商时必填，调用“二级商户入驻接口”生成
        reqDTO.setOrderNo(kftPayZDSM.orderNo);//交易编号，用于标识商户发起的一笔交易
        reqDTO.setTerminalIp(kftPayZDSM.terminalIp);//APP和网页支付提交用户端ip，主扫支付填调用付API的机器IP
        reqDTO.setNotifyUrl(kftPayZDSM.notifyUrl);//必须可直接访问的url，不能携带参数
        reqDTO.setAmount(kftPayZDSM.amount);//此次交易的具体金额,单位:分,不支持小数点
        //reqDTO.setPlatMerchSubsidyAmt("");此次交易商户补贴金额,单位:分,不支持小数点
        reqDTO.setCurrency("CNY");//币种
        reqDTO.setTradeName(kftPayZDSM.tradeName);//商品描述,简要概括此次交易的内容.可能会在用户App上显示
        reqDTO.setRemark(kftPayZDSM.remark);//商品详情 可空
        reqDTO.setTradeTime(kftPayZDSM.tradeTime);;//商户方交易时间 注意此时间取值一般为商户方系统时间
        reqDTO.setBankNo(kftPayZDSM.bankNo);//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003
        //reqDTO.setOperatorId("");//商户操作员编号 可空， 如员工编号
        //reqDTO.setStoreId("");//商户门店编号 可空
        //reqDTO.setTerminalId("");//商户的机具终端编号 可空
        //reqDTO.setLimitCreditCard("");是否支持信用卡,0：不支持1：支持 仅微信支付支持，为空默认支持信用卡
        //reqDTO.setIsS0("0");//是否是S0支付是否是S0支付，1：是；0：否。默认否。如果是S0支付，金额会实时付给商户。需经快付通审核通过后才可开展此业务
        //reqDTO.setIsGuarantee("0");//是否担保交易,1:是，0:否
        //reqDTO.setIsSplit("0");//是否分账交易,1:是，0：否 ，
        //分账详情，如果是否分账交易为是，该字段为必填，格式如下:
        //reqDTO.setSplitInfo("[{\"merchantId\":\"2017072600081986\",\"amount\":\"1\",\"remark\":\"有线电视费\"},{\"merchantId\":\"2017073100082105\",\"amount\":\"1\",\"remark\":\"宽带费\"}]");

        return service.activeScanPay(reqDTO);
    }

    public PublicNoPayRespDTO initiative_pay_publicno(KFTPayPublicno kftPayPublicno) throws GatewayClientException {
        PublicNoPayReqDTO reqDTO = new PublicNoPayReqDTO();
        reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
        reqDTO.setService("kpp_pa_pay");//接口名称，固定不变
        reqDTO.setVersion(kftConfig.version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
        reqDTO.setMerchantId(kftConfig.merchantId);//替换成快付通提供的商户ID，测试生产不一样
        reqDTO.setSecMerchantId(kftPayPublicno.secMerchantId);//二级商户ID，可空， 商户为平台商时必填，调用“二级商户入驻接口”生成
        reqDTO.setOrderNo(kftPayPublicno.orderNo);//交易编号
        reqDTO.setTerminalIp(kftPayPublicno.terminalIp); //APP和网页支付提交用户端ip，主扫支付填调用付API的机器IP
        reqDTO.setNotifyUrl(kftPayPublicno.notifyUrl);//必须可直接访问的url，不能携带参数
        reqDTO.setProductId(kftPayPublicno.productId);//此id为二维码中包含的商品ID，商户自行定义
        reqDTO.setAmount(kftPayPublicno.amount);//此次交易的具体金额,单位:分,不支持小数点
        //reqDTO.setPlatMerchSubsidyAmt("");//此次交易商户补贴金额,单位:分,可空
        reqDTO.setCurrency("CNY");//币种
        reqDTO.setTradeName(kftPayPublicno.tradeName);//商品描述,简要概括此次交易的内容.可能会在用户App上显示
        reqDTO.setRemark(kftPayPublicno.remark);//商品详情 可空
        reqDTO.setTradeTime(kftPayPublicno.tradeTime);//商户方交易时间 注意此时间取值一般为商户方系统时间

        reqDTO.setUserOpenId(kftPayPublicno.userOpenId);//用户在支付渠道的ID,如微信的openId
        reqDTO.setBankNo(kftPayPublicno.bankNo);//支付渠道   微信渠道:0000001 支付宝渠道：0000002 银联：0000003

        if (kftPayPublicno.bankNo.equals(WECHAT_PAY)) {
            reqDTO.setSubAppId(kftConfig.wechatPayAppID);//商户公众号账号ID
        }
        if (kftPayPublicno.bankNo.equals(ALIPAY)) {
            reqDTO.setSubAppId(kftConfig.aliPayAppID);//商户公众号账号ID
        }

        //reqDTO.setLimitCreditCard("");是否支持信用卡,0：不支持，1：支持 仅微信支付支持，位空默认信用卡
        reqDTO.setIsS0("0");//是否是S0支付是否是S0支付，1：是；0：否。默认否。如果是S0支付，金额会实时付给商户。需经快付通审核通过后才可开展此业务
        reqDTO.setIsGuarantee("0");//是否担保交易,1:是，0:否
        reqDTO.setIsSplit("0");//是否分账交易,1:是，0：否 ，
        //分账详情，如果是否分账交易为是，该字段为必填，格式如下:
        //reqDTO.setSplitInfo("[{\"merchantId\":\"2017072600081986\",\"amount\":\"1\",\"remark\":\"有线电视费\"},{\"merchantId\":\"2017073100082105\",\"amount\":\"1\",\"remark\":\"宽带费\"}]");

        return service.publicNoPay(reqDTO);

    }

    public TradeQueryRespDTO initiative_pay_query(KFTPayQuery query) throws GatewayClientException {
        TradeQueryReqDTO reqDTO = new TradeQueryReqDTO();
        reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
        reqDTO.setService("kpp_trade_record_query");//接口名称,固定不变
        reqDTO.setVersion(kftConfig.version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
        reqDTO.setMerchantId(kftConfig.merchantId);//替换成快付通提供的商户ID，测试生产不一样
        reqDTO.setOriginalOrderNo(query.originalOrderNo);//原订单编号，查询的订单号
        return service.tradeQuery(reqDTO);
    }
}
