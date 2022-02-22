package org.jeecg.modules.testtooldistribute.service;

import org.jeecg.modules.testtooldistribute.entity.DistributeResult;
import org.jeecg.modules.testtooldistribute.entity.EnvironmentOptions;
import org.jeecg.modules.testtooldistribute.entity.TestToolDistribute;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.testtooldistribute.entity.TestToolOptions;

import java.util.List;


/**
 * t2021/07/02
 * @author yeyl
 */
public interface ITestToolDistributeService extends IService<TestToolDistribute> {

    /**
     * 级联下拉
     * @return  list
     */
    List<EnvironmentOptions> envVmToolOptions();

    /**
     * 添加
     * @param testToolDistribute  testToolDistribute
     */
    void add(TestToolDistribute testToolDistribute);

    /**
     * 编辑
     * @param testToolDistribute  testToolDistribute
     */
    void edit(TestToolDistribute testToolDistribute);


    /**
     * 虚拟机分发
     * @param testToolDistribute  testToolDistribute
     * @return boolean
     */
    DistributeResult distributeById(TestToolDistribute testToolDistribute);

    /**
     * 虚拟机批量分发
     * @param stringList  stringList
     * @return boolean
     */
    void distributeByIds(List<String> stringList);

    /**
     * 测试工具下拉
     * @return  List<TestToolOptions>
     */
    List<TestToolOptions> testToolOptions();

}
