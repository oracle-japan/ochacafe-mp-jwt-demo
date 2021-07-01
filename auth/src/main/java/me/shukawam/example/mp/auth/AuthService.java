package me.shukawam.example.mp.auth;

import me.shukawam.example.mp.auth.data.UserProfile;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.logging.Logger;

@Dependent
public class AuthService {
    private final IdcsResource idcsResource;
    @Inject
    private Logger logger;

    @Inject
    public AuthService(IdcsResource idcsResource) {
        this.idcsResource = idcsResource;
    }

    public UserProfile getUserProfile(String bearerToken) {
        return idcsResource.getUserProfile(bearerToken);
    }
}
