package me.shukawam.example.mp.auth;

import me.shukawam.example.mp.auth.data.UserProfile;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

@RegisterRestClient(baseUri = "https://idcs-f0017ddeef0241cfbd70e057c032ff44.identity.oraclecloud.com")
public interface IdcsResource {

    @GET
    @Path("oauth2/v1/userinfo")
    @ClientHeaderParam(name = "Content-Type", value = "application/x-www-form-urlencoded")
    UserProfile getUserProfile(@HeaderParam("Authorization") String authorization);
}
