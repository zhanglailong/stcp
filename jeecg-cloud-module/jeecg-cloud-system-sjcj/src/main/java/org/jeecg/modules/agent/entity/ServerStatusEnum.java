package org.jeecg.modules.agent.entity;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

/**
 * @author zlf
 */
@Getter
public enum ServerStatusEnum {

	/*
	 * ACTIVE 运行 BUILD 创建 RESIZE 调整大小或迁移 SHUTOFF 关机 VERIFY_RESIZE 确认调整大小 PAUSED 暂停
	 * SUSPENDED 挂起 ERROR 错误
	 * 
	 */
	ACTIVE("ACTIVE", "运行"), BUILD("BUILD", "创建"), RESIZE("RESIZE", "调整大小或迁移"), SHUTOFF("SHUTOFF", "确认调整大小"),
	PAUSED("PAUSED", "暂停"), SUSPENDED("SUSPENDED", "挂起"), ERROR("ERROR", "错误"),;

	ServerStatusEnum(String status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	@EnumValue
	private final String status;
	@JsonValue
	private final String desc;

	public static ServerStatusEnum toEnum(String status) {
		if (StringUtils.isEmpty(status)) {
			return null;
		}
		for (ServerStatusEnum item : ServerStatusEnum.values()) {
			if (item.getStatus().equals(status)) {
				return item;
			}
		}
		return null;
	}

}