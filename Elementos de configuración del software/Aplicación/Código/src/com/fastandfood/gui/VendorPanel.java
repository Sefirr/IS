package com.fastandfood.gui;

import com.fastandfood.commons.Commons;
import com.fastandfood.commons.Pair;
import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.gui.actions.vendor.ShowProfit;
import com.fastandfood.gui.actions.vendor.ShowReceipt;
import com.fastandfood.observers.Watchable;
import com.fastandfood.observers.Watcher;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VendorPanel extends JPanel implements Watcher {

    private FrameController controller;
    private JPanel mainPanel;

    private ShowReceipt sr;
    private ShowProfit sp;

    /* Panel de Acciones */
    private JPanel actionPanel;

    private JButton addProductButton,
                    removeProductButton,
                    cancelSaleButton,
                    endSaleButton,
                    dailyProfitsButton;

    private String selectedProductName = null;
    private double selectedProductPrice = -1.0;
    private int selectedProductAmount = 0;

    /* Panel de Lista de Productos de la Venta */
    private JPanel saleProductListPanel;
    private DefaultTableModel saleProductsTableModel;
    private JTable saleProductsTable;
    private JScrollPane productListTableScrollable;

    /* Panel de Contenido */
    private JPanel subContentPanel;

    /* Tabla de Productos */
    private JPanel productTablePanel;
    private DefaultTableModel productTableModel;
    private JTable productTable;
    private JScrollPane saleProductListScrollable;

    /* Panel de información de Producto */
    private JPanel productInfoPanel;

    private JLabel  productNameLabel,
                    productPriceLabel,
                    productAmountLabel;

    private JTextField  productNameTextField,
    productPriceTextField,
    productAmountTextField;


    public VendorPanel(FrameController cont) {
        this.controller = cont;
        this.sr = new ShowReceipt(cont);
        this.sp = new ShowProfit();

        initUI();
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(Material.SCREEN_DIMENSION);

        /* Panel de Acciones */
        actionPanel = new JPanel();
        actionPanel.setBorder(new TitledBorder("Acciones de la venta"));

        addProductButton =      new JButton("Agregar producto");
        removeProductButton =   new JButton("Eliminar producto");
        cancelSaleButton =      new JButton("Cancelar venta");
        endSaleButton =         new JButton("Finalizar venta");
        dailyProfitsButton =    new JButton("Ver caja");

        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(productAmountTextField.getText() == null || productAmountTextField.getText().isEmpty())
                    JOptionPane.showMessageDialog(VendorPanel.this, "Por favor seleccione algún producto");
                else if(!Commons.isInteger(productAmountTextField.getText()) || Integer.parseInt(productAmountTextField.getText()) == 0)
                    JOptionPane.showMessageDialog(VendorPanel.this, "Por favor indica una cantidad correcta");
                else {
                    selectedProductAmount = Integer.parseInt(productAmountTextField.getText());
                    productAmountTextField.setText(null);

                    // search for duplicates
                    for(int i = 0; i < saleProductsTableModel.getRowCount(); i++) {
                        if(selectedProductName.equalsIgnoreCase((String)saleProductsTableModel.getValueAt(i, 0))) {
                            saleProductsTableModel.setValueAt(selectedProductAmount + (int)saleProductsTableModel.getValueAt(i, 1), i, 1);
                            return;
                        }
                    }

                    saleProductsTableModel.addRow(new Object[]{selectedProductName, selectedProductAmount});
                }
            }
        });

        removeProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(saleProductsTable.getSelectedRow() == -1)
                    JOptionPane.showMessageDialog(VendorPanel.this, "Por favor seleccione algún producto");
                else
                    saleProductsTableModel.removeRow(saleProductsTable.getSelectedRow());
            }
        });

        cancelSaleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saleProductsTableModel.setRowCount(0);
            }
        });

        endSaleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(saleProductsTableModel.getRowCount() == 0)
                    JOptionPane.showMessageDialog(VendorPanel.this, "Debes haber añadido algún producto al carrito");
                else {
                    controller.saveSaleEvent(getTableData(saleProductsTable));
                    saleProductsTableModel.setRowCount(0);
                }
            }
        });

        dailyProfitsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.profitEvent();
            }
        });

        actionPanel.add(addProductButton);
        actionPanel.add(removeProductButton);
        actionPanel.add(cancelSaleButton);
        actionPanel.add(endSaleButton);
        actionPanel.add(dailyProfitsButton);
        actionPanel.add(new LogoutButton(controller));

        /* Tabla de productos de la Venta */
        saleProductListPanel = new JPanel();
        saleProductListPanel.setBorder(new TitledBorder("Carrito de la compra"));

        saleProductsTableModel = new NonEditableTableModel(new Object[]{"Producto", "Cantidad"}, 0);
        saleProductsTable = new JTable(saleProductsTableModel);
        saleProductsTable.setEnabled(true);
        saleProductsTable.setRowSelectionAllowed(true);

        saleProductsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedProductName = (String) saleProductsTable.getValueAt(saleProductsTable.getSelectedRow(), 0);
            }
        });

        saleProductListScrollable = new JScrollPane(saleProductsTable);
        saleProductListScrollable.setPreferredSize(new Dimension(200, 625));

        saleProductListPanel.add(saleProductListScrollable);

        /* Panel de contenido principal */
        subContentPanel = new JPanel(new BorderLayout());

        /* Tabla de Productos */
        productTablePanel = new JPanel(new GridLayout(1,2));
        productTablePanel.setBorder(new TitledBorder("Lista de Productos"));

        productTableModel = new NonEditableTableModel(new Object[]{"Producto", "Precio", "Disponibilidad"}, 0);
        productTable = new JTable(productTableModel);
        productTable.setRowSelectionAllowed(true);
        productTable.setEnabled(true);

        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    selectedProductName = (String) productTable.getValueAt(productTable.getSelectedRow(), 0);
                    selectedProductPrice = Double.valueOf((String) productTable.getValueAt(productTable.getSelectedRow(), 1));

                    productNameTextField.setText(selectedProductName);
                    productPriceTextField.setText(String.valueOf(selectedProductPrice));
                } catch (ClassCastException cce) {cce.printStackTrace();}
            }
        });

        productListTableScrollable = new JScrollPane(productTable);

        productTablePanel.add(productListTableScrollable);

        /* Panel de Información de Producto */
        productInfoPanel = new JPanel();
        productInfoPanel.setBorder(new TitledBorder("Información del producto"));

        productNameLabel =      new JLabel("Producto: ");
        productPriceLabel =     new JLabel("Precio: ");
        productAmountLabel =    new JLabel("Cantidad: ");

        productNameTextField =      new JTextField();
        productPriceTextField =     new JTextField();
        productAmountTextField =    new JTextField();

        productNameTextField.setEditable(false);
        productPriceTextField.setEditable(false);
        productAmountTextField.setEditable(true);

        productNameTextField.setColumns(10);
        productPriceTextField.setColumns(10);
        productAmountTextField.setColumns(10);

        productAmountTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(productAmountTextField.getText() == null || productAmountTextField.getText().isEmpty())
                    JOptionPane.showMessageDialog(VendorPanel.this, "Por favor seleccione algún producto");
                else if(!Commons.isInteger(productAmountTextField.getText()) || Integer.parseInt(productAmountTextField.getText()) == 0)
                    JOptionPane.showMessageDialog(VendorPanel.this, "Por favor indica una cantidad correcta");
                else {
                    selectedProductAmount = Integer.parseInt(productAmountTextField.getText());
                    productAmountTextField.setText(null);

                    // search for duplicates
                    for(int i = 0; i < saleProductsTableModel.getRowCount(); i++) {
                        if(selectedProductName.equalsIgnoreCase((String)saleProductsTableModel.getValueAt(i, 0))) {
                            saleProductsTableModel.setValueAt(selectedProductAmount + (int)saleProductsTableModel.getValueAt(i, 1), i, 1);
                            return;
                        }
                    }

                    saleProductsTableModel.addRow(new Object[]{selectedProductName, selectedProductAmount});
                }
            }
        });


        productInfoPanel.add(productNameLabel);
        productInfoPanel.add(productNameTextField);
        productInfoPanel.add(new JLabel("                   "));
        productInfoPanel.add(productPriceLabel);
        productInfoPanel.add(productPriceTextField);
        productInfoPanel.add(new JLabel("                   "));
        productInfoPanel.add(productAmountLabel);
        productInfoPanel.add(productAmountTextField);

        subContentPanel.add(productTablePanel, BorderLayout.CENTER);
        subContentPanel.add(productInfoPanel, BorderLayout.PAGE_END);

        /* Panel principal */
        mainPanel.add(actionPanel, BorderLayout.PAGE_START);
        mainPanel.add(saleProductListPanel, BorderLayout.LINE_START);
        mainPanel.add(subContentPanel, BorderLayout.CENTER);

        this.add(mainPanel);
    }

    /** Assumes table argument to have only two columns */
    private ArrayList<Pair> getTableData(JTable table) {
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();

        // TODO test
        if(dtm.getColumnCount() > 2)
            return null;

        int nRow = dtm.getRowCount();

        ArrayList<Pair> tableData = new ArrayList<>();
        for(int i = 0; i < nRow; i++)
            tableData.add(Pair.createPair(dtm.getValueAt(i, 0), dtm.getValueAt(i, 1)));

        return tableData;
    }

    @Override
    public void update(Watchable o, UpdateMessage arg) {
        switch(arg.getEvent()) {
            case CONTENT:
                updateStockTable((ArrayList<String>) arg.getArg());
                break;
            case SALE:
                this.sr.display((Double) arg.getArg());
                break;
            case FINANCE:
                this.sp.display((Double) arg.getArg());
                break;
        }
    }

    private void updateStockTable(final ArrayList<String> stockContents) {

        if(stockContents != null && (stockContents.size() % 3 == 0)) {
            productTableModel.setRowCount(0);

            for(int i = 0; i < stockContents.size() - 1; i++)
                if(i%3 == 0)
                    productTableModel.addRow(new Object[] {
                            stockContents.get(i),
                            stockContents.get(i+1),
                            (Integer.parseInt(stockContents.get(i+2)) == 0) ? "No disponible" : "Disponible: " + stockContents.get(i+2)
                    });
        }
    }

    private class NonEditableTableModel extends DefaultTableModel {
        NonEditableTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}

