/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject.DataBases.MySQL;

import _03Model.ConfigurationDTO;
import _02Controller.ProgramIntegritySurveillance.SurveillanceReport;
import _01View.WindowConsole;

import java.sql.*;

import _04DataAccessObject.generalController;

/**
 *
 * @author mpuyosa91
 */
public class TableConfiguration_MySQL {
    
    public static boolean showMesgSys = false;
    
    static boolean getAndLoadConfigurationFromServer(){
        boolean r = false;
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        String query = QUERYSELECTFROM;
        WindowConsole.print("          Atempting to load configuration from server... \n");
        try {
            if (showMesgSys) System.out.println(query);
            try{
                Class.forName(JDBC_DRIVER);
                connection =
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
            }
            if (resultSet!=null) {
                while (resultSet.next()) {
                    ConfigurationDTO
                            .setConfigurationValue(
                                    resultSet.getString("Description"),
                                    resultSet.getDouble("Value"));
                }
            }
            WindowConsole.print("          ... Configuration from server, LOADED.\n");
            r = true;
        }   
        catch (SQLException ex){
            if (ex.getErrorCode()!=0 && ex.getSQLState().equals("42S02")) {
                WindowConsole.print("          ... FAILED, Table->\""+TABLENAME+"\"Doesnt Exist.\n");
                WindowConsole.print("          Attempting to create Table->\""+TABLENAME+"\" in DB->\""+DBNAME+"\"... \n");
                createConfigurationTable();
                return getAndLoadConfigurationFromServer();
            }
            else{
                SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),ex,query);
            }
        } finally {
            try {
                if (statement != null){
                    statement.close();
                }
                if (connection != null){
                    connection.close();
                }
                if (resultSet!=null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                SurveillanceReport.reportSQL(Thread.currentThread().getStackTrace(),e,query);
            }
        }
        return r;
    }
       
    static void updateConfigurationToServer(){
        for (int i=0; i<ConfigurationDTO.size(); i++){
            updateConfigurationToServer(i);
        }
    }
    static void updateConfigurationToServer(int i){
        String query = QUERYINSERTINTO;
        query += "(Description, Value) VALUES (";
        query += "'" + ConfigurationDTO.getIdentifier(i) + "', ";
        query += "'" + ConfigurationDTO.getValue(i)+ "') ";
        query += "ON DUPLICATE KEY UPDATE ";
        query += "Value = "+ConfigurationDTO.getValue(i)+";";
        try{
            ResultSet resultSet = null;
            Statement statement = null;
            Connection connection = null;
            try{
                Class.forName(JDBC_DRIVER);
                connection =
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
                    if (connection != null){
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    SurveillanceReport
                            .reportSQL(
                                    Thread.currentThread().getStackTrace(),e,query);
                }
            }
            if (resultSet!=null)
                resultSet.close();
            if (showMesgSys) System.out.println(query);
        } catch (SQLException ex){
            SurveillanceReport
                    .reportSQL(Thread.currentThread().getStackTrace(),ex,query);
        }
    }

    private static final String JDBC_DRIVER = generalController.DB.getDriver();
    private static final String JDBC_DB_URL = generalController.DB.getDbUrl();
    private static final String JDBC_USER = generalController.DB.getUser();
    private static final String JDBC_PASS = generalController.DB.getPass();
    private static final String    DBNAME           = generalController.DB.getDBName();
    private static final String    TABLENAME        = "configuration";
    private static final String    QUERYSELECTFROM  = "SELECT * FROM "+TABLENAME+" ;";
    private static final String    QUERYCREATETABLE = "CREATE TABLE "+TABLENAME+" ";
    private static final String    QUERYINSERTINTO  = "INSERT INTO "+TABLENAME+" ";
        
    private static void createConfigurationTable(){
        String query = QUERYCREATETABLE.concat("("
                + "Description varchar(30) CHARACTER SET utf8 COLLATE utf8_spanish2_ci NOT NULL,\n "
                + "Value DOUBLE, \n"
                + "PRIMARY KEY (Description));");
        Statement statement = null;
        Connection connection = null;
        try{
            Class.forName(JDBC_DRIVER);
            connection =
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
                if (connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                SurveillanceReport
                        .reportSQL(
                                Thread.currentThread().getStackTrace(),e,query);
            }
        }
        if (showMesgSys) System.out.println(query);
        WindowConsole.print("          ... Creationg of Table \""+TABLENAME+"\" CREATED.\n");
        WindowConsole.print("          Attempting to populate Table->\""+TABLENAME+"\" in DB->\""+DBNAME+"\"... \n");
        updateConfigurationToServer();
        WindowConsole.print("          ... Initial data of Table \""+TABLENAME+"\" INSERTED.\n");
    }

}
