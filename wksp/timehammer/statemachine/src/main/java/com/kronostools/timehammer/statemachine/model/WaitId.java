package com.kronostools.timehammer.statemachine.model;

import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.constants.AnswerOption;
import com.kronostools.timehammer.common.messages.constants.StatusContext;
import com.kronostools.timehammer.common.messages.constants.StatusContextAction;

import java.io.Serializable;
import java.util.Objects;

public class WaitId implements Serializable {
    private final String workerInternalId;
    private final StatusContext context;
    private final StatusContextAction contextAction;

    WaitId(final String workerInternalId, final StatusContext context, final StatusContextAction contextAction) {
        this.workerInternalId = workerInternalId;
        this.context = context;
        this.contextAction = contextAction;
    }

    public WaitId(final String workerInternalId, final WorkerStatusAction workerStatusAction) {
        this(workerInternalId, workerStatusAction.getContext(), workerStatusAction.getContextAction());
    }

    public WaitId(final String workerInternalId, final AnswerOption answerOption) {
        this(workerInternalId, answerOption.getContext(), answerOption.getContextAction());
    }

    public String getWorkerInternalId() {
        return workerInternalId;
    }

    public StatusContext getContext() {
        return context;
    }

    public StatusContextAction getContextAction() {
        return contextAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaitId waitId = (WaitId) o;
        return workerInternalId.equals(waitId.workerInternalId) &&
                context == waitId.context &&
                contextAction == waitId.contextAction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerInternalId, context, contextAction);
    }
}