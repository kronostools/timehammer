package com.kronostools.timehammer.statemachine.model;

import com.kronostools.timehammer.common.constants.WorkerStatusAction;
import com.kronostools.timehammer.common.messages.constants.AnswerOption;
import com.kronostools.timehammer.common.messages.constants.StatusContext;
import com.kronostools.timehammer.common.messages.constants.StatusContextAction;

import java.util.Objects;

public class WaitId {
    private String workerInternalId;
    private StatusContext context;
    private StatusContextAction contextAction;

    public WaitId() {}

    public WaitId(final String workerInternalId, final StatusContext context, final StatusContextAction contextAction) {
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

    public void setWorkerInternalId(String workerInternalId) {
        this.workerInternalId = workerInternalId;
    }

    public StatusContext getContext() {
        return context;
    }

    public void setContext(StatusContext context) {
        this.context = context;
    }

    public StatusContextAction getContextAction() {
        return contextAction;
    }

    public void setContextAction(StatusContextAction contextAction) {
        this.contextAction = contextAction;
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

    @Override
    public String toString() {
        return workerInternalId + "#" + context.name() + "#" + contextAction.name();
    }
}