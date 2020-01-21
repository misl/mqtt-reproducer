package org.acme.quarkus.sample;

import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * A simple resource retrieving the "in-memory" "my-data-stream" and sending the items to a server sent event.
 */
@Path("/prices")
public class PriceResource {

    @Inject
    @Channel("my-data-stream") Publisher<Double> prices;

    @Inject
    @Channel("something") Emitter<String> something;

    @Inject
    @Channel("topic-price") Emitter<Integer> topicPrice;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<Double> stream() {
        return prices;
    }


    @POST
    @Path("/something")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public void something( String content ) {
        something.send( content );
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void postPrice( String content ) {
        topicPrice.send( Integer.parseInt( content ) );
    }
}