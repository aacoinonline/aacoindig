package com.aacoin.dig;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.internal.WadlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.RequestContextFilter;

import javax.annotation.PostConstruct;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // Register endpoints, providers, ...
        this.registerEndpoints();
    }

    @PostConstruct
    public void init() {
        // Register components where DI is needed
        this.configureSwagger();
    }

    private void registerEndpoints() {
        packages("com.aacoin.dig");

        this.register(GsonProvider.class);
        this.register(RequestContextFilter.class);
//        this.register(ExceptionMapperSupport.class);
        // Access through /<Jersey's servlet path>/application.wadl
        this.register(WadlResource.class);
    }

    private void configureSwagger() {
        // Available at localhost:port/swagger.json
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);

        BeanConfig config = new BeanConfig();
        config.setConfigId("springboot-jersey-swagger");
        config.setTitle("Spring Boot + Jersey + Swagger");
        config.setVersion("v1");
//        config.setContact("Orlando L Otero");
        config.setSchemes(new String[]{"http"});
        config.setBasePath("/api");
        config.setResourcePackage("com.aacoin.dig");
        config.setPrettyPrint(true);
        config.setScan(true);
    }
}
