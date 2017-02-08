package com.fastandfood.dao;

import com.fastandfood.core.Product;
import com.fastandfood.exceptions.ProductAmountException;
import com.fastandfood.exceptions.ProductNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * TODO
 * Reformat to standard:
 *    insertProduct(Product)
 *    updateProduct(Product)
 *    deleteProduct(Product)
 *    ... etc
 */

public class StockDao {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public StockDao(Database _database) {
        this.conn = _database.getConnection();
    }

    // TODO System.exit()
    public void addProduct(Product product) {
        try {
            if(!inStock(product.getId())) {
                pst = conn.prepareStatement("INSERT INTO Stock VALUES(?, ?, ?, ?, ?)");

                pst.setInt(1, product.getId());
                pst.setString(2, product.getName());
                pst.setInt(3, product.getTag().ordinal());
                pst.setDouble(4, product.getPrice());
                pst.setInt(5, product.getAmount());

                pst.executeUpdate();

            } else {
                pst = conn.prepareStatement("UPDATE Stock SET Amount = ? WHERE Id = ?");

                Product p = searchProduct(product.getId());
                pst.setInt(1, p.getAmount() + product.getAmount());
                pst.setInt(2, product.getId());

                pst.executeUpdate();
            }
        } catch (SQLException ex) {/* TODO */} catch (ProductNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void addProduct(int id, int amount) throws ProductNotFoundException {

        if(!inStock(id))
            throw new ProductNotFoundException("El producto con identificador " + id + " no se encuentra en el almacén.");

        Product p = searchProduct(id);
        try {
            pst = conn.prepareStatement("UPDATE Stock SET Amount = ? WHERE Id = ?");
            pst.setInt(1, p.getAmount() + amount);
            pst.setInt(2, id);

            pst.executeUpdate();
        } catch (SQLException ex) {/* TODO */}
    }

    public void removeProduct(int id, int amount) throws ProductNotFoundException, ProductAmountException {

        if(!inStock(id))
            throw new ProductNotFoundException("El producto con identificador " + id + " no se encuentra en el almacén.");

        Product p = searchProduct(id);
        if((p.getAmount() != 0) && (p.getAmount() >= amount)) {
            try {
                pst = conn.prepareStatement("UPDATE Stock SET Amount = ? WHERE Id = ?");
                pst.setInt(1, p.getAmount() - amount);
                pst.setInt(2, id);

                pst.executeUpdate();
            } catch (SQLException ex) {/* TODO */}
        }
        else
            throw new ProductAmountException("El producto con identificador " + id + " no se pudo eliminar puesto que no hay existencias suficientes.");
    }

    // TODO System.exit()
    public void removeFromStock(int id) throws ProductNotFoundException {

        if(!inStock(id))
            throw new ProductNotFoundException("El producto con identificador " + id + " no se encuentra en el almacén.");

        Product p = searchProduct(id);

        try {
            removeProduct(id, p.getAmount());
        } catch (ProductAmountException pae) {
            System.err.println(pae.getMessage());
            System.exit(1);
        }
    }

    public void dropFromStock(int id) throws ProductNotFoundException {

        if(!inStock(id))
            throw new ProductNotFoundException("El producto con identificador \"" + id + "\"no se encuentra en stock");

        try {
            pst = conn.prepareStatement("DELETE FROM Stock WHERE Id = ? LIMIT 1");
            pst.setInt(1, id);

            pst.executeUpdate();
        } catch (SQLException ex) {/* TODO */}
    }

    public Product searchProduct(int id) throws ProductNotFoundException {

        if(!inStock(id))
            throw new ProductNotFoundException("El producto con identificador \"" + id + "\"no se encuentra en stock");

        try {
            pst = conn.prepareStatement("SELECT * FROM Stock WHERE Id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if(rs.next())
                return new Product(rs.getInt(1), rs.getString(2), Product.Tag.values()[rs.getInt(3)], rs.getDouble(4), rs.getInt(5));

        } catch (SQLException ex) {/* TODO */}
        return null;
    }

    public Product searchProduct(String productName) throws ProductNotFoundException {
        try {
            pst = conn.prepareStatement("SELECT * FROM Stock WHERE Name = ?");
            pst.setString(1, productName);
            rs = pst.executeQuery();

            if(rs.next())
                return new Product(rs.getInt(1), rs.getString(2), Product.Tag.values()[rs.getInt(3)], rs.getDouble(4), rs.getInt(5));
            else
                throw new ProductNotFoundException("El producto \"" + productName + "\"no se encuentra en stock");

        } catch (SQLException ex) {/* TODO */}
        return null;
    }

    private boolean inStock(int id) {
        int count = 0;

        try {
            pst = conn.prepareStatement("SELECT COUNT(*) FROM Stock WHERE Id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if(rs.next())
                count = rs.getInt(1);

            return (count > 0);
        } catch (SQLException ex) {/* TODO */}
        return false;
    }

    public ArrayList<String> displayAll() {
        try {
            pst = conn.prepareStatement("SELECT Name, Price, Amount FROM Stock");
            rs = pst.executeQuery();

            ArrayList<String> set = new ArrayList<>();

            while(rs.next()) {
                String name = rs.getString("Name");
                String price = rs.getString("Price");
                String amount = rs.getString("Amount");

                set.add(name);
                set.add(price);
                set.add(amount);
            }

            return set;
        } catch (SQLException ex) {/* TODO */}

        return null;
    }
}