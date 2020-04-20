package com.kronostools.timehammer.utils;

import com.kronostools.timehammer.enums.SsidTrackingEventType;
import io.quarkus.runtime.configuration.ProfileManager;
import org.slf4j.helpers.MessageFormatter;

import java.util.Random;

public class Utils {
    public static SsidTrackingEventType getSsidTrackingEventType(final String oldSsid, final String newSsid, final String referenceSsid) {
        SsidTrackingEventType ssidTrackingEventType = SsidTrackingEventType.NONE;

        if (referenceSsid.equals(newSsid) && !referenceSsid.equals(oldSsid)) {
            ssidTrackingEventType = SsidTrackingEventType.CONNECT;
        } else if (!referenceSsid.equals(newSsid) && referenceSsid.equals(oldSsid)) {
            ssidTrackingEventType = SsidTrackingEventType.DISCONNECT;
        }

        return ssidTrackingEventType;
    }

    public static int getRandomNumberInRange(final int minInclusive, final int maxExclusive) {
        Random r = new Random();
        return r.ints(minInclusive, maxExclusive).findFirst().getAsInt();
    }

    public static String stringFormat(String format, Object... params) {
        return MessageFormatter.arrayFormat(format, params).getMessage();
    }

    public static boolean isDemoMode() {
        return ProfileManager.getActiveProfile().contains("demo");
    }
}