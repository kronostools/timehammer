package com.kronostools.timehammer.chatbot.converters;

public interface Converter<F, T> {
    T convert(F origin);
}