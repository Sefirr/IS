package com.fastandfood.users;

import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.core.BusinessEvent;
import com.fastandfood.dao.Database;
import com.fastandfood.dao.StaffDao;
import com.fastandfood.exceptions.DuplicateUserException;
import com.fastandfood.exceptions.UserNotFoundException;
import com.fastandfood.observers.Watchable;

import java.util.ArrayList;


/**
 * Lista de usuarios de la aplicación.
 * Los datos de los usuarios se guardan en una base de datos y son accedidos mediante
 * @see com.fastandfood.dao.StaffDao
 *
 * @author Borja
 */
public class Staff extends Watchable {

    private StaffDao dao = null;

    public Staff(Database _database) {
        this.dao = new StaffDao(_database);

    }

    public void init() {
        update();
    }

    public void addUser(User user) throws DuplicateUserException {
       this.dao.addUser(user);
        update();
    }

    public void addUser(UserPermission permission, String employeeName, String userName, String password) throws DuplicateUserException {
        addUser(new User(permission, employeeName, userName, password));
    }

    public void removeUser(String userName) throws UserNotFoundException {
        this.dao.removeUser(userName);
        update();
    }

    public void modifyUser(String userName, String newpass) throws UserNotFoundException {
        this.dao.modifyUser(userName, newpass);
    }

    public void searchByUser(String query) {
        ArrayList<String> set = new ArrayList<>();
        ArrayList<User> result = dao.searchByUser(query);

        if(result != null && !result.isEmpty()) {
            for(User u : result) {
                set.add(UserPermission.getName(u.getPermission()));
                set.add(u.getEmployeeName());
                set.add(u.getUserName());
            }
        }

        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.USER, set));
    }

    public void searchByName(String query) {
        ArrayList<String> set = new ArrayList<>();
        ArrayList<User> result = dao.searchByName(query);

        if(result != null && !result.isEmpty()) {
            for(User u : result) {
                set.add(UserPermission.getName(u.getPermission()));
                set.add(u.getEmployeeName());
                set.add(u.getUserName());
            }
        }

        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.USER, set));
    }

    public void searchByPermission(String query) {
        ArrayList<String> set = new ArrayList<>();
        ArrayList<User> result = dao.searchByPermission(
                UserPermission.contains(query) ? UserPermission.valueOf(query)
                                               : UserPermission.NONE);

        if(result != null && !result.isEmpty()) {
            for(User u : result) {
                set.add(UserPermission.getName(u.getPermission()));
                set.add(u.getEmployeeName());
                set.add(u.getUserName());
            }
        }

        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.USER, set));
    }

    public User validate(String username, String password) {
        User u = dao.findUser(username);

        return (u != null && u.checkPass(password)) ? u : null;
    }

    private void update() {
        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.CONTENT, displayContents()));
    }

    /* Display info from all Users: Permission + name + username */
    private ArrayList<String> displayContents() {
        return dao.displayAll();
    }
}
