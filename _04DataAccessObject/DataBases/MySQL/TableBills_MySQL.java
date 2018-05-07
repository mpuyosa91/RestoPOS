/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject.DataBases.MySQL;

import _03Model.Facility.Accounting.Bill;
import _03Model.Facility.ProductsAndSupplies.DeLaCartaDTO;
import _03Model.Facility.ProductsAndSupplies.IInventariable;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import _03Model.Facility.ProductsAndSupplies.IngredienteDTO;
import _03Model.Facility.ProductsAndSupplies.ProductoDTO;
import _03Model.Facility.ProductsAndSupplies.SubProductoDTO;
import _02Controller.ProgramIntegritySurveillance.SurveillanceReport;
import _01View.WindowConsole;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import _04DataAccessObject.generalController;

/**
 *
 * @author mpuyosa91
 */
public class TableBills_MySQL {
 public static boolean showMesgSys = false;
    //public static final String MESS_OK = "Sistema de Facturas Cargado desde
    // la DB";
       
    
    static void setFacturaDTO(Bill bill){
        facturaDTO = bill;
    }
    static Bill getFacturaDTO(){
        getAndLoadDayBills();
        return facturaDTO;
    }

    /*
    public static void actualize(){
     
    }
    */
    
    public static void insert(Bill dto){
        String query = QUERYINSERTINTO+TABLENAME;
        query += " (ID, TiempoPermanencia_Min, Identifier, Turno, Consumo) VALUES (";
        query += dto.getID() + ", '";
        query += (dto.getDuracion())/(60000)+ "', '";
        query += dto.getIdentifier()+ "', ";
        query += String.valueOf(dto.getTurno())+ ", ";
        query += dto.getConsumo()+ ")";
        try {
            sendQuery(query).close();
            if (showMesgSys) System.out.println(query);
        } catch (SQLException e) {
            SurveillanceReport
                    .reportSQL(Thread.currentThread().getStackTrace(),e,query);
        } catch(NullPointerException ex) {
            SurveillanceReport
                    .generic(Thread.currentThread().getStackTrace(),ex);
        }
        dto.getProductoList().stream().map((producto) -> {
            String query2 = QUERYINSERTINTO+TABLECONTENTNAME;
            query2 += " (bill, element, quantity, price) VALUES (";
            query2 += dto.getID() + ", ";
            query2 += producto.getID()+ ", ";
            query2 += producto.getCantidad()+ ", ";
            query2 += producto.getPrecio()+ ")";
            return query2;
        }).forEachOrdered((query2) -> {
            try{
                sendQuery(query2).close();
                if (showMesgSys) System.out.println(query2);
            }
            catch(SQLException ex){
                SurveillanceReport
                        .reportSQL(
                                Thread.currentThread().getStackTrace()
                                ,ex,query2);
            }
        });     
    }

