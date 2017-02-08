package com.fastandfood.gui.actions.admin;

import com.fastandfood.gui.FrameController;
import com.fastandfood.gui.Material;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ModifyWorker extends JFrame {

	private FrameController controller;

    private Container contentPane;
    private JPanel mainPanel;

    private JPanel formPanel;
    private JLabel userLabel;

    private JLabel newPassLabel,
                   confirmPassLabel;

    private JTextField userField;

    private JPasswordField newPassField,
                           confirmPassField;

    private JPanel buttonPanel;

    private JButton submitButton,
                    cancelButton;

	public ModifyWorker(FrameController cont) {
        super("Modificar Usuario");
        this.controller = cont;

        initUI();
    }

    private void initUI() {
		contentPane = this.getContentPane();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder("Modificar un usuario"));

        formPanel = new JPanel(new GridLayout(3, 2));

        userLabel = new JLabel("Nombre de Usario:");
        newPassLabel     = new JLabel("Nueva contraseña:");
        confirmPassLabel = new JLabel("Confirmar nueva contraseña:");

        userField = new JTextField();
        newPassField     = new JPasswordField();
        confirmPassField = new JPasswordField();

        userField.setPreferredSize(Material.TEXTFIELDSIZE);
        newPassField.setPreferredSize(Material.TEXTFIELDSIZE);
        confirmPassField.setPreferredSize(Material.TEXTFIELDSIZE);

        //Action Listeners
        userField.addActionListener(submitOnEnter());
        newPassField.addActionListener(submitOnEnter());
        confirmPassField.addActionListener(submitOnEnter());

        formPanel.add(userLabel);
        formPanel.add(userField);

        formPanel.add(newPassLabel);
        formPanel.add(newPassField);

        formPanel.add(confirmPassLabel);
        formPanel.add(confirmPassField);

        buttonPanel = new JPanel(new FlowLayout());
        submitButton = new JButton("Modificar Usuario");
        cancelButton = new JButton("Cancelar");

        //Action Listeners
        submitButton.addActionListener(submitOnEnter());

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userField.setText(null);
                newPassField.setText(null);
                confirmPassField.setText(null);

                ModifyWorker.this.setVisible(false);
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
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    private ActionListener submitOnEnter() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = userField.getText();

                char[] newpass     = newPassField.getPassword(),
                       confirmpass = confirmPassField.getPassword();

                if(username.isEmpty() || newpass.length == 0 || confirmpass.length == 0)
                    message("Por favor completa los campos");
                else {
                    if(!Arrays.equals(newpass, confirmpass))
                       message("Las contraseñas no coinciden");
                    else {
                       ModifyWorker.this.setVisible(false);
                       controller.modifyWorkerEvent(username, newpass);

                       userField.setText(null);
                    }

                    newPassField.setText(null);
                    confirmPassField.setText(null);
                }
            }
        };
    }

    private void message(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}