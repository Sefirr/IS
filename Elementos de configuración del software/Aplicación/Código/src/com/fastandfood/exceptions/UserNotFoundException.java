package com.fastandfood.exceptions;

/**
 * Producida cuando intenta consultarse un usuario que no se encuentra dado de alta en el sistema.
 *
 * @author Borja
 */
@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
