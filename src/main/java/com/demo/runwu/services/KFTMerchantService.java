package com.demo.runwu.services;

import com.alibaba.fastjson.JSON;
import com.demo.runwu.models.KFTMerchantBaseInfo;
import com.demo.runwu.models.KFTMerchantQuery;
import com.demo.runwu.models.KFTWorkOrder;
import com.demo.runwu.utils.KFTConfig;
import com.lycheepay.gateway.client.GatewayClientException;
import com.lycheepay.gateway.client.KftSecMerchantService;
import com.lycheepay.gateway.client.dto.secmerchant.*;
import com.lycheepay.gateway.client.security.KeystoreSignProvider;
import com.lycheepay.gateway.client.security.SignProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class KFTMerchantService {
    private static KftSecMerchantService service;

    @Autowired
    private KFTConfig kftConfig;

    public KFTMerchantService init() throws Exception {
        SignProvider keystoreSignProvider = new KeystoreSignProvider("PKCS12", kftConfig.keyStorePath, kftConfig.keyStorePassword.toCharArray(), null, kftConfig.keyPassword.toCharArray());
        // 签名提供者、商户服务器IP(callerIp)、国际化、对账文件或批量回盘文件下载，商户本地保存路径
        service = new KftSecMerchantService(keystoreSignProvider, kftConfig.merchantIp, "zh_CN", kftConfig.tempZipFilePath);
        service.setHttpDomain(kftConfig.httpDomain); // 测试环境交易请求Http地址，不设置默认为生产地址：merchant.kftpay.com.cn
        service.setConnectionTimeoutSeconds(1 * 60);// 连接超时时间（单位秒）,不设置则默认30秒
        service.setResponseTimeoutSeconds(10 * 60);// 响应超时时间（单位秒）,不设置则默认300秒
        service.setSftpAccountName(kftConfig.merchantId);//sftp账号,与商户账户编号相同（MerchantId）
        service.setSftpPassword(kftConfig.sftpPassword);//sftp密码 测试环境:账号后6位
        service.setSftpDomain(kftConfig.sftpDomain);//sftp域名
        service.setSftpPort(22222);
        return this;
    }

    public KFTWorkOrder merchantBaseInfoAdd(KFTMerchantBaseInfo kftMerchantBaseInfo) throws IOException, GatewayClientException {
        SettledSecMerchantRequestDTO reqDTO = new SettledSecMerchantRequestDTO();
        reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
        String orderNo = String.valueOf(System.currentTimeMillis());
        reqDTO.setOrderNo(orderNo);//用于标识商户发起的一笔交易
        reqDTO.setService("amp_secmerchant_baseinfo_add");//接口名称，固定不变
        reqDTO.setVersion(kftConfig.version);//测试：1.0.0-IEST,生产：1.0.0-PRD

        reqDTO.setMerchantId(kftConfig.merchantId);//快付通配给商户的账户编号（测试和生产环境不同）
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
        reqDTO.setMerchantProperty(kftMerchantBaseInfo.merchantProperty);//商户类型：1：个人、2：企业、3：个体工商户、4：事业单位
        reqDTO.setMerchantAttribute("1");//商户属性 1实体特约商户 2 网络特约商户 3 实体兼网络特约商户
        reqDTO.setBusinessFunctions("[\"010101\",\"010102\",\"010201\",\"010202\"]");

        reqDTO.setSettleBankAccount("{\"settleAccountCreditOrDebit\":\"1\",\"settleBankAccountNo\":\"" + kftMerchantBaseInfo.settleBankAccountNo + "\",\"settleBankAcctType\":\"2\",\"settleBankNo\":\"" + kftMerchantBaseInfo.settleBankNo + "\",\"settleName\":\"" + kftMerchantBaseInfo.settleName + "\"}");

//        reqDTO.setCorpCertInfo("[{\"certNo\":\"440305199109241211\",\"certType\":\"0\",\"certValidDate\":\"20991231\"},{\"certNo\":\"11311788100133ABB1\",\"certType\":\"Y\",\"certValidDate\":\"20991231\"}]");
        reqDTO.setCertPath(kftMerchantBaseInfo.fileName);//SFTP目录下的地址，默认在mpp目录下
//        log.info(JSON.toJSONString(kftMerchantBaseInfo.custBeneficiaryInfo));
        reqDTO.setCorpCertInfo(JSON.toJSONString(kftMerchantBaseInfo.corpCertInfo));
        reqDTO.setCustBeneficiaryInfo(JSON.toJSONString(kftMerchantBaseInfo.custBeneficiaryInfo));
        String certDigest = KFTMerchantService.md5File(kftMerchantBaseInfo.localFilePath);
        reqDTO.setCertDigest(certDigest);//上报文件签名

//        reqDTO.setMerchantOperateName(merchantOperateName);
//        reqDTO.setMerchantMCC(merchantMCC);
//        reqDTO.setMerchantEnglishName(merchantEnglishName);
//        reqDTO.setMerchantBankBranchNo(merchantBankBranchNo);
//        reqDTO.setMerchantBankBranchName(merchantBankBranchName);
        SettledSecMerchantResponseDTO settledSecMerchantResponseDTO = service.settledSecMerchant(reqDTO);
        KFTWorkOrder kftWorkOrder = new KFTWorkOrder();
        kftWorkOrder.setOrderNo(orderNo).setResult(settledSecMerchantResponseDTO);
        return kftWorkOrder;
    }


    public KFTWorkOrder merchantBaseInfoUpdate(KFTMerchantBaseInfo kftMerchantBaseInfo) throws GatewayClientException, IOException {
        UpdateSecMerchantRequestDTO reqDTO = new UpdateSecMerchantRequestDTO();
        reqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));//请求编号
        String orderNo = String.valueOf(System.currentTimeMillis());
        reqDTO.setOrderNo(orderNo);//用于标识商户发起的一笔交易
        reqDTO.setService("amp_secmerchant_baseinfo_update");//接口名称，固定不变
        reqDTO.setVersion(kftConfig.version);//接口版本号，测试:1.0.0-IEST,生产:1.0.0-PRD
        reqDTO.setMerchantId(kftConfig.merchantId);//快付通配给商户的账户编号（测试和生产环境不同）
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
        reqDTO.setMerchantProperty(kftMerchantBaseInfo.merchantProperty);//商户类型：1：个人、2：企业、3：个体工商户、4：事业单位
        reqDTO.setCategory("0010010001");//经营类目
        reqDTO.setBusinessScene(kftMerchantBaseInfo.businessScene);//业务场景说明
        reqDTO.setRemark("");//可空，介绍商户营业内容等
        reqDTO.setCertPath(kftMerchantBaseInfo.fileName);//SFTP目录下的地址，默认在mpp目录下
        String certDigest = KFTMerchantService.md5File(kftMerchantBaseInfo.localFilePath);
        reqDTO.setCertDigest(certDigest);//上报文件签名
