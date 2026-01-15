package lml.snir.rest.server;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RestException extends RuntimeException implements ExceptionMapper<RestException>{

    @Override
    public Response toResponse(RestException exception) {
        String msg = exception.getMessage();
        Response rep = Response.status(500).entity(msg).type(MediaType.TEXT_PLAIN).build();
        return rep;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public RestException() {
        super();
    }
    
    public RestException(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
