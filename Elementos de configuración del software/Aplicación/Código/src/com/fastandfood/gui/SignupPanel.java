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

/**
 * @author Borja
 */
public class SignupPanel extends JPanel {

    private FrameController controller;
    private JPanel signupPanel;

    /* Panel de logo */
    private JPanel iconPanel;
    private JLabel iconLabel;
    private Image  iconImage = null;

    /* Panel de Contenido */
    private JPanel      formPanel;
    private JPanel      formSubPanel;

    private JTextField  nameField,
                        usernameField;

    private JPasswordField  passField;

    /* Panel de botones */
    private JPanel  buttonPanel;
    private JButton signupButton;
    private JButton backButton;

    public SignupPanel(FrameController cont) {
        this.controller = cont;
        initUI();
    }

    private void initUI() {
        signupPanel = new JPanel(new BorderLayout());

        /* Panel de logo */
        iconPanel = new JPanel();
        try {
            InputStream is = new BufferedInputStream(this.getClass().getClassLoader().getResourceAsStream(Material.FF_LOGO));
            iconImage = ImageIO.read(is);
            iconLabel = new JLabel(new ImageIcon(iconImage));
        } catch (IOException ex) {
            iconLabel = new JLabel("ICON MISSING");
        }

        iconPanel.add(iconLabel);

        /* Panel de contenido */
        formPanel       =   new JPanel(new GridLayout(2, 0));

        formSubPanel    =   new JPanel(new GridLayout(3, 2));
        nameField       =   new HintTextField("Nombre");
        usernameField   =   new HintTextField("Usuario");
        passField       =   new HintPassField("Password");

        nameField.setPreferredSize(Material.LOGIN_TEXTFIELDSIZE);
        usernameField.setPreferredSize(Material.LOGIN_TEXTFIELDSIZE);
        passField.setPreferredSize(Material.LOGIN_TEXTFIELDSIZE);

        nameField.addActionListener(submitOnEnter());
        usernameField.addActionListener(submitOnEnter());
        passField.addActionListener(submitOnEnter());

        formSubPanel.add(nameField);
        formSubPanel.add(usernameField);
        formSubPanel.add(passField);

        //TODO fix this
        for(int i = 0; i < 2; i++) {
            if(i == 1)
                formPanel.add(formSubPanel);
            else
                formPanel.add(new JPanel());
        }

        /* Panel de botones */
        buttonPanel     =   new JPanel(new GridLayout(3, 0));
        signupButton    =   new JButton("Signup");
        backButton      =   new JButton("<- Volver");

        signupButton.addActionListener(submitOnEnter());

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setText(null);
                usernameField.setText(null);
                passField.setText(null);
                controller.cancelSignupEvent();
            }
        });

        buttonPanel.add(signupButton);
        buttonPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        buttonPanel.add(backButton);

        signupPanel.add(iconPanel, BorderLayout.PAGE_START);
        signupPanel.add(formPanel, BorderLayout.CENTER);
        signupPanel.add(buttonPanel, BorderLayout.PAGE_END);
        this.add(signupPanel);
    }

    private ActionListener submitOnEnter() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(usernameField.getText().isEmpty() || passField.getPassword().length == 0)
                    emptyFieldMessage();
                else {
                    controller.signupEvent(nameField.getText(), usernameField.getText(), String.valueOf(passField.getPassword()));
                    nameField.setText(null);
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
