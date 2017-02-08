package com.fastandfood.exceptions;

/**
 * Producida cuando se intenta eliminar una cantidad de productos superior a la existente.
 *
 * @author Javier
 */
@SuppressWarnings("serial")
public class ProductAmountException extends Exception {

    public ProductAmountException(String message) {
        super(message);
    }

    @Override
	public String getMessage() {
        return super.getMessage();
    }
}