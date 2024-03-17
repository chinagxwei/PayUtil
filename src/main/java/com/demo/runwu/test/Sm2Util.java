package com.demo.runwu.test;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import java.util.Arrays;

public class Sm2Util {

    public static Sm2KeyPair generatorKeyPair() {
        SM2 sm2 = SmUtil.sm2();
        byte[] privateKey_byte = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
        byte[] publicKey_byte = ((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false);

        String publicKey = HexUtil.encodeHexStr(publicKey_byte);
        String privateKey = HexUtil.encodeHexStr(privateKey_byte);

        Sm2KeyPair entity = new Sm2KeyPair();
        entity.setPublicKey(publicKey);
        entity.setPrivateKey(privateKey);
        return entity;
    }

    public static byte[] sign(String privateKey_hex, byte[] dataInput) {
        ECPrivateKeyParameters privateKeyParameters = BCUtil.toSm2Params(privateKey_hex);

        SM2 sm2 = new SM2(privateKeyParameters, null);
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        byte[] sign = sm2.sign(dataInput, null);

        return sign;
    }

    public static boolean verify(String publicKey_hex, byte[] signature, byte[] dataInput) {
        if (publicKey_hex.length() == 130) {
            publicKey_hex = publicKey_hex.substring(2);
        }
        String publicKey_x = publicKey_hex.substring(0, 64);
        String publicKey_y = publicKey_hex.substring(64, 128);
        ECPublicKeyParameters ecPublicKeyParameters = BCUtil.toSm2Params(publicKey_x, publicKey_y);

        SM2 sm2 = new SM2(null, ecPublicKeyParameters);
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        boolean verify = sm2.verify(dataInput, signature);

        return verify;
    }

    public static byte[] encrypt(String publicKey_hex, byte[] dataInput) {
        if (publicKey_hex.length() == 130) {
            publicKey_hex = publicKey_hex.substring(2);
        }
        String publicKey_x = publicKey_hex.substring(0, 64);
        String publicKey_y = publicKey_hex.substring(64, 128);
        ECPublicKeyParameters ecPublicKeyParameters = BCUtil.toSm2Params(publicKey_x, publicKey_y);

        SM2 sm2 = new SM2(null, ecPublicKeyParameters);
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        byte[] encrypt = sm2.encrypt(dataInput, KeyType.PublicKey);

        return encrypt;
    }

    public static byte[] decrypt(String privateKey_hex, byte[] dataInput) {
        ECPrivateKeyParameters privateKeyParameters = BCUtil.toSm2Params(privateKey_hex);

        SM2 sm2 = new SM2(privateKeyParameters, null);
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C3C2);
        byte[] decrypt = sm2.decrypt(dataInput, KeyType.PrivateKey);

        return decrypt;
    }

    private static void testEncryptAndDecrypt() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("生成公钥私钥");

        Sm2KeyPair keyPair = generatorKeyPair();
        String publicKey = keyPair.getPublicKey();
        String privateKey = keyPair.getPrivateKey();

        System.out.println("publicKey=" + publicKey);
        System.out.println("privateKey=" + privateKey);
        System.out.println();

        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("待加密的内容");

        String text = "123456789";
        System.out.println("text=" + text);
        System.out.println();

        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("加密解密");

        byte[] dataOutput_encrypted = encrypt(publicKey, text.getBytes());
        System.out.println("加密后的内容=" + CommonUtil.bytesToBase64(dataOutput_encrypted));
        System.out.println("加密后的内容=" + CommonUtil.bytesToHex(dataOutput_encrypted));
        System.out.println();

        byte[] dataOutput_decrypted = decrypt(privateKey, dataOutput_encrypted);
        System.out.println("解密后的内容=" + CommonUtil.bytesToBase64(dataOutput_decrypted));
        System.out.println("解密后的内容=" + CommonUtil.bytesToHex(dataOutput_decrypted));
        System.out.println("解密后的内容=" + new String(dataOutput_decrypted));

    }

    private static void testSignAndVerify() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("生成公钥私钥");

        Sm2KeyPair keyPair = generatorKeyPair();
        String publicKey = keyPair.getPublicKey();
        String privateKey = keyPair.getPrivateKey();

        System.out.println("publicKey=" + publicKey);
        System.out.println("privateKey=" + privateKey);
        System.out.println();

        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("待加密的内容");

        String text = "{\"privateKey\":\"E3594E983545578B7DF95A851AFFAD17585D73DF5964630C169C14B38B12C1FE\",\"needSign\":\"body.amount=1001&body.clientIp=219.232.77.127&body.merchantOrderNo=hw1700584427&body.notifyUrl=https:\\/\\/pay.zaojiaweb.top\\/notifyUrl.php&body.productName=\\u6d3b\\u52a8\\u8d39\\u75281&merchantMemberNo=21070212563100000040&serviceCode=h5.create&signType=SM2&version=1\"}";
        System.out.println("text=" + text);
        System.out.println();

        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("签名验签");

        byte[] dataOutput_sign = sign(privateKey, text.getBytes());
        System.out.println("dataOutput_sign=" + Arrays.toString(dataOutput_sign));
        boolean dataOutput_verify = verify(publicKey, dataOutput_sign, text.getBytes());
        System.out.println("dataOutput_verify=" + dataOutput_verify);

    }

    public static void main(String[] args) {
        testEncryptAndDecrypt();

        testSignAndVerify();

    }

}
