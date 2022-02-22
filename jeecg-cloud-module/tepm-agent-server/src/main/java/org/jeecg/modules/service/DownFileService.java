package org.jeecg.modules.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;


@Slf4j
@Service
public class DownFileService {
    @Value("${spring.application.name}")
    private String applicationName;
    /**
     * 文件保存位置
     */
    @Value(value = "${testtool.pathJmx}")
    public String pathJmx;


    /**
     * 文件保存在本地返回一个数据流
     * @param path
     * @return
     */
    public DataInputStream downFile(String path) {
        /*String pathJmx= "C:\\home\\tools\\";*/
        try {
            //从服务器下载path
            File files = new File(path);
            // 取得文件名。
            String fileName = files.getName();
            //保存位置
            DataInputStream in = new DataInputStream(new FileInputStream(path));
            DataOutputStream out = new DataOutputStream(new FileOutputStream(pathJmx + fileName));
            BufferedReader d = new BufferedReader(new InputStreamReader(in));
            String count;
            while ((count = d.readLine()) != null) {
                String u = count.toUpperCase();
                out.writeBytes(u+ ",");
            }
            FileInputStream fis = new FileInputStream(pathJmx + fileName);

            BufferedInputStream bis = new BufferedInputStream(fis);

            DataInputStream dis = new DataInputStream(bis);
            /*d.close();
            out.close();*/
            return dis;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用流保存文件
     *
     * @param path
     * @param dataInputStream
     * @return
     */
    public static String downFile(String path, DataInputStream dataInputStream) {
        try {
            //保存位置
            DataOutputStream out = new DataOutputStream(new FileOutputStream(path));
            BufferedReader d = new BufferedReader(new InputStreamReader(dataInputStream));
            String count;
            while ((count = d.readLine()) != null) {
                String u = count.toUpperCase();
                System.out.println(u);
                out.writeBytes(u + "  ,");
            }
            d.close();
            out.close();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把流转换为文件
     * @param path
     * @param is
     * @return
     * @throws IOException
     */
    public  String inputStreamToFile(String path,DataInputStream is) throws IOException {
        File file = new File(path);
        String name = file.getName();
        String paths = "F:\\Program Files\\Tencent\\" + name;
        DataOutputStream outputStream = null;
        outputStream = new DataOutputStream(new FileOutputStream(paths));
        BufferedReader d  = new BufferedReader(new InputStreamReader(is));
        String count;
        while ((count = d.readLine()) != null) {
            String u = count.toUpperCase();
            outputStream.writeBytes(u+ ",");
        }
        is.close();
        outputStream.close();
        return paths;
    }



   /* public static void main(String[] args) throws IOException {
        String path = "C:\\assert.c";
        //新文件
        String pathJmx = "F:\\Program Files\\shx.c";
        DataInputStream dataInputStream = downFile(path);
        //生成新的文件
        String pas = inputStreamToFile(pathJmx, dataInputStream);
        System.out.println("路径:" + pas);
    }*/

    public  DataInputStream readFiles(String path) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(path))) {
            int bytesRead = 0;
            int bytesToRead = 1024;
            byte[] b = new byte[bytesToRead];
            while (bytesRead < bytesToRead) {
                int result = in.read(b, bytesRead,bytesToRead - bytesRead);
                if (result == -1) {
                    break;
                }
                bytesRead += result;
            }
            return in;
        } catch (IOException ex) {
            ex.getMessage();
        }
        return null;
    }
}
