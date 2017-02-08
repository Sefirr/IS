package com.fastandfood.exceptions;

/**
 * @author Borja
 */
public class OrderNotFoundException extends Exception {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
