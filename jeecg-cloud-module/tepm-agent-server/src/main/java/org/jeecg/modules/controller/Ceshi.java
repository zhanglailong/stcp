package org.jeecg.modules.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.SneakyThrows;
import org.jeecg.common.util.PasswordUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/2/1
 * @Description: ceshi
 */
public class Ceshi {
    @SneakyThrows
    public static void main(String[] args) {
        TimeInterval timer = DateUtil.timer();
        while (true){
            //花费分钟数
            System.out.println("秒:"+timer.interval()+",分:"+timer.intervalMinute());
            TimeUnit.SECONDS.sleep(3);
        }
//        String userpassword = PasswordUtil.encrypt("zt", "123456", "RCGTeGiH");
//        System.out.println("userpassword:"+userpassword);

//        int i = 1;
//        while (true) {
//            System.out.println(i % 1);
//            System.out.println(i % 2);
//            System.out.println(i % 3);
//            System.out.println(i % 4);
//            System.out.println(i % 5);
//            System.out.println(i % 6);
//            System.out.println(i % 7);
//            System.out.println(i % 8);
//            System.out.println(i % 9);
//            System.out.println(i % 10);
//            System.out.println("----------------------" + i + "----------------------");
//            i++;
//            if (i > 10) {
//                break;
//            }
//        }

    }
}
