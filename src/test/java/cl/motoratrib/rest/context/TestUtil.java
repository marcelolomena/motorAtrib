package cl.motoratrib.rest.context;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;

public class TestUtil {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static final MediaType TEXT_PLAIN = new MediaType(
            MediaType.TEXT_PLAIN.getType(),
            MediaType.TEXT_PLAIN.getSubtype(), Charset.forName("ISO-8859-1"));

    public static final MediaType APPLICATION_XLS = new MediaType(
            MediaType.APPLICATION_OCTET_STREAM.getType(),
            MediaType.TEXT_PLAIN.getSubtype(), Charset.forName("ISO-8859-1"));

    public static final String OAM_REMOTE_KEY = "12403933-9";
    public static final String X_AUTH_TOKEN = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI5MTU4NzcwSyIsImlhdCI6MTQ0ODQ1NjcwOH0.N9c0TRRzpZx5Rh7SWo6cvnWfGDsH_E9qzVkfWb3NRinikhjTHoJPwpNddv-xSbvnKZXdplxZcoTWOIa4vq7ivg";
    public static final String X_FORWARDED_FOR = "127.0.0.0";

    public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }


}
