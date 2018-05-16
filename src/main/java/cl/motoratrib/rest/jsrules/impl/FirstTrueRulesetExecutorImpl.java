/*
 * The MIT License
 *
 * Copyright 2018 Marcelo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cl.motoratrib.rest.jsrules.impl;

import cl.motoratrib.rest.jsrules.Parameter;
import cl.motoratrib.rest.jsrules.RuleExecutor;
import cl.motoratrib.rest.jsrules.RulesetExecutor;
import cl.motoratrib.rest.jsrules.exception.InvalidParameterException;
import cl.motoratrib.rest.jsrules.impl.FirstTrueRulesetListExecutorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * This executor evaluates a series of rules in order.
 * <p/>
 * It returns the response of the first rule that evaluates true.
 * <p/>
 * If none of the rules are true, it returns a null.
 *
 * @param <T>
 * @author Marcelo
 */
public class FirstTrueRulesetExecutorImpl<T> extends RulesetExecutor<T> {
    private final static Logger LOGGER = LoggerFactory.getLogger(FirstTrueRulesetListExecutorImpl.class);
    private final List<RuleExecutor<T>> ruleSet;
    private String name;
    private String type;

    public FirstTrueRulesetExecutorImpl(String name, String type, List<RuleExecutor<T>> ruleSet) {
        this.name = name;
        this.type = type;
        this.ruleSet = ruleSet;
    }

    @Override
    public T execute(Map<String, Object> parameters) throws InvalidParameterException {
        T result = null;
        for (RuleExecutor<T> rule : ruleSet) {
            if(rule.getRule()!=null)
                LOGGER.debug("RULE NAME --------> " + rule.getRule().getRuleName());
            Parameter left = rule.getLeftParameter();
            String leftName = left.getName();
            Object leftParameter = parameters.get(leftName);
            validateParameter(leftName, leftParameter, left.getKlasse());

            Parameter right = rule.getRightParameter();

            T ruleResponse;

            if (right.getStaticValue() == null) {
                //send both parameters
                String rightName = right.getName();
                Object rightParameter = parameters.get(rightName);
                validateParameter(rightName, rightParameter, right.getKlasse());

                ruleResponse = rule.execute(leftParameter, rightParameter);

            } else {
                //send left parameter only
                ruleResponse = rule.execute(leftParameter);
            }

            // if the rule is true, send its response and stop processing
            if (null != ruleResponse) {
                result = ruleResponse;
                break;
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

}
