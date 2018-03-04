/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.DataBase.PostgreSQL;

import Controller.DataBase.IDataBase;
import Model.DataTransferObjects.ConfigurationDTO;
import Controller.Surveillance.SurveillanceReport;
import View.WindowConsole;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mpuyosa91
 */
public class TableConfiguration_PostgreSQL {
    
    public static boolean showMesgSys = false;
    
    public static boolean getAndLoadConfigurationFromServer(){
        ResultSet rs; 
        WindowConsole.print("          Atempting to load configuration from server... \n");
        try {
            if (showMesgSys) System.out.println(QUERYSELECTFROM);
            if (STMT.execute(QUERYSELECTFROM)) {
                rs = STMT.getResultSet();
                while (rs.next()){ 
                    ConfigurationDTO.setConfigurationValue(rs.getString("Description"),rs.getDouble("Value")); 
                }   
            }
            WindowConsole.print("          ... Configuration from server, LOADED.\n");
            return true; 
        }   
        catch (SQLException ex){
            if (ex.getErrorCode()==0 && ex.getSQLState().equals("42P01")) {
                WindowConsole.print("          ... FAILED, Table->\""+TABLENAME+"\"Doesnt Exist.\n");
                WindowConsole.print("          Attempting to create Table->\""+TABLENAME+"\" in DB->\""+DBNAME+"\"... \n");
                createConfigurationTable();
                return getAndLoadConfigurationFromServer();
            }
            else{
                String query = QUERYSELECTFROM;
                SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
            }
        }
        return false;
    }
       
    public static void updateConfigurationToServer(){
        for (int i=0; i<ConfigurationDTO.size(); i++){
            updateConfigurationToServer(i);
        }
    }
    public static void updateConfigurationToServer(int i){
        String query = QUERYINSERTINTO;
        try{               
            query += "(Description, Value) VALUES (";
            query += "'" + ConfigurationDTO.getIdentifier(i) + "', ";
            query += "'" + ConfigurationDTO.getValue(i)+ "') ";     
            query += "ON CONFLICT (Description) DO UPDATE ";
            query += "SET Value = excluded.Value;";
            STMT.execute(query);
            if (showMesgSys) System.out.println(query);
        }
        catch (SQLException ex){
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }
    
    private static final Statement STMT             = IDataBase.DB.getStatement();
    private static final String    DBNAME           = IDataBase.DB.getDBName();
    private static final String    TABLENAME        = "configuration";
    private static final String    QUERYSELECTFROM  = "SELECT * FROM "+TABLENAME+" ;";
    private static final String    QUERYCREATETABLE = "CREATE TABLE "+TABLENAME+" ";
    private static final String    QUERYINSERTINTO  = "INSERT INTO "+TABLENAME+" ";
        
    private static void createConfigurationTable(){
        String query = QUERYCREATETABLE.concat("(Description text, Value real, PRIMARY KEY (Description));");
        try {
            STMT.execute(query);
            if (showMesgSys) System.out.println(query);
            WindowConsole.print("          ... Creationg of Table \""+TABLENAME+"\" CREATED.\n");
            WindowConsole.print("          Attempting to populate Table->\""+TABLENAME+"\" in DB->\""+DBNAME+"\"... \n");
            updateConfigurationToServer();
            WindowConsole.print("          ... Initial data of Table \""+TABLENAME+"\" INSERTED.\n");
        } catch (SQLException ex) {
            SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }
    
}
