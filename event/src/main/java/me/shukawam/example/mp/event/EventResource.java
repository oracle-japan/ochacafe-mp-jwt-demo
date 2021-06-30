package me.shukawam.example.mp.event;

import io.helidon.microprofile.cors.CrossOrigin;
import io.helidon.security.annotations.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("event")
public class EventResource {
    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private final EventService eventService;

    @Inject
    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @RolesAllowed({"Admin", "Guest"})
    public List<Event> getAllEvent() {
        return eventService.getAllEvent();
    }

    @GET
    @Path("id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @RolesAllowed({"Admin", "Guest"})
    public Event getEventById(@PathParam("id") Integer id) {
        return eventService.getEventById(id);
    }

    @GET
    @Path("season/{season}")
    @Authenticated
    @RolesAllowed({"Admin", "Guest"})
    public List<Event> getEventBySeason(@PathParam("season") String season) {
        return eventService.getEventBySeason(season);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @CrossOrigin(allowMethods = {HttpMethod.POST})
    @Authenticated
    @RolesAllowed("Admin")
    public Event createEvent(CreateEventRequest createEventRequest) {
        return eventService.createEvent(createEventRequest);
    }

    @DELETE
    @Path("id/{id}")
    @Authenticated
    @RolesAllowed("Admin")
    public JsonObject deleteEvent(@PathParam("id") Integer id) {
        eventService.deleteEvent(id);
        return JSON.createObjectBuilder().add("message", String.format("Event: %s is deleted!", id)).build();
    }
}
