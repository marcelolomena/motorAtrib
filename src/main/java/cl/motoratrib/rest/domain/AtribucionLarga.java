package cl.motoratrib.rest.domain;

public class AtribucionLarga {
    String nombre;
    int min;
    int max;
    int opcional;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getOpcional() {
        return opcional;
    }

    public void setOpcional(int opcional) {
        this.opcional = opcional;
    }
}