    private static final String JDBC_DRIVER = generalController.DB.getDriver();
    private static final String JDBC_DB_URL = generalController.DB.getDbUrl();
    private static final String JDBC_USER = generalController.DB.getUser();
    private static final String JDBC_PASS = generalController.DB.getPass();
    private static Bill              facturaDTO;
    private static final String    DBNAME               = generalController.DB.getDBName();
    private static final String    TABLENAME            = "bills";
    private static final String    TABLECONTENTNAME     = "billsxinventory";
    private static final String    QUERYSELECTFROM      = "SELECT * FROM ";
    private static final String    QUERYCREATETABLE     = "CREATE TABLE ";
    private static final String    QUERYINSERTINTO      = "INSERT INTO "; //VALUES (STR_TO_DATE('01/05/2010', '%m/%d/%Y'));
    //private static final String    QUERYUPDATE          = "UPDATE " +
    //        "`"+TABLENAME+"` SET ";

    
    private static void getAndLoadDayBills(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = Calendar.getInstance().getTime();
        String query = QUERYSELECTFROM+TABLENAME+" WHERE FechaHora >='"+sdf.format(date)+"';";
        try {
            WindowConsole.print("          Atempting to load bills from server... \n");
            createTodayBillList(query);
            WindowConsole.print("          ... Bills from server, LOADED.\n");
        }   
        catch (SQLException ex){
            if (ex.getErrorCode()!=0 && ex.getSQLState().equals("42S02")) {
                WindowConsole.print("          ... FAILED, Table->\""+TABLENAME+"\"Doesnt Exist.\n");
                WindowConsole.print("          Attempting to create Table->\""+TABLENAME+"\" in DB->\""+DBNAME+"\"... \n");
                createTable();
                getAndLoadDayBills();
            }
            else{ 
                SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex, query); 
            }
        }
        Bill aux = facturaDTO;
        if (aux!=null){
            do{
                insertContentInBill(aux);
                aux = aux.getDown();
            }while (aux!=null);
        }
    }
        
    private static void createTable(){
        String query =  QUERYCREATETABLE.concat(TABLENAME+" ( "
                        + "ID integer, \n"
                        + "FechaHora timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                        + "TiempoPermanencia_Min double,\n"
                        + "Identifier varchar(30) CHARACTER SET utf8 COLLATE utf8_spanish2_ci,\n"
                        + "Turno integer,\n"
                        + "Consumo integer,\n"
                        + "PRIMARY KEY (ID));");
        try {
            sendQuery(query).close();
            WindowConsole.print("          ... Creationg of Table \""+TABLENAME+"\" CREATED.\n");
        }
        catch (SQLException ex){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
        query =  QUERYCREATETABLE.concat(TABLECONTENTNAME+" ( "
                        + "ID serial, "
                        + "bill integer REFERENCES "+TABLENAME+"(ID),"
                        + "element integer REFERENCES "+TableTreeElements_MySQL.getTableName()+"(ID),"
                        + "quantity integer, "
                        + "price integer, "
                        + "PRIMARY KEY (ID));");
        try {
            sendQuery(query).close();
            WindowConsole.print("          ... Creationg of Table \""+TABLECONTENTNAME+"\" CREATED.\n");
        }
        catch (SQLException ex){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }
    
    private static void createTodayBillList(String query) throws SQLException{
        Bill aux = null;
        if (showMesgSys)    System.out.println(query);
        ResultSet resultSet = sendQuery(query);
        while(resultSet.next()){
            int     rs_ID = resultSet.getInt("ID");
            String  rs_identifier = resultSet.getString("Identifier");
            int     turno = resultSet.getInt("Turno");
            Calendar rs_date = Calendar.getInstance(); rs_date.setTime(resultSet.getTimestamp("FechaHora"));
            double  rs_duracion = resultSet.getDouble("TiempoPermanencia_Min")*60000;
            if (aux == null) {
                facturaDTO = new Bill(rs_ID,turno,rs_identifier,rs_date,rs_duracion);
                aux = facturaDTO;
            }
            else {
                aux.setDown(new Bill(rs_ID,turno,rs_identifier,rs_date,rs_duracion));
                aux = aux.getDown();
            }
        }
        try{
            resultSet.close();
        }catch (NullPointerException ex){
            SurveillanceReport
                    .generic(Thread.currentThread().getStackTrace(),ex);
        }

        if (showMesgSys)    System.out.println("\n\nFacturas Del Dia\n"+facturaDTO);
    }

    private static IInventariable getCopyOf(String id_String, int quantity){
        IInventariable r=null;
        switch (id_String.charAt(0)){
            case '1':
                r = new IngredienteDTO((IngredienteDTO) generalController.getProduct(Integer.parseInt(id_String)));
                break;
            case '2':
                r = new SubProductoDTO((SubProductoDTO) generalController.getProduct(Integer.parseInt(id_String)));
                break;
            case '3':
                r = new ProductoDTO((ProductoDTO) generalController.getProduct(Integer.parseInt(id_String)));
                break;
            case '4':
                r = new DeLaCartaDTO((DeLaCartaDTO) generalController.getProduct(Integer.parseInt(id_String)));
                break;
        }
        Objects.requireNonNull(r).setCantidad(quantity);
        if(showMesgSys) System.out.println("Copy Created: "+r);
        return r;
    }

    private static void insertContentInBill(Bill aux){
        ResultSet resultSet = null;
        String query = QUERYSELECTFROM+TABLECONTENTNAME+" WHERE bill="+aux.getID()+";";
        try{
            if (showMesgSys)    System.out.println(query);
            resultSet = sendQuery(query);
            while (resultSet.next()){
                aux.getProductoList().add((ISellable) getCopyOf(String.valueOf(resultSet.getInt("element")),resultSet.getInt("quantity")));
            }
        }
        catch (SQLException ex1){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex1,query);
        }
        finally {
            try{
                if (resultSet != null) {
                    resultSet.close();
                }
            }catch (NullPointerException ex){
                SurveillanceReport.generic(Thread.currentThread().getStackTrace(),ex);
            } catch (SQLException e) {
                SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),e,query);
            }
        }
    }

    private static ResultSet sendQuery(String query){
        ResultSet resultSet = null;
        Statement statement = null;
        try{
            Class.forName(JDBC_DRIVER);
            Connection connection =
                    DriverManager
                            .getConnection(JDBC_DB_URL,JDBC_USER,JDBC_PASS);
            statement = connection.createStatement();
            statement.execute(query);
            resultSet = statement.getResultSet();
        } catch (ClassNotFoundException e) {
            SurveillanceReport
                    .generic(Thread.currentThread().getStackTrace(),e);
        } catch (SQLException e) {
            SurveillanceReport
                    .reportSQL(Thread.currentThread().getStackTrace(),e,query);
        } finally {
            try{
                if (statement != null){
                    statement.close();
                }
            } catch (SQLException e) {
                SurveillanceReport
                        .reportSQL(
                                Thread.currentThread().getStackTrace(),e,query);
            }
        }
        return resultSet;
    }
}
