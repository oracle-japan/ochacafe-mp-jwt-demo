package me.shukawam.example.mp.event;

import lombok.Data;

@Data
public class CreateEventRequest {
    private String title;
    private String season;
    private String presenter;
    private String eventDate;
}
