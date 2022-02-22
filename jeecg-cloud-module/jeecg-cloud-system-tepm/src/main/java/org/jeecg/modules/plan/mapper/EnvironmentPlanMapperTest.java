package org.jeecg.modules.plan.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.modules.plan.entity.EnvironmentPlan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zlf
 * @version V1.0
 * @date 2020/12/25
 * @Description: 用一句话描述该文件做什么)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EnvironmentPlanMapperTest {


    @Autowired
    private EnvironmentPlanMapper environmentPlanMapper;

    @Test
    public void selectId() {
//        EnvironmentPlan environmentPlan = environmentPlanMapper.selectById(123456);
//        System.out.println(environmentPlan);

    }

    @Test
    public void deleteByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("planFileName", "ceshi");
        environmentPlanMapper.deleteByMap(columnMap);
    }

    @Test
    public void deleteByWrapper() {
        QueryWrapper<EnvironmentPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("planFileName", "ceshi");
        environmentPlanMapper.delete(queryWrapper);
    }

}