package com.fastandfood.gui.actions.vendor;

import com.fastandfood.gui.FrameController;
import com.fastandfood.gui.Material;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowReceipt extends JFrame {

    FrameController controller;

    private Container contentPane;
    private JPanel mainPanel;

    private double price;

    private JPanel contentPanel;

    private JPanel totalPricePanel;
    private JLabel totalPriceText;
    private JLabel totalPriceLabel;

    private JPanel formPanel;
    private JLabel payedAmountLabel;
    private JTextField payedAmountField;
    private JButton displayChangeButton;

    private JPanel differencePanel;
    private JLabel differenceTextLabel;
    private JLabel differenceLabel;

    private JPanel buttonPanel;
    private JLabel printReceiptLabel;
    private Choice receiptChoice;
    private JButton doneButton,
                    cancelButton;

    public ShowReceipt(FrameController cont) {
        super("Factura");
        this.controller = cont;

        initUI();
    }

    private void initUI() {
        contentPane = this.getContentPane();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder("Generar Factura y Cambio"));

        contentPanel = new JPanel(new GridLayout(3,0));
        totalPricePanel = new JPanel();

        totalPriceText = new JLabel("Subtotal: ");
        totalPriceLabel = new JLabel();

        totalPricePanel.add(totalPriceText);
        totalPricePanel.add(totalPriceLabel);

        formPanel = new JPanel(new FlowLayout());

        payedAmountLabel = new JLabel("Cantidad Pagada: ");

        differenceLabel = new JLabel();
        payedAmountField = new JTextField();
        displayChangeButton = new JButton("Ver Cambio");
        payedAmountField.setPreferredSize(Material.TEXTFIELDSIZE);

        // TODO maybe remove - not needed
        payedAmountField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                differenceLabel.setText(String.valueOf(Math.floor((Double.valueOf(payedAmountField.getText()) - price) * 100) / 100));
                payedAmountField.setText(null);
            }
        });

        displayChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                differenceLabel.setText(String.valueOf(Math.floor((Double.valueOf(payedAmountField.getText()) - price) * 100) / 100));
                payedAmountField.setText(null);
            }
        });

        formPanel.add(payedAmountLabel);
        formPanel.add(payedAmountField);
        formPanel.add(displayChangeButton);

        differencePanel = new JPanel();
        differenceTextLabel = new JLabel("Total a devolver: ");
        differencePanel.add(differenceTextLabel);
        differencePanel.add(differenceLabel);

        contentPanel.add(totalPricePanel);
        contentPanel.add(formPanel);
        contentPanel.add(differencePanel);

        buttonPanel = new JPanel(new FlowLayout());
        printReceiptLabel = new JLabel("Imprimir Factura:");
        receiptChoice = new Choice();
        doneButton = new JButton("Aceptar");
        cancelButton = new JButton("Cancelar");

        receiptChoice.add("Sin ticket");
        receiptChoice.add("Simplificado");
        receiptChoice.add("Completo");

        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // confirms the buffer sale and prints receipt if checked
                controller.submitSaleEvent(receiptChoice.getSelectedIndex());
                receiptChoice.select(0);
                ShowReceipt.this.setVisible(false);
                ShowReceipt.this.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // should drop the buffer - maybe not needed
                receiptChoice.select(0);
                ShowReceipt.this.setVisible(false);
                ShowReceipt.this.dispose();
            }
        });

        buttonPanel.add(printReceiptLabel);
        buttonPanel.add(receiptChoice);
        buttonPanel.add(doneButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(contentPanel, BorderLayout.PAGE_START);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPane.add(mainPanel);
    }

    public void display(Double _price) {
        this.price = _price;
        update();

        this.setLocationRelativeTo(null);
        this.setSize(Material.ACTION_DIMENSION);
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    private void update() {
        differenceLabel.setText("0.0");
        totalPriceLabel.setText(String.valueOf(Math.floor(price * 100) / 100));
    }
}
