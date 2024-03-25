package com.demo.runwu.controller;

import com.demo.runwu.models.BaseResponse;
import com.demo.runwu.services.KFTMerchantTest;
import com.demo.runwu.utils.KFTConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/check/config")
class CheckConfigController {

    @Autowired
    private KFTConfig kftConfig;

    @Autowired
    private KFTMerchantTest kftMerchantTest;

    @RequestMapping("/kft-config")
    public ResponseEntity<?> kftconfig(){
        return ResponseEntity.ok(new BaseResponse(kftConfig.toString()).isSuccess());
    }

    @RequestMapping("/kft-test")
    public ResponseEntity<?> kftTest(){
        try {
            kftMerchantTest.init().testUploadFile();
            return ResponseEntity.ok(new BaseResponse("success").isSuccess());
        } catch (Exception e) {
            return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
        }
    }

}
