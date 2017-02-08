package com.fastandfood.core;

import com.fastandfood.commons.Commons;
import com.fastandfood.commons.Pair;
import com.fastandfood.commons.UpdateMessage;
import com.fastandfood.dao.Database;
import com.fastandfood.exceptions.*;
import com.fastandfood.observers.Watchable;
import com.fastandfood.observers.Watcher;
import com.fastandfood.users.Staff;
import com.fastandfood.users.User;
import com.fastandfood.users.UserPermission;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * El núcleo del sistema.
 * Contiene referencias a las listas de productos, usuarios, ventas y pedidos.
 * Se encarga de distribuir las tareas entre los usuarios y actualizar la información que ven,
 * así como funcionar como una capa de funcionalidad entre los usuarios y la información del sistema.
 *
 * @author Borja
 */
public class FastCore extends Watchable {

    private Database database;
    private boolean validConnection;

    private User _logedUser;
    private Staff _hiredUsers;
    private Stock _stock;


    private SaleList saleList;
    private OrderList orderList;

    public FastCore(Database _database) {

        this.database = _database;
        try {
            this.database.openConnection();

            this._hiredUsers = new Staff(_database);
            this._stock = new Stock(_database);
            this.saleList = new SaleList(_database);
            this.orderList = new OrderList(_database);

            this.validConnection = true;
        } catch (ConnectionErrorException cee) {
            System.out.println(cee.getMessage());
            validConnection = false;
        }
    }

    /* LoginPanel Actions */

