/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.motoratrib.rest.jsrules.impl;

import cl.motoratrib.rest.jsrules.Parameter;
import cl.motoratrib.rest.jsrules.Rule;
import cl.motoratrib.rest.jsrules.RuleExecutor;
import cl.motoratrib.rest.jsrules.exception.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Marcelo
 * @param <T>
 * @param <P>
 */
public class RuleExecutorImpl<T, P> extends RuleExecutor<T> {
    private static final Logger LOG = LoggerFactory.getLogger(RuleExecutorImpl.class);
            
    private final Rule<T, P> rule;
    
    public RuleExecutorImpl(Rule<T, P> rule) {
        this.rule = rule;
    }
    
    @Override
    public T execute(Object leftParameter, Object rightParameter) throws InvalidParameterException {
        Object staticValue = rule.getRightParameter().getStaticValue();
        if (staticValue != null) {
            LOG.error("Right parameter has a static value of {} and should not be specified", staticValue);
            throw new InvalidParameterException();
        }

        return executeRule(leftParameter, rightParameter);
    }

    @Override
    public T execute(Object leftParameter) throws InvalidParameterException {
        Object rightParameter = rule.getRightParameter().getStaticValue();

        if (rightParameter == null) {
            LOG.error("Right parameter must be specified");
            throw new InvalidParameterException();
        }

        return executeRule(leftParameter, rightParameter);
    }

    @Override
    public Parameter getLeftParameter() {
        return rule.getLeftParameter();
    }

    @Override
    public Parameter getRightParameter() {
        return rule.getRightParameter();
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    private T executeRule(Object leftParameter, Object rightParameter) throws InvalidParameterException {
        validateParameter(rule.getLeftParameter().getName(), leftParameter, rule.getLeftParameter().getKlasse());
        validateParameter(rule.getRightParameter().getName(), rightParameter, rule.getRightParameter().getKlasse());

        T response = null;

        if (rule.getOperator().compare(leftParameter, rightParameter)) {
            response = rule.getResponse();
        }

        return response;
    }
    
}
