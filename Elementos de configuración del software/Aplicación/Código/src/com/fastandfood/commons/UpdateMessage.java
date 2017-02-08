package com.fastandfood.commons;

import com.fastandfood.core.BusinessEvent;

/**
 * Usage:
 *   UpdateMessage<Object, Object> message = UpdateMessage.composeMessage(0x0F, 0x0F);
 *   pair.getEvent();
 *   pair.getArg();
 *
 * @author Borja
 */
public class UpdateMessage<T> {

    private final BusinessEvent eventType;
    private final T argument;

    public static <T> UpdateMessage<T> composeMessage(BusinessEvent event, T arg) {
        return new UpdateMessage<>(event, arg);
    }

    public UpdateMessage(BusinessEvent event, T arg) {
        this.eventType = event;
        this.argument = arg;
    }

    public BusinessEvent getEvent() {
        return eventType;
    }

    public T getArg() {
        return argument;
    }
}
