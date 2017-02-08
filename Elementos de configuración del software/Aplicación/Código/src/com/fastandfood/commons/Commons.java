package com.fastandfood.commons;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Proporciona funcionalidades comunes a toda la aplicación.
 *
 * @author Borja
 */
public class Commons {

    public static final String PROPERTIES_URL = "./database.properties";

    public static boolean isInteger(String line) {
        try {
            Integer.parseInt(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String line) {
        try {
            Double.parseDouble(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Returns the current date at the time the method is called
    public static String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yy HH:mm").format(Calendar.getInstance().getTime());
    }

    // Capitalize first letter of first of a String
    public static String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
