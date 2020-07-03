package com.kronostools.timehammer.comunytek.constants;

import com.kronostools.timehammer.common.messages.constants.AnswerOption;

public enum ComunytekAction {
    NOOP("-", ""),
    START("E", ""),
    LUNCH_PAUSE("P", "Comida"),
    LUNCH_RESUME("R", ""),
    END("S", "");

    private final String code;
    private final String comment;

    ComunytekAction(final String code, final String comment) {
        this.code = code;
        this.comment = comment;
    }

    public static ComunytekAction fromAnswerOption(final AnswerOption answerOption) {
        final ComunytekAction action;

        switch (answerOption) {
            case SW_Y:
                action = START;
                break;
            case EW_Y:
                action = END;
                break;
            case SL_Y:
                action = LUNCH_PAUSE;
                break;
            case EL_Y:
                action = LUNCH_RESUME;
                break;
            default:
                action = NOOP;
                break;
        }

        return action;
    }

    public String getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }
}
