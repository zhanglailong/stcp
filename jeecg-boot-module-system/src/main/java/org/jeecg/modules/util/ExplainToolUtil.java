package org.jeecg.modules.util;

/**
 * @Author: test
 * */
public class ExplainToolUtil {

    public static String getToolId(String id) {
	String result = id;
	switch (id) {
	case "1341647180284678146":
	    // LDRA TESTBED
	    break;
	case "1342658577659195394":
	    // SQATEST-AT
	    break;
	case "1342658900054372353":
	    // SQATEST-CoBot
	    break;
	case "1342659301919027201":
	    // LoadRunner
	    // 找result下一级目录名
	    // 调用方法，取目录
	    break;
	case "1342659460316917762":
	    // TCS
	    break;
	case "1342659707554361346":
	    // JMeer
	    // result的目录
	    break;
	case "1342659955639054337":
	    // SQATEST-Dcheck
	    break;
	case "1342660167866642433":
	    // RTInsigth
	    break;
	case "1342660509505286145":
	    // Xcelium
	    break;
	default:
	    return "未找到解析工具";
	}
	return result;

    }

}
