package com.demo.runwu.models;

public class KFTCertInfo {
    public String certType;
    public String certNo;
    public String certValidDate;

    @Override
    public String toString() {
        return "KFTCertInfo(" +
                "certType='" + certType + '\'' +
                ", certNo='" + certNo + '\'' +
                ", certValidDate='" + certValidDate + '\'' +
                ')';
    }
}
