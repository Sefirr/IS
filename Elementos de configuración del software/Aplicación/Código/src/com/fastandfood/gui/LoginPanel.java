package com.fastandfood.gui;

import com.fastandfood.gui.res.HintPassField;
import com.fastandfood.gui.res.HintTextField;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoginPanel extends JPanel {

    private FrameController controller;
    private JPanel loginPanel;

    /* Panel de Logo */
    private JPanel iconPanel;
    private JLabel iconLabel;
    private Image iconImage;

    /* Panel de Contenido */
    private JPanel formPanel;

    private JPanel formSubPanel;
    private JTextField usernameField;
    private JPasswordField passField;

    /* Panel de Botones */
    private JPanel buttonPanel;
    private JPanel labelPanel;
    private JButton loginButton;
    private JLabel signupLabel;
    private JButton signupButton;
    private JButton quitButton;

    public LoginPanel(FrameController cont) {
        this.controller = cont;
        initUI();
    }

    private void initUI() {
        loginPanel = new JPanel(new BorderLayout());

        /* Panel de Logo */
        iconPanel = new JPanel();
        try {
            InputStream is = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(Material.FF_LOGO));
            iconImage = ImageIO.read(is);
            iconLabel = new JLabel(new ImageIcon(iconImage));
        } catch (IOException e1) {
            iconLabel = new JLabel("ICON MISSING");
        }

        iconPanel.add(iconLabel);
        
        /* Panel de Contenido */
        formPanel       =   new JPanel(new GridLayout(3,0));

        formSubPanel    =   new JPanel(new GridLayout(2, 2));
        usernameField   =   new HintTextField("Usuario");
        passField       =   new HintPassField("Password");

        usernameField.setPreferredSize(Material.LOGIN_TEXTFIELDSIZE);
        passField.setPreferredSize(Material.LOGIN_TEXTFIELDSIZE);

        usernameField.addActionListener(submitOnEnter());
        passField.addActionListener(submitOnEnter());

        formSubPanel.add(usernameField);
        formSubPanel.add(passField);

        //TODO fix this
        for(int i = 0; i < 3; i++) {
            if(i == 1)
                formPanel.add(formSubPanel);
            else
                formPanel.add(new JPanel());
        }

        /* Panel de Botones */
        buttonPanel     =   new JPanel(new GridLayout(5 ,0));
        labelPanel      =   new JPanel(new FlowLayout());
        signupLabel     =   new JLabel("¿No está registrado?");
        loginButton     =   new JButton("Login");
        signupButton    =   new JButton("Sign Up");
        quitButton      =   new JButton("Salir");

        loginButton.addActionListener(submitOnEnter());

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.quitEvent();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameField.setText(null);
                passField.setText(null);
                controller.signupDialogEvent();
            }
        });


        labelPanel.add(signupLabel);

        buttonPanel.add(loginButton);
        buttonPanel.add(labelPanel);
        buttonPanel.add(signupButton);
        buttonPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        buttonPanel.add(quitButton);

        /* Mainframe add */
        loginPanel.add(iconPanel, BorderLayout.PAGE_START);
        loginPanel.add(formPanel, BorderLayout.CENTER);
        loginPanel.add(buttonPanel, BorderLayout.PAGE_END);
        this.add(loginPanel);
    }

    private ActionListener submitOnEnter() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(usernameField.getText().isEmpty() || passField.getPassword() == null) {
                    emptyFieldMessage();
                } else {
                    controller.loginEvent(usernameField.getText(), String.valueOf(passField.getPassword()));
                    usernameField.setText(null);
                    passField.setText(null);
                }
            }
        };
    }

    private void emptyFieldMessage() {
        JOptionPane.showMessageDialog(this, "Por favor completa los campos");
    }
}
