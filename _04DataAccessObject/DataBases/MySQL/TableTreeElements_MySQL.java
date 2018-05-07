/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject.DataBases.MySQL;

import _03Model.Facility.ProductsAndSupplies.DeLaCartaDTO;
import _03Model.Facility.ProductsAndSupplies.IInventariable;
import _03Model.Facility.ProductsAndSupplies.IngredienteDTO;
import _03Model.Facility.ProductsAndSupplies.ProductoDTO;
import _03Model.Facility.ProductsAndSupplies.SubProductoDTO;
import _02Controller.ProgramIntegritySurveillance.SurveillanceReport;
import _01View.WindowConsole;
import _03Model.Facility.ProductsAndSupplies.Inventory;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.pow;

import java.sql.*;

import _04DataAccessObject.generalController;

/**
 *
 * @author mpuyosa91
 */
public class TableTreeElements_MySQL {
    public static boolean showMesgSys = false;
    //public final String mess_Ok = "Sistema de Clasificacion Cargado desde
    // la DB";

    static boolean getAndLoadModel() {
        WindowConsole.print("          Atempting to load data from server... \n");
        try {
            return loadDataFromServer();
        } catch (SQLException ex) {
            if (ex.getErrorCode()!=0 && ex.getSQLState().equals("42S02")) {
                WindowConsole.print("          ... FAILED, Table->\"" + TABLENAME + "\"Doesnt Exist.\n");
                WindowConsole.print("          Attempting to create Table->\"" + TABLENAME + "\" in DB->\"" + DBNAME + "\"... \n");
                createTableInServer();
                return getAndLoadModel();
            } else SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex);
        }
        return false;
    }
    
    static String getTableName(){ return TABLENAME; }

    public static <G extends IInventariable> void insert(G dto) {
        String query = QUERYINSERT;
        if (dto.isFinal()) {
            query = query + "(ID, Nombre, EsFinal, Unidad, Cantidad, Precio) VALUES (";
            query = query + String.valueOf(dto.getID()) + ", '";
            query = query + dto.getNombre() + "', ";
            query = query + "TRUE" + ", '";
            query = query + dto.getUnidadBase() + "', ";
            query = query + Integer.toString((int)dto.getCantidad())+ ", ";
            query = query + Integer.toString((int)dto.getPrecio())+ ")";
        } else {
            query = query + "(ID, Nombre, EsFinal) VALUES ('";
            query = query + String.valueOf(dto.getID()) + "', '";
            query = query + dto.getNombre() + "', '";
            query = query + "FALSE" + "')";
        }
        try {
            sendQuery(query).close();
            if (showMesgSys) System.out.println(query);
        } catch (SQLException ex) {
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }

    public static <G extends IInventariable> void update(G dto) {        
        String query = QUERYUPDATESET;
        if (dto.isFinal()) {
            query += "Nombre='"+dto.getNombre()+"', ";
            query += "Unidad='"+dto.getUnidadBase()+"', ";
            query += "Cantidad="+dto.getCantidad()+", ";
            query += "Precio="+dto.getPrecio()+" ";
            query += "WHERE ID="+dto.getID()+";";
        } else {
            query += "Nombre='"+dto.getNombre()+"', ";
            query += "WHERE ID="+dto.getID()+";";
        }
        try {
            sendQuery(query).close();
            if (showMesgSys) System.out.println(query);
        } catch (SQLException ex) {
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }

    /*
    public static <G extends IInventariable> void delete(G dto) {
        String query = QUERYDELETE;
    }
    */

    private static final String JDBC_DRIVER = generalController.DB.getDriver();
    private static final String JDBC_DB_URL = generalController.DB.getDbUrl();
    private static final String JDBC_USER = generalController.DB.getUser();
    private static final String JDBC_PASS = generalController.DB.getPass();
    private static final String     DBNAME              = generalController.DB.getDBName();
    private static final String     TABLENAME           = "treeelements";
    private static final String     QUERYSELECTFROM     = "SELECT * FROM "+TABLENAME+";";
    private static final String     QUERYCREATETABLE    = "CREATE TABLE "+TABLENAME;
    private static final String     QUERYINSERT         = "INSERT INTO "+TABLENAME+" ";
    private static final String     QUERYUPDATESET      = "UPDATE "+TABLENAME+" SET ";
    //private static final String     QUERYDELETE         = "DELETE FROM " +
    //        TABLENAME + " WHERE ";

    private static void createTableInServer() {
        String query = QUERYCREATETABLE.concat(" ( "
            + "ID int, \n"
            + "Nombre varchar(30) CHARACTER SET utf8 COLLATE utf8_spanish2_ci not null, \n"
            + "EsFinal boolean NOT NULL , \n"
            + "Unidad varchar(30) CHARACTER SET utf8 COLLATE utf8_spanish2_ci, \n"
            + "Cantidad double, \n"
            + "Precio int, \n"
            + "PRIMARY KEY (ID));");
        try {
            sendQuery(query).close();
            if (showMesgSys) System.out.println(query);
            WindowConsole.print("          ... Status of table \"" + TABLENAME + "\" creation, CREATED.\n");
        } catch (SQLException ex) {
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }

    private static boolean loadDataFromServer() throws SQLException{
        ResultSet resultSet;
        int id;
        String query = QUERYSELECTFROM;
        if (showMesgSys) System.out.println(query);
        resultSet = sendQuery(query);
        while (resultSet.next()) {
            id = resultSet.getInt("ID");
            if (showMesgSys) System.out.println(id);
            switch ((int) (id / pow(10, floor(log10(id))))) {
                case 1:
                    IngredienteDTO dto1 = new IngredienteDTO((IngredienteDTO)generalController.getProduct((id < 100)?(id / 10):(id / 1000)), id, resultSet.getString("Nombre"));
                    dto1.setFinal(resultSet.getBoolean("EsFinal"));
                    if (dto1.isFinal()) dto1.setCantidad(resultSet.getDouble("Cantidad"), resultSet.getString("Unidad"));
                    if (showMesgSys) System.out.println(dto1);
                    generalController.insertProductInItsOwnTree(dto1);
                    break;
                case 2:
                    SubProductoDTO dto2 = new SubProductoDTO((SubProductoDTO)generalController.getProduct((id < 100)?(id / 10):(id / 1000)), id, resultSet.getString("Nombre"));
                    dto2.setFinal(resultSet.getBoolean("EsFinal"));
                    if (dto2.isFinal()) dto2.setCantidad(resultSet.getDouble("Cantidad"), resultSet.getString("Unidad"));
                    if (showMesgSys) System.out.println(dto2);
                    generalController.insertProductInItsOwnTree(dto2);
                    break;
                case 3:
                    ProductoDTO dto3 = new ProductoDTO((ProductoDTO)generalController.getProduct((id < 100)?(id / 10):(id / 1000)), id, resultSet.getString("Nombre"));
                    dto3.setFinal(resultSet.getBoolean("EsFinal"));
                    if (dto3.isFinal()) dto3.setCantidad(resultSet.getDouble("Cantidad"), resultSet.getString("Unidad"));
                    if (dto3.isFinal()) dto3.setPrecio(resultSet.getDouble("Precio"));
                    if (showMesgSys) System.out.println(dto3);
                    generalController.insertProductInItsOwnTree(dto3);
                    break;
                case 4:
                    DeLaCartaDTO dto4 = new DeLaCartaDTO((DeLaCartaDTO)generalController.getProduct((id < 100)?(id / 10):(id / 1000)), id, resultSet.getString("Nombre"));
                    dto4.setFinal(resultSet.getBoolean("EsFinal"));
                    if (dto4.isFinal()) dto4.setCantidad(resultSet.getDouble("Cantidad"), resultSet.getString("Unidad"));
                    if (dto4.isFinal()) dto4.setPrecio(resultSet.getDouble("Precio"));
                    if (showMesgSys) System.out.println(dto4);
                    generalController.insertProductInItsOwnTree(dto4);
                    break;
            }
        }
        if(showMesgSys) System.out.println(generalController.getModel(Inventory.DeInventarioType.Ingrediente).treeToString());
        if(showMesgSys) System.out.println(generalController.getModel(Inventory.DeInventarioType.SubProducto).treeToString());
        if(showMesgSys) System.out.println(generalController.getModel(Inventory.DeInventarioType.Producto).treeToString());
        if(showMesgSys) System.out.println(generalController.getModel(Inventory.DeInventarioType.DeLaCarta).treeToString());
        WindowConsole.print("          ... Data from server, LOADED.\n");
        resultSet.close();
        return true;
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
