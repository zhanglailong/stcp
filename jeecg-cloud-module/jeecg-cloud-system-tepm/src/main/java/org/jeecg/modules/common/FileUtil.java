package org.jeecg.modules.common;

import java.io.File;

/**
 * @author hxsi
 * @description 文件处理
 * @date 2021年06月08日 10:51
 */
public class FileUtil {

    /**
     * 递归删除某个目录及目录下所有的子文件和子目录
     * @param file 文件或者目录
     * @return 删除结果
     */
    public static boolean delFiles(File file) {
        boolean result = false;
        if (file.isDirectory()) {
            File[] childrenFiles = file.listFiles();
            for (File childFile : childrenFiles) {
                result = delFiles(childFile);
                if (!result) {
                    return result;
                }
            }
        }
        //删除文件、空目录
        result = file.delete();
        return result;
    }
}
