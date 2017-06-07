package com.thoughtworks.petstore;

import com.thoughtworks.petstore.core.order.Order;
import com.thoughtworks.petstore.core.pet.Pet;
import com.thoughtworks.petstore.core.user.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(User.class);
        config.exposeIdsFor(Order.class);
        config.exposeIdsFor(Pet.class);
    }
}
