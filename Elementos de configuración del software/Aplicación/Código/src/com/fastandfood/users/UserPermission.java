package com.fastandfood.users;

/**
 * Nivel de permisos que tiene un usuario de la aplicación.
 * Se le presentará una interfaz, y se le permitirá realizar distintas funciones,
 * dependiendo de sus permisos.
 * <p>
 * Un usuario sólo puede tener un rol particular en cualquier momento dado.
 *
 * @author Borja
 */
public enum UserPermission {
    NONE,       // Usuario no registrado
    REGISTERED, // Usuario registrado, pero no autenticado
    ADMIN,      // Administrador
    MANAGER,    // Encargado de stock
    VENDOR;      // Vendedor en caja

    public static boolean contains(String s) {
        for(UserPermission up : UserPermission.values()) {
            if(up.name().equals(s)) return true;
        }
        return false;
    }

    public static String getName(UserPermission p) {
        String name;
        switch(p) {
            case ADMIN:
                name = "Administrador";
                break;
            case MANAGER:
                name = "Encargado";
                break;
            case VENDOR:
                name = "Vendedor";
                break;
            default:
                name = null;
        }

        return name;
    }
}
