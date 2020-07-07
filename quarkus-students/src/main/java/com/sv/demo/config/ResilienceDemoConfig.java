package com.sv.demo.config;

import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.properties.IfBuildProperty;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.ws.rs.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResilienceDemoConfig {

    @ApplicationScoped
    @IfBuildProperty(name = "demo.resilience.enabled", stringValue = "true")
    @Named("countErrorGenerator")
    public Runnable requestCountErrorGenerator() {
        return new Runnable() {
            private final AtomicLong counter = new AtomicLong(0);

            @Override
            public void run() {
                final long invocationNumber = counter.getAndIncrement();
                if (invocationNumber % 4 > 1) { // alternate 2 successful and 2 failing invocations
                    throw new InternalServerErrorException("Simulated failure on request count " + invocationNumber);
                }
            }
        };
    }

    @ApplicationScoped
    @DefaultBean
    @Named("countErrorGenerator")
    public Runnable noopRequestCountErrorGenerator() {
        return () -> {
        };
    }

    @ApplicationScoped
    @IfBuildProperty(name = "demo.resilience.enabled", stringValue = "true")
    @Named("sleeperRunnable")
    public Runnable sleeper() {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(new Random().nextInt(500));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @ApplicationScoped
    @DefaultBean
    @Named("sleeperRunnable")
    public Runnable noopSleeper() {
        return () -> {
        };
    }

    @ApplicationScoped
    @IfBuildProperty(name = "demo.resilience.enabled", stringValue = "true")
    @Named("randomErrorGenerator")
    public Runnable randomErrorGenerator() {
        return new Runnable() {
            private final Logger log = LoggerFactory.getLogger(ResilienceDemoConfig.class);

            @Override
            public void run() {
                if (new Random().nextBoolean()) {
                    log.info("generating an error");
                    throw new InternalServerErrorException("Simulated failure");
                }
            }
        };
    }

    @ApplicationScoped
    @DefaultBean
    @Named("randomErrorGenerator")
    public Runnable noopRandomErrorGenerator() {
        return () -> {
        };
    }

}
