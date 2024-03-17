package com.demo.runwu.test;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;

import java.io.UnsupportedEncodingException;

public class RsaUtil
{

    public static String sign(String data, String key)
    {
        Sign signUtil = new Sign(SignAlgorithm.SHA256withRSA, key, null);
        byte[] dataSigned = null;
        try
        {
            dataSigned = signUtil.sign(data.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        String sign = Base64.encode(dataSigned);
        return sign;
    }

    public static boolean verify(String data, String sign, String key)
    {
        Sign signUtil = new Sign(SignAlgorithm.SHA256withRSA, null, key);
        boolean isOk = false;
        try
        {
            isOk = signUtil.verify(data.getBytes("UTF-8"), Base64.decode(sign));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return isOk;
    }

    public static void main(String[] args)
    {
        String demoPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYAvpWipESBUH34WkZIQW5AF7PYfnciZvlmpziPekJ/XEvYAT4lcIB+3q0C4lpWuP5mjuXBYN53Tx8u/NkNNOY0smiGxYlBEaBxPwCjxT3d4WLA9m4BOwqeTB7YELCX2638MUlLDrovi4hzfVv3fN4WDVeBxIoNRAr54ZnLyr/TQIDAQAB";
        String demoPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJgC+laKkRIFQffhaRkhBbkAXs9h+dyJm+WanOI96Qn9cS9gBPiVwgH7erQLiWla4/maO5cFg3ndPHy782Q005jSyaIbFiUERoHE/AKPFPd3hYsD2bgE7Cp5MHtgQsJfbrfwxSUsOui+LiHN9W/d83hYNV4HEig1ECvnhmcvKv9NAgMBAAECgYBaRGJt44jz/4VF8mfbkT15t1uVoKOkL18RADgrLQJmeUauEEHDcAKt9KzPn44wrtVz0f+S1aZWRmb11xJYLfp7+bpTbfA4bCb9bLncIUVedmdg4ifYjL/KF4UMX7H4rg4CGoDfwcCW+LvdZgURw7lbsaXPV3Aj1cGcjUMmVtp1bQJBANRpcU8fanZRl5Okz62GSUqYVtuo4Kho3+nbWbgEqJg8akXMdXxpDsuOR25q68NYP6vkyFf/GJOJCm3pBN0J+lsCQQC3NIzqLDgv7uRdzNX9p9z0MQWnIwQaCfWOPVoymqbxqrHctWdbP9bLckJjDPsfdc1pGK8eqYbaaY9nDae4Qw13AkEAw6LOKsrLmmgxzFlqse7RyDOhJDVZnaLiQJi3/KXFxlk2pEzZFJoKd2722Xr+5G9+TUqtP2cDcrcwdOS+hvsuqwJAXiL/gVhFasPaziPxlQwLKeKCoBYLBoI1jegSj8wNmkH2cI051ZZg/Vrxp8nEw66ZZLZNTXkYlOQGzpIiqzixEwJAMyTsKg1pptjDaOrEg6zyL/gttzJFJWG5KP63XPsoML1iDxngDOY/VTd0nTFA+x+c/D0mKBZ0Zk+1asHLv+Yzfg==";

        String sign = RsaUtil.sign("1", demoPrivateKey);
        boolean verify = RsaUtil.verify("1", sign, demoPublicKey);

        System.out.println(verify);

    }

}
