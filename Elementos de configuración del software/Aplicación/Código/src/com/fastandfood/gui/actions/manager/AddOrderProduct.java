package com.fastandfood.gui.actions.manager;

import com.fastandfood.gui.FrameController;
import com.fastandfood.gui.Material;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Borja
 */
public class AddOrderProduct extends JFrame {

    private FrameController controller;

    private Container contentPane;
    private JPanel mainPanel;

    private JPanel formPanel;
    private JLabel nameLabel,
                   amountLabel;

    private JTextField nameField,
                       amountField;

    private JPanel buttonPanel;
    private JButton submitButton,
                    cancelButton;

    public AddOrderProduct(FrameController cont) {
        super("Añadir Producto");
        this.controller = cont;

        initUI();
    }

    private void initUI() {
        contentPane = this.getContentPane();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder("Datos para añadir producto"));

        formPanel = new JPanel(new GridLayout(2, 2));

        nameLabel = new JLabel("Nombre del producto");
        amountLabel = new JLabel("Cantidad");

        nameField = new JTextField();
        amountField = new JTextField();

        nameField.setPreferredSize(Material.TEXTFIELDSIZE);
        amountField.setPreferredSize(Material.TEXTFIELDSIZE);

        nameField.addActionListener(submitOnEnter());
        amountField.addActionListener(submitOnEnter());

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);

        buttonPanel = new JPanel(new FlowLayout());
        submitButton = new JButton("Insertar Producto");
        cancelButton = new JButton("Cancelar");

        submitButton.addActionListener(submitOnEnter());

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setText(null);
                amountField.setText(null);

                AddOrderProduct.this.setVisible(false);
            }
        });

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

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
                if(nameField.getText().isEmpty() || amountField.getText().isEmpty())
                    emptyFieldMessage("Por favor completa los campos");
                else {
                    AddOrderProduct.this.setVisible(false);

                    // TODO call controller

                    nameField.setText(null);
                    amountField.setText(null);
                }

            }
        };
    }

    private void emptyFieldMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
