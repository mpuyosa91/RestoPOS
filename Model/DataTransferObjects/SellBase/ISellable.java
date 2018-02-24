/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataTransferObjects.SellBase;

import Model.Printer.PrinterOptions;
import java.util.ArrayList;

/**
 *
 * @author MoisesE
 */
public interface ISellable extends IInventariable{
    
    public double getPrecio();
    public void setPrecio(double precio);
    public ArrayList<ProductoDTO> getProductoList();
    
    public static <G extends ISellable> ArrayList<G> treeToList(G dto, ArrayList<G> list){
        ArrayList<G> aux;
        for (int i=0; i<dto.getDown().size(); i++){
            aux = (ArrayList<G>) dto.getDown();
            if (aux.get(i).isFinal()) list.add(aux.get(i));
            else                      treeToList(aux.get(i),list);}
        return list;
    }
    
    public static <G extends ISellable> void toThermalPrinter(G dto){
        PRINTEROPTIONS.resetAll();
        PRINTEROPTIONS.initialize();
        PRINTEROPTIONS.feedBack((byte)2);
        PRINTEROPTIONS.setFont(4, true);
        PRINTEROPTIONS.setTextCenter("The Panera");                          PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Bakery and Food");                     PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setFont(2, false);
        PRINTEROPTIONS.setTextCenter("REGIMEN SIMPLIFICADO");                PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("NIT: 15.444.730-9");                   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Direccion: Cr.81 #43-19 Local 108");   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Rionegro, Antioquia");                 PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Domicilios: 562 9979");                PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter(" - Codigos de Producto - ");           PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextLeft("Nro.     Item                         Precio");
        PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                   PRINTEROPTIONS.newLine();
        ArrayList<G> list = new ArrayList<>();
        list = treeToList(dto, list);
        G aux;
        for (int i=0; i<list.size(); i++){
            aux = list.get(i);
            
            String spaces = "";
            String ID = String.valueOf(aux.getID());
            String nombre = aux.getNombre();
            String precio = String.valueOf((int) aux.getPrecio());
            
            spaces = ""; for (int j=0; j<8-ID.length(); j++) {spaces +=" ";}
            ID = spaces.concat(ID+" ");
            
            spaces = "";
            if (nombre.length()<28) for (int j=0;j<29-nombre.length();j++){ spaces += " "; }
            else                    nombre = aux.getNombre().substring(0, 28)+" ";
            nombre = nombre.concat(spaces); 
            
            spaces = ""; for (int j=0;j<6-precio.length();j++){ spaces += " "; }
            precio = spaces.concat(precio);
            
            PRINTEROPTIONS.setTextLeft(ID+nombre+precio);
            PRINTEROPTIONS.newLine();
        }
        PRINTEROPTIONS.addLineSeperator();                                   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.feed((byte)3);
        PRINTEROPTIONS.finit();
        PrinterOptions.feedPrinter(PRINTEROPTIONS.finalCommandSet().getBytes());
        //System.out.println(printerOptions.finalCommandSet());
    }
    public static final PrinterOptions PRINTEROPTIONS = new PrinterOptions();
}
