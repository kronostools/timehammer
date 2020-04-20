package com.kronostools.timehammer.chatbot.converters;

import com.kronostools.timehammer.comunytek.enums.ComunytekAction;
import com.kronostools.timehammer.enums.QuestionType;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class QuestionTypeToComunytekActionConverter implements Converter<QuestionType, ComunytekAction> {
    @Override
    public ComunytekAction convert(QuestionType origin) {
        ComunytekAction result = null;

        switch (origin) {
            case START:
                result = ComunytekAction.START;
                break;
            case LUNCH_START:
                result = ComunytekAction.LUNCH_PAUSE;
                break;
            case LUNCH_RESUME:
                result = ComunytekAction.LUNCH_RESUME;
                break;
            case END:
                result = ComunytekAction.END;
                break;
        }

        return result;
    }
}
