package com.kronostools.timehammer.enums;

import com.kronostools.timehammer.utils.Utils;
import com.kronostools.timehammer.vo.AnswerOptionVo;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Map.entry;

public enum QuestionType {
    START("START",
            new String[] {
                    "Ya va siendo hora de ponerse a currar no?"
            },
            new EnumMap(Map.ofEntries(
                    entry(AnswerType.Y, new AnswerOptionVo(AnswerType.Y, "Se ha registrado correctamente la entrada.")),
                    entry(AnswerType.N5M, new AnswerOptionVo(AnswerType.N5M)),
                    entry(AnswerType.N10M, new AnswerOptionVo(AnswerType.N10M)),
                    entry(AnswerType.N15M, new AnswerOptionVo(AnswerType.N15M)),
                    entry(AnswerType.N, new AnswerOptionVo(AnswerType.N))
            ))
    ),
    LUNCH_START("LUNCH_START",
            new String[] {
                    "Se come o qué?"
            },
            new EnumMap(Map.ofEntries(
                    entry(AnswerType.Y, new AnswerOptionVo(AnswerType.Y, "Se ha registrado correctamente la pausa para la comida.")),
                    entry(AnswerType.N5M, new AnswerOptionVo(AnswerType.N5M)),
                    entry(AnswerType.N10M, new AnswerOptionVo(AnswerType.N10M)),
                    entry(AnswerType.N15M, new AnswerOptionVo(AnswerType.N15M)),
                    entry(AnswerType.N, new AnswerOptionVo(AnswerType.N))
            ))
    ),
    LUNCH_RESUME("LUNCH_RESUME",
            new String[] {
                    "Has terminado ya de comer?"
            },
            new EnumMap(Map.ofEntries(
                    entry(AnswerType.Y, new AnswerOptionVo(AnswerType.Y, "Se ha registrado correctamente el final de la pausa para la comida.")),
                    entry(AnswerType.N5M, new AnswerOptionVo(AnswerType.N5M)),
                    entry(AnswerType.N10M, new AnswerOptionVo(AnswerType.N10M)),
                    entry(AnswerType.N15M, new AnswerOptionVo(AnswerType.N15M)),
                    entry(AnswerType.N20M, new AnswerOptionVo(AnswerType.N20M))
            ))
    ),
    END("END",
            new String[] {
                    "Habrá que irse ya para casa no?"
            },
            new EnumMap(Map.ofEntries(
                    entry(AnswerType.Y, new AnswerOptionVo(AnswerType.Y, "Se ha registrado correctamente la salida.")),
                    entry(AnswerType.N5M, new AnswerOptionVo(AnswerType.N5M)),
                    entry(AnswerType.N10M, new AnswerOptionVo(AnswerType.N10M)),
                    entry(AnswerType.N15M, new AnswerOptionVo(AnswerType.N15M)),
                    entry(AnswerType.N20M, new AnswerOptionVo(AnswerType.N20M))
            ))
    );

    private final String questionCode;
    private final String[] questionText;
    private final Map<AnswerType, AnswerOptionVo> answerOptions;

    QuestionType(final String questionCode, final String[] questionText, final Map<AnswerType, AnswerOptionVo> answerOptions) {
        this.questionCode = questionCode;
        this.questionText = questionText;
        this.answerOptions = answerOptions;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public String getQuestionText() {
        return questionText[Utils.getRandomNumberInRange(0, questionText.length)];
    }

    public Map<AnswerType, AnswerOptionVo> getAnswerOptions() {
        return answerOptions;
    }

    public AnswerOptionVo getAnswerOption(final AnswerType answerType) {
        return answerOptions.get(answerType);
    }

    public static Optional<QuestionType> fromQuestionCode(final String questionCode) {
        return Stream.of(QuestionType.values())
                .filter(c -> c.getQuestionCode().equals(questionCode))
                .findFirst();
    }
}
