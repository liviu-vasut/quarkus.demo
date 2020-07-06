package com.sv.demo.health;

import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class AmqpConnectionHealthCheck implements HealthCheck {

    @ConfigProperty(name = "amqp.url")
    private URI AMQP_URL;

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("AMQP connection health check");

        try {
            long time = getConnectionTime();
            responseBuilder
                    .withData("responseTime", String.valueOf(time))
                    .up();

        } catch (IOException e) {
            responseBuilder
                    .withData("error", e.getMessage())
                    .down();
        }

        return responseBuilder.build();
    }

    private long getConnectionTime() throws IOException {
        URLConnection urlConnection = AMQP_URL.toURL().openConnection();
        urlConnection.connect();
        //...
        return 123;
    }
}