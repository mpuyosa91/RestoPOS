/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.DataBase;

import java.sql.Statement;

/**
 *
 * @author MoisesE
 */
public interface IDB {
    
    public Statement createStatement();    
    public Statement getStatement();
    public String getDBName();
    public String getCreateTablePrefix();
    
}
