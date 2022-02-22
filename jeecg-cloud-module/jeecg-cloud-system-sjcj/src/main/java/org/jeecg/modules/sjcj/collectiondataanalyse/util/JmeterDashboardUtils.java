package org.jeecg.modules.sjcj.collectiondataanalyse.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.Apdex;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.Errors;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.Statistics;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author: test
 * */
public class JmeterDashboardUtils {

	/**
	 * 解析apdex模块
	 *
	 * @param path
	 * @throws Exception
	 * @return
	 */
	public static JSONObject analysisApdex(String path) throws Exception {
		// 通过Jsoup.parse方法进行解析html文档
		String document = HttpClientUtils.testCrawler(path);
		Document parse = Jsoup.parse(document);
		// 获取 标签<thead> 并复制给 staticApdex
		Element staticApdex = parse.getElementsByTag("table").get(1);

		// 获取该table标签下所有thead标签
		Elements staticThed = staticApdex.select("thead");
		// 获取该thead标签下的tr标签
		Elements staticTr = staticThed.select("tr");

		// 创建map集合
		Map<String, Object> resultMap = new HashMap<>(2000);
		// for循环
		for (Element tr : staticTr) {
			// 获取th标签
			Elements th = tr.select("th");
			String k = th.text();
			// 获取b标签
			Elements div = th.get(0).getElementsByTag("div");
			String v = div.text();
			// 返回 JSONObject
			JSONObject jsonObject = new JSONObject();
		}
		// 解析 apdex 模块
		// 获取第一个table标签 apdexTable
		Element apdexTable = parse.getElementById("apdexTable");
		// 获取tbody标签
		Element apdexTbody = apdexTable.getElementsByTag("tbody").get(0);

		Element apdexTbodyTr = apdexTbody.select("tr").get(0);

		// 创建list集合
		Apdex apdexList = applicationPerformanceIndex(apdexTbodyTr);
		String jsonString = JSONObject.toJSONString(apdexList);

		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		// 实体
		resultMap.put("ApplicationPerformanceIndex", jsonObject);


		return JSONObject.parseObject(JSON.toJSONString(resultMap));
	}

	private static Apdex applicationPerformanceIndex(Element apdexTbodyTr) {
		// 创建实体
		Apdex applicationPerformanceIndex = new Apdex();

		String apdex = apdexTbodyTr.getElementsByTag("td").get(0).text();
		applicationPerformanceIndex.setApdex(apdex);

		String tolerationThreshold = apdexTbodyTr.getElementsByTag("td").get(1).text();
		applicationPerformanceIndex.setTolerationThreshold(tolerationThreshold);

		String frustrationThreshold = apdexTbodyTr.getElementsByTag("td").get(2).text();
		applicationPerformanceIndex.setFrustrationThreshold(frustrationThreshold);

		String label = apdexTbodyTr.getElementsByTag("td").get(3).text();
		applicationPerformanceIndex.setLabel(label);

		return applicationPerformanceIndex;
	}

	/**
	 * 解析 Statistics 模块
	 *
	 * @param path
	 * @return
	 * @throws Exception
	 */

