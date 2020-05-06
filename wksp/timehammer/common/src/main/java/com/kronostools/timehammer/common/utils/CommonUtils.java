package com.kronostools.timehammer.common.utils;

import com.kronostools.timehammer.common.constants.CommonConstants;
import org.slf4j.helpers.MessageFormatter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class CommonUtils {

    private CommonUtils() {}

    public static int getRandomNumberInRange(final int minInclusive, final int maxExclusive) {
        final Random r = new Random();
        return r.ints(minInclusive, maxExclusive).findFirst().orElse(minInclusive);
    }

    public static <T> T getRandomElementFromArray(final T[] array) {
        return array[CommonUtils.getRandomNumberInRange(0, array.length)];
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
        return truncateString(text, CommonConstants.DEFAULT_TEXT_TRUNCATE_WIDTH);
    }

    public static String truncateString(final String text, final int width) {
        String result = text;

        if (text.length() > width) {
            result = text.substring(0, width - 3) + "...";
        }

        return result;
    }
}