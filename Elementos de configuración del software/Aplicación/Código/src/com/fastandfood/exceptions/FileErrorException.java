package com.fastandfood.exceptions;

/**
 * Producida cuando existe un error de formato en la base de datos.
 * En caso de que la información leída desde BD contenga campos incompletos, o tengan valores extraños.
 *
 * @author Borja
 */
@SuppressWarnings("serial")
public class FileErrorException extends Exception {

    public FileErrorException(String message) {
        super(message);
    }

    @Override
	public String getMessage() {
        return super.getMessage();
    }
}