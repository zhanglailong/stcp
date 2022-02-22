package org.jeecg.modules.eval.vo;

import lombok.Data;

/**
 * @program: jeecg-boot-parent
 * @description: EvalMeasureParameterDto
 * @author: Wangly
 * @create: 2021-04-22 10:33
 */
@Data
public class EvalMeasureParameterDto
{
    private String measureId;
    private String parKey;

    public EvalMeasureParameterDto() {
    }

    public EvalMeasureParameterDto(String measureId, String parKey) {
        this.measureId = measureId;
        this.parKey = parKey;
    }

    @Override
    public String toString() {
        return "EvalMeasureParameterDto{" +
                "measureId='" + measureId + '\'' +
                ", parKey='" + parKey + '\'' +
                '}';
    }
}