package org.jeecg.modules.number.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.number.entity.NumberRuleInfo;
import org.jeecg.modules.number.entity.NumberType;
import org.jeecg.modules.number.mapper.NumberRuleInfoMapper;
import org.jeecg.modules.number.mapper.NumberTypeMapper;
import org.jeecg.modules.number.service.INumberRuleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.transaction.Transactional;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description: 编号信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
@Service
public class NumberRuleInfoServiceImpl extends ServiceImpl<NumberRuleInfoMapper, NumberRuleInfo> implements INumberRuleInfoService {

    @Autowired
    private NumberTypeMapper numberTypeMapper;
    @Autowired
    private NumberRuleInfoMapper numberRuleInfoMapper;

    @Override
    /**
     * dq add,根据标识编码生成编号字符串,由于有更新当前序号操作于是引入事物
     * 参数:
     *      numberCode: number_type表中的code
     */
    @Transactional(rollbackOn = Exception.class)
    public String generateNumberStrByNumberCode(String numberCode)
    {
        StringBuffer resultStr = new StringBuffer();
        // 根据code查主键id
        QueryWrapper<NumberType> numberTypeQueryWrapper = new QueryWrapper<>();
        numberTypeQueryWrapper.eq("code",numberCode);
        NumberType numberType = numberTypeMapper.selectOne(numberTypeQueryWrapper);
        if(numberType == null)
        {
            return "标识未找到";
        }
        // 根据主表主键在规则表中查询规则list
        String numberTypeId = numberType.getId();
        QueryWrapper<NumberRuleInfo> numberRuleInfoQueryWrapper = new QueryWrapper<>();
        numberRuleInfoQueryWrapper.eq("type_info_id",numberTypeId);
        numberRuleInfoQueryWrapper.orderByAsc("sort");
        List<NumberRuleInfo> numberRuleInfoList = numberRuleInfoMapper.selectList(numberRuleInfoQueryWrapper);
        // 根据规则list生成编号字符串
        for(NumberRuleInfo numberRuleInfo : numberRuleInfoList)
        {
            String ruleType = numberRuleInfo.getRuleType();
            // 固定值
            if("01".equals(ruleType))
            {
                resultStr.append(numberRuleInfo.getValue());
            }
            // 连接符
            if("02".equals(ruleType))
            {
                resultStr.append(numberRuleInfo.getValue());
            }
            // 日期格式
            if("03".equals(ruleType))
            {
                resultStr.append(generateStrByDateFormat(numberRuleInfo.getDateFormat()));
            }
            // 部门简称
            if("04".equals(ruleType))
            {
                // todo,这里一个用户可能对应多个部门,暂时先写死
                resultStr.append("BMJC");
            }
            // 序列号
            if("05".equals(ruleType))
            {
                resultStr.append(generateSerialStrByRule(numberRuleInfo));
            }
        }
        return resultStr.toString().replaceAll("-","");
    }

    /**
     * dq add
     * 根据当前时间和日期格式字符串返回日期字符串
     * @param
     *      dateFormat: 日期格式字符串,比如yyyy-MM-dd
     * @return
     */
    public String generateStrByDateFormat(String dateFormat)
    {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        String returnStr = df.format(new Date());
        return returnStr;
    }

    /**
     * dq add
     * 根据序列号规则产生序列号字符串,此处沿用之前项目代码,暂时未加入新逻辑
     * @param
     *      numberRuleInfo
     *      实体,包含开始值，结束值，位数，自增值，当前序号
     * @return
     */
    public String generateSerialStrByRule(NumberRuleInfo numberRuleInfo)
    {
        // 转成Long 型
        // 当前序号
        Long xuhao = Long.parseLong(numberRuleInfo.getNowNumber());
        //开始值
        Long kaishi = Long.parseLong(numberRuleInfo.getStartValue());
        // 结束值
        Long jieshu = Long.parseLong(numberRuleInfo.getEndValue());
        // 位数
        Long weishu = Long.parseLong(numberRuleInfo.getDigits());
        // 自增
        Long zijia = Long.parseLong(numberRuleInfo.getZizeng());
        // 补位0
        StringBuilder buwei = new StringBuilder();
        // 最终返回的字符串
        String xunumber;
        if (xuhao < kaishi) {
            xuhao = kaishi;
        } else if (xuhao < jieshu) {
            xuhao += zijia;
        } else {
            return "已超出范围";
        }
        // 补充位数
        int length = xuhao.toString().length();
        if (length < weishu) {
            Long cha = weishu - length;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < cha; i++) {
                stringBuilder.append("0");
            }
            buwei.append(stringBuilder);
        }
        // 更新序号
        numberRuleInfo.setNowNumber(String.valueOf(xuhao));
        numberRuleInfoMapper.updateById(numberRuleInfo);
        xunumber = buwei.toString() + xuhao;
        return xunumber;
    }
}
