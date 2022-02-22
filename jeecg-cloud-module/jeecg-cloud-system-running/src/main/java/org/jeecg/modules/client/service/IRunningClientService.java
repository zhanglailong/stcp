package org.jeecg.modules.client.service;

import org.jeecg.modules.client.entity.RunningClient;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 云化工具客户端
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
public interface IRunningClientService extends IService<RunningClient> {
	/**
	 * 转换数据
	 * @param ip true
	 * @param state true
	 * */
	public void changeState(String ip,String state);
	/**
	 * 转换数据
	 * @param ip true
	 * @return 没有返回值
	 * */
	public RunningClient getByIp(String ip);
}
