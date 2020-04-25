package com.kronostools.timehammer.comunytek.restclient;

import com.kronostools.timehammer.comunytek.enums.ComunytekAction;
import com.kronostools.timehammer.comunytek.utils.ComunytekConstants;
import com.kronostools.timehammer.enums.SupportedTimezone;
import com.kronostools.timehammer.service.TimeMachineService;
import com.kronostools.timehammer.utils.Utils;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.kronostools.timehammer.utils.Constants.LINE_BREAK;

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
        return Utils.stringFormat("{}\nN\n11111111\nNONE\nv4.2.2 build 20191211-18:26:16", par2);
    }

    @Override
    public String getHolidays(String sessionId, String par1, String par2, String par3, String par4) {
        final LocalDate[] allYearDays = timeMachineService.getAllYearBusinessDays();

        final Set<LocalDate> holidays = Utils.getNDifferentRandomElementsFromArray(allYearDays, 25);

        return holidays.stream()
                .map(d -> Utils.stringFormat("{}\tBLA\tBLA\tBLA\tBLA\tBLA", TimeMachineService.formatDate(d, TimeMachineService.FORMAT_DDMMYYYY_SEP_FWS)))
                .collect(Collectors.joining(LINE_BREAK));
    }

    @Override
    public String getDayRegistry(String sessionId, String par1, String par2, String par3, String par4) {
        String result = "";

        final LocalDateTime timestamp = TimeMachineService.parseDateTime(par4, TimeMachineService.FORMAT_YYYYMMDDTHHMM);

        if (registry.containsKey(par2) && registry.get(par2).containsKey(timestamp.toLocalDate())) {
            result = String.join(LINE_BREAK, registry.get(par2).get(timestamp.toLocalDate()));
        }

        return result;
    }

    @Override
    public String executeAction(String sessionId, String par1, String par2, String par3, String par4, String par5) {
        final LocalDateTime now = timeMachineService.getNowAtZone(getTimezone());
        final ComunytekAction action = ComunytekAction.fromCode(par4);

        if (!registry.containsKey(par2)) {
            registry.put(par2, new HashMap<>());
        }

        if (!registry.get(par2).containsKey(now.toLocalDate())) {
            registry.get(par2).put(now.toLocalDate(), new ArrayList<>());
        }

        final String registryEntry = Utils.stringFormat("{}\t{}\t{}\t{}\t00.00\t{}",
                TimeMachineService.formatDateTime(now, TimeMachineService.FORMAT_DDMMYYYY_SEP_FWS),
                TimeMachineService.formatDateTime(now, TimeMachineService.FORMAT_HHMMSSSSS),
                action.getCode(),
                action.getDescription(),
                action.getComment());

        registry.get(par2).get(now.toLocalDate()).add(registryEntry);

        return ComunytekConstants.OK;
    }

    private SupportedTimezone getTimezone() {
        return SupportedTimezone.EUROPE_MADRID;
    }
}