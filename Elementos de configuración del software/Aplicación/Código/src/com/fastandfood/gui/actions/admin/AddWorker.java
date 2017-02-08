package com.fastandfood.gui.actions.admin;

import com.fastandfood.gui.FrameController;
import com.fastandfood.gui.Material;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddWorker extends JFrame{

    private FrameController controller;

    private Container contentPane;
    private JPanel mainPanel;

    private JPanel  formPanel;
    private	JLabel  nameLabel,
                    usernameLabel,
                    passLabel,
                    permissionLabel;

    private JTextField nameField,
                       usernameField;
    private JPasswordField passwordField;

    private	Choice permissionChoice;

    private JPanel  buttonPanel;
    private JButton addUserButton,
                    cancelButton;

    public AddWorker(FrameController cont) {
        super("Añadir usuario");
        this.controller = cont;

        initUI();
    }

    private void initUI() {
        contentPane = this.getContentPane();

        mainPanel = new JPanel(new GridLayout(2, 0));
        mainPanel.setBorder(new TitledBorder("Datos para dar de alta a usuario"));

        formPanel = new JPanel(new GridLayout(4,2));

        nameLabel         = new JLabel("Nombre de empleado");
        usernameLabel     = new JLabel("Nombre de usuario");
        passLabel         = new JLabel("Contraseña");
        permissionLabel   = new JLabel("Puesto de Trabajo");

        nameField         = new JTextField();
        usernameField     = new JTextField();
        passwordField     = new JPasswordField();

        nameField.setPreferredSize(Material.TEXTFIELDSIZE);
        usernameField.setPreferredSize(Material.TEXTFIELDSIZE);
        passwordField.setPreferredSize(Material.TEXTFIELDSIZE);


        nameField.addActionListener(submitOnEnter());
        usernameField.addActionListener(submitOnEnter());
        passwordField.addActionListener(submitOnEnter());

        permissionChoice  = new Choice();
        permissionChoice.setBounds(67, 60, 170, 90);
        permissionChoice.add("Administrador");
        permissionChoice.add("Encargado");
        permissionChoice.add("Vendedor");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        formPanel.add(permissionLabel);
        formPanel.add(permissionChoice);

        buttonPanel = new JPanel(new FlowLayout());
        addUserButton = new JButton("Insertar usuario");

        addUserButton.addActionListener(submitOnEnter());

        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                permissionChoice.select(0);
                nameField.setText(null);
                usernameField.setText(null);
                passwordField.setText(null);

                AddWorker.this.setVisible(false);
            }
        });

        buttonPanel.add(addUserButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);

        contentPane.add(mainPanel);
    }

    public void display() {
        this.setLocationRelativeTo(null);
        this.setSize(Material.ACTION_DIMENSION);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    private ActionListener submitOnEnter() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getPassword().length == 0)
                    emptyFieldMessage();
                else {
                    AddWorker.this.setVisible(false);
                    controller.addWorkerEvent(permissionChoice.getSelectedIndex(), nameField.getText(),
                                              usernameField.getText(), String.valueOf(passwordField.getPassword()));
                    nameField.setText(null);
                    usernameField.setText(null);
                    passwordField.setText(null);
                    permissionChoice.select(0);
                }
            }
        };
    }

    private void emptyFieldMessage() {
        JOptionPane.showMessageDialog(this, "Por favor completa los campos");
    }
}