package com.fastandfood.dao;

import com.fastandfood.exceptions.DuplicateUserException;
import com.fastandfood.exceptions.UserNotFoundException;
import com.fastandfood.users.User;
import com.fastandfood.users.UserPermission;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StaffDao {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public StaffDao(Database _database) {
        this.conn = _database.getConnection();
    }

    public void addUser(User user) throws DuplicateUserException {
        if(inStaff(user.getUserName()))
            throw new DuplicateUserException(user.getUserName() + " no se encuentra disponible.");
        try {
            pst = conn.prepareStatement("INSERT INTO Staff VALUES(?, ?, ?, ?)");

            pst.setInt(1, (user.getPermission().ordinal()));
            pst.setString(2,  user.getEmployeeName());
            pst.setString(3,  user.getUserName());
            pst.setString(4, user.getPassword());

            pst.executeUpdate();
        } catch (SQLException ex) {
            //TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void removeUser(String userName) throws UserNotFoundException {

        if(!inStaff(userName))
            throw new UserNotFoundException(userName + " no se encuentra en el staff.");

        try {
            pst = conn.prepareStatement("DELETE FROM Staff WHERE User = ? LIMIT 1");
            pst.setString(1, userName);
            pst.executeUpdate();
        } catch (SQLException ex) {
            // TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void modifyUser(String oldusername, String newpass) throws UserNotFoundException {

        if(!inStaff(oldusername))
            throw new UserNotFoundException(oldusername + " no se encuentra en el staff.");
        try {
            pst = conn.prepareStatement("UPDATE Staff SET Pass = ? WHERE User = ? ");

            pst.setString(1, newpass);
            pst.setString(2, oldusername);

            pst.executeUpdate();
        } catch (SQLException ex) {
            // TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /* Returns null if not found - always returns a single User */
    public User findUser(String username) {
        try {
            pst = conn.prepareStatement("SELECT * FROM Staff WHERE USER = ? LIMIT 1");

            pst.setString(1, username);
            rs = pst.executeQuery();

            if(rs.next())
                return new User(UserPermission.values()[rs.getInt(1)], rs.getString(2), rs.getString(3), rs.getString(4));
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    /* Returns an empty arrat if not found, null if SQLException happens */
    public ArrayList<User> searchByUser(String userName) {
        try {

            /* Search first for results like username */
            pst = conn.prepareStatement("SELECT * FROM Staff WHERE User LIKE ?");
            pst.setString(1, userName+"%");
            rs = pst.executeQuery();

            ArrayList<User> resultUsers = new ArrayList<>();

            while(rs.next())
                resultUsers.add(new User(UserPermission.values()[rs.getInt(1)], rs.getString(2), rs.getString(3), rs.getString(4)));

            if(!resultUsers.isEmpty())
                return resultUsers;

            /* If no matches were found, now search for exact matches of username */
            /* Code apparently never reached */
            pst = conn.prepareStatement("SELECT * FROM Staff WHERE User = ? LIMIT 1"); // LIMIT 1 because two users can't have the same username
            pst.setString(1, userName);
            rs = pst.executeQuery();

            if(rs.next())
                resultUsers.add(new User(UserPermission.values()[rs.getInt(1)], rs.getString(2), rs.getString(3), rs.getString(4)));

            return resultUsers;

        } catch (SQLException ex) {
            // TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    /* Returns an empty array if not found, null if SQLException happens */
    public ArrayList<User> searchByName(String employeename) {
        try {
            pst = conn.prepareStatement("SELECT * FROM Staff WHERE Name LIKE ?");
            pst.setString(1, employeename+"%");
            rs = pst.executeQuery();

            ArrayList<User> resultUsers = new ArrayList<>();

            while(rs.next())
                resultUsers.add(new User(UserPermission.values()[rs.getInt(1)], rs.getString(2), rs.getString(3), rs.getString(4)));

            if(!resultUsers.isEmpty())
                return resultUsers;


            pst = conn.prepareStatement("SELECT * FROM Staff WHERE Name = ?");
            pst.setString(1, employeename);
            rs = pst.executeQuery();



            while(rs.next())
                resultUsers.add(new User(UserPermission.values()[rs.getInt(1)], rs.getString(2), rs.getString(3), rs.getString(4)));

            return resultUsers;
        } catch (SQLException ex) {
            // TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    /* Returns an empty array if not found, null if SQLException happens */
    public ArrayList<User> searchByPermission(UserPermission permission) {
        try {
            pst = conn.prepareStatement("SELECT * FROM Staff WHERE Permission = ?");
            pst.setInt(1, permission.ordinal());
            rs = pst.executeQuery();

            ArrayList<User> resultUsers = new ArrayList<>();

            while(rs.next()) {
                resultUsers.add(new User(UserPermission.values()[rs.getInt(1)], rs.getString(2), rs.getString(3), rs.getString(4)));
            }

            return resultUsers;

        } catch (SQLException ex) {
            // TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    private boolean inStaff(String userName) {
        int count = 0;

        try {
            pst = conn.prepareStatement("SELECT COUNT(*) FROM Staff WHERE User = ? ");
            pst.setString(1, userName);
            rs = pst.executeQuery();

            if(rs.next())
                count = rs.getInt(1);

            return (count == 1);
        } catch (SQLException ex) {
            //TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> displayAll() {
        try {
            pst = conn.prepareStatement("SELECT Permission, Name, User FROM Staff");
            rs = pst.executeQuery();

            ArrayList<String> set = new ArrayList<>();

            while(rs.next()) {
                UserPermission permission = UserPermission.values()[rs.getInt("Permission")];
                String name = rs.getString("Name");
                String username = rs.getString("User");

                set.add(UserPermission.getName(permission));
                set.add(name);
                set.add(username);
            }

            return set;
        } catch (SQLException ex) {
            // TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}