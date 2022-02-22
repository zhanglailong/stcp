package org.jeecg.modules.mass.vo;

import java.io.Serializable;

import lombok.Data;

@Data
/**
 * @Author: test
 * */
public class CustomSystemTreeVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String templateId;
	
	private String templateName;
	
	private String id;
	
	private String masterName;
	
	private String pid;
	
	private String customWeight;
	
	private String elementLevel;
	
	private String checked;
	
	private String resultId;
	
	private String score;
	
	private String needFlag;

}
