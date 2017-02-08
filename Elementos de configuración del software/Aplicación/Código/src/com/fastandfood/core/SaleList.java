package com.fastandfood.core;

import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.dao.Database;
import com.fastandfood.dao.SaleDao;
import com.fastandfood.exceptions.SaleNotFoundException;
import com.fastandfood.observers.Watchable;

import java.util.ArrayList;

/**
 * @author Borja
 */
public class SaleList extends Watchable {

    private SaleDao dao;
    private Sale saleBuffer;

    public SaleList(Database _database) {
        this.dao = new SaleDao(_database);
    }

    public void init() {
        update();
    }

    public void addSale(Sale sale) {
        dao.insertSale(sale);
    }

    public void saveBuffer(Sale sale) {
        this.saleBuffer = sale;
    }

    public Sale popBuffer() {
        return saleBuffer;
    }

    public void flushBuffer() {
        saleBuffer = null;
    }

    public void confirmSale() {
        dao.insertSale(saleBuffer);
    }

    public void removeSale(int id) throws SaleNotFoundException {
        dao.deleteSale(id);
        update();
    }

    public double getTotalPrice() {
        return dao.getTotal();
    }

    public void update() {
        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.CONTENT, displayContents()));
    }

    private ArrayList<String> displayContents() {
        return dao.displayAll();
    }
}
