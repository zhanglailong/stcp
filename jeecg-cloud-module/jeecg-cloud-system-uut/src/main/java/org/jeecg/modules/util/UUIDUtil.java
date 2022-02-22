package org.jeecg.modules.util;

import java.util.Random;
import java.util.UUID;


/**
 *
 * @ClassName:UUIDUtil.java     
 * @version v2.3
 * @author: http://www.wgstart.com
 * @date: 2019年11月16日
 * @Description: UUIDUtil.java
 * @Copyright: 2019-2020 wgcloud. All rights reserved.
 *
 */
public class UUIDUtil {
	
	public static String getUuId(){
		return String.valueOf(UUID.randomUUID()).replace("-", "");
	}
	
	/**
	 * 随机6位数字
	 * @return
	 */
	public static String getRandomSix(){
		return ""+new Random().nextInt(999999);
	}
	
}
