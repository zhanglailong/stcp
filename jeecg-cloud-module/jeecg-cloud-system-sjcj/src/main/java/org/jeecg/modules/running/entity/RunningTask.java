package org.jeecg.modules.running.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RunningTask implements Serializable {
	private static final long serialVersionUID = 1L;
	private String key; // 测试项ID
	private String title; // 测试项名称
	private List<RunningCase> children; // 测试用例列表
}
