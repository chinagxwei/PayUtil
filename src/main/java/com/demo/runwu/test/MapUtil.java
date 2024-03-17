package com.demo.runwu.test;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil
{

    public static String mapToString(Map<String, Object> map)
    {

        StringBuilder param = new StringBuilder();
        for (Iterator<Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();)
        {
            Entry<String, Object> e = it.next();
            if (e.getValue() != null && !isBlank(e.getValue().toString()))
            {
                param.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }
        }

        String str = param.toString().substring(0, param.length() - 1);
        return str;

    }
    

    public static String mapToString2(Map<String, String> map)
    {

        StringBuilder param = new StringBuilder();
        for (Iterator<Entry<String, String>> it = map.entrySet().iterator(); it.hasNext();)
        {
            Entry<String, String> e = it.next();
            if (e.getValue() != null && !isBlank(e.getValue().toString()))
            {
                param.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }
        }

        String str = param.toString().substring(0, param.length() - 1);
        return str;

    }

    // 判断字符串是否为空字符串
    private static boolean isBlank(CharSequence cs)
    {
        if ((cs == null) || (cs.length() == 0))
        {
            return true;
        }

        return false;
    }

    public static void main(String[] args)
    {
        String a = null;
        String b = "";
        String c = "1 2";
        String d = "123";

        System.out.println(isBlank(a));
        System.out.println(isBlank(b));
        System.out.println(isBlank(c));
        System.out.println(isBlank(d));
    }

}
