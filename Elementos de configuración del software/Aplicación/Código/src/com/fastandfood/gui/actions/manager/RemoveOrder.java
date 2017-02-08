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
public class RemoveOrder extends JFrame {

    private FrameController controller;

    private Container contentPane;
    private JPanel mainPanel;

    private JPanel formPanel;
    private JLabel idLabel;
    private JTextField idField;
    private JButton submitButton,
                    cancelButton;

    public RemoveOrder(FrameController cont) {
        super("Eliminar un pedido");
        this.controller = cont;

        initUI();
    }

    private void initUI() {
        contentPane = this.getContentPane();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder("Datos para eliminar pedido"));

        formPanel = new JPanel(new FlowLayout());

        idLabel = new JLabel("Introduzca id del pedido");
        idField = new JTextField();
        submitButton = new JButton("Eliminar Pedido");
        cancelButton = new JButton("Cancelar");

        idField.setPreferredSize(Material.TEXTFIELDSIZE);

        idField.addActionListener(submitOnEnter());
        submitButton.addActionListener(submitOnEnter());

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idField.setText(null);
                RemoveOrder.this.setVisible(false);
            }
        });

        formPanel.add(idLabel);
        formPanel.add(idField);
        formPanel.add(submitButton);
        formPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        contentPane.add(mainPanel);
    }

    public void display() {
        this.setLocationRelativeTo(null);
        //this.setSize(Material.ACTION_DIMENSION);
        this.setResizable(false);
        this.pack();
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    private ActionListener submitOnEnter() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idField.getText().isEmpty())
                    JOptionPane.showMessageDialog(RemoveOrder.this, "Por favor completa los campos");
                else {
                    RemoveOrder.this.setVisible(false);

                    //TODO call controller

                    idField.setText(null);
                }
            }
        };
    }
}
