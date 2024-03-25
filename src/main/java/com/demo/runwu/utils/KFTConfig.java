package com.demo.runwu.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KFTConfig {
    @Value("${kftpay.version}")
    public String version;
    @Value("${kftpay.httpDomain}")
    public String httpDomain;
    @Value("${kftpay.merchant.ip}")
    public String merchantIp;
    @Value("${kftpay.merchant.keyStorePath}")
    public String keyStorePath;
    @Value("${kftpay.merchant.keyStorePassword}")
    public String keyStorePassword;
    @Value("${kftpay.merchant.keyPassword}")
    public String keyPassword;
    @Value("${kftpay.merchant.tempZipFilePath}")
    public String tempZipFilePath;
    @Value("${kftpay.merchant.merchantId}")
    public String merchantId;
    @Value("${kftpay.merchant.sftp.domain}")
    public String sftpDomain;
    @Value("${kftpay.merchant.sftp.password}")
    public String sftpPassword;
    @Value("${kftpay.merchant.wechat.appid}")
    public String wechatPayAppID;
    @Value("${kftpay.merchant.alipay.appid}")
    public String aliPayAppID;
    @Value("${kftpay.merchant.notifyUrl}")
    public String notifyUrl;

    @Override
    public String toString() {
        return "KFTConfig{" +
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
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }
}
