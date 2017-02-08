package com.fastandfood.gui;

import com.fastandfood.commons.Pair;
import com.fastandfood.core.FastCore;
import com.fastandfood.exceptions.*;
import com.fastandfood.observers.Watcher;
import com.fastandfood.users.UserPermission;

import java.util.ArrayList;

public class FrameController {

    private FastCore core;
    private MainFrame frame;
    private StartupFrame startupFrame;

    public FrameController(FastCore _core) {
        this.core = _core;
    }

    public void addViews(StartupFrame stFrame, MainFrame mFrame) {
        this.startupFrame = stFrame;
        this.frame = mFrame;
    }

    /* Self Methods */

    void init(Watcher w) {
        core.addWatcher(w);
    }

    boolean ready() {
        return core.isConnectionReady();
    }

    void addCoreWatchers(Watcher admin, Watcher vendor, Watcher manager) {
        core.addStaffWatcher(admin);
        core.addOrderWatcher(new Watcher[]{admin, manager});
        core.addStockWatcher(new Watcher[]{vendor, manager});
    }

    /* Application Methods */

    void quitEvent() {
        startupFrame.quitDialog();
    }

    void shutdown() {
        core.removeWatchers();
        core.shutdown();
    }

    /* Login and Signup Panels */

    void loginEvent(String username, String password) {
        try {
            core.validate(username, password);
            frame.enable();
            core.login();
        } catch (InvalidLoginException e) {
            startupFrame.display(e.getMessage());
        }
    }

    void logoutEvent() {
        core.logout();
        core.deleteWatcher(frame);
        frame.dispose();
    }

    void signupDialogEvent() {
        core.signupDialog();
    }

    void signupEvent(String employeeName, String username, String password) {
        try {
            core.signup(employeeName, username, password);
            frame.enable();
        } catch (DuplicateUserException e) {
            startupFrame.display(e.getMessage());
        }
    }

    void cancelSignupEvent() {
        core.logout();
    }

    /* Vendor Panel Methods */

    void saveSaleEvent(ArrayList<Pair> tableData) {
        try {
            core.saveSale(tableData);
        } catch (ProductNotFoundException e) {
            frame.display(e.getMessage());
        }
    }

    public void submitSaleEvent(int receiptChoice) {
        try {
            core.confirmSale(receiptChoice);
        } catch (ProductNotFoundException | ProductAmountException e) {
            frame.display(e.getMessage());
        }
    }

    void profitEvent() {
        core.showProfits();
    }

    /* Admin Panel Methods */

    public void addWorkerEvent(int permission, String name, String username, String password) {
        UserPermission _permission = UserPermission.values()[permission + 2];
        try {
            core.addUser(_permission, name, username, password);
        } catch (DuplicateUserException e) {
            frame.display(e.getMessage());
        }
    }

    public void deleteWorkerEvent(String username) {
        try {
            core.removeUser(username);
        } catch (UserNotFoundException ex) {
            frame.display(ex.getMessage());
        }
    }

    public void modifyWorkerEvent(String username, char[] newPassword) {
        try {
            core.modifyUser(username, String.valueOf(newPassword));
        } catch (UserNotFoundException ex) {
            frame.display(ex.getMessage());
        }
    }

    public void findWorkerEvent(int queryOption, String query) {
        if(queryOption == 0)
            core.findUserByPerm(query);
        else if(queryOption == 1)
            core.findUserByName(query);
        else if(queryOption == 2)
            core.findUserByUser(query);

    }

    void confirmOrders() {
        core.confirmOrders();
    }
}
