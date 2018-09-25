/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.motoratrib.rest.jsrules.impl;

import cl.bancochile.plataformabase.error.PlataformaBaseException;
import cl.motoratrib.rest.jsrules.Parameter;
import cl.motoratrib.rest.jsrules.Rule;
import cl.motoratrib.rest.jsrules.RuleExecutor;
import cl.motoratrib.rest.jsrules.exception.InvalidParameterException;
import cl.motoratrib.rest.service.EngineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * @author Marcelo
 * @param <T>
 * @param <P>
 */
@SuppressWarnings("rawtypes")
public class RuleExecutorImpl<T, P> extends RuleExecutor<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleExecutorImpl.class);
    EngineService engineService;
    private final Rule<T, P> rule;

    public RuleExecutorImpl(Rule<T, P> rule,EngineService engineService) {
        this.rule = rule;
        this.engineService = engineService;
    }

    @Override
    /*
    no toma en cuenta que el valor del la variable sea nula y usa el valor estatico
    el rightParameter debe ser un json. se obtiene el valor a partir del json desde la base de datos
    el leftParameter debe ser el rut y el rightParameter un json
     */
    public T execute(Object leftParameter, Object rightParameter) throws InvalidParameterException {
        Object staticValue = rule.getRightParameter().getStaticValue();//este valor estatic debe venir de la DB
        if (staticValue != null) {
            LOGGER.error("Right parameter has a static value of {} and should not be specified", staticValue);
            throw new InvalidParameterException();
        }

        return executeRule(leftParameter, rightParameter);
    }

    @Override
    public T execute(Object leftParameter) throws InvalidParameterException {//cuando no se encuentra el valor de la variable del lado derecho se toma por lado derecho el valor estatico
        Object rightParameter = rule.getRightParameter().getStaticValue();

        if (rightParameter == null) {
            LOGGER.error("Right parameter must be specified");
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
        System.out.println("leftParameter :" + leftParameter);
        System.out.println("rightParameter :" + rightParameter);
        validateParameter(rule.getLeftParameter().getName(), leftParameter, rule.getLeftParameter().getKlasse());
        validateParameter(rule.getRightParameter().getName(), rightParameter, rule.getRightParameter().getKlasse());

        T response = null;

        String className = rule.getLeftParameter().getKlasse().getSimpleName().trim().toUpperCase();

        System.out.println("className :" + className);

        try {
            if (className.equals("JSONNODE")) {
                ObjectMapper mapper = new ObjectMapper();
                String leftParam = leftParameter.toString();
                String json = this.engineService.getValorAtrib(rule.getRuleName(), leftParam).getPResult();
                System.out.println("VALOR DEVUELTO POR EL SUPER SP :" + json);
                JsonNode root = mapper.readTree(json.replaceAll("[\\\\]",""));
                String valor = root.get("valor").asText();
                String tipo =  root.get("tipo").asText();
                System.out.println("VALOR DEVUELTO POR EL SUPER SP valor  :" + valor);
                System.out.println("VALOR DEVUELTO POR EL SUPER SP tipo :" + tipo);
                if(tipo.equals("Long"))
                    leftParameter=Long.valueOf(valor);
            }

        }catch(PlataformaBaseException e){
            throw new InvalidParameterException(e.getMessage());
        }catch(JsonProcessingException e){
            throw new InvalidParameterException(e.getMessage());
        }catch(IOException e){
            throw new InvalidParameterException(e.getMessage());
        }
        System.out.println("leftParameter :" + leftParameter);
        System.out.println("rightParameter :" + rightParameter);
        if (rule.getOperator().compare(leftParameter, rightParameter)) {
            response = rule.getResponse();
        }

        return response;
    }

}
