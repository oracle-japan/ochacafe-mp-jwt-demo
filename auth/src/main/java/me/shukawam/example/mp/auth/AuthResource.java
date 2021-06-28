package me.shukawam.example.mp.auth;

import io.helidon.config.Config;
import io.helidon.security.SecurityContext;
import io.helidon.security.annotations.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@Path("auth")
@ApplicationScoped
public class AuthResource {
    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private final String cookieName;
    @Inject
    private JsonWebToken jwt;

    public AuthResource() {
        Config config = Config.create();
        cookieName = config.get("security.providers.oidc.cookie-name").asString().orElse("JSESSIONID");
    }

    @GET
    @Path("login")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject login(@Context SecurityContext securityContext, @Context ContainerRequestContext containerRequestContext) {
        return JSON.createObjectBuilder().add("access_token", getAccessToken(containerRequestContext))
                .build();
    }

    @GET
    @Path("jaxrs")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Principal getJsonWebToken(@Context SecurityContext securityContext) {
        if(securityContext.userPrincipal().isPresent() == false) {
            // do something.
        }
        return securityContext.userPrincipal().get();
    }

    private String getAccessToken(ContainerRequestContext containerRequestContext) {
        var cookie = containerRequestContext.getCookies().get(cookieName);
        if (Optional.ofNullable(cookie).isPresent()) {
            return cookie.getValue();
        } else {
            throw new RuntimeException("No cookie is existing.");
        }
    }
}
