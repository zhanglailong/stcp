package org.jeecg.modules.sjcj.file.entity;

import java.io.Serializable;

/**
 * @Author: test
 * */
public class FileView implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String filePath;
	
	private String fileName;
	
	private String fileType;
	
	private String fileCreateTime;
	
	private String fileSize;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileCreateTime() {
		return fileCreateTime;
	}

	public void setFileCreateTime(String fileCreateTime) {
		this.fileCreateTime = fileCreateTime;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
}
