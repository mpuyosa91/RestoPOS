/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.DataBase.MySQL;

/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
*/
import java.sql.*;
import javax.swing.JOptionPane;
import Controller.DataBase.DBDataHandler;
import Model.DataTransferObjects.Bills.FacturaDTO;
import Model.DataTransferObjects.ConfigurationDTO;
import Model.DataTransferObjects.SellBase.DeInventario;
import Model.ErrorReport;
import View.WindowConsole;
import java.util.Calendar;

/**
 *
 * @author MoisesE
 */
public class DB_MySQL implements DBDataHandler{

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
    @Override public boolean    getAndLoadModel()                   { return TableTreeElements_MySQL.getAndLoadModel();                        }
    @Override public boolean    getAndLoadModelComposition()        { return TableTreeElementsComposition_MySQL.getAndLoadModelComposition();  }
    @Override public FacturaDTO getAndLoadTodaysBills()             { return TableBills_MySQL.getFacturaDTO();                                 }                 
    
    @Override public <G extends DeInventario> void insertTreeElement(G dto) {
        WindowConsole.print("Inserting in DB: "+dto+"\n");
        TableTreeElements_MySQL.insert(dto);
        if (dto.isFinal()) TableTreeElementsComposition_MySQL.insert(dto);
        WindowConsole.print("Inserted in DB: "+dto+"\n");
    }
    
    @Override public <G extends DeInventario> void updateTreeElement(G dto) {
        WindowConsole.print("Updating in DB: "+dto+"\n");
        TableTreeElements_MySQL.update(dto);
        if (dto.isFinal()) TableTreeElementsComposition_MySQL.update(dto);
        WindowConsole.print("Updated in DB: "+dto+"\n");
    }
    
    @Override public void insertBill(FacturaDTO bill) {
        Calendar today = Calendar.getInstance();
        int todayDate = today.get(Calendar.YEAR)*10000+(today.get(Calendar.MONTH)+1)*100+today.get(Calendar.DATE);
        ConfigurationDTO.setConfigurationValueAndPutOnServer(ConfigurationDTO.Label.FechaUltimaFactura, todayDate);
        TableBills_MySQL.insert(bill);
        if (TableBills_MySQL.getFacturaDTO()!=null) {
            FacturaDTO aux = TableBills_MySQL.getFacturaDTO();
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
    private static final String     GESTORLOCATION      =   "jdbc:mysql://localhost/?";
    private static final String     DBLOCATION          =   "jdbc:mysql://localhost/"+DBNAME+"?";
    private static final String     DBACCESS            =   "user=root&password=moi050391";
    private static final String     QUERYCREATEDB       =   "CREATE DATABASE "+DBNAME;
    private static final String     QUERYCREATETABLE    =   "CREATE TABLE `"+DBNAME+"`.";
    
    private static Statement createStatement(){
        Statement r_stmt;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            String mess = "Ha Ocurrido un error en 'Driver_MySQL.Class.forName()': \n";
            mess = mess.concat("<").concat(ex.toString()).concat(">\n");
            mess = mess.concat("<").concat(ex.getLocalizedMessage()).concat(">\n");
            mess = mess.concat("<").concat(ex.getMessage()).concat(">\n");
            JOptionPane.showMessageDialog(null,mess);
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
                if (ex.getSQLState().equals("3D000")){
                    WindowConsole.print("         DataBase does not exist, Creating.\n");
                    try{
                        r_stmt.execute(QUERYCREATEDB);
                        WindowConsole.print("         Database CREATED.\n");
                        return r_stmt;
                    } catch (SQLException ex1) { ErrorReport.reportSQL(Thread.currentThread().getStackTrace(),ex1,QUERYCREATEDB); }
                } else ErrorReport.reportSQL(Thread.currentThread().getStackTrace(),ex);
            }   
        } catch (SQLException ex) { ErrorReport.reportSQL(Thread.currentThread().getStackTrace(),ex); }   
        return null;
    }    
   
}