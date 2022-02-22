package org.jeecg.modules.access.feign;

import org.jeecg.common.api.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient("sqa-cloud-access-handler")
public interface IAccessFeignController {

    static String PARENT_URL = "/access";

    /**
     * 获取测试项和其测试用例子项的分页列表方法
     * @param likeFullName 模糊查询全称
     * @param pageNo 分页Index
     * @param pageSize 单分页大小
     * @return 测试项和其测试用例子项的分页列表
     */
    @RequestMapping(value = PARENT_URL + "/list/task2case", method = RequestMethod.GET)
    public Result<?> listTask2Case(
            @RequestParam("likeFullName")String likeFullName,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize);

    /**
     * 从Access数据源同步数据到MySQL数据源
     * @param map JSON对象Map, 其中含有
     *            key: [ids], value: [测试用例ID列表字符串 ","分割 String]
     *            key: [projectId], value: [项目ID, String]
     *            key: [turnId], value: [轮次ID String]
     *            key: [turnVerId], value: [轮次版本ID String]
     *
     * @return 成功同步的测试用例ID列表
     */
    @RequestMapping(value = PARENT_URL + "/sync", method = RequestMethod.POST)
    public Result<?> syncData2MySQL(@RequestBody(required = false) Map<String, ?> map);

    /**
     * 更新数据源文件路径
     * @param map JSON对象Map, 其中含
     *            key: [path], value: [文件路径 String]
     * @return
     */
    @RequestMapping(value = PARENT_URL + "/update/data-source/path", method = RequestMethod.POST)
    public Result<?> updateAccessDataSource(@RequestBody(required = false) Map<String, ?> map);

    /**
     * 获取存储的Access数据源文件路径
     * @return Access数据源文件路径
     */
    @RequestMapping(value = PARENT_URL + "/get/data-source/path", method = RequestMethod.GET)
    public Result<?> getAccessDataSource();


    /**
     * 检查该Access数据源文件是否存在
     * @param path Access数据源文件路径(eg. D:\0.mdb)
     * @return 是否存在
     */
    @RequestMapping(value = PARENT_URL + "/check/data-source/path", method = RequestMethod.GET)
    public Result<?> checkAccessDateSource(@RequestParam("path")String path);
}
