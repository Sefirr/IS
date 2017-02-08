package com.fastandfood.users;

/**
 * Usuario de la aplicación.
 * Actor principal de todas las funciones en la interfaz,
 * a la hora de los casos de uso se tienen en cuenta sus permisos para poderle mostrar la información necesaria.
 *
 * @author Borja
 */
public class User {

    /**
     * @see com.fastandfood.users.UserPermission
     */
    private UserPermission _permission;
    private String _employeeName;
    private String _userName,
                    _password;

    public User(UserPermission permission, String employeeName, String userName, String password) {
        this._permission = permission;
        this._employeeName = employeeName;
        this._userName = userName;
        this._password = password;
    }

    public UserPermission getPermission() {
        return this._permission;
    }

    public boolean hasPermission(UserPermission p) {
        return this._permission == p;
    }

    public void changeUserName(String userName) {
        this._userName = userName;
    }

    public void changePassword(String newPassword) {
        this._password = newPassword;
    }

    public String getEmployeeName() {
        return this._employeeName;
    }

    public String getUserName() {
        return this._userName;
    }

    public String getPassword() {
        return this._password;
    }

    public boolean checkPass(String password) {
        return this._password.equals(password);
    }

    @Override
    public String toString() {
        return  this._permission.ordinal() + "\n" +
                this._employeeName + "\n" +
                this._userName + "\n" +
                this._password + "\n";
    }
}
