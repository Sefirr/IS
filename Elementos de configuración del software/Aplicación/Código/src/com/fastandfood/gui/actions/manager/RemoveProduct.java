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
public class RemoveProduct extends JFrame {

    private FrameController controller;

    private Container contentPane;
    private JPanel mainPanel;

    private JPanel formPanel;
    private JLabel idLabel,
                   amountLabel;

    private JTextField idField,
                       amountField;

    private JPanel buttonPanel;
    private JButton submitButton,
                    cancelButton;

    public RemoveProduct(FrameController cont) {
        super("Eliminar Producto");
        this.controller = cont;

        initUI();
    }

    private void initUI() {
        contentPane = this.getContentPane();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder("Datos para eliminar producto"));

        formPanel = new JPanel(new GridLayout(2,2));

        idLabel = new JLabel("ID del producto");
        amountLabel = new JLabel("Cantidad");

        idField = new JTextField();
        amountField = new JTextField();

        idField.setPreferredSize(Material.TEXTFIELDSIZE);
        amountField.setPreferredSize(Material.TEXTFIELDSIZE);

        idField.addActionListener(submitOnEnter());
        amountField.addActionListener(submitOnEnter());

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);

        buttonPanel = new JPanel(new FlowLayout());
        submitButton = new JButton("Eliminar Producto");
        cancelButton = new JButton("Cancelar");

        submitButton.addActionListener(submitOnEnter());
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idField.setText(null);
                amountField.setText(null);

                RemoveProduct.this.setVisible(false);
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
                if(idField.getText().isEmpty() || amountField.getText().isEmpty())
                    JOptionPane.showMessageDialog(RemoveProduct.this, "Por favor complete los campos");
                else {
                    RemoveProduct.this.setVisible(false);

                    //TODO call controller

                    idField.setText(null);
                    amountField.setText(null);
                }
            }
        };
    }
}
