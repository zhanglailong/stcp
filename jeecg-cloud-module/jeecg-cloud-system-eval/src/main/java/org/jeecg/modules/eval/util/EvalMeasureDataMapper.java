package org.jeecg.modules.eval.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: test
 * */
public class EvalMeasureDataMapper {

	private final static Map<String, String> MAPPER = new HashMap<String, String>() {{
		// 功能性部分 start
		put("12A", "DTNOPFITE");
		put("12B", "NOFE");
		put("13A", "DTNOMFITE");
		put("13B", "TNOFDITRSA");
		put("14A", "TNOFDITETCBICOAM");
		put("14B", "TNOFDITRSB");
		put("15A", "TNOFMFTBOSOTTEOO");
		put("15B", "TTNOFDITRS");
		put("16A", "ATTERRTUCGTRQTARODUC");
		put("16B", "TNOTC");
		put("17A", "NOTCWAR");
		put("17B", "TTNOTCDTVCA");
		put("18A", "TNOTTGTTRTMTAR");
		put("18B", "TTNOTCETVTAOC");
		put("19A", "WTDETNODFTCBSEWOSOS");
		put("19B", "TTNODFTBE");
		put("20A", "TNOTAUCEDWOSOS");
		put("20B", "TTNOUATED");
		put("21A", "TNOUATSADRITAHD");
		put("21B", "TNOUATSADRDTE");
		put("22A", "NODTOCIOD");
		put("22B", "TTNOTOIOITS");
		put("23A", "NOMDCE");
		put("23N", "TNOTCTATCDCE");
		put("23B", "NOSDCE");
		put("24A", "COTSNOFHNBAITT");
		put("24B", "TNOCIFSF");
		put("25A", "NOICIAS");
		put("25B", "TNOIRC");
		// 功能性部分 end
		// 可靠性部分 start
		// 可靠性部分 end
		// 易用性部分 start
		// 易用性部分 end
		// 效率部分 start
		// 效率部分 end
		// 维护性部分 start
		// 维护性部分 end
		// 可移植性部分 start
		// 可移植性部分 end
	}};
	
	/**
	 * 获取度量评价计算参数对应的数据库中的字段别名
	 * @param masterId 度量ID
	 * @param paramKey 计算参数
	 * @return 数据库中的字段别名
	 */
	public static String get(String masterId, String paramKey) {
		return MAPPER.get(masterId.concat(paramKey));
	}
}
