/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject.DataBases.MySQL;

/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
*/
import java.sql.*;
import _03Model.Facility.Accounting.Bill;
import _03Model.ConfigurationDTO;
import _03Model.Facility.ProductsAndSupplies.Inventory;
import _02Controller.ProgramIntegritySurveillance.SurveillanceReport;
import _01View.WindowConsole;
import java.util.Calendar;
import _04DataAccessObject.DataBases.IDataBase;

/**
 *
 * @author MoisesE
 */
public class DB_MySQL implements IDataBase{

//******************************************************************************
//******************************** CONSTRUCTORS ********************************        

    public DB_MySQL(){}
    
    
//******************************************************************************
//*********************************** PUBLIC ***********************************    
    
    @Override public Statement  getStatement()                      { return STMT;                                                                  }
    @Override public String     getDBName()                         { return DBNAME;                                                                }
    
    @Override public void       updateConfigurationToServer()       { TableConfiguration_MySQL.updateConfigurationToServer();                  }
    @Override public void       updateConfigurationToServer(int i)  { TableConfiguration_MySQL.updateConfigurationToServer(i);                 }
    
    @Override public boolean    getAndLoadConfigurationFromServer() { return TableConfiguration_MySQL.getAndLoadConfigurationFromServer();     }
    @Override public boolean    getProductsAndSupply()                   { return TableTreeElements_MySQL.getAndLoadModel();                        }
    @Override public boolean    getProductsAndSupplyCompositions()        { return TableTreeElementsComposition_MySQL.getAndLoadModelComposition();  }
    @Override public Bill getTodayBills()             { return TableBills_MySQL.getFacturaDTO();                                 }                 
    
    @Override public <G extends Inventory> void insertTreeElement(G dto) {
        WindowConsole.print("Inserting in DB: "+dto+"\n");
        TableTreeElements_MySQL.insert(dto);
        if (dto.isFinal()) TableTreeElementsComposition_MySQL.insert(dto);
        WindowConsole.print("Inserted in DB: "+dto+"\n");
    }
    
    @Override public <G extends Inventory> void updateTreeElement(G dto) {
        WindowConsole.print("Updating in DB: "+dto+"\n");
        TableTreeElements_MySQL.update(dto);
        if (dto.isFinal()) TableTreeElementsComposition_MySQL.update(dto);
        WindowConsole.print("Updated in DB: "+dto+"\n");
    }
    
    @Override public void insertBill(Bill bill) {
        Calendar today = Calendar.getInstance();
        int todayDate = today.get(Calendar.YEAR)*10000+(today.get(Calendar.MONTH)+1)*100+today.get(Calendar.DATE);
        ConfigurationDTO.setConfigurationValueAndPutOnServer(ConfigurationDTO.Label.FechaUltimaFactura, todayDate);
        TableBills_MySQL.insert(bill);
        if (TableBills_MySQL.getFacturaDTO()!=null) {
            Bill aux = TableBills_MySQL.getFacturaDTO();
            while (aux.getDown()!=null){
                aux = aux.getDown();
            }
            aux.setDown(bill);
        }
        else TableBills_MySQL.setFacturaDTO(bill);
    }
    
    
    
//******************************************************************************
//********************************** PRIVATES **********************************    
    private static final Statement  STMT                =   createStatement();
    private static final String     DBNAME              =   "thepanera";
    private static final String     DBUSER              =   "root";
    private static final String     DBPASS              =   "moi050391";
    private static final String     GESTORLOCATION      =   "jdbc:mysql://localhost/?useSSL=false";
    private static final String     DBLOCATION          =   "jdbc:mysql://localhost/"+DBNAME+"?useSSL=false";
    private static final String     DBACCESS            =   "user=root&password=moi050391";
    private static final String     QUERYCREATEDB       =   "CREATE DATABASE "+DBNAME;
    private static final String     QUERYCREATETABLE    =   "CREATE TABLE `"+DBNAME+"`.";
    
    private static Statement createStatement(){
        Statement r_stmt;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            SurveillanceReport.generic(Thread.currentThread().getStackTrace(), ex);
        }
        try{
            WindowConsole.print("     Checking connection with server...\n");
            Connection conn = DriverManager.getConnection(GESTORLOCATION,DBUSER,DBPASS);
            WindowConsole.print("         Connection with server is OK.\n");
            r_stmt = conn.createStatement(); 
            try {
                WindowConsole.print("     Checking DataBase existence...\n");
                conn = DriverManager.getConnection(DBLOCATION,DBUSER,DBPASS);
                WindowConsole.print("         DataBase is OK.\n");
                return conn.createStatement();
            } catch (SQLException ex) {
                if (ex.getSQLState().equals("3D000")||ex.getSQLState().equals("42000")){
                    WindowConsole.print("         DataBase does not exist, Creating.\n");
                    try{
                        r_stmt.execute(QUERYCREATEDB);
                        WindowConsole.print("         Database CREATED.\n");
                        return createStatement();
                    } catch (SQLException ex1) { SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex1,QUERYCREATEDB); }
                } else SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex);
            }   
        } catch (SQLException ex) { SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex); }   
        return null;
    }    
   
}