package com.fastandfood.exceptions;

/**
 * Producida cuando un producto consultado no existe.
 *
 * @author Borja
 */
@SuppressWarnings("serial")
public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String message) {
        super(message);
    }

    @Override
	public String getMessage() {
        return super.getMessage();
    }
}
