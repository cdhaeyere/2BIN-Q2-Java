package be.vinci.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class WebExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof WebApplicationException)
            return ((WebApplicationException) throwable).getResponse();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(throwable.getMessage())
                .build();
    }
}
