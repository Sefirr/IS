package com.fastandfood.gui;

import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.gui.actions.manager.*;
import com.fastandfood.observers.Watchable;
import com.fastandfood.observers.Watcher;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class StockPanel extends JPanel implements Watcher {

    private FrameController controller;
    private JPanel mainPanel;

    private AddProduct addProduct;
    private RemoveProduct removeProduct;
    private FindProduct findProduct;

    private AddOrder addOrder;
    private RemoveOrder removeOrder;
    private ModifyOrder modifyOrder;

    /* Panel de Acción sobre Stock */
    private JPanel productActionPanel;

    private JButton addProductButton,
                    removeProductButton,
                    searchProductButton;

    /* Panel de Acción sobre Pedidos */
    private JPanel orderActionPanel;

    private JButton addOrderButton,
                    deleteOrderButton,
                    finishOrderButton,
                    changeOrderButton;

    /* Panel de Contenido */
    private JPanel contentPanel;

    /* Tabla de Productos */
    private JPanel stockTableHolder;
    private DefaultTableModel stockTableModel;
    private JTable stockTable;
    private JScrollPane stockScrollable;

    /* Tabla de Pedidos */
    private JPanel orderTableHolder;
    private DefaultTableModel orderListTableModel;
    private JTable orderListTable;
    private JScrollPane orderListScrollable;


    public StockPanel(FrameController cont) {
        this.controller = cont;
        this.addProduct = new AddProduct(cont);
        this.removeProduct = new RemoveProduct(cont);
        this.findProduct = new FindProduct(cont);


        this.addOrder = new AddOrder(cont);
        this.removeOrder = new RemoveOrder(cont);
        this.modifyOrder = new ModifyOrder(cont);

        initUI();
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(Material.SCREEN_DIMENSION);

        /* Panel de Acciones de Productos */
        productActionPanel = new JPanel();
        productActionPanel.setBorder(new TitledBorder("Opciones de encargado - Almacén"));

        addProductButton = new JButton("Agregar producto");
        removeProductButton = new JButton("Eliminar producto");
        searchProductButton = new JButton("Buscar producto");

        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct.display();
            }
        });

        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProduct.display();
            }
        });

        searchProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findProduct.display();
            }
        });

        productActionPanel.add(addProductButton);
        productActionPanel.add(removeProductButton);
        productActionPanel.add(searchProductButton);
        productActionPanel.add(new LogoutButton(controller));

        /* Panel de Contenido */
        contentPanel = new JPanel(new GridLayout(0,2));
        
        /* Tabla de Productos */
        stockTableHolder = new JPanel(new BorderLayout());
        stockTableHolder.setBorder(new TitledBorder("Almacén"));

        stockTableModel = new DefaultTableModel(new Object[]{"Producto", "Cantidad"}, 0);
        stockTable = new JTable(stockTableModel);

        stockScrollable = new JScrollPane(stockTable);
        stockTableHolder.add(stockScrollable);

        /* Tabla de Pedidos */
        orderTableHolder = new JPanel(new BorderLayout());
        orderTableHolder.setBorder(new TitledBorder("Pedidos"));

        orderListTableModel = new DefaultTableModel(new Object[]{"Pedido", "Confirmado", "Proveedor", "Fecha"}, 0);
        orderListTable = new JTable(orderListTableModel);

        orderListScrollable = new JScrollPane(orderListTable);
        orderTableHolder.add(orderListScrollable);

        contentPanel.add(stockTableHolder);
        contentPanel.add(orderTableHolder);

        /* Panel de Acciones de Pedidos */
        orderActionPanel = new JPanel();
        orderActionPanel.setBorder(new TitledBorder("Opciones de encargado - Pedidos"));

        addOrderButton = new JButton("Agregar pedido");
        deleteOrderButton = new JButton("Eliminar pedido");
        finishOrderButton = new JButton("Finalizar pedido");
        changeOrderButton = new JButton("Modificar pedido");

        addOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrder.display();
            }
        });

        deleteOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeOrder.display();
            }
        });

        finishOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(orderListTable.getSelectedRow() == -1)
                    JOptionPane.showMessageDialog(StockPanel.this, "Por favor seleccione algún pedido");
                else {
                    // TODO call controller confirm selected order
                    // TODO with orderListTable.getSelectedRow()
                }
            }
        });

        changeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(orderListTable.getSelectedRow() == -1)
                    JOptionPane.showMessageDialog(StockPanel.this, "Por favor seleccione algún pedido");
                else {
                    // TODO get data from the order table w/ orderListTable.getSelectedRow()
                    // TODO call controller to get order details
                    // TODO pipe details data to display
                    modifyOrder.display(null, null); //TODO
                }
            }
        });

        orderActionPanel.add(addOrderButton);
        orderActionPanel.add(deleteOrderButton);
        orderActionPanel.add(finishOrderButton);
        orderActionPanel.add(changeOrderButton);


        mainPanel.add(productActionPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(orderActionPanel, BorderLayout.SOUTH);
        this.add(mainPanel);
    }

    @Override
    public void update(Watchable o, UpdateMessage arg) {
        switch(arg.getEvent()) {
            case CONTENT:
                updateStockTable((ArrayList<String>) arg.getArg());
                break;
            case NONE:
                updateOrderTable((ArrayList<String>) arg.getArg());
                break;
            default:break;
        }
    }

    private void updateOrderTable(final ArrayList<String> orderList) {

        if(orderList != null && (orderList.size() % 3 == 0)) {
            orderListTableModel.setRowCount(0);

            for(int i = 0; i < orderList.size() - 1; i++)
                if(i%4 == 0) {
                    orderListTableModel.addRow(new Object[] {
                            orderList.get(i),
                            orderList.get(i+3), // Confirmed goes last on the message, second on the table
                            orderList.get(i+1),
                            orderList.get(i+2)
                    });
                }
        }
    }

    private void updateStockTable(ArrayList<String> stockContents) {

        if(stockContents != null && (stockContents.size() % 3 == 0)) {
            stockTableModel.setRowCount(0);

            for(int i = 0; i < stockContents.size() - 1; i++)
                if(i%3 == 0)
                    stockTableModel.addRow(new Object[] {
                            stockContents.get(i),
                            stockContents.get(i+2)
                    });
        }
    }
}

