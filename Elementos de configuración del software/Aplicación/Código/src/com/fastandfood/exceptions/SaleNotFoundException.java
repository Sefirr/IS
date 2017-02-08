package com.fastandfood.exceptions;

/**
 * @author Borja
 */
public class SaleNotFoundException extends Exception {

    public SaleNotFoundException(String message) {
        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}
