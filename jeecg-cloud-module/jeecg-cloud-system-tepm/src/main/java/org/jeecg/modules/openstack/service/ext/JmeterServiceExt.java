package org.jeecg.modules.openstack.service.ext;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.samplers.SampleSaveConfiguration;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.*;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FileUtil;
import org.jeecg.modules.openstack.service.OpenStackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hxsi
 * @description jmeter测试工具集成
 * @date 2021年06月03日 9:49
 */
@Service
@Slf4j
public class JmeterServiceExt extends OpenStackService {
    /**
     * 环境里面jmeter位置
     */
    @Value(value = "${testtool.jmeterHome}")
    public String jmeterHome;
    /**
     * 文件保存位置
     */
    @Value(value = "${testtool.pathJmx}")
    public String pathJmx;
    /**
     * 调用jmeter工具生成报告
     * @param planName 测试名称
     * @param numThreads 线程数
     * @param loops 循环次数
     * @param url 域名或者ip
     * @param port 端口号
     * @param httpRequest 请求方式
     * @param duration 并发时间
     * @param path 测试用例路径
     * @param request 请求参数
     * @param jmeterPath 测试用例地址
     */
    public String getJmeter(String planName,int numThreads,String loops,String url,String port,String httpRequest,int duration,String path,String request,String jmeterPath) throws IOException {
        String jmxPath = null;
        String jtlPath = null;
        String htmlPath = null;
        String osName = System.getProperties().getProperty(CommonConstant.OS_NAME).toLowerCase();
        if (StringUtils.isNotEmpty(planName)){
            SimpleDateFormat sdf = new SimpleDateFormat(CommonConstant.Y_M_D);
            String dateNowStr = sdf.format(new Date());
            if (osName.contains(CommonConstant.WINDOWS)) {
                //测试用例保存路径
                jmxPath = pathJmx + dateNowStr + CommonConstant.SYMBOL + planName + dateNowStr + CommonConstant.SYMBOL + planName + CommonConstant.JMX;
                jtlPath = pathJmx + dateNowStr + CommonConstant.SYMBOL + planName + dateNowStr + CommonConstant.SYMBOL + planName + CommonConstant.JTL;
                //测试报告
                htmlPath = pathJmx + dateNowStr + CommonConstant.SYMBOL + planName + dateNowStr + CommonConstant.SYMBOL + planName;
            } else {
                //测试用例保存路径
                jmxPath = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + planName + dateNowStr + CommonConstant.SYMBOL + planName + CommonConstant.JMX;
                jtlPath = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + planName + dateNowStr + CommonConstant.SYMBOL + planName + CommonConstant.JTL;
                //测试报告
                htmlPath = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + planName + dateNowStr + CommonConstant.SYMBOL + planName;
            }
            //判断文件是否存在
            File fileJmx = null;
            if (osName.contains(CommonConstant.WINDOWS)) {
                fileJmx = new File(pathJmx + dateNowStr + CommonConstant.SYMBOL + planName + dateNowStr);
            }else {
                fileJmx = new File(CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + planName + dateNowStr);
            }
            if (fileJmx.exists()){
                //有文件就删除
                Boolean result = FileUtil.delFiles(fileJmx);
                if (!result){
                    log.error(planName + dateNowStr + ":文件删除失败");
                    return null;
                }
            }
            File file = new File(jmxPath);
            File fileParent = file.getParentFile();
            if(!fileParent.exists()){
                fileParent.mkdirs();
            }
            file.createNewFile();

        }
        //获取当前环境的jmeter工具
        JMeterUtils.setJMeterHome(jmeterHome);
        JMeterUtils.loadJMeterProperties(JMeterUtils.getJMeterBinDir() + "/jmeter.properties");

        //创建一个计划
        TestPlan testPlan = getTestPlan(planName);

        // 获取设置循环控制器
        LoopController loopController = getLoopController(loops);

        //创建线程组
        ThreadGroup threadGroup = getThreadGroup(loopController,numThreads,duration);;

        //创建http请求
        HTTPSamplerProxy httpSamplerProxy = getHttpSamplerProxy(url, port,request,httpRequest);

        // 获取结果：如汇总报告、察看结果树
        List<ResultCollector> resultCollector = getResultCollector(CommonConstant.REPLAYLOGPATH);
        if (StringUtils.isNotEmpty(jmxPath)){
            //生成测试用例文件
            ListedHashTree listedHashTree  =getHashTree(testPlan,threadGroup,httpSamplerProxy,jmxPath);
        }
        //传入的是测试用例文件
        if (osName.contains(CommonConstant.WINDOWS)) {
            jmeterPath = CommonConstant.C_PATH + jmeterPath;
        } else {
            jmeterPath = CommonConstant.HOME + jmeterPath;
        }
        String command = null;
        File jmeterPropertiesFile = new File(jmeterPath);
        String jtlPaths = jmeterPropertiesFile.getParent();
        String jtlName = StrUtil.removeSuffix(jmeterPropertiesFile.getName(),CommonConstant.JMX);
        StringBuilder readCount = new StringBuilder();
        if (StringUtils.isNotEmpty(jmeterPath) && jmeterPropertiesFile.exists()){
            //测试报告位置
            htmlPath = jtlPaths + CommonConstant.SYMBOL + jtlName;
            File fileJmx = new File(htmlPath);
            if (fileJmx.exists()){
                //有文件就删除
                Boolean result = FileUtil.delFiles(fileJmx);
                if (!result){
                    return null;
                }
            }
            command = JMeterUtils.getJMeterBinDir() + CommonConstant.JMETER_N_T_S + jmeterPath + CommonConstant.JMETER__L + jtlPaths+CommonConstant.SYMBOL+jtlName+CommonConstant.JTL+ CommonConstant.JMETER_E_O + htmlPath;
            Process process = Runtime.getRuntime().exec(CommonConstant.CMD_EXE_START + command);
            if (process != null) {
                process.getOutputStream().close();
                //以下为读取cmd窗口返回的内容
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                log.info("以下为读取cmd窗口返回的内容");
                while ((line = bufferedReader.readLine()) != null) {
                    readCount.append(line).append("\n");
                }
            }
        }else {
            // 使用命令
            command = JMeterUtils.getJMeterBinDir() + CommonConstant.JMETER_N_T + jmxPath + CommonConstant.JMETER__L + jtlPath + CommonConstant.JMETER_E_O + htmlPath;
            Process process = Runtime.getRuntime().exec(CommonConstant.CMD_EXE_START + command);
            if (process != null) {
                process.getOutputStream().close();
                //以下为读取cmd窗口返回的内容
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                log.info("以下为读取cmd窗口返回的内容");
                while ((line = bufferedReader.readLine()) != null) {
                    readCount.append(line).append("\n");
                }
            }
        }
        return htmlPath;
    }

