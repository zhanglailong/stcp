package org.jeecg.modules.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

@Component
@Data
@Slf4j
/**
 * @author Administrator
 *
 * */
public class FtpUtil
{
    public static final int IMAGE_CUTSIZE = 300;

    @Value("${ftp.username}")
    /**用户名*/
    private String userName;

    @Value("${ftp.password}")
    /**密码*/
    private String passWord;

    @Value("${ftp.host}")
    /**ip地址*/
    private String ip;

    @Value("${ftp.port}")
    /**端口号*/
    private int port;

    @Value("${ftp.dir}")
    /**文件存放的目录*/
    private String currentDir;

    public static final String DIRSPLIT = "/";
    private String downloadDir;
    private FTPClient ftpClient = new FTPClient();

   /**上传*/
    public boolean uploadToFtp(InputStream buffIn, String fileName, boolean needDelete)
            throws FTPConnectionClosedException, IOException, Exception {
        boolean returnValue = false;
        try
        {
            // 建立连接
            connectToServer();
            // 设置传输二进制文件
            setFileType(FTP.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftpClient.disconnect();
                throw new IOException("failed to connect to the FTP Server:" + ip);
            }
            //进入文件目录
            ftpClient.changeWorkingDirectory(currentDir);
            // 上传文件到ftp,这里需要重新编码,否则上传中文文件会出现乱码
            returnValue = ftpClient.storeFile(new String(fileName.getBytes(),"utf-8"), buffIn);
            if (needDelete)
            {
                ftpClient.deleteFile(fileName);
            }
            // 输出操作结果信息
            if (returnValue)
            {
                log.info("uploadToFtp INFO: upload file  to ftp : succeed!");
            }
            else
            {
                log.error("uploadToFtp INFO: upload file  to ftp : failed!");
            }
            buffIn.close();
            // 关闭连接
            closeConnect();
        }
        catch (FTPConnectionClosedException e)
        {
            log.info("ftp连接被关闭！");
            throw e;
        }
        catch (Exception e)
        {
            returnValue = false;
            log.error("ERR : upload file  to ftp : failed! ");
            throw e;
        }
        finally
        {
            try {
                if (buffIn != null) {
                    buffIn.close();
                }
            } catch (Exception e) {
                log.error("ftp关闭输入流时失败！");
            }
            if (ftpClient.isConnected()) {
                closeConnect();
            }
        }
        return returnValue;
    }

    /**下载*/
    public InputStream downloadFile(String filename) throws IOException
    {
        InputStream in = null;
        try
        {
            /**建立连接*/
            connectToServer();
            // 设置传输二进制文件
            setFileType(FTP.BINARY_FILE_TYPE);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftpClient.disconnect();
                throw new IOException("failed to connect to the FTP Server:" + ip);
            }
            ftpClient.changeWorkingDirectory(currentDir);
            // ftp文件获取文件
            in = ftpClient.retrieveFileStream(filename);
        }
        catch (FTPConnectionClosedException e)
        {
            log.error("ftp连接被关闭！");
            throw e;
        }
        catch (Exception e)
        {
            log.error("ERR : upload file " + filename + " from ftp : failed!");
        }
        finally
        {
            if (ftpClient.isConnected()) {
                closeConnect();
            }
        }
        return in;
    }

    private void setFileType(int fileType) {
        try {
            ftpClient.setFileType(fileType);
        } catch (Exception e) {
            System.out.println("ftp设置传输文件的类型时失败！");
        }
    }

    public void closeConnect() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            System.out.println("ftp连接关闭失败！");

        }
    }

    private void connectToServer() throws FTPConnectionClosedException, Exception {
        if (!ftpClient.isConnected()) {
            int reply;
            try {
                ftpClient = new FTPClient();
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalPassiveMode();
                ftpClient.connect(ip, port);
                ftpClient.login(userName, passWord);
                ftpClient.changeToParentDirectory();
                ftpClient.setBufferSize(2*1024*1024*10);
                if(!existDirectory(currentDir)) {
                    createDirectory(currentDir);
                }
                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    log.info("connectToServer FTP server refused connection.");
                }
            } catch (FTPConnectionClosedException ex) {
                log.info("没有连接数！there are too many connected users,please try later");
                throw ex;
            } catch (Exception e) {
                log.info("登录ftp服务器失败");
                throw e;
            }
        }
    }

    /**Check the path is exist; exist return true, else false.*/
    public boolean existDirectory(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles("/");
        for (FTPFile ftpFile : ftpFileArr) {
            if (ftpFile.isDirectory()
                    && ftpFile.getName().equalsIgnoreCase(path)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean createDirectory(String pathName) throws IOException {
        boolean isSuccess = false;
        try {
            isSuccess = ftpClient.makeDirectory(pathName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    public static String getNoPointExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos + 1);
    }

    public static String getDateDir(Date dateParam) {
        Calendar cal = Calendar.getInstance();
        if (null != dateParam) {
            cal.setTime(dateParam);
        }
        int currentYear = cal.get(Calendar.YEAR);
        int currentMouth = cal.get(Calendar.MONTH) + 1;
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        return currentYear + FtpUtil.DIRSPLIT + currentMouth + FtpUtil.DIRSPLIT + currentDay;
    }
}