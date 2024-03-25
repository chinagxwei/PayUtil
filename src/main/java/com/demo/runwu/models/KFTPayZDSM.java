package com.demo.runwu.models;

public class KFTPayZDSM {
    public String orderNo;
    public String secMerchantId;
    public String terminalIp;
    public String amount;
    public String tradeName;
    public String tradeTime;
    public String bankNo;
    public String remark;
    public String notifyUrl;

    @Override
    public String toString() {
        return "KFTPayZDSM{" +
                "orderNo='" + orderNo + '\'' +
                ", secMerchantId='" + secMerchantId + '\'' +
                ", terminalIp='" + terminalIp + '\'' +
                ", amount='" + amount + '\'' +
                ", tradeName='" + tradeName + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ", remark='" + remark + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }
}
