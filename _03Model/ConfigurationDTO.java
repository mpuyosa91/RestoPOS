/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model;

import _01View.WindowConsole;
import java.util.ArrayList;
import _04DataAccessObject.generalController;

/**
 *
 * @author MoisesE
 */
public class ConfigurationDTO {
       
    public static enum Label{
        NroMesas(10),
        ConsecutivoFacturas(128090),
        ConsecutivoExterno(6),
        FechaUltimaFactura(20171101),
        ConsecutivoComandas(0),
        ClienteExternoAutent(4733),
        TurnoActual(1);
        
        private final int value;
        Label(int value){ this.value = value;   }
        int getValue()  { return value;         }
    }

    public static void setConfigurationValue(String identifier, double value){
        VALUELIST.set(Label.valueOf(identifier).ordinal(), value);
    }
    public static void setConfigurationValueAndPutOnServer(Label identifier, double value){
        setConfigurationValue(identifier.name(),value);
        generalController.DB.updateConfigurationToServer(identifier.ordinal());
    }
        
    public static double getConfigurationValue(Label identifier){
        return VALUELIST.get(identifier.ordinal());
    }
    public static double getConfigurationValueAndIncrement(Label identifier,int incr){
        double r = getConfigurationValue(identifier);
        setConfigurationValueAndPutOnServer(identifier,r+incr);
        return r;
    }
    
    public static String getIdentifier(int i){
        return Label.values()[i].name();
    }
    public static double getValue(int i){
        return VALUELIST.get(i);
    }
    public static int size(){
        return Label.values().length;
    }
    
    public static String toConsole(){
        String r;
        r = "\nConfigurationDTO:";
        for (int i=0; i<size();i++){ r += "\n'"+Label.values()[i].name()+"': "+String.valueOf(VALUELIST.get(i)); }
        r += "\n";
        return r;
    }
    
    private static final ArrayList<Double> VALUELIST = constructList();
    private static final boolean SYNC = isSynchronized();
    
    private static ArrayList<Double> constructList(){
        ArrayList<Double> r = new ArrayList<>();
        for (Label value : Label.values()) { r.add((double) value.getValue()); }
        return r;
    }
    
    private static boolean isSynchronized(){
        boolean r;
        WindowConsole.print("     Attempting to load configuration... \n");
        r = generalController.DB.getAndLoadConfigurationFromServer();
        WindowConsole.print("     [CORRECT] Configuration Loaded from server.\n");
        generalController.DB.updateConfigurationToServer();
        return r;
    }
}
