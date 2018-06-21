package cl.motoratrib.rest.domain;

public class ClaseGenerica<T> {
    private T obj;

    public ClaseGenerica(T o) {
        this.obj = o;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String classType() {
        return obj.getClass().getName();
    }
}