package com.fastandfood.exceptions;

/**
 * Producida a la hora de añadir nuevos usuarios a la aplicación.
 * En general, se lidia con esta excepción mostrando un aviso al usuario.
 * En caso de que se produzca durante el IO desde la base de datos, se mostrará un aviso, y luego se cerrará la aplicación,
 * pues supone que los archivos de datos han sido comprometidos de alguna manera.
 *
 * @author Borja
 */
@SuppressWarnings("serial")
public class DuplicateUserException extends Exception {

    public DuplicateUserException(String message) {
        super(message);
    }

    @Override
	public String getMessage() {
        return super.getMessage();
    }
}
