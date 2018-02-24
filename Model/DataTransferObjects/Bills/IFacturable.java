/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataTransferObjects.Bills;

import java.util.ArrayList;
import Model.DataTransferObjects.SellBase.ISellable;
import Model.Printer.PrinterOptions;
import java.util.Calendar;

/**
 *
 * @author MoisesE
 */
public interface IFacturable {
    
    public int getID();
    public String getIdentifier();
    public Calendar getDate();
    public double getDuracion();
    public int getTurno();
    public void setDown(FacturaDTO dto);
    public FacturaDTO getDown();
    public ArrayList<ISellable> getProductoList ();
    public double getConsumo();
    
    public static String billHeader(){
        PrinterOptions  printerOptions = new PrinterOptions();
        printerOptions.setFont(4, true);
        printerOptions.setTextCenter("The Panera");                          printerOptions.newLine();
        printerOptions.setTextCenter("Bakery and Food");                     printerOptions.newLine();
        printerOptions.setFont(2, false);
        printerOptions.setTextCenter("NIT: 15.444.730-9");                   printerOptions.newLine();
        printerOptions.setTextCenter("REGIMEN SIMPLIFICADO");                printerOptions.newLine();
        printerOptions.setTextCenter("Direccion: Cr.81 #43-19 Local 108");   printerOptions.newLine();
        printerOptions.setTextCenter("Rionegro, Antioquia");                 printerOptions.newLine();
        printerOptions.setTextCenter("Domicilios: 562 9979");                printerOptions.newLine();
        return printerOptions.finalCommandSet();
    }
    
    public static String dateTimeHeader(Calendar date,int font){
        PrinterOptions  printerOptions = new PrinterOptions();
        String fecha, hora;
        hora = "Hora: "+hour(date); 
        fecha = "Fecha: "+date(date);
        printerOptions.setTextLeft(hora);       printerOptions.newLine();
        printerOptions.setTextRight(fecha);     printerOptions.newLine();
        return printerOptions.finalCommandSet();
    }
    
    public static String hour(Calendar date){
        String hora = String.valueOf(date.get(Calendar.HOUR))+":";
        if (date.get(Calendar.HOUR)==0)     hora = "12:";
        if (date.get(Calendar.MINUTE)<10)   hora += "0"+String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        else                                hora += String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        if (Calendar.getInstance().get(Calendar.AM_PM)==1){ hora += " p.m."; }
        else                                                hora += " a.m.";
        return hora;
    }
    
    public static String date(Calendar date){
        String fecha = String.valueOf(date.get(Calendar.DATE))+"/"+String.valueOf(date.get(Calendar.MONTH)+1)+"/"+String.valueOf(date.get(Calendar.YEAR));
        return fecha;
    }
}
