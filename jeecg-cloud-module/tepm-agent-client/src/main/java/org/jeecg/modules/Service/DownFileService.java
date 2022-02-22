package org.jeecg.modules.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FileUtil;
import org.jeecg.modules.common.Result;
import org.jeecg.modules.common.WebToolUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
     * 本地文件上传
     *
     * @param request
     * @return
     */
    public Result<?> uploadLocal(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String type = StringUtils.isEmpty(request.getParameter("type")) ? "DEFAULT" : request.getParameter("type");
        String filePath = applicationName + prefixPath(type);
        // 获取上传文件对象
        List<MultipartFile> file = multipartRequest.getFiles("files");
        if (file.isEmpty()) {
            return Result.error("参数错误");
        }
        List<String> paths = new ArrayList<>();

        for (MultipartFile multipartFile : file) {
            if (file == null) {
                return Result.error("上传的文件不存在");
            }
            String oName = multipartFile.getOriginalFilename();
            //获取扩展名
            String suffixName = oName.substring(oName.lastIndexOf("."));

            //随机生成文件的名称
            String fileName = System.currentTimeMillis() + (int) (Math.random() * 10) + "_" + oName.substring(0, oName.lastIndexOf(".")) + suffixName;

            //生成目录
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String format = dateFormat.format(new Date());

            String OSName = System.getProperties().getProperty("os.name").toLowerCase();
            if (OSName.contains("windows")) {
                filePath = "c:/" + filePath;
            } else {
                filePath = "/home/" + filePath;
            }

            // 判断路径是否存在 如果不存在则需要创建
            File subPath = new File(filePath + format);
            if (!subPath.exists()) {
                subPath.mkdirs();
            }
            try {
                multipartFile.transferTo(new File(filePath + format + "/" + fileName));
                paths.add("/" + applicationName + prefixPath(type) + format + "/" + fileName);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                return Result.error("文件上传错误");
            }
            filePath = applicationName + prefixPath(type);
        }

        return Result.OK(paths);
    }


    /**
     * 本地文件下载
     *
     * @param response
     * @param filePath
     * @throws Exception
     */
    public String download(String filePath,HttpServletResponse response) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        response.setContentType("text/html;charset=UTF-8");
        String downloadFilePath = null;
        try {
            String OSName = System.getProperties().getProperty("os.name").toLowerCase();
            if (OSName.contains("windows")) {
                downloadFilePath = "c://" + filePath;
            }
            File file = new File(downloadFilePath);
            if (file.exists()) {
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                //下载文件名称删除前15位时间戳加随机数
//                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "iso-8859-1").substring(14));
                response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
                String name = file.getName();
                System.out.println(name);
                inputStream = new BufferedInputStream(new FileInputStream(file));
                outputStream = response.getOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                response.flushBuffer();
            } else {
                response.getWriter().write(JSON.toJSONString(Result.error("文件不存在")));
            }

        } catch (Exception e) {
            log.info("文件下载失败" + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return downloadFilePath;
    }

    /**
     * 下载并压缩
     *
     * @param path
     * @param response
     * @throws ZipException
     * @throws IOException
     */
    public String zipFile(String path, HttpServletResponse response) throws ZipException, IOException {
        String zipPath = "";
        // path是指欲下载的文件的路径。
        File file = new File(path);
        // 取得文件名。
        String filename = file.getName();
        String OSName = System.getProperties().getProperty(CommonConstant.OS_NAME).toLowerCase();
        if (OSName.contains(CommonConstant.WINDOWS)) {
            zipPath = CommonConstant.C_PATH + filename + "/" + filename + CommonConstant.ZIP;
        } else {
            zipPath = CommonConstant.HOME + filename + CommonConstant.ZIP;
        }
        File fileJmx = new File(zipPath);
        if (fileJmx.exists()) {
            //有文件就删除
            Boolean result = FileUtil.delFiles(fileJmx);
        } else {
            //没有就创建一个
            File files = new File(zipPath);
            File fileParent = files.getParentFile();
            fileParent.mkdirs();
            files.createNewFile();
        }
        // 生成的压缩文件
        ZipFile zipFile = new ZipFile(zipPath);
        zipFile.setFileNameCharset("GBK");
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        // 要打包的文件夹
        File currentFile = new File(path);
        File[] fs = currentFile.listFiles();
        // 遍历test文件夹下所有的文件、文件夹
        for (File f : fs) {
            if (f.isDirectory()) {
                zipFile.addFolder(f.getPath(), parameters);
            } else {
                zipFile.addFile(f, parameters);
            }
        }
        if (!zipFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }
        OutputStream outputStream = null;
        InputStream inputStream = null;
        inputStream = new BufferedInputStream(new FileInputStream(fileJmx));
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
        outputStream = response.getOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }
        response.flushBuffer();
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return zipPath;
    }

    /**
     * 下载压缩
     *
     * @param jsonObject
     * @param response
     * @throws Exception
     */
    public void downloadZip(@RequestBody JSONObject jsonObject, HttpServletResponse response) throws Exception {
        JSONArray filePaths = jsonObject.getJSONArray("filePaths");
        response.setContentType("application/force-download");
        //下载文件名称删除前15位时间戳加随机数
        response.addHeader("Content-Disposition", "attachment;fileName=" + new String((System.currentTimeMillis() + ".zip").getBytes("UTF-8"), "iso-8859-1"));
        OutputStream outputStream = response.getOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        for (int i = 0; i < filePaths.size(); i++) {
            String path = filePaths.getString(i);
            if (WebToolUtils.isWindowsOS()) {
                path = "c:" + path;
            } else {
                path = "/home" + path;
            }
            File file = new File(path);
            if (file.exists()) {
                InputStream is = null;
                try {
                    String[] split = path.split("/");
                    String fileName = split[split.length - 1];
                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    is = new FileInputStream(file);
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = is.read(b)) != -1) {
                        zipOutputStream.write(b, 0, len);
                    }
                    zipOutputStream.closeEntry();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        zipOutputStream.flush();
        zipOutputStream.close();
        response.flushBuffer();
    }

    private String prefixPath(String type) {
        String filePath = "/file";
        FilePathType filePathType;
        try {
            filePathType = FilePathType.valueOf(type);
        } catch (Exception e) {
            filePathType = FilePathType.DEFAULT;
        }
        switch (filePathType) {
            case ORDER_RELATED_DOC:
                filePath = filePath + "/ntepmOrder/relatedDoc/";
                break;
            case ORDER_TEST_DOC:
                filePath = filePath + "/ntepmOrder/testDoc/";
                break;
            case INVOICE_PDF:
                filePath = filePath + "/invoice/pdf/";
                break;
            default:
                filePath = filePath + "/default/";
                break;
        }
        return filePath;
    }

    private enum FilePathType {
        //订单相关文档
        ORDER_RELATED_DOC,

        //订单测试文档
        ORDER_TEST_DOC,

        //发票PDF
        INVOICE_PDF,

        //默认
        DEFAULT
    }

    /**
     * 文件下载
     *
     * @param url      请求路径
     * @param filePath 保存路径
     * @param fileName 文件名字
     */
    public static String saveUrlAs(String url, String filePath, String fileName) {
        //创建不同的文件夹目录
        File file = new File(filePath);
        //判断文件夹是否存在
        if (!file.exists()) {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            // 统一资源
            /*url  = "http://"+WebToolUtils.getLocalIP()+":8080" + url;*/
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            // 设置字符编码
            conn.setRequestProperty("Charset", "UTF-8");
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }
        /*//文件保存路径
        String osName = System.getProperties().getProperty(CommonConstant.OS_NAME).toLowerCase();
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstant.Y_M_D);
        String dateNowStr = sdf.format(new Date());
        String addPath = null;
        //报告保存位置
        if (osName.contains(CommonConstant.WINDOWS)) {
            addPath = "C:\\home\\tools\\" + dateNowStr + CommonConstant.SYMBOL +CommonConstant.UNDER_STAND + fileName;
        }else {
            addPath = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + fileName;
        }*/
        return filePath+fileName;
    }

    /**
     * 文件压缩
     *
     * @throws ZipException 压缩文件
     */
    public String zipFiles(String path) throws ZipException, IOException {
        // 生成的压缩文件
        String osName = System.getProperties().getProperty("os.name").toLowerCase();
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstant.Y_M_D);
        String dateNowStr = sdf.format(new Date());
        String addPath = null;
        if (osName.contains(CommonConstant.WINDOWS)) {
            addPath = pathJmx + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + CommonConstant.ZIP;
        } else {
            addPath = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + CommonConstant.ZIP;
        }
        ZipFile zipFile = new ZipFile(addPath);
        ZipParameters parameters = new ZipParameters();
        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        // 要打包的文件夹
        File currentFile = new File(path);
        File[] fs = currentFile.listFiles();
        // 遍历test文件夹下所有的文件、文件夹
        for (File f : fs) {
            if (f.isDirectory()) {
                zipFile.addFolder(f.getPath(), parameters);
            } else {
                zipFile.addFile(f, parameters);
            }
        }
        //把压缩包转化为流

        //上传
        File files = new File(addPath);
        FileInputStream fileInputStream = new FileInputStream(files);
        MultipartFile file = new MockMultipartFile("file", files.getName(), "text/plain", IOUtils.toByteArray(fileInputStream));;
        //获取文件名
        String name = file.getOriginalFilename();
        //获取文件后缀名,如果需要重新命名就需用拼接，原样保存直接用name
        // 设置文件上传路径
        String url = null;
        //报告保存位置
        if (osName.contains(CommonConstant.WINDOWS)) {
            url = pathJmx + dateNowStr + CommonConstant.SYMBOL +CommonConstant.UNDER_STAND + dateNowStr;
        }else {
            url = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr;
        }
        File dest = new File(url);
        // 检测是否存在目录，没有就创建
        if (!dest.exists()) {
            dest.mkdirs();
        }
        //以绝对路径保存文件
        String upStr = url + "\\" + name;
        file.transferTo(new File(upStr));
        return upStr;
    }

    /**
     * 文件上传
     * @param file 压缩文件
     * @return upStr
     * @throws Exception
     */
    public String importFile(MultipartFile file) throws Exception {
        //获取文件名
        String name = file.getOriginalFilename();
        //获取文件后缀名,如果需要重新命名就需用拼接，原样保存直接用name
        // 设置文件上传路径
        String osName = System.getProperties().getProperty(CommonConstant.OS_NAME).toLowerCase();
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstant.Y_M_D);
        String dateNowStr = sdf.format(new Date());
        String url = null;
        //报告保存位置
        if (osName.contains(CommonConstant.WINDOWS)) {
            url = pathJmx + dateNowStr + CommonConstant.SYMBOL +CommonConstant.UNDER_STAND + dateNowStr;
        }else {
            url = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr;
        }
        File dest = new File(url);
        // 检测是否存在目录，没有就创建
        if (!dest.exists()) {
            dest.mkdirs();
        }
        //以绝对路径保存文件
        String upStr = url + "\\" + name;
        file.transferTo(new File(upStr));
        return upStr;
    }

    public String downloadPhotos(String urlString,String filePath, String fileName) throws Exception {
        String downloadDir = filePath + fileName;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = new File(urlString);
        if (file.exists()) {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            outputStream = new FileOutputStream(downloadDir);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
        }
        outputStream.close();
        inputStream.close();
        return  filePath + fileName;
    }

    public String downFile(String path,DataInputStream dataInputStream){
        try {
            //从服务器下载path
            File files = new File(path);
            // 取得文件名。
            String fileName = files.getName();
            //保存位置
            DataOutputStream out = new DataOutputStream(new FileOutputStream(pathJmx + fileName));
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
     * @param is 文件流
     * @return path
     * @throws IOException
     */
    public  String inputStreamToFile(String path,DataInputStream is){
        try {
            File file = new File(path);
            String fileName = file.getName();
            String filePath = pathJmx + fileName;
            DataOutputStream outputStream = null;
            outputStream = new DataOutputStream(new FileOutputStream(filePath));
            BufferedReader d  = new BufferedReader(new InputStreamReader(is));
            String count;
            while ((count = d.readLine()) != null) {
                String u = count.toUpperCase();
                outputStream.writeBytes(u+ ",");
            }
            /*is.close();
            outputStream.close();*/
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
