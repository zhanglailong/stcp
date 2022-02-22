package org.jeecg.modules.ngalain.service;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;
/**
 * @Author: test
 * */
public interface NgAlainService {
    /**
     * json
     * @param id  true
     * @throws Exception
     * @return 获取菜单
     * */
    public JSONArray getMenu(String id) throws Exception;
    /**
     * json
     * @param id  true
     * @throws Exception
     * @return 获取菜单*/
    public JSONArray getJeecgMenu(String id) throws Exception;
    /**
     * json
     * @param table  true
     * @param key  true
     * @param value  true
     * @return 获取菜单
     * */
    public List<Map<String, String>> getDictByTable(String table, String key, String value);
}
