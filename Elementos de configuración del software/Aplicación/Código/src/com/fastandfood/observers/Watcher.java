package com.fastandfood.observers;

import com.fastandfood.commons.UpdateMessage;

/**
 * Ver Observer
 * Implementación propia de la clase Observer del patrón Observer.
 *
 * @author Borja
 */
public interface Watcher {

    void update(Watchable o, UpdateMessage arg);

}
