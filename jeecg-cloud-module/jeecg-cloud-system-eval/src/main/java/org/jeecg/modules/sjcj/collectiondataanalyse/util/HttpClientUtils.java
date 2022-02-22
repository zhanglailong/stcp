package org.jeecg.modules.sjcj.collectiondataanalyse.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: test
 * */
public class HttpClientUtils {

    public static String getResponseBody(String url) throws IOException {
        // 创建一个 httpGet
        HttpGet get = new HttpGet(url);
        // 创建httpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 执行http get请求
        CloseableHttpResponse response = httpclient.execute(get);

        // 获取返回实体
        HttpEntity entity = response.getEntity();
        // 获取页面内容
        String  responseBody =  EntityUtils.toString(entity, "utf-8");
        return responseBody;
    }

    public static String testCrawler(String path) throws Exception {
        /**HtmlUnit请求web页面*/
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        // 启用JS解释器，默认为true
        wc.getOptions().setJavaScriptEnabled(true);
        // 禁用css支持
        wc.getOptions().setCssEnabled(false);
        // js运行错误时，是否抛出异常
        wc.getOptions().setThrowExceptionOnScriptError(false);
        // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        wc.getOptions().setTimeout(50000);
        URL url = new URL(path);
        WebRequest request = new WebRequest(url, HttpMethod.GET);
        Map<String, String> additionalHeaders = new HashMap<>(2000);
        additionalHeaders
                .put("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
        additionalHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
        additionalHeaders.put("Accept", "*/*");
        request.setAdditionalHeaders(additionalHeaders);
        HtmlPage page = wc.getPage(request);
        wc.waitForBackgroundJavaScript(3 * 1000);
        //以xml的形式获取响应文本
        String pageXml = page.asXml();
        /**jsoup解析文档*/
        return pageXml;
    }


    public static void main(String[] args) throws Exception {
        //testCrawler();
    }
}
