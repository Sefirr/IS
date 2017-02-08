package com.fastandfood.exceptions;

/**
 * Producida en caso de que la validación del login sea inválida.
 * @see com.fastandfood.core.FastCore#validate(String, String)
 *
 * @author Borja
 */
@SuppressWarnings("serial")
public class InvalidLoginException extends Exception {

    public InvalidLoginException(String message) {
        super(message);
    }

    @Override
	public String getMessage() {
        return super.getMessage();
    }
}