//        reqDTO.setProductFees("[{\"feeOfAttach\":\"0\",\"feeOfRate\":\"1300\",\"feeType\":\"3\",\"productId\":\"010101\"},{\"feeOfAttach\":\"0\",\"feeOfRate\":\"1300\",\"feeType\":\"3\",\"productId\":\"010601\",\"creditOrDebit\":\"1\"}]");
        reqDTO.setSettleBankAccount("{\"settleAccountCreditOrDebit\":\"1\",\"settleBankAccountNo\":\"" + kftMerchantBaseInfo.settleBankAccountNo + "\",\"settleBankAcctType\":\"2\",\"settleBankNo\":\"" + kftMerchantBaseInfo.settleBankNo + "\",\"settleName\":\"" + kftMerchantBaseInfo.settleName + "\"}");
        reqDTO.setCorpCertInfo(JSON.toJSONString(kftMerchantBaseInfo.corpCertInfo));
        reqDTO.setCustBeneficiaryInfo(JSON.toJSONString(kftMerchantBaseInfo.custBeneficiaryInfo));
        //reqDTO.setPersonCertInfo(personCertInfo);
//        reqDTO.setCorpCertInfo("[{\"certNo\":\"999999999999\",\"certType\":\"0\",\"certValidDate\":\"20220930\"},{\"certNo\":\"99999999999999\",\"certType\":\"Y\",\"certValidDate\":\"20210930\"}]");
        reqDTO.setMerchantAttribute("1");//商户属性 1实体特约商户 2 网络特约商户 3 实体兼网络特约商户
        //reqDTO.setIcpRecord("");//ICP备案号,可空,如果商户属性是2，3必填
        //reqDTO.setServiceIp("");//网站服务器IP,可空,如果商户属性是2，3必填
        //reqDTO.setCompanyWebUrl("");//经营网址,可空,如果商户属性是2，3必填
//        reqDTO.setMerchantOperateName("(特约)测试(多媒体)");
//        reqDTO.setMerchantMCC("4816");
//        reqDTO.setMerchantEnglishName("ABC");
//        reqDTO.setMerchantBankBranchNo("105337000019");
//        reqDTO.setMerchantBankBranchName("中国建设银行绍兴分行");

        BaseSecMerchantRespDTO baseSecMerchantRespDTO =  service.updateSecMerchant(reqDTO);
        KFTWorkOrder kftWorkOrder = new KFTWorkOrder();
        kftWorkOrder.setOrderNo(orderNo).setResult(baseSecMerchantRespDTO);
        return kftWorkOrder;
    }

    public QuerySecMerchantResponseDTO merchantBaseInfoQuery(KFTMerchantQuery query) throws GatewayClientException {
        QuerySecMerchantRequestDTO reqDTO = new QuerySecMerchantRequestDTO();
        reqDTO.setService("amp_secmerchant_baseinfo_query");//接口名称，固定不变
        reqDTO.setVersion(kftConfig.version);//测试：1.0.0-IEST,生产：1.0.0-PRD
        reqDTO.setMerchantId(kftConfig.merchantId);//快付通配给商户的账户编号（测试和生产环境不同）
        reqDTO.setMerchantProperty(query.merchantProperty);//商户类型：1：个人2：企业3：个体工商户4：事业单位
        reqDTO.setCertNo(query.certNo);//商户类型是2、3传营业执照,1传身份证号
        reqDTO.setOrderNo(query.orderNo);//查询交易编号,对应新增或者修改里面的同名参数orderNo值
        QuerySecMerchantResponseDTO res = service.querySecMerchant(reqDTO);
        log.info(JSON.toJSONString(res));
        return res;
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
