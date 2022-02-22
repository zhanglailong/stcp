package org.jeecg.modules.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.common.Result;
import org.jeecg.modules.entity.ToolLog;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/3/2
 * @description 用一句话描述该文件做什么)
 */
@Api(tags = "工具管控")
@RestController
@RequestMapping("/socket/client/tools/")
@Slf4j
public class ToolsDataController {

    /**
     * 根据IP查询
     *
     * @param
     * @return
     */
    @PostMapping(value = "/start")
//    public Result<?> startJmeter(@RequestParam(name="jmx",required=true) String jmx) {
    public Result<?> startJmeter(String jmx) {
        try {
            System.out.println("jmx:" + jmx);
            String bin = "D:\\Jmeter\\apache-jmeter-5.0\\bin\\";
            File outFile = new File(bin + "output");
            File testFile = new File("testLogFile");
            if (delFiles(outFile)) {
                System.out.println("删除成功");
            }
            if (testFile.exists() && testFile.isFile()) {
                if (testFile.delete()) {
                    System.out.println("删除单个文件" + testFile.getName() + "成功！");
                }
            }
            String command = bin + "jmeter -n -t " + bin + "templates\\jmeter_api_sample.jmx -l testLogFile -e -o " + bin + "output";
            Process process = Runtime.getRuntime().exec("cmd /c " + command);
            //关闭流释放资源
            System.out.println("关闭流释放资源");
            if (process != null) {
                process.getOutputStream().close();
            }
            print(process);
            System.out.println("结束");
            String dataResult = readFileContent(bin + "output\\content\\js\\dashboard.js");
            if (StringUtils.isEmpty(dataResult)) {
                return Result.error("启动失败，没找到日志文件");
            } else {
                ToolLog toolLog = new ToolLog();
                String[] logs = dataResult.split(",");
            }
            return Result.OK(dataResult);

        } catch (Exception e) {
            return Result.error("异常：" + e.getMessage());
        }
    }

//    public Result<?> startJmeter(String jmx) {
//        try {
//            System.out.println("jmx:"+jmx);
//            File outFile = new File("D:\\Jmeter\\apache-jmeter-5.0\\bin\\output");
//            File testFile = new File("testLogFile");
//            if (delFiles(outFile)) {
//                System.out.println("删除成功");
//            }
//            if (testFile.exists() && testFile.isFile()) {
//                if (testFile.delete()) {
//                    System.out.println("删除单个文件" + testFile.getName() + "成功！");
//                }
//            }
//            String command = "D:\\Jmeter\\apache-jmeter-5.0\\bin\\jmeter -n -t D:\\Jmeter\\apache-jmeter-5.0\\bin\\templates\\jmeter_api_sample.jmx -l testLogFile -e -o D:\\Jmeter\\apache-jmeter-5.0\\bin\\output";
//            Process process = Runtime.getRuntime().exec("cmd /c " + command);
//            //关闭流释放资源
//            System.out.println("关闭流释放资源");
//            if(process != null){
//                process.getOutputStream().close();
//            }
//            print(process);
//            System.out.println("结束");
//            String dataResult = readFileContent("D:\\Jmeter\\apache-jmeter-5.0\\bin\\output\\content\\js\\dashboard.js");
//            if (StringUtils.isEmpty(dataResult)){
//                return Result.error("启动失败，没找到日志文件");
//            }
//            return Result.OK(dataResult);
//
//        }catch (Exception e){
//            return Result.error("异常："+e.getMessage());
//        }
//    }

    /**
     * 获取报表数据
     *
     * @param fileName
     * @return
     */
    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            String tempStr;
            reader = new BufferedReader(new FileReader(file));
            while ((tempStr = reader.readLine()) != null) {
                if (tempStr.contains("statisticsTable")) {
                    tempStr = tempStr.trim();
                    String dataStr = tempStr.substring(tempStr.indexOf("Total") + 7, tempStr.indexOf("isController") - 4);
                    sbf.append(dataStr);
                    System.out.println("dataStr:" + dataStr);
                    break;
                }
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public static void print(Process process) throws Exception {
        //以下为读取cmd窗口返回的内容
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line = null;
        System.out.println("以下为读取cmd窗口返回的内容");
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
    }

    public static boolean delFiles(File file) {
        boolean result = false;
        //目录
        if (file.isDirectory()) {
            File[] childrenFiles = file.listFiles();
            for (File childFile : childrenFiles) {
                result = delFiles(childFile);
                if (!result) {
                    return result;
                }
            }
        }
        //删除 文件、空目录
        result = file.delete();
        return result;
    }

    public void velocity() {
        //设置velocity资源加载器
//        Properties prop = new Properties();
//        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//        Velocity.init(prop);
//        String mainPath = config.getString("mainPath");
//        mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;
//        //封装模板数据
//        Map<String, Object> map = new HashMap<>();
//        map.put("tableName", tableEntity.getTableName());
//        map.put("comments", tableEntity.getComments());
//        map.put("pk", tableEntity.getPk());
//        map.put("className", tableEntity.getClassName());
//        map.put("classname", tableEntity.getClassname());
//        map.put("pathName", tableEntity.getClassname().toLowerCase());
//        map.put("columns", tableEntity.getColumns());
//        map.put("hasList", hasList);
//        map.put("mainPath", mainPath);
//        map.put("package", config.getString("package"));
//        map.put("moduleName", config.getString("moduleName"));
//        map.put("author", config.getString("author"));
//        map.put("email", config.getString("email"));
//        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
//        VelocityContext context = new VelocityContext(map);
//
//        //获取模板列表
//        List<String> templates = getMongoChildTemplates();
//        for (String template : templates) {
//            //渲染模板
//            StringWriter sw = new StringWriter();
//            Template tpl = Velocity.getTemplate(template, "UTF-8");
//            tpl.merge(context, sw);
//            try {
//                //添加到zip
//                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"))));
//                IOUtils.write(sw.toString(), zip, "UTF-8");
//                IOUtils.closeQuietly(sw);
//                zip.closeEntry();
//            } catch (IOException e) {
//                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
//            }
//        }
    }
}
