package org.jeecg.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.oss.entity.OssFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Administrator
 *
 * */
public interface IOssFileService extends IService<OssFile> {
	/**
	 * 上传
	 * @param multipartFile 文件流
	 * @throws IOException
	 * @return 没有返回值
	 * */
	void upload(MultipartFile multipartFile) throws IOException;
	/**
	 * 删除
	 * @param ossFile 文件流
	 * @return 没有返回值
	 * */
	boolean delete(OssFile ossFile);

}
