package cl.motoratrib.rest.domain;

public class ClaseGenerica<T> {
    public T obj;

    public ClaseGenerica(T o) {
        obj = o;
    }

    public String classType() {
        return obj.getClass().getName();
    }
}