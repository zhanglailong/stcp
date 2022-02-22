package org.jeecg.modules.monitor.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeecg.modules.monitor.domain.RedisInfo;
import org.jeecg.modules.monitor.exception.RedisConnectException;

/**
 * @Author: test
 * */
public interface RedisService {

	/**
	 * 获取 redis 的详细信息
	 * @throws RedisConnectException
	 * @return List
	 */
	List<RedisInfo> getRedisInfo() throws RedisConnectException;

	/**
	 * 获取 redis key 数量
	 * @throws RedisConnectException
	 * @return Map
	 */
	Map<String, Object> getKeysSize() throws RedisConnectException;

	/**
	 * 获取 redis 内存信息
	 * @throws RedisConnectException
	 * @return Map
	 */
	Map<String, Object> getMemoryInfo() throws RedisConnectException;

}
