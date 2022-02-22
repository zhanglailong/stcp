package org.jeecg.modules.running.task.vo;

import lombok.Data;

import java.io.Serializable;

@Data
/**
 * @Author: test
 * */
public class CaseTreeVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String rootId;
    /**质量特性*/
	private String rootName;
	
	private String parentId;
	/**质量子特性*/
	private String parentName;
	
	private String id;
	/**度量元名称*/
	private String name;

}
