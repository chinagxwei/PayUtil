package com.demo.runwu.controller;

import com.demo.runwu.test.Sm2Util;

public class Test {

    public final String publicKey = "04a311679947de87b1c324bd72102f829b0b3194095de0f0fa0761f99b9352f6544db7130c12fb67a5d87f522d26f9925819ed986d661139dca857bbf45976a2cd";

    public final String privateKey = "0081a1aaf1b1230bb4538266c6e80e9ef8489d6bdfc12d312ad9c5a1a1cfe9ee83";

    public static void main(String[] args) {
        String test = "123";
        Test t = new Test();
        byte[] dataOutput_encrypted = Sm2Util.encrypt(t.publicKey, test.getBytes());

        byte[] dataOutput_decrypted = Sm2Util.decrypt(t.privateKey, dataOutput_encrypted);
    }
}
