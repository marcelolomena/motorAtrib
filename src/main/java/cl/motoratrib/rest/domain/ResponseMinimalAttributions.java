package cl.motoratrib.rest.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseMinimalAttributions {
    List<RespuestaDBAtribucion> respuestaDBAtribucion;

    @JsonCreator
    public ResponseMinimalAttributions(@JsonProperty("respuestaDBAtribucion")
                                                   List<RespuestaDBAtribucion> respuestaDBAtribucion) {
        this.respuestaDBAtribucion = respuestaDBAtribucion;
    }

    public List<RespuestaDBAtribucion> getRespuestaDBAtribucion() {
        return respuestaDBAtribucion;
    }

    public void setRespuestaDBAtribucion(List<RespuestaDBAtribucion> respuestaDBAtribucion) {
        this.respuestaDBAtribucion = respuestaDBAtribucion;
    }
}
