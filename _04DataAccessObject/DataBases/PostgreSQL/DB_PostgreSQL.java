/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject.DataBases.PostgreSQL;

/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
*/
import java.sql.*;
import javax.swing.JOptionPane;
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
public class DB_PostgreSQL implements IDataBase{

//******************************************************************************
//******************************** CONSTRUCTORS ********************************        

    public DB_PostgreSQL(){}
    
    
//******************************************************************************
//*********************************** PUBLIC ***********************************    
    
    @Override public Statement  getStatement()                      { return STMT;                                                                  }
    @Override public String     getDBName()                         { return DBNAME;                                                                }
    
    @Override public void       updateConfigurationToServer()       { TableConfiguration_PostgreSQL.updateConfigurationToServer();                  }
    @Override public void       updateConfigurationToServer(int i)  { TableConfiguration_PostgreSQL.updateConfigurationToServer(i);                 }
    
    @Override public boolean    getAndLoadConfigurationFromServer() { return TableConfiguration_PostgreSQL.getAndLoadConfigurationFromServer();     }
    @Override public boolean    getProductsAndSupply()                   { return TableTreeElements_PostgreSQL.getAndLoadModel();                        }
    @Override public boolean    getProductsAndSupplyCompositions()        { return TableTreeElementsComposition_PostgreSQL.getAndLoadModelComposition();  }
    @Override public Bill getTodayBills()             { return TableBills_PostgreSQL.getFacturaDTO();                                 }                 
    
    @Override public <G extends Inventory> void insertTreeElement(G dto) {
        WindowConsole.print("Inserting in DB: "+dto+"\n");
        TableTreeElements_PostgreSQL.insert(dto);
        if (dto.isFinal()) TableTreeElementsComposition_PostgreSQL.insert(dto);
        WindowConsole.print("Inserted in DB: "+dto+"\n");
    }
    
    @Override public <G extends Inventory> void updateTreeElement(G dto) {
        WindowConsole.print("Updating in DB: "+dto+"\n");
        TableTreeElements_PostgreSQL.update(dto);
        if (dto.isFinal()) TableTreeElementsComposition_PostgreSQL.update(dto);
        WindowConsole.print("Updated in DB: "+dto+"\n");
    }
    
    @Override public void insertBill(Bill bill) {
        Calendar today = Calendar.getInstance();
        int todayDate = today.get(Calendar.YEAR)*10000+(today.get(Calendar.MONTH)+1)*100+today.get(Calendar.DATE);
        ConfigurationDTO.setConfigurationValueAndPutOnServer(ConfigurationDTO.Label.FechaUltimaFactura, todayDate);
        TableBills_PostgreSQL.insert(bill);
        if (TableBills_PostgreSQL.getFacturaDTO()!=null) {
            Bill aux = TableBills_PostgreSQL.getFacturaDTO();
            while (aux.getDown()!=null){
                aux = aux.getDown();
            }
            aux.setDown(bill);
        }
        else TableBills_PostgreSQL.setFacturaDTO(bill);
    }
    
    
    
//******************************************************************************
//********************************** PRIVATES **********************************    
    private static final Statement  STMT                =   createStatement();
    private static final String     DBUSER              =   "restopos";
    private static final String     DBPASS              =   "restoposdb";
    private static final String     DBNAME              =   "thepanera";
    private static final String     GESTORLOCATION      =   "jdbc:postgresql://localhost:5432/";
    private static final String     DBLOCATION          =   "jdbc:postgresql://localhost:5432/"+DBNAME+"?";
    private static final String     QUERYCREATEDB       =   "CREATE DATABASE "+DBNAME+";";
    
    private static Statement createStatement(){
        Statement r_stmt;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            String mess = "Ha Ocurrido un error en 'Driver_PostgreSQL.Class.forName()': \n";
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
                    } catch (SQLException ex1) { SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex1,QUERYCREATEDB); }
                } else SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex);
            }   
        } catch (SQLException ex) { SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex); }   
        return null;
    }    

    

   
}