package com.demo.runwu.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KFTConfigCheckService {

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
    @Value("${kftpay.merchant.wechat.appid}")
    private String wechatPayAppID;
    @Value("${kftpay.merchant.alipay.appid}")
    private String aliPayAppID;

    @Override
    public String toString() {
        return "KFTConfigCheckService(" +
                "httpDomain='" + httpDomain + '\'' +
                ", merchantIp='" + merchantIp + '\'' +
                ", keyStorePath='" + keyStorePath + '\'' +
                ", keyStorePassword='" + keyStorePassword + '\'' +
                ", keyPassword='" + keyPassword + '\'' +
                ", tempZipFilePath='" + tempZipFilePath + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", sftpDomain='" + sftpDomain + '\'' +
                ", sftpPassword='" + sftpPassword + '\'' +
                ", wechatPayAppID='" + wechatPayAppID + '\'' +
                ", aliPayAppID='" + aliPayAppID + '\'' +
                ')';
    }
}
