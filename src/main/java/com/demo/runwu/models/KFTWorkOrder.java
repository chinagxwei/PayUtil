package com.demo.runwu.models;

import com.lycheepay.gateway.client.dto.base.BaseResponseParameters;

import java.io.Serializable;

public class KFTWorkOrder implements Serializable {
    private String orderNo;

    private BaseResponseParameters result;

    public KFTWorkOrder setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public KFTWorkOrder setResult(BaseResponseParameters result) {
        this.result = result;
        return this;
    }
}
