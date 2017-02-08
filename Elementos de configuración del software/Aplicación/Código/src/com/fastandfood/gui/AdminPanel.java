package com.fastandfood.gui;

import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.gui.actions.admin.AddWorker;
import com.fastandfood.gui.actions.admin.DeleteWorker;
import com.fastandfood.gui.actions.admin.FindWorker;
import com.fastandfood.gui.actions.admin.ModifyWorker;
import com.fastandfood.observers.Watchable;
import com.fastandfood.observers.Watcher;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AdminPanel extends JPanel implements Watcher {

    private FrameController controller;
    private JPanel mainPanel;

    private AddWorker aw;
    private DeleteWorker dw;
    private ModifyWorker mw;
    private FindWorker fw;

    /* Panel de Acciones */
    private JPanel actionPanel;

    private JButton addUserButton,
                    removeUserButton,
                    editUserButton,
                    searchUserButton,
                    validateOrderButton;

    /* Panel de Contenido */
    private JPanel tablePanel;

    /* Tabla de Usuarios */
    private JPanel staffTableHolder;
    private DefaultTableModel staffTableModel;
    private JTable staffTable;
    private JScrollPane staffTableScrollable;

    /* Tabla de Pedidos */
    private JPanel orderTableHolder;
    private DefaultTableModel orderTableModel;
    private JTable orderTable;
    private JScrollPane orderTableScrollable;


    public AdminPanel(FrameController cont) {
        this.controller = cont;
        this.aw = new AddWorker(cont);
        this.dw = new DeleteWorker(cont);
        this.mw = new ModifyWorker(cont);
        this.fw = new FindWorker(cont);

        initUI();
    }

    private void initUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(Material.SCREEN_DIMENSION);
        
        /* Panel de Acciones */
        actionPanel = new JPanel();
        actionPanel.setBorder(new TitledBorder("Opciones de administración"));

        addUserButton =         new JButton("Insertar usuario");
        removeUserButton =      new JButton("Eliminar usuario");
        editUserButton =        new JButton("Modificar usuario");
        searchUserButton =      new JButton("Buscar usuario");
        validateOrderButton =   new JButton("Confirmar pedidos");

        addUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aw.display();
            }
        });

        removeUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dw.display();
            }
        });

        editUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mw.display();
            }
        });

        searchUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fw.display();
            }
        });

        validateOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(orderTableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(AdminPanel.this, "No existen pedidos por confirmar");
                    return;
                }

                controller.confirmOrders();
                JOptionPane.showMessageDialog(AdminPanel.this, "Pedidos confirmados con éxito");

            }
        });

        actionPanel.add(addUserButton);
        actionPanel.add(removeUserButton);
        actionPanel.add(editUserButton);
        actionPanel.add(searchUserButton);
        actionPanel.add(validateOrderButton);
        actionPanel.add(new LogoutButton(this.controller));
        
        /* Panel de Tablas */
        tablePanel = new JPanel(new GridLayout(0,2));
        
        /* Tabla de Empleados */
        staffTableHolder = new JPanel(new BorderLayout());
        staffTableHolder.setBorder(new TitledBorder("Panel de empleados"));

        staffTableModel =   new DefaultTableModel(new Object[]{"Puesto", "Nombre", "Usuario"}, 0);
        staffTable =        new JTable(staffTableModel);

        staffTableScrollable = new JScrollPane(staffTable);
        staffTableHolder.add(staffTableScrollable);
        
        /* Tabla de Pedidos */
        orderTableHolder = new JPanel(new BorderLayout());
        orderTableHolder.setBorder(new TitledBorder("Panel de Pedidos"));

        orderTableModel =   new DefaultTableModel(new Object[]{"Pedido", "Proveedor", "Fecha"}, 0);
        orderTable =        new JTable(orderTableModel);

        orderTableScrollable = new JScrollPane(orderTable);
        orderTableHolder.add(orderTableScrollable);

        tablePanel.add(staffTableHolder);
        tablePanel.add(orderTableHolder);


        mainPanel.add(actionPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        this.add(mainPanel);
    }

    @Override
    public void update(Watchable o, UpdateMessage arg) {
        switch(arg.getEvent()) {
            case CONTENT:
                updateUserTable((ArrayList<String>) arg.getArg());
                break;
            case USER:
                fw.updateResultTable((ArrayList<String>) arg.getArg());
                break;
            case ORDER:
                updateOrderTable((ArrayList<String>) arg.getArg());
                break;
            default: break;
        }

    }

    private void updateOrderTable(final ArrayList<String> orderList) {

        if(orderList != null && (orderList.size() % 3 == 0)) {
            orderTableModel.setRowCount(0);

            for(int i = 0; i < orderList.size() - 1; i++)
                if(i%3 == 0) {
                    orderTableModel.addRow(new Object[] {
                            orderList.get(i),
                            orderList.get(i+1),
                            orderList.get(i+2)
                    });
                }
        }
    }

    private void updateUserTable(final ArrayList<String> userList) {

        if(userList != null && (userList.size() % 3 == 0)) {
            staffTableModel.setRowCount(0);

            for(int i = 0; i < userList.size() - 1; i++)
                if(i%3 == 0)
                    staffTableModel.addRow(new Object[] {
                            userList.get(i),
                            userList.get(i+1),
                            userList.get(i+2)
                    });
        }
    }
}