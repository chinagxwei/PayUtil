package com.demo.runwu.models;

import java.io.Serializable;
import java.util.List;

public class KFTMerchantBaseInfo implements Serializable {
    public String fileName;
    public String localFilePath;
    public String secMerchantName;
    public String shortName;
    public String district;
    public String address;
    public String legalName;
    public String contactName;
    public String contactPhone;
    public String contactEmail;
    public String category;
    public String businessScene;
    public String businessMode;
    public String registeredFundStr;
    public String settleBankNo;
    public String settleBankAccountNo;
    public String settleName;
    public List<KFTCertInfo> corpCertInfo;
    public List<KFTCustBeneficiary> custBeneficiaryInfo;

    @Override
    public String toString() {
        return "KFTMerchantBaseInfo{" +
                "fileName='" + fileName + '\'' +
                ", localFilePath='" + localFilePath + '\'' +
                ", secMerchantName='" + secMerchantName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", legalName='" + legalName + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", category='" + category + '\'' +
                ", businessScene='" + businessScene + '\'' +
                ", businessMode='" + businessMode + '\'' +
                ", registeredFundStr='" + registeredFundStr + '\'' +
                ", settleBankNo='" + settleBankNo + '\'' +
                ", settleBankAccountNo='" + settleBankAccountNo + '\'' +
                ", settleName='" + settleName + '\'' +
                ", corpCertInfo=" + corpCertInfo +
                ", custBeneficiaryInfo=" + custBeneficiaryInfo +
                '}';
    }
}
