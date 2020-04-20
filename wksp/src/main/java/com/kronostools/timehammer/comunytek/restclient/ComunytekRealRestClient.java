package com.kronostools.timehammer.comunytek.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RegisterRestClient(configKey="comunytek-api")
public interface ComunytekRealRestClient extends ComunytekRestClient {
    @GET
    @Path("/about")
    @Produces(MediaType.TEXT_HTML)
    String about();

    @POST
    @Path("/selfweb")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    String login(@FormParam("sessionId") String sessionId,
                 @FormParam("par_1") String par1,
                 @FormParam("par_2") String par2,
                 @FormParam("par_3") String par3,
                 @FormParam("par_4") String par4);

    @POST
    @Path("/regvac")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    String getHolidays(@FormParam("sessionId") String sessionId,
                       @FormParam("par_1") String par1,
                       @FormParam("par_2") String par2,
                       @FormParam("par_3") String par3,
                       @FormParam("par_4") String par4);

    @POST
    @Path("/reghoras")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    String getDayRegistry(@FormParam("sessionId") String sessionId,
                          @FormParam("par_1") String par1,
                          @FormParam("par_2") String par2,
                          @FormParam("par_3") String par3,
                          @FormParam("par_4") String par4);

    @POST
    @Path("/reghoras")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    String executeAction(@FormParam("sessionId") String sessionId,
                         @FormParam("par_1") String par1,
                         @FormParam("par_2") String par2,
                         @FormParam("par_3") String par3,
                         @FormParam("par_4") String par4,
                         @FormParam("par_5") String par5);
}
