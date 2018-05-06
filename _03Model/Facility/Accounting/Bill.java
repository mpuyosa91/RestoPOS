/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.Accounting;

import _03Model.ConfigurationDTO;
import _03Model.ConfigurationDTO.Label;
import _03Model.Facility.Accounting.Printer.PointOfServicePrinter;

import java.util.ArrayList;
import java.util.Calendar;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import _03Model.Customers.IClientable;

/**
 *
 * @author MoisesE
 */
public final class Bill implements IPrintable{
    public static boolean showMesgSys = true;

    public Bill (int ID,int turno,String identifier,Calendar date, double duration){
        this.ID = ID;
        this.identifier = identifier;
        this.date = date;
        this.turno = turno;
        this.duration = duration;
        this.productoList = new ArrayList<>();
        this.consumo = 0.0;
        this.down = null;
        printer = new PointOfServicePrinter();
    }
    public Bill (IClientable cliente, int idFactura){
        ID = idFactura;
        turno = (int) ConfigurationDTO.getConfigurationValue(Label.TurnoActual);
        identifier = cliente.getIdentifier();
        date = cliente.getHoraSalida();
        duration = cliente.getDuracionInSeconds()*1000;
        productoList = new ArrayList<>(); productoList.addAll(cliente.getProductoList());
        consumo = cliente.getConsumo();
        down = null;
        printer = new PointOfServicePrinter();
    }

    @Override public void       setDown(Bill dto) { this.down = dto; }
    @Override public int        getID() { return ID; }
    @Override public String     getIdentifier() { return identifier; }
    @Override public int        getTurno() { return turno; }
    @Override public Calendar   getDate() { return date; }
    @Override public double     getDuracion() { return duration; }
    @Override public Bill       getDown() { return down; }
    @Override public ArrayList<ISellable>  getProductoList() {
        return productoList; }

    @Override public double getConsumo() {
        consumo = 0;
        for (int i=0; i<getProductoList().size(); i++)
            consumo += (getProductoList().get(i).getPrecio()*getProductoList().get(i).getCantidad());
        return consumo;  
    }

    public void print(){
        String line44;
        int id_width=6;
        int item_width=21;
        int precio_width=6;
        int cantidad_width=4;
        int subtotal_width=7;
        ID = (int)
                ConfigurationDTO
                        .getConfigurationValue(Label.ConsecutivoFacturas);
        
        printer.resetAll();
        printer.initialize();
        printer.feedBack((byte)2);
        printer.setFont(4, true);
        printer.setTextLeft(IPrintable.billHeader());
        printer.setFont(2, false);
        printer.addLineSeperator();                         printer.newLine();
        printer.setTextRight(String.valueOf(ID));           printer.newLine();
        printer.setTextLeft(
                IPrintable.dateTimeHeader(
                        Calendar.getInstance(),0));
        printer.setTextLeft(getIdentifier());               printer.newLine();
        printer.addLineSeperator();                         printer.newLine();
        printer.setTextCenter(" - Detalles de Compra - ");  printer.newLine();
        printer.addLineSeperator();                         printer.newLine();
        line44 =  rightPadding("Nro",id_width);
        line44 += rightPadding("Item",item_width);
        line44 += leftPadding("Prec",precio_width);
        line44 += leftPadding("#",cantidad_width);
        line44 += leftPadding("SubT.",subtotal_width);
        printer.setTextLeft(line44);                        printer.newLine();
        printer.addLineSeperator();                         printer.newLine();

        for (ISellable product : productoList) {

            line44 = validateString2Right(
                    String.valueOf(product.getID()),
                    id_width);

            String nombre = String.valueOf(product.getNombre());
            if (nombre.length() < item_width)
                line44 += rightPadding(nombre, item_width);
            else
                line44 += nombre.substring(0, item_width - 1) + " ";

            line44 += validateString2Left(
                    String.valueOf((int) product.getPrecio()),
                    precio_width);

            line44 += validateString2Left(
                    String.valueOf((int) product.getCantidad()),
                    cantidad_width);

            line44 += validateString2Left(
                    String.valueOf(
                            (int)(product.getPrecio()*product.getCantidad())),
                    subtotal_width);

            printer.setTextLeft(line44);
            printer.newLine();
        }
        printer.addLineSeperator();                         printer.newLine();
        printer.setTextRight("Total: "+(int)getConsumo());  printer.newLine();
        printer.addLineSeperator();                         printer.newLine();
        printer.setTextCenter("Gracias por su Compra");     printer.newLine();
        printer.setTextCenter("Nuestro Placer es Servirle");
        printer.feed((byte)3);
        printer.feedCutAndDrawerKick();
        printer.printAll();
    }

    public void openTrack(){
        printer.resetAll();
        printer.initialize();
        printer.drawerKick();
        printer.printAll();
    }

    @Override public String toString(){
        StringBuilder r = new StringBuilder();
        r.append("\nFacturaDTO toString():\n");
        r.append("ID: ").append(ID).append(". Date: ").append(date.getTime().toString()).append("\n");
        r.append("Identifer: ").append(getIdentifier()).append("\n");
        for (int i=0; i<getProductoList().size(); i++){
            ISellable aux = getProductoList().get(i);
            r.append(aux).append("\n");
        }
        r.append("Consumo: ").append(getConsumo());
        if (down!=null) r.append(down.toString());
        return r.toString();
    }
    
    private int ID;
    private final String identifier;
    private final Calendar date;
    private final int turno;
    private final ArrayList<ISellable> productoList;
    private double consumo;
    private final double duration;
    private Bill down;    
    private final PointOfServicePrinter printer;

    private static String rightPadding(String str, int num) {
        return String.format("%1$-" + num + "s", str);
    }
    private static String leftPadding(String str, int n) {
        return String.format("%1$" + n + "s", str);
    }
    private static String validateString2Left(String string, int width){
        return (string.length()<width)?
                leftPadding(string, width):
                rightPadding("!", width)
                        .replace(" ", "#")
                        .replace("!", " ");
    }
    private static String validateString2Right(String string, int width){
        return (string.length()<width)?
                rightPadding(string, width):
                leftPadding("!", width)
                        .replace(" ", "#")
                        .replace("!", " ");
    }
}

