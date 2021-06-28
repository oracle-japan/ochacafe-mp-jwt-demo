package me.shukawam.example.mp.event;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "Event")
@Table(name = "event")
@NamedQueries({
        @NamedQuery(name = "getAllEvent", query = "select e from Event e"),
        @NamedQuery(name = "getEventByTitle", query = "select e from Event e where e.title like '%' || :title || '%'"),
        @NamedQuery(name = "getEventBySeason", query = "select e from Event e where e.season = :season"),
        @NamedQuery(name = "getLatestEvent", query = "select e from Event e order by event_date desc")
})
@Data
public class Event {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "season")
    private String season;
    @Column(name = "presenter")
    private String presenter;
    @Column(name = "event_date")
    private Date eventDate;
}
