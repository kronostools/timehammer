package com.kronostools.timehammer.common.constants;

import com.kronostools.timehammer.common.messages.constants.ChatbotMessages;
import com.kronostools.timehammer.common.messages.constants.StatusContext;
import com.kronostools.timehammer.common.messages.constants.StatusContextAction;
import com.kronostools.timehammer.common.utils.CommonUtils;

public enum WorkerStatusAction {
    CLOCKIN_WORK(true, StatusContext.WORK, StatusContextAction.START, ChatbotMessages.QUESTION_WORK_START),
    CLOCKOUT_WORK(true, StatusContext.WORK, StatusContextAction.END, ChatbotMessages.QUESTION_WORK_END),
    CLOCKIN_LUNCH(true, StatusContext.LUNCH, StatusContextAction.START, ChatbotMessages.QUESTION_LUNCH_START),
    CLOCKOUT_LUNCH(true, StatusContext.LUNCH, StatusContextAction.END, ChatbotMessages.QUESTION_LUNCH_END),
    NOOP(false, null, null, null);

    private final boolean hasQuestion;
    private final StatusContext context;
    private final StatusContextAction contextAction;
    private final String[] messages;

    WorkerStatusAction(final boolean hasQuestion, final StatusContext context, final StatusContextAction contextAction, final String[] messages) {
        this.hasQuestion = hasQuestion;
        this.context = context;
        this.contextAction = contextAction;
        this.messages = messages;
    }

    public boolean hasQuestion() {
        return hasQuestion;
    }

    public StatusContext getContext() {
        return context;
    }

    public StatusContextAction getContextAction() {
        return contextAction;
    }

    public String getText() {
        return CommonUtils.getRandomElementFromArray(messages);
    }
}