package org.jeecg.common.util.dynamic.db;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.codegenerate.generate.util.SimpleFormat;

import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author 赵俊夫
 * @version V1.0
 * @Title:FreemarkerHelper
 * @description:Freemarker引擎协助类
 * @date Jul 5, 2013 2:58:29 PM
 */
@Slf4j
public class FreemarkerParseFactory {

    private static final String ENCODE = "utf-8";
    /**
     * 参数格式化工具类
     */
    private static final String MINI_DAO_FORMAT = "DaoFormat";

    /**
     * 文件缓存
     */
    private static final Configuration DTPL_CONFIG = new Configuration(new Version(2, 3, 0));
    /**
     * SQL 缓存
     */
    private static final Configuration DSQL_CONFIG = new Configuration(new Version(2, 3, 0));

    private static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

    // 使用内嵌的(?ms)打开单行和多行模式
    private final static Pattern P = Pattern
            .compile("(?ms)/\\*.*?\\*/|^\\s*//.*?$");

    static {
        DTPL_CONFIG.setClassForTemplateLoading(
                new FreemarkerParseFactory().getClass(), "/");
        DTPL_CONFIG.setNumberFormat("0.#####################");
        DSQL_CONFIG.setTemplateLoader(stringTemplateLoader);
        DSQL_CONFIG.setNumberFormat("0.#####################");
        //classic_compatible设置，解决报空指针错误
        DSQL_CONFIG.setClassicCompatible(true);
    }

    /**
     * 判断模板是否存在
     *
     * @throws Exception
     */
    public static boolean isExistTemplate(String tplName) throws Exception {
        try {
            Template mytpl = DTPL_CONFIG.getTemplate(tplName, "UTF-8");
            if (mytpl == null) {
                return false;
            }
        } catch (Exception e) {
            //update-begin--Author:scott  Date:20180320 for：解决问题 - 错误提示sql文件不存在，实际问题是sql freemarker用法错误-----
            if (e instanceof ParseException) {
                log.error(e.getMessage(), e.fillInStackTrace());
                throw new Exception(e);
            }
            log.debug("----isExistTemplate----" + e.toString());
            //update-end--Author:scott  Date:20180320 for：解决问题 - 错误提示sql文件不存在，实际问题是sql freemarker用法错误------
            return false;
        }
        return true;
    }

    /**
     * 解析ftl模板
     *
     * @param tplName 模板名
     * @param paras   参数
     * @return
     */
    public static String parseTemplate(String tplName, Map<String, Object> paras) {
        try {
            log.debug(" minidao sql templdate : " + tplName);
            StringWriter swriter = new StringWriter();
            Template mytpl = DTPL_CONFIG.getTemplate(tplName, ENCODE);
            if (paras.containsKey(MINI_DAO_FORMAT)) {
                throw new RuntimeException("DaoFormat 是 minidao 保留关键字，不允许使用 ，请更改参数定义！");
            }
            paras.put(MINI_DAO_FORMAT, new SimpleFormat());
            mytpl.process(paras, swriter);
            String sql = getSqlText(swriter.toString());
            paras.remove(MINI_DAO_FORMAT);
            return sql;
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            log.error("发送一次的模板key:{ " + tplName + " }");
            //System.err.println(e.getMessage());
            //System.err.println("模板名:{ "+ tplName +" }");
            throw new RuntimeException("解析SQL模板异常");
        }
    }

    /**
     * 解析ftl
     *
     * @param tplContent 模板内容
     * @param paras      参数
     * @return String 模板解析后内容
     */
    public static String parseTemplateContent(String tplContent,
                                              Map<String, Object> paras) {
        try {
            StringWriter swriter = new StringWriter();
            if (stringTemplateLoader.findTemplateSource("sql_" + tplContent.hashCode()) == null) {
                stringTemplateLoader.putTemplate("sql_" + tplContent.hashCode(), tplContent);
            }
            Template mytpl = DSQL_CONFIG.getTemplate("sql_" + tplContent.hashCode(), ENCODE);
            if (paras.containsKey(MINI_DAO_FORMAT)) {
                throw new RuntimeException("DaoFormat 是 minidao 保留关键字，不允许使用 ，请更改参数定义！");
            }
            paras.put(MINI_DAO_FORMAT, new SimpleFormat());
            mytpl.process(paras, swriter);
            String sql = getSqlText(swriter.toString());
            paras.remove(MINI_DAO_FORMAT);
            return sql;
        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            log.error("发送一次的模板key:{ " + tplContent + " }");
            //System.err.println(e.getMessage());
            //System.err.println("模板内容:{ "+ tplContent +" }");
            throw new RuntimeException("解析SQL模板异常");
        }
    }

    /**
     * 除去无效字段，去掉注释 不然批量处理可能报错 去除无效的等于
     */
    private static String getSqlText(String sql) {
        // 将注释替换成""
        sql = P.matcher(sql).replaceAll("");
        sql = sql.replaceAll("\\n", " ").replaceAll("\\t", " ")
                .replaceAll("\\s{1,}", " ").trim();
        // 去掉 最后是 where这样的问题
        if (sql.endsWith("where") || sql.endsWith("where ")) {
            sql = sql.substring(0, sql.lastIndexOf("where"));
        }
        // 去掉where and 这样的问题
        int index = 0;
        while ((index = StringUtils.indexOfIgnoreCase(sql, "where and", index)) != -1) {
            sql = sql.substring(0, index + 5)
                    + sql.substring(index + 9, sql.length());
        }
        // 去掉 , where 这样的问题
        index = 0;
        while ((index = StringUtils.indexOfIgnoreCase(sql, ", where", index)) != -1) {
            sql = sql.substring(0, index)
                    + sql.substring(index + 1, sql.length());
        }
        // 去掉 最后是 ,这样的问题
        if (sql.endsWith(",") || sql.endsWith(", ")) {
            sql = sql.substring(0, sql.lastIndexOf(","));
        }
        return sql;
    }
}