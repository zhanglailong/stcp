package org.jeecg.modules.eval.util;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Stack;
/**
* @Author: test
* */
public class EvalAverage extends PostfixMathCommand {

    public EvalAverage(){
        super();
        numberOfParameters = 2;
    }


    @Override
    public void run(Stack stack) throws ParseException {
        //栈检查
        checkStack(stack);
        Object paramA = stack.pop();
        Object paramB = stack.pop();
        BigDecimal rMax = BigDecimal.valueOf(0);
        Integer max = Integer.valueOf(0);

        //判断最大值
        if(paramB instanceof List){
            List<Integer> list = (List<Integer>) paramB;
            max = Collections.max(list);
        }else {
            throw new ParseException("Invalid parameter type");
        }

        Date date = (Date)paramA;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(date);

        BigDecimal condition = null;
        try {
            //获取时间数值
            condition = BigDecimal.valueOf((simpleDateFormat.parse(str).getTime())).divide(new BigDecimal(1000));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if(condition instanceof Number){
            //Rmax响应时间
            rMax = new BigDecimal(String.valueOf(condition));
        }else{
            throw new ParseException("Invalid parameter type");
        }

        //Tmax最大值
        BigDecimal tMax = (new BigDecimal(max));
        BigDecimal result = tMax.divide(rMax,2,BigDecimal.ROUND_UP);

        stack.push(result);
    }
}
