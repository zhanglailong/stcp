package org.jeecg.modules.system.util;

import java.util.Random;

/**
 * 随机字符串工具类
 * @Author: test
 * */
public class RandomStringUtil {
    /**
     * 生成一个指定位数的由字母和数字组成的字符串
     * @param length
     * @return
     */
    public static String getRamdomString(int length){
        String val = "";
        Random random = new Random();
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 生成token
     * @return
     */
    public static String getToken(){
        String token = getRamdomString(20);
        return token;
    }

    /**
     * 生成密钥
     * @return
     */
    public static String getSecretKey(){
        String secretKey = getRamdomString(16);
        return secretKey;
    }

    /**
     * 生成公钥
     * @return
     */
    public static String getPublicKey(){
        String publicKey = getRamdomString(28);
        return publicKey;
    }

}
