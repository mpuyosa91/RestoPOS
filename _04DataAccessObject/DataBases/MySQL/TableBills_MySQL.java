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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import _04DataAccessObject.generalController;

/**
 *
 * @author mpuyosa91
 */
public class TableBills_MySQL {
 public static boolean showMesgSys = false;
    public static final String MESS_OK = "Sistema de Facturas Cargado desde la DB";
       
    
    public static void setFacturaDTO(Bill bill){
        facturaDTO = bill;
    }
    public static Bill getFacturaDTO(){
        getAndLoadDayBills();
        return facturaDTO;
    }
            
    public static void actualize(){
     
    }
    
    public static void insert(Bill dto){
        String query = QUERYINSERTINTO+TABLENAME;
        query += " (ID, TiempoPermanencia_Min, Identifier, Turno, Consumo) VALUES (";
        query += dto.getID() + ", '";
        query += (dto.getDuracion())/(60000)+ "', '";
        query += dto.getIdentifier()+ "', ";
        query += String.valueOf(dto.getTurno())+ ", ";
        query += dto.getConsumo()+ ")";
        try{
            STMT.execute(query);
            if (showMesgSys) System.out.println(query);
        }
        catch(SQLException ex){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
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
                STMT.execute(query2);
                if (showMesgSys) System.out.println(query2);
            }
            catch(SQLException ex){
                SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query2);
            }
        });     
    }
        
    private static Bill              facturaDTO;
    private static ArrayList<ProductoDTO>  productoList;
    private static ArrayList<DeLaCartaDTO> deLaCartaList;
    private static final Statement STMT                 = generalController.DB.getStatement();
    private static final String    DBNAME               = generalController.DB.getDBName();
    private static final String    TABLENAME            = "bills";
    private static final String    TABLECONTENTNAME     = "billsxinventory";
    private static final String    QUERYSELECTFROM      = "SELECT * FROM ";
    private static final String    QUERYCREATETABLE     = "CREATE TABLE ";
    private static final String    QUERYINSERTINTO      = "INSERT INTO "; //VALUES (STR_TO_DATE('01/05/2010', '%m/%d/%Y'));
    private static final String    QUERYUPDATE          = "UPDATE `"+TABLENAME+"` SET ";

    
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
            STMT.execute(query);
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
            STMT.execute(query);
            WindowConsole.print("          ... Creationg of Table \""+TABLECONTENTNAME+"\" CREATED.\n");
        }
        catch (SQLException ex){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }
    
    private static void createTodayBillList(String query) throws SQLException{
        Bill aux = null;
        STMT.execute(query);
        if (showMesgSys)    System.out.println(query);
        ResultSet rs = STMT.getResultSet();     
        while(rs.next()){
            int     rs_ID = rs.getInt("ID");
            String  rs_identifier = rs.getString("Identifier");
            int     turno = rs.getInt("Turno");
            Calendar rs_date = Calendar.getInstance(); rs_date.setTime(rs.getTimestamp("FechaHora"));
            double  rs_duracion = rs.getDouble("TiempoPermanencia_Min")*60000;
            if (aux == null) {
                facturaDTO = new Bill(rs_ID,turno,rs_identifier,rs_date,rs_duracion);
                aux = facturaDTO;
            }
            else {
                aux.setDown(new Bill(rs_ID,turno,rs_identifier,rs_date,rs_duracion));
                aux = aux.getDown();
            }
            if (showMesgSys) System.out.print("\n"+facturaDTO);            
        }
        if (showMesgSys)    System.out.println("\n"+facturaDTO);
    }
    
    public static void insertContentInBill(Bill aux){
        String query = QUERYSELECTFROM+TABLECONTENTNAME+" WHERE bill="+aux.getID()+";";
        try{
            STMT.execute(query);
            if (showMesgSys)    System.out.println(query);
            ResultSet rs = STMT.getResultSet();
            while (rs.next()){
                aux.getProductoList().add((ISellable) getCopyOf(String.valueOf(rs.getInt("element")),rs.getInt("quantity")));
            }
        }
        catch (SQLException ex1){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex1,query);
        }
    }
    
    public static IInventariable getCopyOf(String id_String, int quantity){
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
        r.setCantidad(quantity);
        if(showMesgSys) System.out.println("Copy Created: "+r);
        return r;
    }
    
}
