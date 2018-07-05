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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

/**
 *
 * Este ejecutor evalúa una serie de reglas en orden.
 * 
 * Si todas las reglas se evalúan como verdaderas, devuelve la respuesta dada. De lo contrario, la respuesta es nula.
 * 
 * @author Marcelo
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class AllTrueRulesetExecutorImpl<T> extends RulesetExecutor<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllTrueRulesetExecutorImpl.class);

    private final List<RuleExecutor> ruleSet;
    private String type;
    private final T response;
    private String name;

    public AllTrueRulesetExecutorImpl(String name, String type, List<RuleExecutor> ruleSet, T response) {
        this.name = name;
        this.type = type;
        this.ruleSet = ruleSet;
        this.response = response;
    }
    
    @Override
    public T execute(Map<String, Object> parameters) throws InvalidParameterException {
        T result = response;
        boolean aborted = false;
        for(RuleExecutor rule:ruleSet) {
            if(!aborted) {
                aborted =worker(rule,parameters);
                if(aborted)
                    result = null;
            }
        }
        LOGGER.debug("cool? : " + result);

        return result;
    }

    private static boolean worker (RuleExecutor rule, Map<String, Object> parameters) throws InvalidParameterException {
        Parameter ruleParamRight = rule.getRightParameter();
        Object leftParameter = parameters.get(rule.getLeftParameter().getName());
        Object rightParameter = parameters.get(ruleParamRight.getName());
        boolean ret = false;
        if (ruleParamRight.getStaticValue() == null) {
            // verifique ambos parámetros - las verificaciones de reglas fallidas devuelven nulo
            if (rule.execute(leftParameter, rightParameter) == null) {
                ret = true;
            }
        } else {
            // verifique solo el parámetro izquierdo - las verificaciones de reglas fallidas devuelven nulo
            if (rule.execute(leftParameter) == null) {
                ret = true;
            }
        }
        return ret;

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
