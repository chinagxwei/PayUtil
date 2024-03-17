package com.demo.runwu.test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class CommonUtil
{

    public static String bytesToBase64(byte[] bytes)
    {
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return base64;
    }

    public static byte[] base64ToBytes(String base64)
    {
        byte[] bytes = Base64.getDecoder().decode(base64);
        return bytes;
    }

    public static String bytesToHex(byte[] bytes)
    {
        final char[] allHex_array = "0123456789ABCDEF".toCharArray();

        char[] output_array = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            int number = bytes[j] & 0xFF;

            output_array[j * 2] = allHex_array[(number >> 4) & 0x0F];
            output_array[j * 2 + 1] = allHex_array[number & 0x0F];

        }
        String output = new String(output_array);
        return output;
    }

    public static byte[] hexToBytes(String hex)
    {
        final String allHex = "0123456789ABCDEF";

        if (hex.length() % 2 != 0)
        {
            throw new RuntimeException("length % 2 != 0");
        }

        // 转换为大写
        hex = hex.toUpperCase();

        char[] hex_array = hex.toCharArray();

        byte[] output = new byte[hex_array.length / 2];
        for (int j = 0; j < hex_array.length; j += 2)
        {
            char byteHigh_hex = hex_array[j];
            char byteLow_hex = hex_array[j + 1];

            int byteHigh = (allHex.indexOf(byteHigh_hex) & 0x0F) << 4;
            int byteLow = allHex.indexOf(byteLow_hex) & 0x0F;

            output[j / 2] = (byte) (byteHigh | byteLow);
        }
        return output;
    }

    public static String base64ToHex(String base64)
    {
        String hex = bytesToHex(base64ToBytes(base64));
        return hex;
    }

    public static String hexToBase64(String hex)
    {
        String base64 = bytesToBase64(hexToBytes(hex));
        return base64;
    }

    /**
     * "ISO-8859-1"
     */
    public static String base64ToStr(String base64)
    {

        try
        {
            String hex = new String(base64ToBytes(base64), "ISO-8859-1");
            return hex;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * "ISO-8859-1"
     */
    public static String strToBase64(String str)
    {
        try
        {
            String base64 = bytesToBase64(str.getBytes("ISO-8859-1"));
            return base64;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    // bytes数组长度为4
    public static int byteToInt(byte[] dataInput)
    {
        if (dataInput.length != 4)
        {
            throw new RuntimeException("length != 4");
        }

        int dataOutput_xx000000 = (dataInput[0] & 0xFF) << 24;
        int dataOutput_00xx0000 = (dataInput[1] & 0xFF) << 16;
        int dataOutput_0000xx00 = (dataInput[2] & 0xFF) << 8;
        int dataOutput_000000xx = (dataInput[3] & 0xFF);
        int dataOutput = dataOutput_xx000000 | dataOutput_00xx0000 | dataOutput_0000xx00 | dataOutput_000000xx;
        return dataOutput;
    }

    public static byte[] intToByte(int dataInput)
    {
        byte[] dataOutput = new byte[4];
        dataOutput[0] = (byte) ((dataInput >> 24) & 0xFF);
        dataOutput[1] = (byte) ((dataInput >> 16) & 0xFF);
        dataOutput[2] = (byte) ((dataInput >> 8) & 0xFF);
        dataOutput[3] = (byte) ((dataInput) & 0xFF);
        return dataOutput;
    }

}
