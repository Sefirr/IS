package com.fastandfood.gui.actions.manager;

import com.fastandfood.gui.FrameController;
import com.fastandfood.gui.Material;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Borja
 */
public class AddOrder extends JFrame {

    private FrameController controller;
    private AddOrderProduct addProduct;

    private Container contentPane;
    private JPanel mainPanel;

    private JPanel actionPanel;
    private JButton addProductButton,
                    removeProductButton,
                    submitButton,
                    cancelButton;

    private JPanel detailsPanel;
    private DefaultTableModel detailsTableModel;
    private JTable detailsTable;
    private JScrollPane detailsTableScrollable;

    private JPanel footerPanel;
    private JLabel supplierLabel;
    private JTextField supplierField;

    public AddOrder(FrameController cont) {
        super("Añadir pedido");
        this.controller = cont;
        addProduct = new AddOrderProduct(cont);

        initUI();
    }

    private void initUI() {
        contentPane = this.getContentPane();

        mainPanel = new JPanel(new BorderLayout());

        actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBorder(new TitledBorder("Acciones del pedio"));

        addProductButton = new JButton("Agregar Producto");
        removeProductButton = new JButton("Eliminar Producto");
        submitButton = new JButton("Finalizar Pedido");
        cancelButton = new JButton("Cancelar");

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct.display();
                //TODO this should search in database, the insert in the table
            }
        });

        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(detailsTable.getSelectedRow() < 0)
                    JOptionPane.showMessageDialog(AddOrder.this, "Por favor seleccione algún producto");
                else
                    detailsTableModel.removeRow(detailsTable.getSelectedRow());
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailsTableModel.setRowCount(0);
                supplierField.setText(null);

                AddOrder.this.setVisible(false);
            }
        });

        actionPanel.add(addProductButton);
        actionPanel.add(removeProductButton);
        actionPanel.add(submitButton);
        actionPanel.add(cancelButton);

        /* Table Panel */

        detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(new TitledBorder("Lista de productos del pedido"));
        detailsTableModel = new DefaultTableModel(new Object[]{"Producto", "Cantidad"}, 0);
        detailsTable = new JTable(detailsTableModel);
        detailsTable.setRowSelectionAllowed(true);
        detailsTable.setEnabled(true);

        detailsTableScrollable = new JScrollPane(detailsTable);
        detailsPanel.add(detailsTableScrollable);

        /* Footer Panel */

        footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBorder(new TitledBorder("Detalles del proveedor"));
        supplierLabel = new JLabel("Nombre del proveedor:");
        supplierField = new JTextField();
        supplierField.setPreferredSize(Material.TEXTFIELDSIZE);

        footerPanel.add(supplierLabel);
        footerPanel.add(supplierField);

        mainPanel.add(actionPanel, BorderLayout.PAGE_START);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.PAGE_END);

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
        if(results == null || results.isEmpty() || (results.size() % 2 != 0)) {
            return;
        }

        for(int i = 0; i < results.size() - 1; i++)
            if(i%2 == 0)
                detailsTableModel.addRow(new Object[] {
                        results.get(i),
                        results.get(i+1)
                });

    }
}
