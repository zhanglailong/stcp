package org.jeecg.modules.running.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class RunningCase implements Serializable {
	private static final long serialVersionUID = 1L;
	private String key; // 测试用例ID
	private String title; // 测试用例名称
}
