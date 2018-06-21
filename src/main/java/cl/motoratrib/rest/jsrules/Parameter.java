package cl.motoratrib.rest.jsrules;

import cl.motoratrib.rest.jsrules.util.JsonBean;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Parameter<?> parameter = (Parameter<?>) o;
        return Objects.equals(name, parameter.name) &&
                Objects.equals(klasse, parameter.klasse) &&
                Objects.equals(staticValue, parameter.staticValue);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name, klasse, staticValue);
    }
   
}