    /**
     * 创建计划
     * @param planName
     * @return testPlan
     */
    private static TestPlan getTestPlan(String planName) {
        TestPlan testPlan = new TestPlan(planName);
        testPlan.setFunctionalMode(false);
        testPlan.setSerialized(false);
        testPlan.setTearDownOnShutdown(true);
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, "TestPlanGui");
        testPlan.setProperty(new BooleanProperty(TestElement.ENABLED, true));
        testPlan.setProperty(new StringProperty("TestPlan.comments", ""));
        testPlan.setProperty(new StringProperty("TestPlan.user_define_classpath", ""));
        Arguments arguments = new Arguments();
        testPlan.setProperty(new TestElementProperty("TestPlan.user_defined_variables", arguments));
        return testPlan;
    }

    /**
     * 设置循环控制器
     * @param loops
     * @return loopController
     */
    private static LoopController getLoopController(String loops) {
        LoopController loopController = new LoopController();
        loopController.setContinueForever(false);
        loopController.setProperty(new StringProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName()));
        loopController.setProperty(new StringProperty(TestElement.TEST_CLASS, LoopController.class.getName()));
        loopController.setProperty(new StringProperty(TestElement.NAME, "循环控制器"));
        loopController.setProperty(new StringProperty(TestElement.ENABLED, "true"));
        //-1 为永久循环
        loopController.setProperty(new StringProperty(LoopController.LOOPS, loops));
        return loopController;
    }
    /***
     * 创建线程组
     * @param loopController 循环控制器
     * @param numThreads 线程数量
     * @return threadGroup
     */
    private static ThreadGroup getThreadGroup(LoopController loopController, int numThreads,Integer duration) {
        ThreadGroup threadGroup = new ThreadGroup();
        //线程数量
        threadGroup.setNumThreads(numThreads);
        threadGroup.setRampUp(1);
        threadGroup.setDelay(0);
        //线程组持续时间
        threadGroup.setDuration(duration);
        threadGroup.setProperty(new StringProperty(ThreadGroup.ON_SAMPLE_ERROR, "continue"));
        threadGroup.setScheduler(false);
        threadGroup.setIsSameUserOnNextIteration(true);
        threadGroup.setName("线程组");
        threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
        threadGroup.setProperty(TestElement.GUI_CLASS, "ThreadGroup");
        threadGroup.setProperty(new TestElementProperty(ThreadGroup.MAIN_CONTROLLER, loopController));
        threadGroup.setProperty(new BooleanProperty(TestElement.ENABLED, true));
        return threadGroup;
    }

    /**
     * 创建http采样器
     * @param url
     * @param port
     * @param request
     * @param httpRequest
     * @return httpSamplerProxy
     */
    public static HTTPSamplerProxy getHttpSamplerProxy(String url, String port, String request,String httpRequest) {
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        Arguments httpPsamplerArguments = new Arguments();
        HTTPArgument httpArgument = new HTTPArgument();
        httpArgument.setProperty(new BooleanProperty("HTTPArgument.always_encode", false));
        httpArgument.setProperty(new StringProperty("Argument.value", request));
        httpArgument.setProperty(new StringProperty("Argument.metadata", "="));
        ArrayList<TestElementProperty> list1 = new ArrayList<>();
        list1.add(new TestElementProperty("", httpArgument));
        httpPsamplerArguments.setProperty(new CollectionProperty("Arguments.arguments", list1));
        httpSamplerProxy.setProperty(new TestElementProperty("HTTPsampler.Arguments", httpPsamplerArguments));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.domain", url));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.port", port));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.protocol", "http"));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.path", ""));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.method", httpRequest));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.contentEncoding", ""));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.follow_redirects", true));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.postBodyRaw", true));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.auto_redirects", false));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.use_keepalive", true));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.DO_MULTIPART_POST", false));
        httpSamplerProxy.setProperty(new StringProperty("TestElement.gui_class", "org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui"));
        httpSamplerProxy.setProperty(new StringProperty("TestElement.test_class", "org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy"));
        httpSamplerProxy.setProperty(new StringProperty("TestElement.name", "HTTP请求"));
        httpSamplerProxy.setProperty(new StringProperty("TestElement.enabled", "true"));
        httpSamplerProxy.setProperty(new BooleanProperty("HTTPSampler.postBodyRaw", true));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.embedded_url_re", ""));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.connect_timeout", ""));
        httpSamplerProxy.setProperty(new StringProperty("HTTPSampler.response_timeout", ""));
        return httpSamplerProxy;
    }
    /***
     * 监听结果
     * @param replayLogPath  将结果保存到文件中，这个是文件的路径
     * @return resultCollectors
     */
    private static List<ResultCollector> getResultCollector(String replayLogPath) {
        // 察看结果数
        List<ResultCollector> resultCollectors = new ArrayList<>();
        Summariser summariser = new Summariser("速度");
        ResultCollector resultCollector = new ResultCollector(summariser);
        resultCollector.setProperty(new BooleanProperty("ResultCollector.error_logging", false));
        resultCollector.setProperty(new ObjectProperty("saveConfig", getSampleSaveConfig()));
        resultCollector.setProperty(new StringProperty("TestElement.gui_class", "org.apache.jmeter.visualizers.ViewResultsFullVisualizer"));
        resultCollector.setProperty(new StringProperty("TestElement.name", "察看结果树"));
        resultCollector.setProperty(new StringProperty("TestElement.enabled", "true"));
        resultCollector.setProperty(new StringProperty("filename", replayLogPath));
        resultCollectors.add(resultCollector);

        // 结果汇总
        ResultCollector resultTotalCollector = new ResultCollector();
        resultTotalCollector.setProperty(new BooleanProperty("ResultCollector.error_logging", false));
        resultTotalCollector.setProperty(new ObjectProperty("saveConfig", getSampleSaveConfig()));
        resultTotalCollector.setProperty(new StringProperty("TestElement.gui_class", "org.apache.jmeter.visualizers.SummaryReport"));
        resultTotalCollector.setProperty(new StringProperty("TestElement.name", "汇总报告"));
        resultTotalCollector.setProperty(new StringProperty("TestElement.enabled", "true"));
        resultTotalCollector.setProperty(new StringProperty("filename", replayLogPath));
        resultCollectors.add(resultTotalCollector);
        return resultCollectors;
    }

    /**
     * 生成测试用例文件
     * @param testPlan 测试计划
     * @param threadGroup
     * @param httpSamplerProxy
     * @return listedHashTree
     * @throws IOException
     */
    private static ListedHashTree getHashTree(TestPlan testPlan,ThreadGroup threadGroup,HTTPSamplerProxy httpSamplerProxy,String jmxPath) throws IOException {
        //请求的Hander
        ArrayList<TestElementProperty> headerMangerList = new ArrayList<>();
        HeaderManager headerManager = new HeaderManager();
        Header header = new Header("Content-Type", "application/json");
        TestElementProperty headerElement = new TestElementProperty("", header);
        headerMangerList.add(headerElement);
        headerManager.setEnabled(true);
        headerManager.setName("HTTP Header Manager");
        headerManager.setProperty(new CollectionProperty(HeaderManager.HEADERS, headerMangerList));
        headerManager.setProperty(new StringProperty(TestElement.TEST_CLASS, HeaderManager.class.getName()));
        headerManager.setProperty(new StringProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName()));

        ListedHashTree listedHashTree = new ListedHashTree();
        listedHashTree.add(headerManager);
        ListedHashTree hashTreeHttpSamplerProxy = new ListedHashTree();
        hashTreeHttpSamplerProxy.add(httpSamplerProxy, listedHashTree);
        ListedHashTree hashTreeThreadGroup = new ListedHashTree();
        hashTreeThreadGroup.add(threadGroup, hashTreeHttpSamplerProxy);
        ListedHashTree hashTreeTestPlan = new ListedHashTree();
        hashTreeTestPlan.add(testPlan, hashTreeThreadGroup);
        SaveService.saveTree(hashTreeTestPlan, new FileOutputStream(jmxPath));
        return listedHashTree;
    }

    /**
     * 察看结果数
     * @return sampleSaveConfiguration
     */
    private static SampleSaveConfiguration getSampleSaveConfig() {
        SampleSaveConfiguration sampleSaveConfiguration = new SampleSaveConfiguration();
        sampleSaveConfiguration.setTime(true);
        sampleSaveConfiguration.setLatency(true);
        sampleSaveConfiguration.setTimestamp(true);
        sampleSaveConfiguration.setSuccess(true);
        sampleSaveConfiguration.setLabel(true);
        sampleSaveConfiguration.setCode(true);
        sampleSaveConfiguration.setMessage(true);
        sampleSaveConfiguration.setThreadName(true);
        sampleSaveConfiguration.setDataType(true);
        sampleSaveConfiguration.setEncoding(false);
        sampleSaveConfiguration.setAssertions(true);
        sampleSaveConfiguration.setSubresults(true);
        sampleSaveConfiguration.setResponseData(false);
        sampleSaveConfiguration.setSamplerData(false);
        sampleSaveConfiguration.setAsXml(false);
        sampleSaveConfiguration.setFieldNames(true);
        sampleSaveConfiguration.setResponseHeaders(false);
        sampleSaveConfiguration.setRequestHeaders(false);
        sampleSaveConfiguration.setAssertionResultsFailureMessage(true);
        sampleSaveConfiguration.setBytes(true);
        sampleSaveConfiguration.setSentBytes(true);
        sampleSaveConfiguration.setUrl(true);
        sampleSaveConfiguration.setThreadCounts(true);
        sampleSaveConfiguration.setIdleTime(true);
        sampleSaveConfiguration.setConnectTime(true);
        return sampleSaveConfiguration;
    }
}
