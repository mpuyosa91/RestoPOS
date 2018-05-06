/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.Accounting;

import java.util.ArrayList;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import _03Model.Facility.Accounting.Printer.PointOfServicePrinter;
import java.util.Calendar;

/**
 *
 * @author MoisesE
 */
public interface IPrintable {
    
    int getID();
    String getIdentifier();
    Calendar getDate();
    double getDuracion();
    int getTurno();
    void setDown(Bill dto);
    Bill getDown();
    ArrayList<ISellable> getProductoList ();
    double getConsumo();
    
    static String billHeader(){
        PointOfServicePrinter pointOfServicePrinter =
                new PointOfServicePrinter();
        pointOfServicePrinter.setFont(4, true);
        pointOfServicePrinter.setTextCenter("The Panera");                          pointOfServicePrinter.newLine();
        pointOfServicePrinter.setTextCenter("Bakery and Food");                     pointOfServicePrinter.newLine();
        pointOfServicePrinter.setFont(2, false);
        pointOfServicePrinter.setTextCenter("NIT: 15.444.730-9");                   pointOfServicePrinter.newLine();
        pointOfServicePrinter.setTextCenter("REGIMEN SIMPLIFICADO");                pointOfServicePrinter.newLine();
        pointOfServicePrinter.setTextCenter("Direccion: Cr.81 #43-19 Local 108");   pointOfServicePrinter.newLine();
        pointOfServicePrinter.setTextCenter("Rionegro, Antioquia");                 pointOfServicePrinter.newLine();
        pointOfServicePrinter.setTextCenter("Domicilios: 562 9979");                pointOfServicePrinter.newLine();
        return pointOfServicePrinter.finalCommandSet();
    }
    
    static String dateTimeHeader(Calendar date,int font){
        PointOfServicePrinter pointOfServicePrinter = new PointOfServicePrinter();
        String fecha, hora;
        hora = "Hora: "+hour(date); 
        fecha = "Fecha: "+date(date);
        pointOfServicePrinter.setTextLeft(hora);       pointOfServicePrinter.newLine();
        pointOfServicePrinter.setTextRight(fecha);     pointOfServicePrinter.newLine();
        return pointOfServicePrinter.finalCommandSet();
    }
    
    static String hour(Calendar date){
        String hora = String.valueOf(date.get(Calendar.HOUR))+":";
        if (date.get(Calendar.HOUR)==0)     hora = "12:";
        if (date.get(Calendar.MINUTE)<10)   hora += "0"+String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        else                                hora += String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        if (Calendar.getInstance().get(Calendar.AM_PM)==1){ hora += " p.m."; }
        else                                                hora += " a.m.";
        return hora;
    }
    
    static String date(Calendar date){
        String fecha = String.valueOf(date.get(Calendar.DATE))+"/"+String.valueOf(date.get(Calendar.MONTH)+1)+"/"+String.valueOf(date.get(Calendar.YEAR));
        return fecha;
    }
}
