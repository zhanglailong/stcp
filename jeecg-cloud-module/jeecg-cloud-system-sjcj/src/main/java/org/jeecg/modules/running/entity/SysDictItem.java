package org.jeecg.modules.running.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SysDictItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private String key; // 测试类型ID
	private String title; // 测试类型名称
	private List<RunningTask> children; // 测试项列表
}
