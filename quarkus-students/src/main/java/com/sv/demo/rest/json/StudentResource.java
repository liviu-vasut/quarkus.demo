package com.sv.demo.rest.json;

import com.sv.demo.dto.Student;
import com.sv.demo.service.StudentService;
import io.reactivex.Flowable;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentResource.class);

    private final AtomicInteger counter = new AtomicInteger();

    @Inject
    StudentService studentService;

    @Inject
    @Named("sleeperRunnable")
    Runnable sleeperRunnable;

    @Inject
    @Named("randomErrorGenerator")
    Runnable randomErrorGenerator;

    @GET
    @Path("/list")
    @Timed(name = "listTimer", description = "How long it takes to list students (ms).", unit = MetricUnits.MILLISECONDS)
    @Timeout(250)
    public Set<Student> list() {
        sleeperRunnable.run();
        return studentService.findAll();
    }

    @GET
    @Path("/{id}")
    @Counted(name = "getByIdCount", description = "How many calls to the getById endpoint.")
    @Retry(maxRetries = 4)
    public Student getById(@PathParam("id") long id) {
        randomErrorGenerator.run();
        final int requestId = counter.getAndIncrement();
        LOG.debug(requestId + "--->" + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.debug(requestId + "+++>" + Thread.currentThread().getName());
        return studentService.findById(id);
    }

    @GET
    @Path("/r/{id}")
    public CompletionStage<Student> getByIdReactive(@PathParam("id") long id) {
        randomErrorGenerator.run();
        final int requestId = counter.getAndIncrement();
        LOG.debug(requestId + "--->" + Thread.currentThread().getName());
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOG.debug(requestId + "+++>" + Thread.currentThread().getName());
            return studentService.findById(id);
        });
    }

    @GET
    @Path("/search")
    public Student getByName(@QueryParam("name") String name) {
        return studentService.findByName(name);
    }

    @POST
    @Traced
    public Student add(Student student) {
        return studentService.add(student);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        studentService.delete(id);
    }

}
