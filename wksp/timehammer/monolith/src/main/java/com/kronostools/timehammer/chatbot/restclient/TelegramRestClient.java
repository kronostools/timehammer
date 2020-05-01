package com.kronostools.timehammer.chatbot.restclient;

import org.apache.camel.component.telegram.model.SetMyCommandsMessage;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RegisterRestClient(configKey="telegram-api")
public interface TelegramRestClient {
    @GET
    @Path("/getMe")
    @Produces(MediaType.APPLICATION_JSON)
    String getMe();

    @POST
    @Path("/setMyCommands")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void setMyCommands(SetMyCommandsMessage commands);
}