package com.kronostools.timehammer.comunytek.restclient;

import javax.ws.rs.FormParam;

public interface ComunytekRestClient {
    String about();

    String login(@FormParam("sessionId") String sessionId,
                 @FormParam("par_1") String par1,
                 @FormParam("par_2") String par2,
                 @FormParam("par_3") String par3,
                 @FormParam("par_4") String par4);

    String getHolidays(@FormParam("sessionId") String sessionId,
                       @FormParam("par_1") String par1,
                       @FormParam("par_2") String par2,
                       @FormParam("par_3") String par3,
                       @FormParam("par_4") String par4);

    String getDayRegistry(@FormParam("sessionId") String sessionId,
                          @FormParam("par_1") String par1,
                          @FormParam("par_2") String par2,
                          @FormParam("par_3") String par3,
                          @FormParam("par_4") String par4);

    String executeAction(@FormParam("sessionId") String sessionId,
                         @FormParam("par_1") String par1,
                         @FormParam("par_2") String par2,
                         @FormParam("par_3") String par3,
                         @FormParam("par_4") String par4,
                         @FormParam("par_5") String par5);
}