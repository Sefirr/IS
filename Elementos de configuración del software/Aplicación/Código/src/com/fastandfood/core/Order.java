package com.fastandfood.core;

import com.fastandfood.exceptions.ProductAmountException;
import com.fastandfood.exceptions.ProductNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author JAVIER
 *
 */
public class Order {

    private int _id;
    private boolean _approved;
    private String _supplier;
    private ArrayList<Product> _productList;
    private String _date;

    public Order(String supplier, ArrayList<Product> productList) {
        this._supplier = supplier;
        this._productList = productList;
    }

    public Order(int id, String supplier, String date) {
        this._id = id;
        this._supplier = supplier;
        this._productList = new ArrayList<>();
        this._approved = false;
        this._date = date;
    }

    public Order(int id, String supplier, ArrayList<Product> productList, String date) {
        this(id, supplier, productList, date, true);
    }

    public Order(int id, String supplier, ArrayList<Product> productList, String date, boolean confirmed) {
        this._id = id;
        this._supplier = supplier;
        this._productList = productList;
        this._approved = confirmed;
        this._date = date;
    }

    public int getId() {
        return this._id;
    }

    public String getSupplier() {
        return _supplier;
    }

    public String getDate() {
        return this._date;
    }

    public void addProduct(int id, int amount) throws ProductNotFoundException {
        Product p = searchProduct(id);
        if(p == null)
            throw new ProductNotFoundException("El producto con identificador " + id + " no se encuentra en el pedido.");

        p.setAmount(p.getAmount() + amount);
    }

    public void addProduct(int id, String productName, Product.Tag tag, double price, int amount) throws ProductNotFoundException {
        addProduct(new Product(id, productName, tag, price, amount));
    }

    public void addProduct(Product product) throws ProductNotFoundException {
        Product p = searchProduct(product.getId());

        if(p != null)
            p.setAmount(p.getAmount() + product.getAmount());
        else
            this._productList.add(product);
    }

    public void removeProduct(int id) throws ProductNotFoundException {
        Product p = searchProduct(id);

        if(p == null)
            throw new ProductNotFoundException("El producto con identificador " + id + " no se encuentra en el pedido.");

        _productList.remove(p);
    }

    public void removeProduct(int id, int amount) throws ProductAmountException, ProductNotFoundException {
        Product p = searchProduct(id);

        if(p == null)
            throw new ProductNotFoundException("El producto con identificador " + id + " no se encuentra en el pedido.");

        if((p.getAmount() != 0) && (p.getAmount() >= amount))
            p.setAmount(p.getAmount() - amount);
        else
            throw new ProductAmountException("El producto con identificador " + id + " se encuentra en una cantidad inferior a la que se pide en este pedido.");
    }

    private Product searchProduct(int id) {
        for(Product p : this._productList)
            if(p.getId() == id)
                return p;

        return null;
    }

    public ArrayList<Product> getProducts() {
        return _productList;
    }


    public void approve() {
        _approved = true;
    }

    public boolean isApproved() {
        return _approved;
    }

    @Override
    public String toString() {
        String productList;
        Iterator<Product> it = this._productList.iterator();

        if(!it.hasNext())
            return "Pedido: Lista de productos vacía";

        StringBuilder sb = new StringBuilder();

        do {
            Product p = it.next();
            sb.append(p.toString());
        } while(it.hasNext());

        productList = sb.toString();

        return  this._id + "\n" +
        this._supplier + "\n" +
        "--\n" +
        productList +
        "--\n" +
        this._date + "\n";
    }
}
