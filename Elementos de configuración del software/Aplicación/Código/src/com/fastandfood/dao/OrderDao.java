package com.fastandfood.dao;

import com.fastandfood.core.Order;
import com.fastandfood.core.Product;
import com.fastandfood.exceptions.ProductNotFoundException;

import java.sql.*;
import java.util.ArrayList;

public class OrderDao {

    StockDao stockDao;

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public OrderDao(Database _database) {
        this.conn = _database.getConnection();
        this.stockDao = new StockDao(_database);
    }

    public void insertOrder(Order o) {
        try {
            if(inList(o.getId()))
                return;

            // Add to Orders Id, Supplier and Date
            pst = conn.prepareStatement("INSERT INTO Orders (Vendor_Name, Date, Confirmed) VALUES(?, ?, ?)");

            pst.setString(1, o.getSupplier());
            pst.setDate(2, Date.valueOf(o.getDate()));
            pst.setInt(3, (o.isApproved() ? 1 : 0));

            pst.executeUpdate();

            // Add to Orders_Pr relations to Products
            insertOrderProducts(o.getProducts());

        } catch (SQLException ex) {
            //TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void insertOrderProducts(ArrayList<Product> productList) throws SQLException {
        int orderId;

        pst = conn.prepareStatement("SELECT Id FROM Orders ORDER BY Id DESC LIMIT 1");
        rs = pst.executeQuery();

        if(!rs.next())
            return;

        orderId = rs.getInt(1);

        pst = conn.prepareStatement("INSERT INTO Orders_Pr VALUES(?, ?, ?)");
        pst.setInt(1, orderId);

        for(Product p : productList) {
            pst.setInt(2, p.getId());
            pst.setInt(3, p.getAmount());

            pst.executeUpdate();
        }
    }

    public void confirmOrders() {
        try {
            pst = conn.prepareStatement("UPDATE Orders SET Confirmed=1 WHERE Confirmed=0");
            pst.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteSale(int id) {
        try {
            pst = conn.prepareStatement("DELETE FROM Orders WHERE Id = ? LIMIT 1");
            pst.setInt(1, id);
            pst.executeUpdate();

            pst = conn.prepareStatement("DELETE FROM Orders_Pr WHERE Id = ?");
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            //TODO
        }
    }

    // TODO products shouldn't be in stock for this [^1]
    public Order searchOrder(int id) {
        try {
            int orderId = -1;
            String orderSupplier = null;
            ArrayList<Product> productList = new ArrayList<>();
            String orderDate = null;
            boolean confirmed = false;

            int productId;
            int productAmount;

            Product product;

            pst = conn.prepareStatement("SELECT * FROM Orders WHERE Id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if(rs.next()) {
                orderId = rs.getInt(1);
                orderSupplier = rs.getString(2);
                orderDate = rs.getDate(3).toString();
                confirmed = ( rs.getInt(4) == 1 );
            }

            pst = conn.prepareStatement("SELECT * FROM Orders_Pr WHERE Id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            while(rs.next()) {
                productId = rs.getInt(2);
                productAmount = rs.getInt(3);

                product = stockDao.searchProduct(productId); /* TODO [^1] */
                product.setAmount(productAmount);

                productList.add(product);
            }

            if((orderId >= 0) && orderSupplier != null && orderDate != null)
                return new Order(orderId, orderSupplier, productList, orderDate, confirmed);

            else
                return null;

        } catch (SQLException | ProductNotFoundException ex) {
            //TODO
        }

        return null;
    }

    private boolean inList(int id) {
        int count = 0;
        try {
            pst = conn.prepareStatement("SELECT COUNT(*) FROM Orders WHERE Id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if(rs.next())
                count = rs.getInt(1);

            return (count ==1 );

        } catch (SQLException ex) {
            //TODO
            return false;
        }
    }

    public ArrayList<String> displayNotConfirmed() {
        try {
            pst = conn.prepareStatement("SELECT Id, Vendor_Name, Date FROM Orders WHERE Confirmed = 0");
            rs = pst.executeQuery();

            ArrayList<String> set = new ArrayList<>();

            while(rs.next()) {
                String id = String.valueOf(rs.getInt("Id"));
                String vendorName = rs.getString("Vendor_Name");
                String date = String.valueOf(rs.getDate("Date"));

                set.add(id);
                set.add(vendorName);
                set.add(date);
            }

            return set;
        } catch (SQLException ex) {
            // TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }

    //TODO
    public ArrayList<String> displayAll() {
        try {
            pst = conn.prepareStatement("SELECT Id, Vendor_Name, Date, Confirmed FROM Orders");
            rs = pst.executeQuery();

            ArrayList<String> set = new ArrayList<>();

            while(rs.next()) {
                String id = String.valueOf(rs.getInt("Id"));
                String vendorName = rs.getString("Vendor_Name");
                String date = String.valueOf(rs.getDate("Date"));
                String confirmed = rs.getInt("Confirmed") == 1 ? "Confirmado" : "No Confirmado";

                set.add(id);
                set.add(vendorName);
                set.add(date);
                set.add(confirmed);
            }

            return set;
        } catch (SQLException ex) {
            // TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return null;
    }
}
