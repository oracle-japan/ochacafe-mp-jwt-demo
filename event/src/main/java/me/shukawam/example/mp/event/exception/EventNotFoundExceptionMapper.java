package me.shukawam.example.mp.event.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EventNotFoundExceptionMapper implements ExceptionMapper<EventNotFoundException> {

    @Override
    public Response toResponse(EventNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
                .build();
    }
}
