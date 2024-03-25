package com.demo.runwu.controller;

import com.demo.runwu.models.*;
import com.demo.runwu.services.KFTCcsService;
import com.demo.runwu.services.KFTMerchantService;
import com.demo.runwu.services.KFTPayService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lycheepay.gateway.client.dto.ccs.OpenPackageRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.ActiveScanPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.PublicNoPayRespDTO;
import com.lycheepay.gateway.client.dto.initiativepay.TradeQueryRespDTO;
import com.lycheepay.gateway.client.dto.secmerchant.QuerySecMerchantResponseDTO;
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

    @Autowired
    private KFTCcsService kftCcsService;


    @RequestMapping({"", "/", "/index"})
    public String index() {
        return "KFT Pay";
    }

    @RequestMapping("/open-package")
    public ResponseEntity<?> openPackage(@RequestBody String data) {
        log.info(data);
        Gson gson = new Gson();
        Type type = new TypeToken<KFTPackage>() {
        }.getType();

        KFTPackage requestData = gson.fromJson(data, type);

        if (data != null) {
            try {
                OpenPackageRespDTO res = kftCcsService.init().openPackage(requestData);
                return ResponseEntity.ok(new DataResponse<>(res).isSuccess());
            } catch (Exception e) {
                return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
            }
        }
        return ResponseEntity.ok(new BaseResponse("data error").isError());
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
                KFTWorkOrder res = kftMerchantService.init().merchantBaseInfoAdd(requestData);
                return ResponseEntity.ok(new DataResponse<>(res).isSuccess());
            } catch (Exception e) {
                return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
            }
        }
        return ResponseEntity.ok(new BaseResponse("data error").isError());
    }

    @RequestMapping("/merchant-update")
    public ResponseEntity<?> merchantUpdate(@RequestBody String data) {
        log.info(data);
        Gson gson = new Gson();
        Type type = new TypeToken<KFTMerchantBaseInfo>() {
        }.getType();

        KFTMerchantBaseInfo requestData = gson.fromJson(data, type);

        if (data != null) {
            try {
                KFTWorkOrder res = kftMerchantService.init().merchantBaseInfoUpdate(requestData);
                return ResponseEntity.ok(new DataResponse<>(res).isSuccess());
            } catch (Exception e) {
                return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
            }
        }
        return ResponseEntity.ok(new BaseResponse("data error").isError());
    }

    @RequestMapping("/merchant-query")
    public ResponseEntity<?> merchantQuery(@RequestBody String data) {
        log.info(data);
        Gson gson = new Gson();
        Type type = new TypeToken<KFTMerchantQuery>() {
        }.getType();

        KFTMerchantQuery requestData = gson.fromJson(data, type);

        if (data != null) {
            try {
                QuerySecMerchantResponseDTO res = kftMerchantService.init().merchantBaseInfoQuery(requestData);
                return ResponseEntity.ok(new DataResponse<>(res).isSuccess());
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
                return ResponseEntity.ok(new DataResponse<>(res).isSuccess());
            } catch (Exception e) {
                return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
            }
        }
        return ResponseEntity.ok(new BaseResponse("data error").isError());
    }

    @RequestMapping("/zdsm")
    public ResponseEntity<?> zdsm(@RequestBody String data) {
        log.info(data);
        Gson gson = new Gson();
        Type type = new TypeToken<KFTPayZDSM>() {
        }.getType();

        KFTPayZDSM requestData = gson.fromJson(data, type);

        if (data != null) {
            try {
                ActiveScanPayRespDTO res = kftPayService.init().initiative_pay_zdsm(requestData);
                return ResponseEntity.ok(new DataResponse<>(res).isSuccess());
            } catch (Exception e) {
                return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
            }
        }
        return ResponseEntity.ok(new BaseResponse("data error").isError());
    }

    @RequestMapping("/pay-query")
    public ResponseEntity<?> payQuery(@RequestBody String data) {
        log.info(data);
        Gson gson = new Gson();
        Type type = new TypeToken<KFTPayQuery>() {
        }.getType();

        KFTPayQuery requestData = gson.fromJson(data, type);

        if (data != null) {
            try {
                TradeQueryRespDTO res = kftPayService.init().initiative_pay_query(requestData);
                return ResponseEntity.ok(new DataResponse<>(res).isSuccess());
            } catch (Exception e) {
                return ResponseEntity.ok(new BaseResponse(e.getMessage()).isError());
            }
        }
        return ResponseEntity.ok(new BaseResponse("data error").isError());
    }
}
