package org.jeecg.modules.eval.util;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Stack;
/**
 * @Author: test
 * */
public class EvalSum extends PostfixMathCommand {

    public EvalSum() {
        super();
        numberOfParameters = 1;
    }

    @Override
    public void run(Stack inStack) throws ParseException {
        //检查栈
        checkStack(inStack);
        Object param = inStack.pop();

        if ((param instanceof List)) {
            BigDecimal b = new BigDecimal(BigInteger.ZERO);
            List<Double> params = (List<Double>) param;
            for (int i = 0; i < params.size(); i++) {
                b = b.add(new BigDecimal(params.get(i)));
            }

            double result = b.doubleValue();

            inStack.push(new Double(result));
        } else {
            throw new ParseException("Invalid parameter type");
        }
        return;
    }
}
