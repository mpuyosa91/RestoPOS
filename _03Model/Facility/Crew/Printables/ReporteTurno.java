/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.Crew.Printables;

import _03Model.Facility.Accounting.Printer.PrinterOptions;

import java.util.Calendar;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import java.util.ArrayList;
import _03Model.Facility.Accounting.IPrintable;

/**
 *
 * @author MoisesE
 */
public class ReporteTurno{
    public static boolean showMesgSys = false;
    
    public static void                  flushProductoList(){
        PRODUCTOLIST.clear();
    }
    public static void                  addProductoList(ISellable sellable) {
        PRODUCTOLIST.add(sellable);
    }
    public static ArrayList<ISellable>  getProductoList()                   {
        return PRODUCTOLIST;
    }
    public static double                getConsumo()                        { 
        consumo = 0;
        for (int i=0; i<getProductoList().size(); i++)
            consumo += (getProductoList().get(i).getPrecio()*getProductoList().get(i).getCantidad());
        return consumo;                           
    }
    public static void                  imprimir()                          {
        nroReporte++;
        ArrayList<ISellable> list = PRODUCTOLIST;
        list.sort((ISellable o1, ISellable o2) -> { return o1.getID() - o2.getID(); });
        
        PRINTEROPTIONS.resetAll();
        PRINTEROPTIONS.initialize();
        PRINTEROPTIONS.feedBack((byte)2);
        PRINTEROPTIONS.setTextLeft(IPrintable.billHeader());
        PRINTEROPTIONS.setFont(2, true);
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextLeft(IPrintable.dateTimeHeader(Calendar.getInstance(),0));
        PRINTEROPTIONS.setTextLeft("Turno: " + nroReporte);                 PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter(" - Entrega De Turno - ");             PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextLeft("Nro.  Item                    Prec  #   SubT");        
        PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();

        ISellable aux;
        String spaces;
        for (int i=0; i<list.size(); i++){
            aux = list.get(i);
            
            String nombre = aux.getNombre(); spaces = "";
            if (nombre.length()<22) for (int j=0;j<23-nombre.length();j++){ spaces += " "; }
            else                    nombre = aux.getNombre().substring(0, 22)+" ";
            nombre = nombre.concat(spaces); 
            
            String cantidad = String.valueOf((int)aux.getCantidad()); spaces = ""; 
            for (int j=0;j<2-cantidad.length();j++){ spaces += " "; }
            cantidad = spaces.concat(cantidad);
            
            String precio = String.valueOf((int) aux.getPrecio()); spaces = ""; 
            for (int j=0;j<5-precio.length();j++){ spaces += " "; }
            precio = spaces.concat(precio);
            
            String subT = String.valueOf((int)(aux.getPrecio()*aux.getCantidad())); spaces = ""; 
            for (int j=0;j<5-subT.length();j++){ spaces += " "; }
            subT = spaces.concat(subT);
            
            PRINTEROPTIONS.setTextLeft(aux.getID()+"  "+nombre+precio+" "+cantidad+"  "+subT);
            PRINTEROPTIONS.newLine();
        }
        
        PRINTEROPTIONS.addLineSeperator();                                   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextRight("Total: "+(int)getConsumo());    PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Total sin incluir la \"Base\" ");      PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Recuerde entregar la estacion en Buen Estado");
        PRINTEROPTIONS.feed((byte)3);
        PRINTEROPTIONS.finitWithDrawer();
        System.out.print("-------------------------");
        if (showMesgSys) System.out.print("-------------------------");
        if (showMesgSys) System.out.print(PRINTEROPTIONS.finalCommandSet());
        PrinterOptions.feedPrinter(PRINTEROPTIONS.finalCommandSet().getBytes());
    }
    
    private static int nroReporte = 0;
    private static int consumo = 0;
    private static final ArrayList<ISellable> PRODUCTOLIST = new ArrayList<>();
    private static final PrinterOptions PRINTEROPTIONS = new PrinterOptions();

}

