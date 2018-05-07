/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject.DataBases.MySQL;

import _04DataAccessObject.DataBases.PostgreSQL.TableTreeElements_PostgreSQL;
import _03Model.Facility.ProductsAndSupplies.Inventory;
import _03Model.Facility.ProductsAndSupplies.DeLaCartaDTO;
import _03Model.Facility.ProductsAndSupplies.IInventariable;
import _03Model.Facility.ProductsAndSupplies.IRetainerDeLaCarta;
import _03Model.Facility.ProductsAndSupplies.IRetainerIngrediente;
import _03Model.Facility.ProductsAndSupplies.IRetainerProducto;
import _03Model.Facility.ProductsAndSupplies.IRetainerSubProducto;
import _03Model.Facility.ProductsAndSupplies.IngredienteDTO;
import _03Model.Facility.ProductsAndSupplies.ProductoDTO;
import _03Model.Facility.ProductsAndSupplies.SubProductoDTO;
import _02Controller.ProgramIntegritySurveillance.SurveillanceReport;
import _01View.WindowConsole;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.pow;

import java.sql.*;

import _04DataAccessObject.generalController;

/**
 *
 * @author mpuyosa91
 */
public class TableTreeElementsComposition_MySQL {
    public static boolean showMesgSys = false;

    static boolean getAndLoadModelComposition(){
        boolean r = false;
        ResultSet rs = null;
        WindowConsole.print("          Atempting to load compositions from server... \n");
        try {
            rs = getComposicionesList();
            WindowConsole.print("          ... Compositions from server, LOADED.\n");
            r = true;
        }   
        catch (SQLException ex){
            if (ex.getErrorCode()!=0 && ex.getSQLState().equals("42S02")) {
                WindowConsole.print("          ... FAILED, Table->\""+TABLENAME+"\"Doesnt Exist.\n");
                WindowConsole.print("          Attempting to create Table->\""+TABLENAME+"\" in DB->\""+DBNAME+"\"... \n");
                createTable();
                r = getAndLoadModelComposition();
            }
            else SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),e);
            }
        }
        return r;
    }
    
    public static <G extends Inventory> void insert(G dto){
        if (dto.getComposition()!=null){
            dto.getComposition().stream().map((contained) -> {
                String query = QUERYINSERTINTO;
                query += "(container, contained, unit, quantity) VALUES (";
                query += dto.getID() + ", ";
                query += contained.getID() + ", '";
                query += contained.getUnidadBase() + "', ";
                query += contained.getCantidad()+ ")";
                return query;
            }).forEachOrdered((query) -> {
                Statement statement = null;
                try{
                    Class.forName(JDBC_DRIVER);
                    Connection connection =
                            DriverManager
                                    .getConnection(JDBC_DB_URL,JDBC_USER,JDBC_PASS);
                    statement = connection.createStatement();
                    statement.execute(query);
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
                if (showMesgSys) System.out.println(query);
            });
        }
    }
    public static <G extends Inventory> void update(G dto){
        if (dto.getComposition()!=null){
            dto.getComposition().stream().map((contained) -> {
                String query = QUERYUPDATESET;
                query += "unit='"+contained.getUnidadBase()+"', ";
                query += "quantity="+contained.getCantidad()+" ";
                query += "WHERE container="+dto.getID()+" AND "+"contained="+contained.getID()+";";
                return query;
            }).forEachOrdered((query) -> {
                Statement statement = null;
                try{
                    Class.forName(JDBC_DRIVER);
                    Connection connection =
                            DriverManager
                                    .getConnection(JDBC_DB_URL,JDBC_USER,JDBC_PASS);
                    statement = connection.createStatement();
                    statement.execute(query);
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
                if (showMesgSys) System.out.println(query);
            });
        }
    }
    /*
    public static <G extends IInventariable> void delete(G dto){
        String query = QUERYDELETE;
        query = query + "`ID` = "+String.valueOf(dto.getID());
        WindowConsole.print(query+"\n");
        try {
            sendQuery(query).close();
            if (showMesgSys) System.out.println(query);
        }
        catch (SQLException ex){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }
    */

    private static final String JDBC_DRIVER = generalController.DB.getDriver();
    private static final String JDBC_DB_URL = generalController.DB.getDbUrl();
    private static final String JDBC_USER = generalController.DB.getUser();
    private static final String JDBC_PASS = generalController.DB.getPass();
    private static final String    DBNAME               = generalController.DB.getDBName();
    private static final String    TABLENAME            = "treeelementscomposition";
    private static final String    QUERYSELECTFROM      = "SELECT * FROM "+TABLENAME+";";
    private static final String    QUERYCREATETABLE     = "CREATE TABLE "+TABLENAME+"";
    private static final String    QUERYINSERTINTO      = "INSERT INTO "+TABLENAME+" ";
    private static final String    QUERYUPDATESET       = "UPDATE "+TABLENAME+" SET ";
    //private static final String    QUERYDELETE          = "DELETE FROM " +
    //            ""+TABLENAME+" WHERE ";
    private static final String MESS_OK = "Menu Creado";
        
    private static void createTable(){
        String query = QUERYCREATETABLE.concat(" ( "
            + "ID serial, "
            + "Container integer REFERENCES "+TableTreeElements_PostgreSQL.getTableName()+"(ID),"
            + "Contained integer REFERENCES "+TableTreeElements_PostgreSQL.getTableName()+"(ID),"
            + "Unit text, "
            + "Quantity real, "
            + "PRIMARY KEY (ID, Container, Contained));");
        WindowConsole.print("          ... Creationg of Table \""+TABLENAME+"\" CREATED \n");
        Statement statement = null;
        try{
            Class.forName(JDBC_DRIVER);
            Connection connection =
                    DriverManager
                            .getConnection(JDBC_DB_URL,JDBC_USER,JDBC_PASS);
            statement = connection.createStatement();
            statement.execute(query);
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
        if (showMesgSys) System.out.println(query);
    }
    
    private static ResultSet getComposicionesList() {
        String query = QUERYSELECTFROM, rs_ID;
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
            if (showMesgSys) System.out.println(query);
            if (resultSet!=null) {
                while (resultSet.next()){
                    rs_ID = String.valueOf(resultSet.getInt("Container"));
                    if (showMesgSys) System.out.println("ID: "+rs_ID);
                    switch(rs_ID.charAt(0)){
                        case '1':
                            setIInventariable( generalController.getProduct(resultSet.getInt("Container")),getCopyOf(String.valueOf(resultSet.getInt("Contained")),resultSet.getInt("Quantity")));
                            break;
                        case '2':
                            setIInventariable( generalController.getProduct(resultSet.getInt("Container")),getCopyOf(String.valueOf(resultSet.getInt("Contained")),resultSet.getInt("Quantity")));
                            break;
                        case '3':
                            setIInventariable( generalController.getProduct(resultSet.getInt("Container")),getCopyOf(String.valueOf(resultSet.getInt("Contained")),resultSet.getInt("Quantity")));
                            break;
                        case '4':
                            setIInventariable( generalController.getProduct(resultSet.getInt("Container")),getCopyOf(String.valueOf(resultSet.getInt("Contained")),resultSet.getInt("Quantity")));
                            break;
                    }
                }
                if (showMesgSys) System.out.println(MESS_OK);
            }
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
        if (r != null) {
            r.setCantidad(quantity);
        }
        if(showMesgSys) System.out.println("Copy Created: "+r);
        return r;
    }
    
    private static void setIInventariable(IInventariable container, IInventariable contained){
        if(showMesgSys) System.out.println("Container: "+container+" <- Contained: "+contained);
        int l = (int) floor(log10(contained.getID()));
        int first = contained.getID()/(int)pow(10,l);
        switch (first){
            case 1: 
                ((IRetainerIngrediente) container).addToIngredienteList((IngredienteDTO) contained);
                break;
            case 2: 
                ((IRetainerSubProducto) container).addToSubProductoList((SubProductoDTO) contained);
                break;
            case 3:
                ((IRetainerProducto) container).addToProductoList((ProductoDTO) contained);
                break;
            case 4: 
                ((IRetainerDeLaCarta) container).addToDeLaCartaList((DeLaCartaDTO) contained);
                break;
        }
    }

}
