package com.kronostools.timehammer.utils;

import com.kronostools.timehammer.enums.SsidTrackingEventType;
import io.quarkus.runtime.configuration.ProfileManager;
import org.slf4j.helpers.MessageFormatter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
        final Random r = new Random();
        return r.ints(minInclusive, maxExclusive).findFirst().getAsInt();
    }

    public static <T> T getRandomElementFromArray(final T[] array) {
        return array[Utils.getRandomNumberInRange(0, array.length)];
    }

    public static <T> Set<T> getNDifferentRandomElementsFromArray(final T[] array, final int n) {
        final Set<T> result = new HashSet<>();

        while (result.size() < n) {
            result.add(getRandomElementFromArray(array));
        }

        return result;
    }

    public static String stringFormat(final String format, final Object... params) {
        return MessageFormatter.arrayFormat(format, params).getMessage();
    }

    public static String truncateString(final String text) {
        return truncateString(text, Constants.DEFAULT_TEXT_TRUNCATE_WIDTH);
    }

    public static String truncateString(final String text, final int width) {
        String result = text;

        if (text.length() > width) {
            result = text.substring(0, width - 3) + "...";
        }

        return result;
    }

    public static boolean isDemoMode() {
        return ProfileManager.getActiveProfile().contains("demo");
    }
}