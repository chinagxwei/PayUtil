package com.demo.runwu.controller;

import com.alibaba.fastjson.JSON;
import com.demo.runwu.models.BaseResponse;
import com.demo.runwu.models.KFTMerchantBaseInfo;
import com.demo.runwu.models.KFTMerchantFile;
import com.demo.runwu.models.KFTPayPublicno;
import com.demo.runwu.services.KFTMerchantService;
import com.demo.runwu.services.KFTPayService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayRespDTO;
import com.lycheepay.gateway.client.dto.secmerchant.SettledSecMerchantResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;

@Slf4j
@RestController
@RequestMapping(value = "/kftpay")
public class KFTPayController {

    @Autowired
    private KFTMerchantService kftMerchantService;

    @Autowired
    private KFTPayService kftPayService;

    @RequestMapping({"", "/", "/index"})
    public String index() {
        return "KFT Pay";
    }

    @RequestMapping("/merchant-add")
    public ResponseEntity<?> merchantAdd(@RequestBody String data) {
        log.info(data);
        Gson gson = new Gson();
        Type type = new TypeToken<KFTMerchantBaseInfo>() {
        }.getType();

        KFTMerchantBaseInfo requestData = gson.fromJson(data, type);

        if (data != null) {
            try {
                SettledSecMerchantResponseDTO res = kftMerchantService.init().merchantBaseInfoAdd(requestData);
                return ResponseEntity.ok(new BaseResponse(JSON.toJSONString(res)).isSuccess());
            } catch (Exception e) {
                return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
            }
        }
        return ResponseEntity.ok(new BaseResponse("data error").isError());
    }

    @RequestMapping("/upload-merchant-file")
    public ResponseEntity<?> uploadMerchantFile(@RequestBody String data) {
        log.info(data);
        Gson gson = new Gson();
        Type type = new TypeToken<KFTMerchantFile>() {
        }.getType();

        KFTMerchantFile requestData = gson.fromJson(data, type);

        if (data != null) {
            try {
                kftMerchantService.init().uploadFile(requestData.localFilePath);
                return ResponseEntity.ok(new BaseResponse("success").isSuccess());
            } catch (Exception e) {
                return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
            }
        }
        return ResponseEntity.ok(new BaseResponse("data error").isError());
    }

    @RequestMapping("/publicno")
    public ResponseEntity<?> publicno(@RequestBody String data) {
        log.info(data);
        Gson gson = new Gson();
        Type type = new TypeToken<KFTPayPublicno>() {
        }.getType();

        KFTPayPublicno requestData = gson.fromJson(data, type);

        if (data != null) {
            try {
                PublicNoPayRespDTO res = kftPayService.init().initiative_pay_publicno(requestData);
                return ResponseEntity.ok(new BaseResponse(JSON.toJSONString(res)).isSuccess());
            } catch (Exception e) {
                return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
            }
        }
        return ResponseEntity.ok(new BaseResponse("data error").isError());
    }
}
