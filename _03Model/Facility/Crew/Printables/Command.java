/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.Crew.Printables;

import _03Model.Facility.ProductsAndSupplies.DeLaCartaDTO;
import _03Model.Facility.Accounting.Printer.KitchenPrinter;

import java.util.Calendar;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import _03Model.Customers.IClientable;
import static java.lang.Math.round;
import _03Model.Facility.Accounting.IPrintable;

/**
 *
 * @author MoisesE
 */
public class Command{
              
    public static void imprimir(IClientable cliente, int nroComanda){
        boolean valid = false;
        KITCHEN_PRINTER.resetAll();
        KITCHEN_PRINTER.initialize();
        KITCHEN_PRINTER.feedBack((byte)2);
        KITCHEN_PRINTER.setFont(5, false);
        KITCHEN_PRINTER.setTextCenter("COMANDA");                                KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.setFont(2, true);
        KITCHEN_PRINTER.setTextCenter("The Panera");                             KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.setTextCenter("Bakery and Food");                        KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.setFont(4, false);
        KITCHEN_PRINTER.addLineSeperator();                                      KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.setTextLeft(IPrintable.dateTimeHeader(Calendar.getInstance(),2));
        KITCHEN_PRINTER.setTextLeft(cliente.getIdentifier());                    KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.setTextLeft("Orden: "+cliente.getNroOrden()+"        Comanda: "+nroComanda); KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.setTextLeft("Tiempo Clientes: " + String.valueOf((int)round(cliente.getDuracionInSeconds()/60))+" Min.");  KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.addLineSeperator();                                      KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.setTextCenter(" - Ordenes - ");                          KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.addLineSeperator();                                      KITCHEN_PRINTER.newLine();
        ISellable aux;
        for (int i=0; i<cliente.getNuevosProductoList().size(); i++){
            aux = cliente.getNuevosProductoList().get(i);
            if (aux.getClass()==DeLaCartaDTO.class){
                valid = true;
                KITCHEN_PRINTER.setTextLeft(" (*)"+aux.getNombre());                   KITCHEN_PRINTER.newLine();
                KITCHEN_PRINTER.setTextLeft("    "+((DeLaCartaDTO)aux).getEspecial()); KITCHEN_PRINTER.newLine();
            }           
            /*
            aux.getComposition().sort((DeInventario o1, DeInventario o2) -> (o1.getID()-o2.getID()));
            aux.getComposition().forEach(composition -> {
                int auxID = composition.getID();
                String spaces = ""; 
                do{ spaces+=" "; auxID/=10; }while(auxID>0);
                KITCHEN_PRINTER.setTextLeft(spaces+"  -"+composition.getNombre());
                KITCHEN_PRINTER.newLine();
            });
            */
        }
        KITCHEN_PRINTER.addLineSeperator();                                      KITCHEN_PRINTER.newLine();
        KITCHEN_PRINTER.setTextCenter("Fin Comanda");
        KITCHEN_PRINTER.feed((byte)3);
        KITCHEN_PRINTER.finit();
        if (valid)
            KitchenPrinter.feedPrinter(KITCHEN_PRINTER.finalCommandSet().getBytes());
    }
    
    private static final KitchenPrinter KITCHEN_PRINTER = new KitchenPrinter();

}

