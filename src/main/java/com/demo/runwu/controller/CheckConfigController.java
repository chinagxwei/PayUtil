package com.demo.runwu.controller;

import com.demo.runwu.models.BaseResponse;
import com.demo.runwu.models.DataResponse;
import com.demo.runwu.services.KFTConfigCheckService;
import com.demo.runwu.services.KFTMerchantService;
import com.demo.runwu.services.KFTMerchantTest;
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
    private KFTConfigCheckService kftConfigCheckService;

    @Autowired
    private KFTMerchantTest kftMerchantTest;

    @RequestMapping("/kft-config")
    public ResponseEntity<?> kftconfig(){
        return ResponseEntity.ok(new BaseResponse(kftConfigCheckService.toString()).isSuccess());
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
