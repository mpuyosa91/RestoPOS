/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.Crew.Printables;

import _03Model.Facility.Accounting.Printer.PointOfServicePrinter;

import java.util.Calendar;

import _03Model.Facility.ProductsAndSupplies.IInventariable;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import java.util.ArrayList;
import java.util.Comparator;

import _03Model.Facility.Accounting.IPrintable;

/**
 *
 * @author MoisesE
 */
public class ReporteTurno{
    public static boolean showMesgSys = false;
    
    public static void flushProductList() {
        PRODUCT_LIST.clear();
    }
    public static void addToProductList(ISellable sellable) {
        PRODUCT_LIST.add(sellable);
    }
    public static ArrayList<ISellable> getProductList() {
        return PRODUCT_LIST;
    }
    public static double getConsumo() {
        int consumo = 0;
        for (int i = 0; i< getProductList().size(); i++)
            consumo += (
                    getProductList().get(i).getPrecio()*
                            getProductList().get(i).getCantidad());
        return consumo;                           
    }
    public static void print() {
        nroReporte++;

        PRINTER.resetAll();
        PRINTER.initialize();
        PRINTER.feedBack((byte)2);
        PRINTER.setTextLeft(IPrintable.billHeader());
        PRINTER.setFont(2, true);
        PRINTER.addLineSeperator();                         PRINTER.newLine();
        PRINTER.setTextLeft(
                IPrintable.dateTimeHeader(Calendar.getInstance(),0));
        PRINTER.setTextLeft("Turno: " + nroReporte);        PRINTER.newLine();
        PRINTER.addLineSeperator();                         PRINTER.newLine();
        PRINTER.setTextCenter(" - Entrega De Turno - ");    PRINTER.newLine();
        PRINTER.addLineSeperator();                         PRINTER.newLine();
        PRINTER.setTextLeft("Nro.  Item                 Prec  #   SubT   ");
        PRINTER.newLine();
        PRINTER.addLineSeperator();                         PRINTER.newLine();

        //PRODUCT_LIST.sort((ISellable o1, ISellable o2) -> { return o1.getID() - o2.getID(); });
        PRODUCT_LIST.sort(Comparator.comparingInt(IInventariable::getID));
        for (ISellable product : PRODUCT_LIST) {

            String id = String.valueOf(product.getID());
            String nombre = product.getNombre();
            String precio = String.valueOf((int) product.getPrecio());
            String cantidad = String.valueOf((int) product.getCantidad());
            String subT = String.valueOf(
                    (int) (product.getPrecio()* product.getCantidad()));


            id = id.concat(addSpaces(6 - id.length()));
            nombre = (nombre.length()<21)?
                    nombre.concat(addSpaces(21-nombre.length())):
                    nombre.substring(0, 20).concat(" ");
            precio = addSpaces(6 - precio.length()).concat(precio);
            cantidad = addSpaces(4 - cantidad.length()).concat(cantidad);
            subT = addSpaces(7 - subT.length()).concat(subT);

            PRINTER.setTextLeft(id+nombre+precio+cantidad+subT);
            PRINTER.newLine();
        }
        
        PRINTER.addLineSeperator();                         PRINTER.newLine();
        PRINTER.setTextRight("Total: "+(int)getConsumo());  PRINTER.newLine();
        PRINTER.addLineSeperator();                         PRINTER.newLine();
        PRINTER.setTextCenter("Total sin incluir la \"Base\" ");
        PRINTER.newLine();
        PRINTER.setTextCenter("Recuerde entregar la estacion en Buen Estado");
        PRINTER.feed((byte)3);
        PRINTER.feedCutAndDrawerKick();
        PRINTER.printAll();
        if (showMesgSys) System.out.print("-------------------------");
        if (showMesgSys) System.out.print(PRINTER.finalCommandSet());
    }
    
    private static int nroReporte = 0;
    private static final ArrayList<ISellable> PRODUCT_LIST = new ArrayList<>();
    private static final PointOfServicePrinter PRINTER = new PointOfServicePrinter();

    private static String addSpaces(int spaces){
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < spaces; j++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}

