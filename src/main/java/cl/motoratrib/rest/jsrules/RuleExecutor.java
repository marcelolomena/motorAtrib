/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.motoratrib.rest.jsrules;

import cl.motoratrib.rest.jsrules.Executor;
import cl.motoratrib.rest.jsrules.Parameter;
import cl.motoratrib.rest.jsrules.Rule;
import cl.motoratrib.rest.jsrules.exception.InvalidParameterException;

/**
 *
 * @author Marcelo
 */
public abstract class RuleExecutor<T> extends Executor {
    public abstract T execute(Object leftParameter, Object rightParameter) throws InvalidParameterException;

    public abstract T execute(Object leftParameter) throws InvalidParameterException;

    public abstract Parameter getLeftParameter();

    public abstract Parameter getRightParameter();

    public abstract Rule getRule();
}
