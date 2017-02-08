package com.fastandfood.gui;

import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.observers.Watchable;
import com.fastandfood.observers.Watcher;
import com.fastandfood.users.UserPermission;

import javax.swing.*;
import java.awt.*;

/**
 * @author Borja
 */
public class StartupFrame implements Watcher {

    public enum Panels {
        LOGIN, SIGNUP
    }

    private FrameController controller;
    private boolean isShowing;

    private JFrame holderFrame;
    private JPanel switcherPanel;
    private CardLayout switcherLayout;

    private LoginPanel loginPanel;
    private SignupPanel signupPanel;

    public StartupFrame(FrameController cont) {
        this.controller = cont;
        this.switcherLayout = new CardLayout();

        this.holderFrame = new JFrame();
        this.switcherPanel = new JPanel(switcherLayout);

        this.loginPanel = new LoginPanel(cont);
        this.signupPanel = new SignupPanel(cont);

        initUI();
    }

    private void initUI() {
        holderFrame.setTitle("Fast and Food Inc.");
        holderFrame.setLayout(new BorderLayout());

        switcherPanel.add(this.loginPanel, Panels.LOGIN.name());
        switcherPanel.add(this.signupPanel, Panels.SIGNUP.name());

        holderFrame.add(switcherPanel, BorderLayout.CENTER);
        holderFrame.setLocationRelativeTo(null);
        holderFrame.setResizable(false);
        holderFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void enable() {
        if(!controller.ready()) {
            display("Error: No pudo establecerse la conexión con la base de datos");
            quit(1);
        }

        controller.init(StartupFrame.this);
        holderFrame.setPreferredSize(new Dimension(380, 700));
        holderFrame.pack();
        holderFrame.setVisible(true);
        isShowing = true;
    }

    void display(String message) {
        JOptionPane.showMessageDialog(holderFrame, message);
    }

    void show() {
        switchCards(Panels.LOGIN);
        holderFrame.setVisible(true);
        isShowing = true;
    }

    void hide() {
        holderFrame.setVisible(false);
        isShowing = false;
    }

    void quitDialog() {
        if(JOptionPane.showConfirmDialog
                (null, "¿Estás seguro de querer salir?", "Salir", JOptionPane.YES_NO_OPTION) ==
                JOptionPane.YES_OPTION) {
            controller.shutdown();
            System.exit(0);
        }
    }

    void quit(int status) {
        System.exit(status);
    }

    private void switchCards(Panels panelToShow) {
        switcherLayout.show(switcherPanel, panelToShow.name());
    }

    @Override
    public void update(Watchable o, UpdateMessage arg) {

        switch(arg.getEvent()) {
            case ACCOUNT:
                update((UserPermission) arg.getArg()); break;
        }
    }

    private void update(UserPermission permission) {
        switch(permission) {
            case REGISTERED:
                if(!isShowing) show();
                switchCards(Panels.LOGIN); break;
            case NONE:
                if(!isShowing) show();
                switchCards(Panels.SIGNUP); break;
            default:
                hide();
                switchCards(Panels.LOGIN);
        }
    }
}
