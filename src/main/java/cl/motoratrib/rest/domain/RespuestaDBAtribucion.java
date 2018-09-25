package cl.motoratrib.rest.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class RespuestaDBAtribucion implements Serializable {
    String descripcion;
    String ref;
    List<AtribucionCorta> atribucionCorta;
    List<AtribucionLarga> atribucionLarga;

    @JsonCreator
    public RespuestaDBAtribucion(
            @JsonProperty("descripcion") String descripcion,
            @JsonProperty("ref") String ref,
            @JsonProperty("atribucionCorta") List<AtribucionCorta> atribucionCorta,
            @JsonProperty("atribucionLarga") List<AtribucionLarga> atribucionLarga
    ) {
        this.descripcion = descripcion;
        this.ref = ref;
        this.atribucionCorta = atribucionCorta;
        this.atribucionLarga = atribucionLarga;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public List<AtribucionCorta> getAtribucionCorta() {
        return atribucionCorta;
    }

    public void setAtribucionCorta(List<AtribucionCorta> atribucionCorta) {
        this.atribucionCorta = atribucionCorta;
    }

    public List<AtribucionLarga> getAtribucionLarga() {
        return atribucionLarga;
    }

    public void setAtribucionLarga(List<AtribucionLarga> atribucionLarga) {
        this.atribucionLarga = atribucionLarga;
    }

}
