package com.fastandfood.core;

import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.dao.Database;
import com.fastandfood.dao.OrderDao;
import com.fastandfood.exceptions.OrderNotFoundException;
import com.fastandfood.observers.Watchable;

import java.util.ArrayList;

/**
 * @author Borja
 */
public class OrderList extends Watchable {

    private OrderDao dao;

    public OrderList(Database _database) {
        this.dao = new OrderDao(_database);
    }

    public void init() {
        update();
    }

    public void addOrder(Order order) {
        dao.insertOrder(order);
    }

    public void removeOrder(int id)
                        throws OrderNotFoundException {
        dao.deleteSale(id);
        update();
    }

    public void confirmOrders() {
        dao.confirmOrders();
        update();
    }

    public void update() {
        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.NONE, displayContents()));
        // TODO, event has to be CONTENT
        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.ORDER, displayNotConfirmed()));
        // ^ perhaphs overkill
    }

    private ArrayList<String> displayNotConfirmed() {
        return dao.displayNotConfirmed();
    }

    private ArrayList<String> displayContents() {
        return dao.displayAll();
    }
}
