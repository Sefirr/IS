package com.fastandfood.gui.actions.manager;

import com.fastandfood.commons.Commons;
import com.fastandfood.core.Product;
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
public class AddProduct extends JFrame {

    private FrameController controller;

    private Container contentPane;
    private JPanel mainPanel;

    private JPanel formPanel;
    private JLabel idLabel,
                   nameLabel,
                   tagLabel,
                   amountLabel,
                   priceLabel;

    private JTextField idField,
                       nameField,
                       amountField,
                       priceField;

    private Choice tagChoice;

    private JPanel buttonPanel;
    private JButton submitButton,
                    cancelButton;

    public AddProduct(FrameController cont) {
        super("Añadir Producto");
        this.controller = cont;

        initUI();
    }

    private void initUI() {
        contentPane = this.getContentPane();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder("Datos para añadir producto"));

        formPanel = new JPanel(new GridLayout(5, 2));

        idLabel = new JLabel("ID del producto");
        nameLabel = new JLabel("Nombre del producto");
        tagLabel = new JLabel("Etiqueta");
        amountLabel = new JLabel("Cantidad");
        priceLabel = new JLabel("Precio");

        idField = new JTextField();
        nameField = new JTextField();
        amountField = new JTextField();
        priceField = new JTextField();

        idField.setPreferredSize(Material.TEXTFIELDSIZE);
        nameField.setPreferredSize(Material.TEXTFIELDSIZE);
        amountField.setPreferredSize(Material.TEXTFIELDSIZE);
        priceField.setPreferredSize(Material.TEXTFIELDSIZE);

        idField.addActionListener(submitOnEnter());
        nameField.addActionListener(submitOnEnter());
        amountField.addActionListener(submitOnEnter());
        priceField.addActionListener(submitOnEnter());

        tagChoice = new Choice();
        tagChoice.setBounds(67, 60, 170, 90);

        for(Product.Tag t : Product.Tag.values())
            tagChoice.add(Commons.capitalize(t.name().toLowerCase()));

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(tagLabel);
        formPanel.add(tagChoice);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(priceLabel);
        formPanel.add(priceField);

        buttonPanel = new JPanel(new FlowLayout());
        submitButton = new JButton("Insertar Producto");
        cancelButton = new JButton("Cancelar");

        submitButton.addActionListener(submitOnEnter());

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagChoice.select(0);
                idField.setText(null);
                nameField.setText(null);
                amountField.setText(null);
                priceField.setText(null);

                AddProduct.this.setVisible(false);
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
                if(idField.getText().isEmpty() || nameField.getText().isEmpty() || amountField.getText().isEmpty() || priceField.getText().isEmpty())
                    emptyFieldMessage("Por favor completa los campos");
                else if(tagChoice.getSelectedIndex() == 0)
                    emptyFieldMessage("Debes seleccionar una categoría válida");
                else {
                    AddProduct.this.setVisible(false);

                    // TODO call controller

                    idField.setText(null);
                    nameField.setText(null);
                    amountField.setText(null);
                    priceField.setText(null);
                    tagChoice.select(0);
                }

            }
        };
    }

    private void emptyFieldMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
