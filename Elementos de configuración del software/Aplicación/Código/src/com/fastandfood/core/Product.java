package com.fastandfood.core;

import com.fastandfood.commons.Commons;

import java.util.ArrayList;

/**
 * Producto que se vende en la aplicación.
 * Se encuentra representado en el stock, las ventas y los pedidos.
 *
 * @author Borja
 */
public class Product {

    public static enum Tag {
        NONE, HAMBURGUESAS, COMPLEMENTOS, ENSALADAS, BEBIDAS;

        public static boolean contains(String test) {
            for(Tag t : Tag.values())
                if(t.name().equals(test))
                    return true;

            return false;
        }
    }

    private int _productIdentifier;

    private String _name;
    private Tag _tag;

    private int _amount;

    private double _price;

    public Product(int id, String name, Tag tag, double price, int amount) {
        this._productIdentifier = id;
        this._name = name;
        this._tag = tag;
        this._price = price;
        this._amount = amount;

    }

    public int getId() {
        return this._productIdentifier;

    }

    public String getName() {
        return this._name;

    }

    public Tag getTag() {
        return this._tag;

    }

    public double getPrice() {
        return this._price;

    }

    public int getAmount() {
        return this._amount;

    }

    public void setAmount(int newAmount) {
        this._amount = newAmount;

    }

    public ArrayList<String> getFormatted() {
        ArrayList<String> format = new ArrayList<>();

        format.add(String.valueOf(this._amount));
        format.add(this._name);
        format.add(String.valueOf(this._price*this._amount));

        return format;
    }

    @Override
    public String toString() {
        String tag = Commons.capitalize(this._tag.toString().toLowerCase());
        return  "Id: "       +  this._productIdentifier + "\n" +
                "Nombre: "   +  this._name              + "\n" +
                "Tipo: "     +  tag                     + "\n" +
                "Precio: "   +  this._price             + "\n" +
                "Cantidad: " +  this._amount            + "\n";
    }
}
