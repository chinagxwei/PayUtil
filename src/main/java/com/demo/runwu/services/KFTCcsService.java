package com.demo.runwu.services;

import com.demo.runwu.models.KFTPackage;
import com.demo.runwu.utils.KFTConfig;
import com.lycheepay.gateway.client.CcsService;
import com.lycheepay.gateway.client.GatewayClientException;
import com.lycheepay.gateway.client.dto.ccs.OpenPackageReqDTO;
import com.lycheepay.gateway.client.dto.ccs.OpenPackageRespDTO;
import com.lycheepay.gateway.client.security.KeystoreSignProvider;
import com.lycheepay.gateway.client.security.SignProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KFTCcsService {
    public static CcsService service;

    @Autowired
    private KFTConfig kftConfig;

    public KFTCcsService init() throws Exception {
        // 初始化证书
        // 证书类型、证书路径、证书密码、别名、证书容器密码
        SignProvider keystoreSignProvider = new KeystoreSignProvider("PKCS12", kftConfig.keyStorePath, kftConfig.keyStorePassword.toCharArray(), null, kftConfig.keyPassword.toCharArray());
        // 签名提供者、商户服务器IP(callerIp)、国际化、对账文件或批量回盘文件下载，商户本地保存路径
        service = new CcsService(keystoreSignProvider, kftConfig.merchantIp, "zh_CN", kftConfig.tempZipFilePath);
        service.setHttpDomain(kftConfig.httpDomain); // 测试环境交易请求Http地址，不设置默认为生产地址：merchant.kftpay.com.cn
        service.setConnectionTimeoutSeconds(1 * 60);// 连接超时时间（单位秒）,不设置则默认30秒
        service.setResponseTimeoutSeconds(10 * 60);// 响应超时时间（单位秒）,不设置则默认300秒
        service.setSftpAccountName(kftConfig.merchantId);//sftp账号,与商户账户编号相同（MerchantId）
        service.setSftpPassword(kftConfig.sftpPassword);//sftp密码
        service.setSftpDomain(kftConfig.sftpDomain);//sftp域名
        return this;
    }

    public OpenPackageRespDTO openPackage(KFTPackage kftPackage) throws GatewayClientException {
        OpenPackageReqDTO openPackageReqDTO = new OpenPackageReqDTO();
        openPackageReqDTO.setReqNo(String.valueOf(System.currentTimeMillis()));
        openPackageReqDTO.setService("ccs_package_open");
        openPackageReqDTO.setVersion(kftConfig.version);
        openPackageReqDTO.setMerchantId(kftConfig.merchantId);
        openPackageReqDTO.setPackageCode(kftPackage.packageCode);
        openPackageReqDTO.setSettleBankNo(kftPackage.settleBankNo);
        openPackageReqDTO.setSubMerchantId(kftPackage.subMerchantId);
        return service.openPackage(openPackageReqDTO);
    }
}
