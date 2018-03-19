/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _02Controller.ProgramIntegritySurveillance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author MoisesE
 */
public class SurveillanceReport {
    public static boolean EXITONERROR = false;
    
    public static void log(String msg){
        LOGGER.log(Level.INFO, msg);
    }
    
    public static void generic(StackTraceElement[] st, Exception ex){
        String mess = "Ha Ocurrido un error en <";
        mess += st[1].getClassName()+".";
        mess += st[1].getMethodName()+"()>: \n";
        mess = mess.concat("< Exception: " + ex.toString() + " >\n");
        mess = mess.concat("< Class: " + ex.getClass()+ " >\n");
        JOptionPane.showMessageDialog(null,mess);
        LOGGER.log(Level.SEVERE, ex.toString());
        if (EXITONERROR) System.exit(0);
    }
    
    public static void reportSQL(StackTraceElement[] st, SQLException ex){
        String mess = "Ha Ocurrido un error en <";
        mess += st[1].getClassName()+".";
        mess += st[1].getMethodName()+"()>: \n";
        mess = mess.concat("< SQLException: " + ex.getMessage() + " >\n");
        mess = mess.concat("< SQLState: " + ex.getSQLState() + " >\n");
        mess = mess.concat("< VendorError: " + ex.getErrorCode() + " >\n");
        JOptionPane.showMessageDialog(null,mess);
        LOGGER.log(Level.SEVERE, ex.toString());
        if (EXITONERROR) System.exit(0);
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
        String [] params= {ex.toString(), query};
        LOGGER.log(Level.SEVERE, "{0}\nQUERY: {1}", params);
        if (EXITONERROR) System.exit(0);
    }
    
    private static final Logger LOGGER = getLogger();
    
    private static Logger getLogger(){
        Logger logger = Logger.getLogger("MyLog");
        String logPath = "./log/";
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream(logPath+"log.properties"));
        } catch (IOException | SecurityException ex) {
            if (ex.getClass()!=java.io.FileNotFoundException.class){
                Logger.getLogger(SurveillanceReport.class.getName()).log(Level.SEVERE, null, ex);
                SurveillanceReport.generic(Thread.currentThread().getStackTrace(), ex);
            }
            else{
                try {
                    File file = new File (logPath+"log.properties");
                    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                    if (!file.exists()) {
                        file.createNewFile();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
                        writer.append(  "# especificacion de detalle de log\n" +
                                "# nivel de log global\n" +
                                ".level = INFO\n" +
                                "\n" +
                                "# manejadores de salida de log\n" +
                                "# se cargaron un manejador de archivos y\n" +
                                "# manejador de consola\n" +
                                "handlers = java.util.logging.FileHandler, java.util.logging.ConsoleHandler\n" +
                                "\n" +
                                "# configuración de manejador de archivos\n" +
                                "# nivel soportado para archivos\n" +
                                "java.util.logging.FileHandler.level = ALL\n" +
                                "# archivo de almacenamiento de las salidas de log\n" +
                                "java.util.logging.FileHandler.pattern = ./log/RestoPos%g.log\n" +
                                "# maximo tamaño de archivo en bytes\n" +
                                "java.util.logging.FileHandler.limit = 10485760\n" +
                                "# maximo numero de archivos de logs\n" +
                                "java.util.logging.FileHandler.count = 3\n" +
                                "# clase para formatear salida hacia el archivo de log\n" +
                                "java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter\n" +
                                "# anexar entrada al ultimo archivo (si es falso escribirá al\n" +
                                "# inicio del archivo cuando la aplicación sea ejecutada)\n" +
                                "java.util.logging.FileHandler.append = true\n" +
                                "\n" +
                                "# configuración de manejador de consola\n" +
                                "# nivel soportado para consola\n" +
                                "java.util.logging.ConsoleHandler.level = SEVERE\n" +
                                "# clase para formatear salida hacia consola\n" +
                                "java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter\n");
                        writer.close();
                    }
                    LogManager.getLogManager().readConfiguration(new FileInputStream(logPath+"log.properties"));
                } catch (IOException ex1) {
                    Logger.getLogger(SurveillanceReport.class.getName()).log(Level.SEVERE, null, ex1);
                    SurveillanceReport.generic(Thread.currentThread().getStackTrace(), ex1);
                }
            }
        }
        logger.info("\n\n\n\n*********************** LOG  CREATED ***********************");
        return logger;
    }
}
