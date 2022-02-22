package org.jeecg.modules.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author hxsi
 * @description 文件上传下载
 * @date 2021年06月09日 14:57
 */
@Slf4j
@Api(tags = "文件上传下载")
@RestController
@RequestMapping("/file")
public class UpDownFileController {
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 本地文件上传
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/uploadLocal")
    @ResponseBody
    public Result<?> uploadLocal(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String filePath = applicationName;
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
                filePath = CommonConstant.C_PATH + filePath;
            } else {
                filePath = CommonConstant.HOME + filePath;
            }

            // 判断路径是否存在 如果不存在则需要创建
            File subPath = new File(filePath + format);
            if (!subPath.exists()) {
                subPath.mkdirs();
            }
            try {
                multipartFile.transferTo(new File(filePath + format + "/" + fileName));
                paths.add("/" + applicationName + format + "/" + fileName);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                return Result.error("文件上传错误");
            }
            filePath = applicationName;
        }

        return Result.OK(paths);
    }


    @AutoLog(value = "文件下载压缩")
    @ApiOperation(value = "文件下载压缩/", notes = "文件下载压缩")
    @GetMapping(value = "/downloadZip")
    private void zipFile(String path,HttpServletResponse response) throws ZipException, IOException {
        String zipPath ="";
        // path是指欲下载的文件的路径。
        File file = new File(path);
        // 取得文件名。
        String filename = file.getName();
        String OSName = System.getProperties().getProperty(CommonConstant.OS_NAME).toLowerCase();
        if (OSName.contains(CommonConstant.WINDOWS)) {
            zipPath = CommonConstant.C_PATH + filename + "/" + filename+CommonConstant.ZIP;
        } else {
            zipPath = CommonConstant.HOME + filename + CommonConstant.ZIP;
        }
        File fileJmx = new File(zipPath);
        if (fileJmx.exists()){
            //有文件就删除
            Boolean result = FileUtil.delFiles(fileJmx);
        }else {
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
    }

    /*本地文件下载*/
    @AutoLog(value = "本地文件下载")
    @ApiOperation(value = "本地文件下载/", notes = "本地文件下载")
    @GetMapping(value = "/downloadLocal")
    public void download(HttpServletResponse response, String filePath) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        response.setContentType("text/html;charset=UTF-8");
        try {
            String downloadFilePath;
            String OSName = System.getProperties().getProperty("os.name").toLowerCase();
            if (OSName.contains("windows")) {
                downloadFilePath = filePath;
            } else {
//                downloadFilePath = "/home" + filePath;
                downloadFilePath = filePath;
            }
            File file = new File(downloadFilePath);
            if (file.exists()) {
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                //下载文件名称删除前15位时间戳加随机数
//                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "iso-8859-1").substring(14));
                response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
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

    }

    @PostMapping(value = "/downloadZipLocal")
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
}
