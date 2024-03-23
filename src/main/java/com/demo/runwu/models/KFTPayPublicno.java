package com.demo.runwu.models;

import java.io.Serializable;

public class KFTPayPublicno implements Serializable {
    public String orderNo;
    public String terminalIp;
    public String productId;
    public String amount;
    public String tradeName;
    public String tradeTime;
    public String userOpenId;
    public String bankNo;

    @Override
    public String toString() {
        return "KFTPayPublicno(" +
                "orderNo='" + orderNo + '\'' +
                ", terminalIp='" + terminalIp + '\'' +
                ", productId='" + productId + '\'' +
                ", amount='" + amount + '\'' +
                ", tradeName='" + tradeName + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                ", userOpenId='" + userOpenId + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ')';
    }
}
