package cl.motoratrib.rest.jsrules;

import cl.motoratrib.rest.jsrules.util.JsonBean;

/**
 *
 * @author Marcelo
 * @param <T>
 */
public class Parameter<T> extends JsonBean {
    private final String name;
    private final Class<T> klasse;
    private final T staticValue;

    public Parameter(String name, Class<T> klasse) {
        this.name = name;
        this.klasse = klasse;
        this.staticValue = null;
    }

    public Parameter(String name, Class<T> klasse, T staticValue) {
        this.name = name;
        this.klasse = klasse;
        this.staticValue = staticValue;
    }

    public String getName() {
        return name;
    }

    public Class<T> getKlasse() {
        return klasse;
    }

    public T getStaticValue() {
        return staticValue;
    }


}
