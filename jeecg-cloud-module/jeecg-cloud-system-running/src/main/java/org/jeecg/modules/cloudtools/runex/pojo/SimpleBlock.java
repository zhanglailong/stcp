package org.jeecg.modules.cloudtools.runex.pojo;

import java.io.Serializable;

import org.jeecg.modules.cloudtools.runex.entity.RunParamsSet;

import lombok.Data;
@Data
/**
 * @author Administrator
 *
 * */
public class SimpleBlock implements Serializable{

	private static final long serialVersionUID = 1L;

	private String param;
	private String des;
	private String pattern;
	
	public SimpleBlock(RunParamsSet xrun) {
		this.param = xrun.getParam();
		this.des = xrun.getDes();
		this.pattern = xrun.getPattern();
	}
	
	
	
}
