package com.demo.runwu.services;

import com.alibaba.fastjson.JSON;
import com.demo.runwu.models.KFTMerchantBaseInfo;
import com.lycheepay.gateway.client.GatewayClientException;
import com.lycheepay.gateway.client.KftSecMerchantService;
import com.lycheepay.gateway.client.dto.secmerchant.SettledSecMerchantRequestDTO;
import com.lycheepay.gateway.client.dto.secmerchant.SettledSecMerchantResponseDTO;
import com.lycheepay.gateway.client.security.KeystoreSignProvider;
import com.lycheepay.gateway.client.security.SignProvider;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class KFTMerchantService {
    private static KftSecMerchantService service;

    @Value("${kftpay.httpDomain}")
    private String httpDomain;
    @Value("${kftpay.merchant.ip}")
    private String merchantIp;
    @Value("${kftpay.merchant.keyStorePath}")
    private String keyStorePath;
    @Value("${kftpay.merchant.keyStorePassword}")
    private String keyStorePassword;
    @Value("${kftpay.merchant.keyPassword}")
    private String keyPassword;
    @Value("${kftpay.merchant.tempZipFilePath}")
    private String tempZipFilePath;
    @Value("${kftpay.merchant.merchantId}")
    private String merchantId;
    @Value("${kftpay.merchant.sftp.domain}")
    private String sftpDomain;
    @Value("${kftpay.merchant.sftp.password}")
    private String sftpPassword;

    public KFTMerchantService init() throws Exception {
        SignProvider keystoreSignProvider = new KeystoreSignProvider("PKCS12", keyStorePath, keyStorePassword.toCharArray(), null, keyPassword.toCharArray());
        // 签名提供者、商户服务器IP(callerIp)、国际化、对账文件或批量回盘文件下载，商户本地保存路径
        service = new KftSecMerchantService(keystoreSignProvider, merchantIp, "zh_CN", tempZipFilePath);
        service.setHttpDomain(httpDomain); // 测试环境交易请求Http地址，不设置默认为生产地址：merchant.kftpay.com.cn
        service.setConnectionTimeoutSeconds(1 * 60);// 连接超时时间（单位秒）,不设置则默认30秒
        service.setResponseTimeoutSeconds(10 * 60);// 响应超时时间（单位秒）,不设置则默认300秒
        service.setSftpAccountName(merchantId);//sftp账号,与商户账户编号相同（MerchantId）
        service.setSftpPassword(sftpPassword);//sftp密码 测试环境:账号后6位
        service.setSftpDomain(sftpDomain);//sftp域名
        return this;
    }

    public SettledSecMerchantResponseDTO merchantBaseInfoAdd(KFTMerchantBaseInfo kftMerchantBaseInfo) throws IOException, GatewayClientException {
        SettledSecMerchantRequestDTO reqDTO = new SettledSecMerchantRequestDTO();
        reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
        reqDTO.setOrderNo(String.valueOf(System.currentTimeMillis()));//用于标识商户发起的一笔交易
        reqDTO.setService("amp_secmerchant_baseinfo_add");//接口名称，固定不变
        reqDTO.setVersion("1.0.0-IEST");//测试：1.0.0-IEST,生产：1.0.0-PRD

        reqDTO.setMerchantId(merchantId);//快付通配给商户的账户编号（测试和生产环境不同）
        reqDTO.setSecMerchantName(kftMerchantBaseInfo.secMerchantName);//二级商户名称
        reqDTO.setShortName(kftMerchantBaseInfo.shortName);//会显示在用户订单信息中
        //reqDTO.setProvince(province);//可空
        //reqDTO.setCity(city);//可空
        reqDTO.setDistrict(kftMerchantBaseInfo.district);//注册地址区县编码
        reqDTO.setAddress(kftMerchantBaseInfo.address);//地址
        reqDTO.setLegalName(kftMerchantBaseInfo.legalName);//法人姓名,如果是个人商户，填个人商户姓名
        reqDTO.setContactName(kftMerchantBaseInfo.contactName);//联系人名称
        reqDTO.setContactPhone(kftMerchantBaseInfo.contactPhone);//联系人手机号
        reqDTO.setContactEmail(kftMerchantBaseInfo.contactEmail);//联系人邮箱
        reqDTO.setCategory(kftMerchantBaseInfo.category);//经营类目
        reqDTO.setBusinessScene(kftMerchantBaseInfo.businessScene);//业务场景说明
        reqDTO.setBusinessMode(kftMerchantBaseInfo.businessMode);
        reqDTO.setRegisteredFundStr(kftMerchantBaseInfo.registeredFundStr);
        reqDTO.setRemark("");//可空，介绍商户营业内容等
        reqDTO.setMerchantProperty("3");//商户类型：1：个人、2：企业、3：个体工商户、4：事业单位
        reqDTO.setMerchantAttribute("1");//商户属性 1实体特约商户 2 网络特约商户 3 实体兼网络特约商户
        reqDTO.setBusinessFunctions("[\"010101\",\"010102\",\"010201\",\"010202\"]");

        reqDTO.setSettleBankAccount("{\"settleAccountCreditOrDebit\":\"1\",\"settleBankAccountNo\":\"" + kftMerchantBaseInfo.settleBankAccountNo + "\",\"settleBankAcctType\":\"2\",\"settleBankNo\":\"" + kftMerchantBaseInfo.settleBankNo + "\",\"settleName\":\"" + kftMerchantBaseInfo.settleName + "\"}");

//        reqDTO.setCorpCertInfo("[{\"certNo\":\"440305199109241211\",\"certType\":\"0\",\"certValidDate\":\"20991231\"},{\"certNo\":\"11311788100133ABB1\",\"certType\":\"Y\",\"certValidDate\":\"20991231\"}]");
        reqDTO.setCertPath(kftMerchantBaseInfo.fileName);//SFTP目录下的地址，默认在mpp目录下
        reqDTO.setCustBeneficiaryInfo(JSON.toJSONString(kftMerchantBaseInfo.custBeneficiaryInfo));
        String certDigest = KFTMerchantService.md5File(kftMerchantBaseInfo.localFilePath);
        reqDTO.setCertDigest(certDigest);//上报文件签名

//        reqDTO.setMerchantOperateName(merchantOperateName);
//        reqDTO.setMerchantMCC(merchantMCC);
//        reqDTO.setMerchantEnglishName(merchantEnglishName);
//        reqDTO.setMerchantBankBranchNo(merchantBankBranchNo);
//        reqDTO.setMerchantBankBranchName(merchantBankBranchName);

        return service.settledSecMerchant(reqDTO);
    }

    public void uploadFile(String localFilePath) throws GatewayClientException {
        String remotePath = "/cashier/mpp";
        service.uploadFile(localFilePath, remotePath);
    }

    /**
     * 对文件md5加密
     *
     * @author wy
     */
    private static String md5File(String uploadFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(uploadFile);
        byte[] md5Bytes = DigestUtils.md5(fileInputStream);
        String result = new String(Base64.encodeBase64(md5Bytes), StandardCharsets.UTF_8);
        IOUtils.closeQuietly(fileInputStream);
        return result;
    }
}
