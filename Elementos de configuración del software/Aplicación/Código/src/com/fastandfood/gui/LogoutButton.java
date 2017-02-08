package com.fastandfood.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
class LogoutButton extends JButton {

    private FrameController controller;

    LogoutButton(FrameController cont) {
        super("Logout");
        this.controller = cont;
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.logoutEvent();
            }
        });
    }
}
