package com.fastandfood.exceptions;

/**
 * Producida cuando se produce un error al conectarse con la base de datos
 *
 * @author Borja
 */
public class ConnectionErrorException extends Exception {

    public ConnectionErrorException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
