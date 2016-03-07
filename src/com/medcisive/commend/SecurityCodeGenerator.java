package com.medcisive.commend;

import java.util.*;

/**
 *
 * @author vhapalchambj
 */
public class SecurityCodeGenerator {

    public Random rand;
    private String extMap = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public SecurityCodeGenerator() {
        rand = new Random();
    }

    public String getCode(int digits) {
        String retStr = "";
        int index = 0;
        for (int i = 0; i < digits; i++) {
            index = rand.nextInt(62);
            retStr += extMap.charAt(index);
        }
        return retStr;
    }
}
