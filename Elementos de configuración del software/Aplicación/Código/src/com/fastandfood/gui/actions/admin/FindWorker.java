package com.fastandfood.gui.actions.admin;

import com.fastandfood.gui.FrameController;
import com.fastandfood.gui.Material;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FindWorker extends JFrame{

    private FrameController controller;

	private Container contentPane;
	private JPanel mainPanel;

    private Choice searchTerm;
    private JPanel formPanel;
    private JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton,
                    cancelButton;

    private JPanel resultsPanel;
    private DefaultTableModel resultTableModel;
    private JTable resutlTable;
    private JScrollPane resultTableScrollable;

	public FindWorker(FrameController cont) {
        super("Buscar usuario");
        this.controller = cont;

        initUI();
    }

    private void initUI() {
		contentPane = this.getContentPane();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder("Buscar usuario"));

        formPanel = new JPanel(new FlowLayout());

        searchLabel = new JLabel("Buscar:");

        searchTerm = new Choice();
        searchTerm.add("por Rol");
        searchTerm.add("por Nombre");
        searchTerm.add("por Usuario");

        searchField = new JTextField();
        searchButton = new JButton("Buscar");
        cancelButton = new JButton("Cancelar");

        searchField.setPreferredSize(Material.TEXTFIELDSIZE);

        /* Action Listeners for TextField and Button */

        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(searchField.getText().isEmpty())
                    JOptionPane.showMessageDialog(FindWorker.this, "Por favor complete los campos");
                else {
                    controller.findWorkerEvent(searchTerm.getSelectedIndex(), searchField.getText());

                    searchField.setText(null);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(searchField.getText().isEmpty())
                    JOptionPane.showMessageDialog(FindWorker.this, "Por favor complete los campos");
                else {
                    controller.findWorkerEvent(searchTerm.getSelectedIndex(), searchField.getText());

                    searchField.setText(null);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTerm.select(0);
                searchField.setText(null);
                resultTableModel.setRowCount(0);

                FindWorker.this.setVisible(false);
            }
        });

        formPanel.add(searchLabel);
        formPanel.add(searchTerm);
        formPanel.add(searchField);
        formPanel.add(searchButton);
        formPanel.add(cancelButton);

        resultsPanel = new JPanel(new BorderLayout());

        resultTableModel = new DefaultTableModel(new Object[]{"Rol", "Nombre", "Usario"}, 0);
        resutlTable = new JTable(resultTableModel);
        resutlTable.setRowSelectionAllowed(true);
        resutlTable.setEnabled(true);

        /* Action Listeners for Table */


        resultTableScrollable = new JScrollPane(resutlTable);
        resultsPanel.add(resultTableScrollable);

        mainPanel.add(formPanel, BorderLayout.PAGE_START);
        mainPanel.add(resultsPanel, BorderLayout.CENTER);

        contentPane.add(mainPanel);
	}

    public void display() {
        this.setLocationRelativeTo(null);
        this.setSize(Material.BIG_ACTION_DIMENSION);
        this.setResizable(false);
        /* Allows the user to resize ONLY the height of the window
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setSize(new Dimension(600, getHeight()));
                super.componentResized(e);
            }
        });
        */
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    public void updateResultTable(final ArrayList<String> results) {
        if(results != null && !results.isEmpty() && (results.size() % 3 == 0)) {
            resultTableModel.setRowCount(0);

            for(int i = 0; i < results.size() - 1; i++)
                if(i%3 == 0)
                    resultTableModel.addRow(new Object[] {
                            results.get(i),
                            results.get(i+1),
                            results.get(i+2)
                    });
        }
        else {
            resultTableModel.setRowCount(0);
            resultTableModel.addRow(new Object[] {
                    "N/A",
                    "N/A",
                    "N/A"
            });
        }
    }
}