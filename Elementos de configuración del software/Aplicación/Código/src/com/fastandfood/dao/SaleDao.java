package com.fastandfood.dao;

import com.fastandfood.core.Product;
import com.fastandfood.core.Sale;
import com.fastandfood.exceptions.ProductNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SaleDao {

    StockDao stockDao;

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public SaleDao(Database _database) {
        this.conn = _database.getConnection();
        this.stockDao = new StockDao(_database);
    }

    public void insertSale(Sale s) {
        try {
            // Add to Sales the Total Cost
            pst = conn.prepareStatement("INSERT INTO Sales (Total) VALUES(?)");
            pst.setDouble(1, s.getPrice());

            pst.executeUpdate();

            // Add to Sales_Pr relations to Products
            insertSaleProducts(s.getProducts());

        } catch (SQLException ex) {
            //TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void insertSaleProducts(ArrayList<Product> productList) throws SQLException {
        int saleId;

        pst = conn.prepareStatement("SELECT Id FROM Sales ORDER BY Id DESC LIMIT 1");
        rs = pst.executeQuery();

        if(!rs.next())
            return;

        saleId = rs.getInt(1);

        pst = conn.prepareStatement("INSERT INTO Sales_Pr VALUES(?, ?, ?)");
        pst.setInt(1, saleId);

        for(Product p : productList) {
            pst.setInt(2, p.getId());
            pst.setInt(3, p.getAmount());

            pst.executeUpdate();
        }
    }

    public void deleteSale(int id) {
        try {
            pst = conn.prepareStatement("DELETE FROM Sales WHERE Id = ? LIMIT 1");
            pst.setInt(1, id);
            pst.executeUpdate();

            pst = conn.prepareStatement("DELETE FROM Sales_Pr WHERE Id = ?");
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            //TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public double getTotal() {
        try {
            pst = conn.prepareStatement("SELECT SUM(Total) FROM Sales");
            rs = pst.executeQuery();

            if(!rs.next())
                return -1;

            return rs.getDouble(1);
        } catch (SQLException ex) {
            //TODO
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return -1;
        }
    }

    /* TODO Same as OrderDao#searchOrder, the product shouldn't be in stock for this [^1] */
    public Sale searchSale(int id) {
        try {
            int saleId = -1;
            ArrayList<Product> productList = new ArrayList<>();
            double saleTotal = -1;

            int productId;
            int productAmount;

            Product product;

            pst = conn.prepareStatement("SELECT * FROM Sales WHERE Id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if(rs.next()) {
                saleId = rs.getInt(1);
                saleTotal = rs.getDouble(2);
            }

            pst = conn.prepareStatement("SELECT * FROM Sales_Pr WHERE Sale_Id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            while(rs.next()) {
                productId = rs.getInt(2);
                productAmount = rs.getInt(3);

                product = stockDao.searchProduct(productId); // TODO [^1]
                product.setAmount(productAmount);

                productList.add(product);
            }

            if((saleId >= 0) && (saleTotal >= 0))
                return new Sale(saleId, productList, saleTotal);

            else
                return null;

        } catch (SQLException | ProductNotFoundException ex) {
            // TODO
        }

        return null;
    }

    private boolean inList(int id) {
        int count = 0;

        try {
            pst = conn.prepareStatement("SELECT COUNT(*) FROM Sales WHERE Id = ?");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if(rs.next())
                count = rs.getInt(1);

            return (count == 1);
        } catch (SQLException ex) {
            //TODO
            return false;
        }
    }

    //TODO
    public ArrayList<String> displayAll() {
        return null;
    }
}
