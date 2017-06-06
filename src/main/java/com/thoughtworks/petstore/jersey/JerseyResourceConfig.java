package com.thoughtworks.petstore.jersey;

import com.thoughtworks.petstore.api.UsersApi;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyResourceConfig extends ResourceConfig {
    public JerseyResourceConfig() {
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
        register(ValidationExceptionMapper.class);
        register(UsersApi.class);
    }
}
