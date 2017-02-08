package com.fastandfood.gui;

import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.observers.Watchable;
import com.fastandfood.observers.Watcher;
import com.fastandfood.users.UserPermission;

import javax.swing.*;
import java.awt.*;


public class MainFrame implements Watcher {

    public enum Panels {
        LOGIN, SALES, STOCK, ADMIN
    }

    private FrameController controller;

    private JFrame holderFrame;
    private JPanel switcherPanel;
    private CardLayout switcherLayout;

    private AdminPanel adminPanel;
    private VendorPanel vendorPanel;
    private StockPanel managerPanel;
    // ...

    public MainFrame(FrameController cont) {
        this.controller = cont;
        this.switcherLayout =   new CardLayout();

        this.holderFrame =      new JFrame();
        this.switcherPanel =    new JPanel(switcherLayout);

        this.adminPanel =       new AdminPanel(cont);
        this.vendorPanel =      new VendorPanel(cont);
        this.managerPanel =     new StockPanel(cont);

        initUI();
    }

    private void initUI() {
        holderFrame.setTitle("Fast and Food Inc.");
        holderFrame.setLayout(new BorderLayout());

        switcherPanel.add(this.adminPanel, Panels.ADMIN.name());
        switcherPanel.add(this.vendorPanel, Panels.SALES.name());
        switcherPanel.add(this.managerPanel, Panels.STOCK.name());

        holderFrame.add(switcherPanel, BorderLayout.CENTER);

        holderFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void enable() {
        controller.init(MainFrame.this);
        controller.addCoreWatchers(this.adminPanel, this.vendorPanel, this.managerPanel);
        holderFrame.pack();
        holderFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        holderFrame.setVisible(true);
    }

    void display(String message) {
        JOptionPane.showMessageDialog(holderFrame, message);
    }

    void hide() {
        holderFrame.setVisible(false);
    }

    void dispose() {
        holderFrame.dispose();
    }

    void switchCards(Panels panelToShow) {
        switcherLayout.show(switcherPanel, panelToShow.name());
    }

    @Override
    public void update(Watchable o, UpdateMessage arg) {
        switch(arg.getEvent()) {
            case ACCOUNT:
                update((UserPermission) arg.getArg());
                break;
            case SALE:
            case FINANCE:
                this.vendorPanel.update(o, arg);
                break;
        }
    }

    private void update(UserPermission permission) {
        switch(permission) {
            case ADMIN:
                switchCards(Panels.ADMIN); break;
            case MANAGER:
                switchCards(Panels.STOCK); break;
            case VENDOR:
                switchCards(Panels.SALES); break;
            default:
                hide();
        }
    }
}