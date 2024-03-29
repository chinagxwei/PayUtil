package com.demo.runwu.models;

import java.io.Serializable;

public class KFTPayPublicno implements Serializable {
    public String orderNo;
    public String secMerchantId;
    public String terminalIp;
    public String productId;
    public String amount;
    public String tradeName;
    public String tradeTime;
    public String userOpenId;
    public String bankNo;
    public String remark;
    public String notifyUrl;

    @Override
    public String toString() {
        return "KFTPayPublicno{" +
                "orderNo='" + orderNo + '\'' +
                ", secMerchantId='" + secMerchantId + '\'' +
                ", terminalIp='" + terminalIp + '\'' +
                ", productId='" + productId + '\'' +
                ", amount='" + amount + '\'' +
                ", tradeName='" + tradeName + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                ", userOpenId='" + userOpenId + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ", remark='" + remark + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }
}
