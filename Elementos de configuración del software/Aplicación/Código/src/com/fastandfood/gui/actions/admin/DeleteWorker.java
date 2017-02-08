package com.fastandfood.gui.actions.admin;

import com.fastandfood.gui.FrameController;
import com.fastandfood.gui.Material;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteWorker extends JFrame {

    private FrameController controller;

    private Container contentPane;
    private JPanel mainPanel;

    private JPanel formPanel;
    private JLabel removeUserLabel;
    private JTextField removeUserField;

    private JPanel buttonPanel;
    private JButton removeUserButton,
                    cancelButton;

    public DeleteWorker(FrameController cont) {
        super("Eliminar Usuario");
        this.controller = cont;

        initUI();
    }

    private void initUI() {
        contentPane = this.getContentPane();

        mainPanel = new JPanel(new GridLayout(2, 0));
        mainPanel.setBorder(new TitledBorder("Dar de baja a un usuario"));

        formPanel = new JPanel(new FlowLayout());
        removeUserLabel = new JLabel("Introduzca el nombre de usuario");
        removeUserField = new JTextField();

        removeUserField.setPreferredSize(Material.TEXTFIELDSIZE);
        removeUserField.addActionListener(submitOnEnter());

        formPanel.add(removeUserLabel);
        formPanel.add(removeUserField);

        buttonPanel = new JPanel(new FlowLayout());

        removeUserButton = new JButton("Eliminar usuario");
        cancelButton     = new JButton("Cancelar");

        removeUserButton.setBounds(361, 50, 170, 30);
        removeUserButton.addActionListener(submitOnEnter());

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeUserField.setText(null);

                DeleteWorker.this.setVisible(false);
            }
        });

        buttonPanel.add(removeUserButton);
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
                if(removeUserField.getText().isEmpty())
                    emptyFieldMessage();
                else {
                    DeleteWorker.this.setVisible(false);
                    controller.deleteWorkerEvent(removeUserField.getText());
                    removeUserField.setText(null);
                }
            }
        };
    }

    private void emptyFieldMessage() {
        JOptionPane.showMessageDialog(this, "Por favor completa los campos");
    }
}