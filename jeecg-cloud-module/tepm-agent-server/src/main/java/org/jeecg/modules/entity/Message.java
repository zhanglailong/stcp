package org.jeecg.modules.entity;

import lombok.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/3/4
 * @Description: 把图像数据封装在一个类里面并implements Serializable接口
 */
@Data
public class Message implements Serializable {
    // 文件名称
    private String fileName;
    // 文件长度
    private long fileLength;
    // 文件内容
    private byte[] fileContent;

    public Message(String filePath) throws IOException {
        File file = new File(filePath);
        this.fileLength = file.length();
        this.fileName = file.getName();

        FileInputStream FIS = new FileInputStream(filePath);
        byte[] bytes = new byte[(int) fileLength];
        FIS.read(bytes, 0, (int) fileLength);
        this.fileContent = bytes;
    }
}
