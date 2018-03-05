/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject.DataBases.PostgreSQL;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import _04DataAccessObject.generalController;

/**
 *
 * @author mpuyosa91
 */
public class TableTreeElements_PostgreSQL {
    public static boolean showMesgSys = false;
    public final String mess_Ok = "Sistema de Clasificacion Cargado desde la DB";

    public static boolean getAndLoadModel() {
        WindowConsole.print("          Atempting to load data from server... \n");
        try {
            return loadDataFromServer();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 0 && ex.getSQLState().equals("42P01")) {
                WindowConsole.print("          ... FAILED, Table->\"" + TABLENAME + "\"Doesnt Exist.\n");
                WindowConsole.print("          Attempting to create Table->\"" + TABLENAME + "\" in DB->\"" + DBNAME + "\"... \n");
                createTableInServer();
                return getAndLoadModel();
            } else SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex);
        }
        return false;
    }
    
    public static String getTableName(){ return TABLENAME; }

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
            STMT.execute(query);
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
            STMT.execute(query);
            if (showMesgSys) System.out.println(query);
        } catch (SQLException ex) {
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }

    public static <G extends IInventariable> void delete(G dto) {
        String query = QUERYDELETE;
    }

    private static final Statement  STMT                = generalController.DB.getStatement();
    private static final String     DBNAME              = generalController.DB.getDBName();
    private static final String     TABLENAME           = "treeelements";
    private static final String     QUERYSELECTFROM     = "SELECT * FROM "+TABLENAME+";";
    private static final String     QUERYCREATETABLE    = "CREATE TABLE "+TABLENAME;
    private static final String     QUERYINSERT         = "INSERT INTO "+TABLENAME+" ";
    private static final String     QUERYUPDATESET      = "UPDATE "+TABLENAME+" SET ";
    private static final String     QUERYDELETE         = "DELETE FROM " + TABLENAME + " WHERE ";

    private static void createTableInServer() {
        String query = QUERYCREATETABLE.concat(" ( "
            + "ID integer, "
            + "Nombre text not null, "
            + "EsFinal boolean NOT NULL , "
            + "Unidad text, "
            + "Cantidad real, "
            + "Precio integer, "
            + "PRIMARY KEY (ID));");
        try {
            STMT.execute(query);
            if (showMesgSys) System.out.println(query);
            WindowConsole.print("          ... Status of table \"" + TABLENAME + "\" creation, CREATED.\n");
        } catch (SQLException ex) {
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }

    private static boolean loadDataFromServer() throws SQLException{
        ResultSet rs;
        int id;
        String query = QUERYSELECTFROM;
        STMT.execute(query);
        if (showMesgSys) System.out.println(query);
        rs = STMT.getResultSet();
        while (rs.next()) {
            id = rs.getInt("ID");
            if (showMesgSys) System.out.println(id);
            switch ((int) (id / pow(10, floor(log10(id))))) {
                case 1:
                    IngredienteDTO dto1 = new IngredienteDTO((IngredienteDTO)generalController.getProduct((id < 100)?(id / 10):(id / 1000)), id, rs.getString("Nombre"));
                    dto1.setFinal(rs.getBoolean("EsFinal"));
                    if (dto1.isFinal()) dto1.setCantidad(rs.getDouble("Cantidad"), rs.getString("Unidad"));
                    if (showMesgSys) System.out.println(dto1);
                    generalController.insertProductInItsOwnTree(dto1);
                    break;
                case 2:
                    SubProductoDTO dto2 = new SubProductoDTO((SubProductoDTO)generalController.getProduct((id < 100)?(id / 10):(id / 1000)), id, rs.getString("Nombre"));
                    dto2.setFinal(rs.getBoolean("EsFinal"));
                    if (dto2.isFinal()) dto2.setCantidad(rs.getDouble("Cantidad"), rs.getString("Unidad"));
                    if (showMesgSys) System.out.println(dto2);
                    generalController.insertProductInItsOwnTree(dto2);
                    break;
                case 3:
                    ProductoDTO dto3 = new ProductoDTO((ProductoDTO)generalController.getProduct((id < 100)?(id / 10):(id / 1000)), id, rs.getString("Nombre"));
                    dto3.setFinal(rs.getBoolean("EsFinal"));
                    if (dto3.isFinal()) dto3.setCantidad(rs.getDouble("Cantidad"), rs.getString("Unidad"));
                    if (dto3.isFinal()) dto3.setPrecio(rs.getDouble("Precio"));
                    if (showMesgSys) System.out.println(dto3);
                    generalController.insertProductInItsOwnTree(dto3);
                    break;
                case 4:
                    DeLaCartaDTO dto4 = new DeLaCartaDTO((DeLaCartaDTO)generalController.getProduct((id < 100)?(id / 10):(id / 1000)), id, rs.getString("Nombre"));
                    dto4.setFinal(rs.getBoolean("EsFinal"));
                    if (dto4.isFinal()) dto4.setCantidad(rs.getDouble("Cantidad"), rs.getString("Unidad"));
                    if (dto4.isFinal()) dto4.setPrecio(rs.getDouble("Precio"));
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
        return true;
    }

}
