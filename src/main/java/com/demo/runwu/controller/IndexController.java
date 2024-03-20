package com.demo.runwu.controller;

import com.demo.runwu.models.NeedSign;
import com.demo.runwu.test.CommonUtil;
import com.demo.runwu.test.Sm2Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class IndexController {

    @RequestMapping({"", "/", "/index"})
    @ResponseBody
    public String index() {
        return "Pay";
    }

    @RequestMapping("/sign")
    @ResponseBody
    public String sign(@RequestBody String data) {
        log.info(data);
        Gson gson = new Gson();
        Type type = new TypeToken<NeedSign>() {
        }.getType();
        NeedSign responseData = gson.fromJson(data, type);
        if (data != null) {
            log.info(responseData.privateKey);
            log.info(responseData.needSign);
            byte[] dataOutput_sign = Sm2Util.sign(responseData.privateKey, responseData.needSign.getBytes(StandardCharsets.UTF_8));
            return CommonUtil.bytesToBase64(dataOutput_sign);
        }

        return "param error";
    }

}
