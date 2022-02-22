package org.jeecg.modules.eval.util;

import org.nfunk.jep.JEP;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
/**
* @Author: test
* */
public class EvalCalculationUtils {

	public static int calc(String expression, Map<String, Object> param) {
		JEP jep = new JEP();
		jep.addStandardConstants();
		jep.addStandardFunctions();
		Boolean flag = false;
		Boolean isInto = false;
		if(expression.contains("/B")){
			flag = true;
		}
		// 设定参数
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			if(flag){
				if(Double.valueOf((Double) entry.getValue()) != 0){
					jep.addVariable(entry.getKey(), entry.getValue());
					isInto = true;
				}
			}else{
				jep.addVariable(entry.getKey(), entry.getValue());
				isInto = true;
			}
		}
		// 计算
		jep.parseExpression(expression);
		if(isInto){
			return format(jep.getValue()).intValue();
		}else{
			return 0;
		}
	}

	public static double originalCalc(String expression, Map<String, Object> param) {
		JEP jep = new JEP();
		jep.addStandardConstants();
		jep.addStandardFunctions();

		// 设定参数
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			jep.addVariable(entry.getKey(), entry.getValue());
		}
		// 计算
		jep.parseExpression(expression);

		return format1(jep.getValue()).doubleValue();
	}
	
	public static int compare(String expression, Map<String, Object> param) {
		JEP jep = new JEP();
		// 设定参数
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			jep.addVariable(entry.getKey(), entry.getValue());
		}
		jep.parseExpression(expression);

		return jep.getValueAsObject().equals(1.0) ? 1 : 0;
	}

	public static Object sum(List<Double> values) {

		JEP jep = new JEP();
		jep.addFunction("sum", new EvalSum());

		jep.addVariable("x", values);
		jep.parseExpression("sum(x)");
		return jep.getValue();
	}

	/**
	 * 最坏情况下的响应时间比率
	 * 最坏情况下的响应时间比率b
	 * @param values
	 * @param date
	 * @return
	 */
	public static Object average(List<Integer> values,Date date){
		JEP jep = new JEP();
		//自定义函数
		jep.addFunction("average",new EvalAverage());
		//参数赋值
		jep.addVariable("x",values);
		jep.addVariable("y",date);
		//运算
		jep.parseExpression("average(x,y)");
		return jep.getValue();
	}

	/**
	 * 最坏吞吐量比率
	 * @return
	 */
	public static Object badThroughPut(List<Integer> list,Integer intA,Integer intB){
		JEP jep = new JEP();
		//自定义函数

		/**变量赋值*/
		jep.addVariable("x",list);
		jep.addVariable("y",intA);
		jep.addVariable("z",intB);
		//运算
		jep.parseExpression("badThroughPut(x,y,z)");
		return jep.getValue();
	}

	/**
	 * 计算结果格式化
	 * @param value 结算结果
	 * @return 格式化后的对象
	 */
	private static BigDecimal format1(double value) {
		// 格式化(绝对值四舍五入保留整数)
		BigDecimal bd = new BigDecimal(value)
				.setScale(3, RoundingMode.HALF_UP);
		return bd;
	}

	/**
	 * 计算结果格式化
	 * @param value 结算结果
	 * @return 格式化后的对象
	 */
	private static BigDecimal format(double value) {
		// 格式化(绝对值四舍五入保留整数)
		BigDecimal bd = new BigDecimal(value)
				.multiply(new BigDecimal(100))
				.setScale(0, RoundingMode.HALF_UP);
		return bd;
	}
	
	public static void main(String[] args) {
		Map<String, Object> param = new HashMap<>(2000);
		param.put("A", 12);
		param.put("B", 15);
		System.out.println(calc("1 - A / B", param));

	}

}
