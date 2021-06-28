package me.shukawam.example.mp.event;

import me.shukawam.example.mp.event.exception.EventNotFoundException;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Dependent
public class EventService {
    @PersistenceContext(unitName = "AtpDataSource")
    private EntityManager entityManager;
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public List<Event> getAllEvent() {
        return entityManager.createNamedQuery("getAllEvent", Event.class).getResultList();
    }

    public Event getEventById(Integer id) {
        var event = entityManager.find(Event.class, id);
        if (event == null) {
            throw new EventNotFoundException("Event is NOT FOUND");
        }
        return event;
    }

    public Event getEventByTitle(String title) {
        var event = entityManager.createNamedQuery("getEventByTitle", Event.class)
                .setParameter("title", title)
                .getSingleResult();
        if (event == null) {
            throw new EventNotFoundException("Event is NOT FOUND");
        }
        return event;
    }

    public List<Event> getEventBySeason(String season) {
        return entityManager.createNamedQuery("getEventBySeason", Event.class)
                .setParameter("season", season)
                .getResultList();
    }

    @Transactional
    public Event createEvent(CreateEventRequest createEventRequest) {
        var event = newEvent(createEventRequest);
        entityManager.persist(event);
        return event;
    }

    @Transactional
    public void deleteEvent(Integer id) {
        var event = entityManager.find(Event.class, id);
        if(event == null) {
            throw new EventNotFoundException("Event is NOT FOUND");
        }
        entityManager.remove(event);
    }

    private Event newEvent(CreateEventRequest createEventRequest) {
        var events = entityManager.createNamedQuery("getLatestEvent", Event.class)
                .getResultList();
        if (events.isEmpty()) {
            throw new EventNotFoundException("Event is NOT FOUND");
        }
        var latestEvent = events.get(0);
        if (latestEvent == null) {
            throw new EventNotFoundException("Event is NOT FOUND");
        }
        try {
            var event = new Event();
            event.setId(latestEvent.getId() + 1000);
            event.setTitle(createEventRequest.getTitle());
            event.setSeason(createEventRequest.getSeason());
            event.setPresenter(createEventRequest.getPresenter());
            event.setEventDate(new Date(new SimpleDateFormat(DATE_FORMAT).parse(createEventRequest.getEventDate()).getTime()));
            return event;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Date format is wrong!");
        }
    }
}
