/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.Crew.Printables;

import _03Model.Facility.ProductsAndSupplies.Inventory;
import _03Model.Facility.ProductsAndSupplies.DeLaCartaDTO;
import _03Model.Facility.Accounting.Printer.PrinterOptions;

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
        PRINTEROPTIONS.resetAll();
        PRINTEROPTIONS.initialize();
        PRINTEROPTIONS.feedBack((byte)2);
        PRINTEROPTIONS.setFont(5, false);
        PRINTEROPTIONS.setTextCenter("COMANDA");                                PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setFont(2, true);
        PRINTEROPTIONS.setTextCenter("The Panera");                             PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Bakery and Food");                        PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setFont(4, false);
        PRINTEROPTIONS.addLineSeperator();                                      PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextLeft(IPrintable.dateTimeHeader(Calendar.getInstance(),2));
        PRINTEROPTIONS.setTextLeft(cliente.getIdentifier());                    PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextLeft("Orden: "+cliente.getNroOrden()+"        Comanda: "+nroComanda); PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextLeft("Tiempo Clientes: " + String.valueOf((int)round(cliente.getDuracionInSeconds()/60))+" Min.");  PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                      PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter(" - Ordenes - ");                          PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                      PRINTEROPTIONS.newLine();
        ISellable aux;
        for (int i=0; i<cliente.getNuevosProductoList().size(); i++){
            aux = cliente.getNuevosProductoList().get(i);
            if (aux.getClass()==DeLaCartaDTO.class){
                valid = true;
                PRINTEROPTIONS.setTextLeft(" (*)"+aux.getNombre());                   PRINTEROPTIONS.newLine();
                PRINTEROPTIONS.setTextLeft("    "+((DeLaCartaDTO)aux).getEspecial()); PRINTEROPTIONS.newLine();
            }           
            /*
            aux.getComposition().sort((DeInventario o1, DeInventario o2) -> (o1.getID()-o2.getID()));
            aux.getComposition().forEach(composition -> {
                int auxID = composition.getID();
                String spaces = ""; 
                do{ spaces+=" "; auxID/=10; }while(auxID>0);
                PRINTEROPTIONS.setTextLeft(spaces+"  -"+composition.getNombre());
                PRINTEROPTIONS.newLine();
            });
            */
        }
        PRINTEROPTIONS.addLineSeperator();                                      PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Fin Comanda");
        PRINTEROPTIONS.feed((byte)3);
        PRINTEROPTIONS.finit();
        if (valid)
            PrinterOptions.feedPrinter(PRINTEROPTIONS.finalCommandSet().getBytes());
    }
    
    private static final PrinterOptions PRINTEROPTIONS = new PrinterOptions();

}