	public static JSONObject analysisStatistics(String path) throws Exception {
		// 通过Jsoup.parse方法进行解析html文档
		String document = HttpClientUtils.testCrawler(path);
		Document parse = Jsoup.parse(document);

		// 解析 Statistics 模块
		// 获取第二个table标签 statisticsTable
		Element statistics = parse.getElementsByTag("table").get(1);
		// 获取table标签下的thead标签
		Elements statisticsThead = statistics.select("thead");

		Element statisticsTable = parse.getElementById("statisticsTable");
		// 获取thead标签下的第一个tr标签
		Element statisticsTr = statisticsThead.select("tr").get(0);
		// 获取thead标签下的第二个tr标签
		Element statisticsTr1 = statisticsTable.select("tr").get(1);
		// 获取th标签
		Elements ths = statisticsTr1.select("th");
		// 获取tbody标签
		Element tbody = statisticsTable.getElementsByTag("tbody").get(1);
		// 获取td标签
		Elements td = tbody.getElementsByTag("td");

		// 创建list数组
		JSONArray jsonArray = new JSONArray();
		// 循环
		for (int i = 0; i < ths.size(); i++) {
			// 创建Statistics对象
			Statistics statistic = new Statistics();
			// 判断是否是Executions
			if (i < 3) {
				statistic.setRequests("Executions");
			}
			// 判断是否是responseTimes
			if (3 < i && i < 10) {
				statistic.setRequests("responseTimes");
			}
			// 判断是否是Throughput
			if (i == 11) {
				statistic.setRequests("Throughput");
			}
			// 判段是否是netWork
			if (i > 11) {
				statistic.setRequests("netWork");
			}
			// 获取div的数据
			String divText = ths.get(i).getElementsByTag("div").text();
			// 添加到Lable
			statistic.setLable(divText);
			// 获取td中的数据
			statistic.setTotal(td.get(i).text());

			String jsonString = JSONObject.toJSONString(statistic);
			JSONObject jsonObject = JSONObject.parseObject(jsonString);

			// 添加
			jsonArray.add(jsonObject);
		}
		// 创建JSONObject
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("statistics", jsonArray);
		// 返回值
		return jsonObject;
	}

	/**
	 * 解析Errors 模块
	 *
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static JSONObject analysisErrors(String path) throws Exception {
		// 通过Jsoup.parse方法进行解析html文档
		String document = HttpClientUtils.testCrawler(path);
		Document parse = Jsoup.parse(document);

		// 解析error 模块
		// 获取第三个table标签 errorsTable
		Element errorTable = parse.getElementById("errorsTable");
		// 获取table下的 tbody标签
		Element errorsTbody = errorTable.getElementsByTag("tbody").get(0);
		// 获取tbody下的tr标签
		Element errorsTbodyTr = errorsTbody.select("tr").get(0);
		// 创建list集合
		Errors errorsList = errors(errorsTbodyTr);
		String jsonString = JSONObject.toJSONString(errorsList);

		JSONObject jsonObject = JSONObject.parseObject(jsonString);

		// 返回 json格式结果
		return jsonObject;
	}

	/**
	 *  获取tdoby下的td 的数据
	 * @param errorTd
	 * @return
	 */
	private static Errors errors(Element errorTd) {
		// 创建实体
		Errors errors = new Errors();

		String typeOfError = errorTd.getElementsByTag("td").get(0).text();
		errors.setTypeOfError(typeOfError);

		String numberOfErrors = errorTd.getElementsByTag("td").get(1).text();
		errors.setNumberOfErrors(numberOfErrors);

		String inErrors = errorTd.getElementsByTag("td").get(2).text();
		errors.setInErrors(inErrors);

		String inAllSamples = errorTd.getElementsByTag("td").get(3).text();
		errors.setInAllSamples(inAllSamples);

		// 返回list集合
		return errors;
	}

	/**
	 * 解析Test and Report information模块
	 */
	public static JSONObject analysisReport(String path) throws Exception {
		String document = HttpClientUtils.testCrawler(path);
		Document parse = Jsoup.parse(document);

		// 通过id进行获取
		Element element = parse.getElementById("generalInfos");
		// 通过id获取第二个tr
		Element elementTr = element.getElementsByTag("tr").get(1);
		// 通过tr获取第二个td
		Element elementTd = elementTr.getElementsByTag("td").get(1);

		String startTime = elementTd.text().replace("\"","");

		Element elementTr1 = element.getElementsByTag("tr").get(2);

		Element elementTd1 = elementTr1.getElementsByTag("td").get(1);

		String endTime = elementTd1.text().replace("\"","");

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("startTime", startTime);
		jsonObject.put("endTime", endTime);

		return jsonObject;
	}

}
