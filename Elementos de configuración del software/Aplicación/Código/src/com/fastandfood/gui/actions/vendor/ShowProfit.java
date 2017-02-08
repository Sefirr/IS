package com.fastandfood.gui.actions.vendor;

import com.fastandfood.gui.Material;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ShowProfit extends JFrame {

    private Container contentPane;
    private JPanel mainPanel;

    private JPanel contentPanel;
    private JLabel textLabel;
    private JLabel profitLabel;

    private JPanel buttonPanel;
    private JButton doneButton;

    public ShowProfit() {
        super("Ver Caja");
        initUI();
    }

    private void initUI() {
        contentPane = this.getContentPane();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder("Ver Caja"));

        contentPanel = new JPanel();

        textLabel = new JLabel("El balance del día es: ");
        profitLabel = new JLabel();
        contentPanel.add(textLabel);
        contentPanel.add(profitLabel);

        buttonPanel = new JPanel();

        doneButton = new JButton("Aceptar");
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShowProfit.this.setVisible(false);
                ShowProfit.this.dispose();
            }
        });
        buttonPanel.add(doneButton);

        mainPanel.add(contentPanel, BorderLayout.PAGE_START);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPane.add(mainPanel);
    }

    public void display(Double profit) {
        //TODO
        this.profitLabel.setText((profit != -1) ?
                String.valueOf(Math.floor(profit * 100) / 100) :
                "Undefined");

        this.setLocationRelativeTo(null);
        this.setSize(Material.ACTION_DIMENSION);
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }
}
