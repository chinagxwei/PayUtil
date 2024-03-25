package com.demo.runwu.models;

public class KFTCustBeneficiary {
    public String name;
    public String certifiType;
    public String certifiNo;
    public String validDateStart;
    public String validDateEnd;
    public String address;
    public String phone;

    @Override
    public String toString() {
        return "KFTCustBeneficiary(" +
                "name='" + name + '\'' +
                ", certifiType='" + certifiType + '\'' +
                ", certifiNo='" + certifiNo + '\'' +
                ", validDateStart='" + validDateStart + '\'' +
                ", validDateEnd='" + validDateEnd + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ')';
    }
}
