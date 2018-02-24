/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author MoisesE
 */
public class ErrorReport {
    
    public static void generic(StackTraceElement[] st, Exception ex){
        String mess = "Ha Ocurrido un error en <";
        mess += st[1].getClassName()+".";
        mess += st[1].getMethodName()+"()>: \n";
        mess = mess.concat("< Exception: " + ex.getMessage() + " >\n");
        mess = mess.concat("< Localized: " + ex.getLocalizedMessage() + " >\n");
        JOptionPane.showMessageDialog(null,mess);
        System.exit(0);
    }
    
    public static void reportSQL(StackTraceElement[] st, SQLException ex){
        String mess = "Ha Ocurrido un error en <";
        mess += st[1].getClassName()+".";
        mess += st[1].getMethodName()+"()>: \n";
        mess = mess.concat("< SQLException: " + ex.getMessage() + " >\n");
        mess = mess.concat("< SQLState: " + ex.getSQLState() + " >\n");
        mess = mess.concat("< VendorError: " + ex.getErrorCode() + " >\n");
        JOptionPane.showMessageDialog(null,mess);
        System.exit(0);
    }
    
    public static void reportSQL(StackTraceElement[] st, SQLException ex, String query){
        String mess = "Ha Ocurrido un error en <";
        mess += st[1].getClassName()+".";
        mess += st[1].getMethodName()+"()>: \n";
        mess = mess.concat("< SQLException: " + ex.getMessage() + " >\n");
        mess = mess.concat("< SQLState: " + ex.getSQLState() + " >\n");
        mess = mess.concat("< VendorError: " + ex.getErrorCode() + " >\n");
        mess = mess.concat("Query: <" + query + " >");
        JOptionPane.showMessageDialog(null,mess);
        System.exit(0);
    }
}
