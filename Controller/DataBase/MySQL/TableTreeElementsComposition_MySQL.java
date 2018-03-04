/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.DataBase.MySQL;

import Controller.DataBase.IDataBase;
import Controller.DataBase.PostgreSQL.TableTreeElements_PostgreSQL;
import Model.DataTransferObjects.SellBase.DeInventario;
import Model.DataTransferObjects.SellBase.DeLaCartaDTO;
import Model.DataTransferObjects.SellBase.IInventariable;
import Model.DataTransferObjects.SellBase.IRetainerDeLaCarta;
import Model.DataTransferObjects.SellBase.IRetainerIngrediente;
import Model.DataTransferObjects.SellBase.IRetainerProducto;
import Model.DataTransferObjects.SellBase.IRetainerSubProducto;
import Model.DataTransferObjects.SellBase.IngredienteDTO;
import Model.DataTransferObjects.SellBase.ProductoDTO;
import Model.DataTransferObjects.SellBase.SubProductoDTO;
import Controller.Surveillance.SurveillanceReport;
import View.WindowConsole;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mpuyosa91
 */
public class TableTreeElementsComposition_MySQL {
    public static boolean showMesgSys = false;
    public static final String MESS_OK = "Menu Creado";

    public static boolean getAndLoadModelComposition(){
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
        }
        return r;
    }
    
    public static <G extends DeInventario> void insert(G dto){
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
                try{
                    STMT.execute(query);
                    if (showMesgSys) System.out.println(query);
                }
                catch(SQLException ex){
                    SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
                }
            });
        }
    }
    public static <G extends DeInventario> void update(G dto){
        if (dto.getComposition()!=null){
            dto.getComposition().stream().map((contained) -> {
                String query = QUERYUPDATESET;
                query += "unit='"+contained.getUnidadBase()+"', ";
                query += "quantity="+contained.getCantidad()+" ";
                query += "WHERE container="+dto.getID()+" AND "+"contained="+contained.getID()+";";
                return query;
            }).forEachOrdered((query) -> {
                try{
                    STMT.execute(query);
                    if (showMesgSys) System.out.println(query);
                }
                catch(SQLException ex){
                    SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
                }
            });
        }
    }
    public static <G extends IInventariable> void delete(G dto){
        String query = QUERYDELETE;
        query = query + "`ID` = "+String.valueOf(dto.getID());
        WindowConsole.print(query+"\n");
        try {
            STMT.execute(query);
            if (showMesgSys) System.out.println(query);
        }
        catch (SQLException ex){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }
    
    private static final Statement STMT                 = IDataBase.DB.getStatement();
    private static final String    DBNAME               = IDataBase.DB.getDBName();
    private static final String    TABLENAME            = "treeelementscomposition";
    private static final String    QUERYSELECTFROM      = "SELECT * FROM "+TABLENAME+";";
    private static final String    QUERYCREATETABLE     = "CREATE TABLE "+TABLENAME+"";
    private static final String    QUERYINSERTINTO      = "INSERT INTO "+TABLENAME+" ";
    private static final String    QUERYUPDATESET       = "UPDATE "+TABLENAME+" SET ";
    private static final String    QUERYDELETE          = "DELETE FROM "+TABLENAME+" WHERE ";
        
    private static String createTable(){
        String query = QUERYCREATETABLE.concat(" ( "
            + "ID serial, "
            + "Container integer REFERENCES "+TableTreeElements_PostgreSQL.getTableName()+"(ID),"
            + "Contained integer REFERENCES "+TableTreeElements_PostgreSQL.getTableName()+"(ID),"
            + "Unit text, "
            + "Quantity real, "
            + "PRIMARY KEY (ID, Container, Contained));");
        WindowConsole.print("          ... Creationg of Table \""+TABLENAME+"\" CREATED \n");
        try {
            STMT.execute(query);
            if (showMesgSys) System.out.println(query);
        } catch (SQLException ex){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
        return query;
    }
    
    private static ResultSet getComposicionesList() throws SQLException{
        ResultSet rs = null;
        String query = QUERYSELECTFROM, rs_ID;
        boolean exist, booleanquery;
        booleanquery = STMT.execute(query);
        if (showMesgSys) System.out.println(query);
        if (booleanquery) {
            rs = STMT.getResultSet();
            while (rs.next()){ 
                rs_ID = String.valueOf(rs.getInt("Container"));
                if (showMesgSys) System.out.println("ID: "+rs_ID);
                switch(rs_ID.charAt(0)){
                    case '1':
                        setIInventariable((IngredienteDTO) IDataBase.searchByIdInDB(rs.getInt("Container")),getCopyOf(String.valueOf(rs.getInt("Contained")),rs.getInt("Quantity")));
                        break;
                    case '2':        
                        setIInventariable((SubProductoDTO) IDataBase.searchByIdInDB(rs.getInt("Container")),getCopyOf(String.valueOf(rs.getInt("Contained")),rs.getInt("Quantity")));
                        break;
                    case '3':
                        setIInventariable((ProductoDTO) IDataBase.searchByIdInDB(rs.getInt("Container")),getCopyOf(String.valueOf(rs.getInt("Contained")),rs.getInt("Quantity")));
                        break;
                    case '4':
                        setIInventariable((DeLaCartaDTO) IDataBase.searchByIdInDB(rs.getInt("Container")),getCopyOf(String.valueOf(rs.getInt("Contained")),rs.getInt("Quantity")));
                        break;
                }
            }   
            if (showMesgSys) System.out.println(MESS_OK);
        }
        return rs;
    }
    
    public static IInventariable getCopyOf(String id_String, int quantity){
        IInventariable r=null;
        switch (id_String.charAt(0)){
            case '1':
                r = new IngredienteDTO((IngredienteDTO) IDataBase.searchByIdInDB(Integer.parseInt(id_String)));
                break;
            case '2':
                r = new SubProductoDTO((SubProductoDTO) IDataBase.searchByIdInDB(Integer.parseInt(id_String)));
                break;
            case '3':
                r = new ProductoDTO((ProductoDTO) IDataBase.searchByIdInDB(Integer.parseInt(id_String)));
                break;
            case '4':
                r = new DeLaCartaDTO((DeLaCartaDTO) IDataBase.searchByIdInDB(Integer.parseInt(id_String)));
                break;
        }
        r.setCantidad(quantity);
        if(showMesgSys) System.out.println("Copy Created: "+r);
        return r;
    }
    
    public static void setIInventariable(IInventariable container, IInventariable contained){
        if(showMesgSys) System.out.println("Container: "+container+" <- Contained: "+contained);
        int l = (int) floor(log10(contained.getID()));
        int first = (int) (contained.getID()/(int)pow(10,l));
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
