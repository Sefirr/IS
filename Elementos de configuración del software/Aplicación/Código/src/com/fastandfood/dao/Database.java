package com.fastandfood.dao;

import com.fastandfood.commons.Commons;
import com.fastandfood.exceptions.ConnectionErrorException;
import com.mysql.jdbc.Driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private String url;
    private String user;
    private String password;

    private Connection conn;

    public Database() {
        Properties properties = getProperties();

        this.url        = properties.getProperty("db.url");
        this.user       = properties.getProperty("db.user");
        this.password   = properties.getProperty("db.pass");
    }

    public void openConnection() throws ConnectionErrorException {
        try {
            DriverManager.registerDriver(new Driver());
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            conn = null;
            throw new ConnectionErrorException("Base de datos no encontrada: " +
                    e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Bse de datos no encontrada");
        }
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        FileInputStream in = null;

        try {
            in = new FileInputStream(Commons.PROPERTIES_URL);
            if(in != null) props.load(in);

        } catch (IOException ex) {
            System.err.println("Archivo de configuración no encontrado");
            System.exit(2);

        } finally {
            try {
                if(in != null)
                    in.close();
            } catch (IOException ex) {
                System.err.println("Archivo de configuración no encontrado");
                System.exit(2);
            }
        }

        return props;
    }
}