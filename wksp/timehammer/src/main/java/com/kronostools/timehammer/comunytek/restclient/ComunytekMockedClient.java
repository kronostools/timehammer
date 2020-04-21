package com.kronostools.timehammer.comunytek.restclient;

import com.kronostools.timehammer.comunytek.enums.ComunytekAction;
import com.kronostools.timehammer.comunytek.utils.ComunytekConstants;
import com.kronostools.timehammer.enums.SupportedTimezone;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.utils.Utils;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ComunytekMockedClient implements ComunytekRestClient {
    private final Map<String, Map<LocalDate, List<String>>> registry;

    private final TimeMachineService timeMachineService;

    public ComunytekMockedClient(final TimeMachineService timeMachineService) {
        this.registry = new HashMap<>();
        this.timeMachineService = timeMachineService;
    }

    @Override
    public String about() {
        return "Mocked about information";
    }

    @Override
    public String login(String sessionId, String par1, String par2, String par3, String par4) {
        return String.format("%s\nN\n11111111\nNONE\nv4.2.2 build 20191211-18:26:16", par2);
    }

    @Override
    public String getHolidays(String sessionId, String par1, String par2, String par3, String par4) {
        String holidaysResponse = "01/01/2020\tBLA\tBLA\tBLA\tBLA\tBLA";
        final int numberOfHolidays = Utils.getRandomNumberInRange(1, 5);

        for (int h = 0; h < numberOfHolidays; h++) {
            holidaysResponse += "\n0" + Utils.getRandomNumberInRange(1, 10) + "/0" + Utils.getRandomNumberInRange(1, 10) + "/2020\tBLA\tBLA\tBLA\tBLA\tBLA";
        }

        return holidaysResponse;
    }

    @Override
    public String getDayRegistry(String sessionId, String par1, String par2, String par3, String par4) {
        String result = "";

        final LocalDateTime timestamp = TimeMachineService.parseDateTime(par4, TimeMachineService.FORMAT_YYYYMMDDTHHMM);

        if (registry.containsKey(par2) && registry.get(par2).containsKey(timestamp.toLocalDate())) {
            result = String.join("\n", registry.get(par2).get(timestamp.toLocalDate()));
        }

        return result;
    }

    @Override
    public String executeAction(String sessionId, String par1, String par2, String par3, String par4, String par5) {
        final LocalDateTime now = timeMachineService.getNowAtZone(getTimezoneName());
        final ComunytekAction action = ComunytekAction.fromCode(par4);

        if (!registry.containsKey(par2)) {
            registry.put(par2, new HashMap<>());
        }

        if (!registry.get(par2).containsKey(now.toLocalDate())) {
            registry.get(par2).put(now.toLocalDate(), new ArrayList<>());
        }

        final String registryEntry = String.format("%s\t%s\t%s\t%s\t00.00\t%s",
                TimeMachineService.formatDateTime(now, TimeMachineService.FORMAT_DDMMYYYY_SEP_FWS),
                TimeMachineService.formatDateTime(now, TimeMachineService.FORMAT_HHMMSSSSS),
                action.getCode(),
                action.getDescription(),
                action.getComment());

        registry.get(par2).get(now.toLocalDate()).add(registryEntry);

        return ComunytekConstants.OK;
    }

    private String getTimezoneName() {
        return SupportedTimezone.EUROPE_MADRID.getTimezoneName();
    }
}