package com.fastandfood.core;

import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.dao.Database;
import com.fastandfood.dao.StockDao;
import com.fastandfood.exceptions.ProductAmountException;
import com.fastandfood.exceptions.ProductNotFoundException;
import com.fastandfood.observers.Watchable;

import java.util.ArrayList;

/**
 * Almacén de la tienda.
 * Representa todos los productos a la venta en ese momento.
 * Los objetos agotados son marcados como tal, y su referencia no es eliminada de almacén
 *
 * @author Borja
 */

public class Stock extends Watchable {

    public static enum SortMethod {
        NOMBRE, CANTIDAD, TAG, PRECIO
    }

    private StockDao dao = null;

    public Stock(Database _database) {
        this.dao = new StockDao(_database);
    }

    public void init() {
        update();
    }

    public void addProduct(Product product) {
        dao.addProduct(product);
        update();
    }

    /* Añadir un nuevo Producto, indicando los distintos parámetros */
    public void addProduct(int id, String productName, Product.Tag tag, double price, int amount) {
        addProduct(new Product(id, productName, tag, price, amount));
    }

    /* Añadir al Producto indicado por ID la cantidad amount */
    void addProduct(int id, int amount) throws ProductNotFoundException {
        dao.addProduct(id, amount);
        update();
    }

    /* Elimina del producto con identificación id la cantidad amount */
    void removeProduct(int id, int amount) throws ProductNotFoundException, ProductAmountException {
        dao.removeProduct(id, amount);
        update();
    }

    /** Pone la cantidad del producto con indicador {@code id} a 0 */
    void removeFromStock(int id) throws ProductNotFoundException {
        dao.removeFromStock(id);
        update();
    }

    /** Pone la cantidad del producto {@code product} a 0 */
    void removeFromStock(Product product) throws ProductNotFoundException {
        removeFromStock(product.getId());
        update();
    }

    /** Elimina de la base de datos el producto {@code id} */
    void dropFromStock(int id) throws ProductNotFoundException {
        dao.dropFromStock(id);
        update();
    }

    Product searchProduct(int id) throws ProductNotFoundException {
        return this.dao.searchProduct(id);
    }

    Product searchProduct(String productName) throws ProductNotFoundException {
        return this.dao.searchProduct(productName);
    }

    private void update() {
        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.CONTENT, displayContents()));
    }

    /* Display info from all products: name + price + amount */
    private ArrayList<String> displayContents() {
        return dao.displayAll();
    }

    /* Unused */
    void updateStock(Order order) {
        for(Product p : order.getProducts())
            addProduct(p);

        update();
    }

    /*
    void sortStock(SortMethod method) {
        switch(method) {
        case NOMBRE:
            Collections.sort(this._productList, new ProductNameComparator());
            break;
        case CANTIDAD:
            Collections.sort(this._productList, new ProductAmountComparator());
            Collections.reverse(this._productList);
            break;
        case TAG:
            Collections.sort(this._productList, new ProductTagComparator());
            break;
        case PRECIO:
            Collections.sort(this._productList, new ProductPriceComparator());
            break;
        }
    }
    */
}
