package me.shukawam.example.mp.auth.data;

import lombok.Data;

@Data
public class UserProfile {
    private String birthdate;
    private String email;
    private boolean email_verified;
    private String family_name;
    private String gender;
    private String[] app_roles;
    private String name;
    private String preferred_username;
    private String sub;
    private Integer update_at;
    private String website;
}
