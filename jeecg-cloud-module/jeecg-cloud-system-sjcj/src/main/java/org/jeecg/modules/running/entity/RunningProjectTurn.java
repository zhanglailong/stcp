package org.jeecg.modules.running.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RunningProjectTurn implements Serializable {
	private static final long serialVersionUID = 1L;
	private String key; // 轮次ID
	private String title; // 轮次名称
	private List<SysDictItem> children; // 测试类型列表
}