    public void login() {
        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.ACCOUNT, _logedUser.getPermission()));
    }

    public void validate(String username, String password) throws InvalidLoginException {
        User u = this._hiredUsers.validate(username, password);

        if(u == null)
            throw new InvalidLoginException("Usuario o contraseña incorrectos");

        this._logedUser = u;
    }

    public void logout() {
        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.ACCOUNT, UserPermission.REGISTERED));
    }

    public void signupDialog() {
        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.ACCOUNT, UserPermission.NONE));
    }

    public void signup(String employeeName, String username, String password) throws DuplicateUserException {
        this._hiredUsers.addUser(UserPermission.VENDOR, employeeName, username, password);
        this._logedUser = this._hiredUsers.validate(username, password);

        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.ACCOUNT, _logedUser.getPermission()));
    }


    /* VendorPanel Actions */

    // TODO throw this exception as soon as I add a product to the Cart in the UI
    public void saveSale(ArrayList<Pair> tableData) throws ProductNotFoundException {
        Product product;
        Sale sale;
        ArrayList<Product> productList = new ArrayList<>();

        for(Pair pair : tableData) {
            product = _stock.searchProduct((String) pair.getFirst());
            product.setAmount((Integer) pair.getSecond());
            productList.add(product);
        }

        sale = new Sale(productList);
        saleList.saveBuffer(sale);

        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.SALE, sale.getPrice()));
    }

    // TODO: ideally it should throw these two exceptions as soon as I add a product to the Cart in the UI
    public void confirmSale(int receiptChoice) throws ProductAmountException, ProductNotFoundException {
        Sale s = saleList.popBuffer();

        for(Product pr : s.getProducts())
            this._stock.removeProduct(pr.getId(), pr.getAmount());

        s.finishSale();
        saleList.addSale(s);
        if(receiptChoice > 0) printReceipt(receiptChoice == 1);
        else saleList.flushBuffer(); // clears the buffer
    }

    // Spits out a html or txt file
    private void printReceipt(boolean simplify) {
        PrintWriter writer = null;
        File receiptFile;
        String fileName = System.getProperty("user.dir") + File.separator +
                          "Receipt-" + new SimpleDateFormat("yyMMdd-hhmmss").format(new Date()) + ((simplify) ? ".txt": ".html");

        Sale sale;
        ArrayList<String> productInfo = new ArrayList<>();
        ArrayList<String> receiptRender;

        receiptFile = new File(fileName);

        try {
            writer = new PrintWriter(new FileWriter(receiptFile));
        } catch (IOException e) {
            System.exit(2);
        }

        sale = saleList.popBuffer();

        for(Product pr : sale.getProducts())
            for(String s : pr.getFormatted())
                productInfo.add(s);

        // if simplified, print a txt file
        receiptRender = (simplify)
                ? renderTicketTXT(Commons.getCurrentDate(), productInfo, sale.getPrice())
                : renderTickerHTML(Commons.getCurrentDate(), productInfo, sale.getPrice());


        for(String line : receiptRender)
            writer.print(line + System.lineSeparator());

        writer.close();
    }

    // renders receipt in txt format
    private ArrayList<String> renderTicketTXT(String date, ArrayList<String> products, Double saleTotal) {
        ArrayList<String> ticket = new ArrayList<>();

        ticket.add("FACTURA SIMPLIFICADA\n\n");
        ticket.add("Restaurante Fast & Food");
        ticket.add("C/ Profesor José García Santesmases, s/n\n");
        ticket.add(date);
        ticket.add("---------------------------------------------");
        ticket.add("UND    ARTICULO                        TOTAL");
        
        ArrayList<Integer> numberList = new ArrayList<>();
        StringBuilder productInfo = new StringBuilder();

        for(int i = 0; i < products.size() - 1; i++)
            if(i%3==0) {
            	numberList.add(8 - products.get(i).split("").length);
        		numberList.add(33 - products.get(i+1).split("").length);

                for(int j = 0; j < 2; j++) {
                    productInfo.append(products.get(i + j));

                    for(int k = numberList.get(j); k > 0; k--)
                        productInfo.append(" ");
            	}

                ticket.add(productInfo.toString() + products.get(i+2));
                productInfo.setLength(0); // flush the contents
                numberList.clear();
            }

        ticket.add("");
        ticket.add("TOTAL...................................."+saleTotal+"\n\n");

        return ticket;
    }

    // Renders a receipt in html format
    // TODO prettify a bit
    private ArrayList<String> renderTickerHTML(String date, ArrayList<String> products, Double saleTotal) {
        ArrayList<String> ticket = new ArrayList<>();

        ticket.add("<!DOCTYPE html><html lang='en'><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, max-scalable=1.0\">");
        ticket.add("<style>*{margin:0;padding:0;}body{font:13.34px helvetica,arial,freesans,clean,sans-serif;color:black;line-height:1.4em;background-color: #F8F8F8;padding: 0.7em;}");
        ticket.add("p{margin:1em 0;line-height:1.5em;}p#date{font-size: .8em;margin-bottom: 0;}table{font-size:inherit;font:100%;margin:1em;}table th{border-bottom:1px solid #bbb;padding:.2em 1em;}");
        ticket.add("table td{border-bottom:1px solid #ddd;padding:.2em 1em;}hr{border:1px solid #ddd;}</style>");
        ticket.add("<title>Receipt</title></head>");
        ticket.add("<body><p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        ticket.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Restaurante Fast &amp;Food <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;C/ Profesor José García Santesmases, s/n </p>");
        ticket.add("<p id='date'>"+date+"</p>");
        ticket.add("<hr/><table>");
        ticket.add("<thead><tr><th align=\"right\">UND</th><th align=\"left\">ARTICULO</th><th align=\"right\">TOTAL</th></tr></thead>");
        ticket.add("<tbody>");

        for(int i = 0; i < products.size() - 1; i++)
            if(i%3==0)
                ticket.add("<tr><td align=\"right\">"+products.get(i)+"</td><td align=\"left\">"+products.get(i+1)+"</td><td align=\"right\">"+products.get(i+2)+"</td></tr>");


        ticket.add("</tbody></table>");
        ticket.add("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>TOTAL:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        ticket.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        ticket.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        ticket.add(saleTotal+"</strong></p><p><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        ticket.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>GRACIAS POR SU VISITA</strong></p></body></html>");

        return ticket;
    }

    public void showProfits() {
        double profit = this.saleList.getTotalPrice();

        this.setChanged();
        this.notifyViews(UpdateMessage.composeMessage(BusinessEvent.FINANCE, profit));
    }


    /* AdminPanel Actions */

    public void addUser(UserPermission permission, String name, String username, String password)  throws DuplicateUserException{
        this._hiredUsers.addUser(permission, name, username, password);
    }

    public void removeUser(String username) throws UserNotFoundException {
        if(username.equalsIgnoreCase(this._logedUser.getUserName()))
            throw new UserNotFoundException("No puedes eliminarte a ti mismo!");
        this._hiredUsers.removeUser(username);
    }

    public void modifyUser(String username, String newpassword) throws UserNotFoundException {
        _hiredUsers.modifyUser(username, newpassword);
    }

    public void findUserByUser(String query) {
        _hiredUsers.searchByUser(query);
    }

    public void findUserByName(String query) {
        _hiredUsers.searchByName(query);
    }

    public void findUserByPerm(String query) {
        _hiredUsers.searchByPermission(query);
    }

    public void confirmOrders() {
        this.orderList.confirmOrders();
    }

    /* Stock Panel Actions */

    /* Self maintenance methods */

    public boolean isConnectionReady() {
        return this.validConnection;
    }

    public void addStaffWatcher(Watcher w) {
        this._hiredUsers.addWatcher(w);
        this._hiredUsers.init();
    }

    public void addStockWatcher(Watcher[] wList) {
        this._stock.addWatcher(wList[0]);
        this._stock.addWatcher(wList[1]);
        this._stock.init();
    }

    public void addOrderWatcher(Watcher[] wList) {
        this.orderList.addWatcher(wList[0]);
        this.orderList.addWatcher(wList[1]);
        this.orderList.init();
    }

    public void removeWatchers() {
        this.deleteWatchers();
        this._stock.deleteWatchers();
    }

    public void shutdown() {
        database.closeConnection();
    }
}
