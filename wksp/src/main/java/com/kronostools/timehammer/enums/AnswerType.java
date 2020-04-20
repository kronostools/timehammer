package com.kronostools.timehammer.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum AnswerType {
    Y("Y", "Sí", "La acción se ha registrado correctamente."),
    N5M("+5M", "+5 m", "Anotado queda. Dentro de 5 minutos te lo volveré a recordar."),
    N10M("+10M", "+10 m", "Anotado queda. Dentro de 10 minutos te lo volveré a recordar."),
    N15M("+15M", "+15 m", "Anotado queda. Dentro de 15 minutos te lo volveré a recordar."),
    N20M("+20M", "+20 m", "Anotado queda. Dentro de 20 minutos te lo volveré a recordar."),
    N("N", "No", "Anotado queda. Hasta el próximo día laborable no se te volverá a notificar de lo mismo.");

    private final String answerCode;
    private final String answerButtonText;
    private final String defaultAnswerMessage;

    AnswerType(final String answerCode, final String answerButtonText, final String defaultAnswerMessage) {
        this.answerCode = answerCode;
        this.answerButtonText = answerButtonText;
        this.defaultAnswerMessage = defaultAnswerMessage;
    }

    public String getAnswerCode() {
        return answerCode;
    }

    public String getAnswerButtonText() {
        return answerButtonText;
    }

    public String getDefaultAnswerResponseText() {
        return defaultAnswerMessage;
    }

    public static Optional<AnswerType> fromAnswerCode(final String answerCode) {
        return Stream.of(AnswerType.values())
                .filter(c -> c.getAnswerCode().equals(answerCode))
                .findFirst();
    }
}
