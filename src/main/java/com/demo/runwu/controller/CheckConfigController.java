package com.demo.runwu.controller;

import com.demo.runwu.models.BaseResponse;
import com.demo.runwu.services.KFTConfigCheckService;
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

    @RequestMapping("/kft-config")
    public ResponseEntity<?> kftconfig(){
        return ResponseEntity.ok(new BaseResponse<>(kftConfigCheckService).isSuccess());
    }

}
