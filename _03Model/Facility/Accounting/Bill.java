/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.Accounting;

import _03Model.ConfigurationDTO;
import _03Model.ConfigurationDTO.Label;
import _03Model.Facility.Accounting.Printer.PrinterOptions;

import java.util.ArrayList;
import java.util.Calendar;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import _03Model.Customers.IClientable;
import java.util.Arrays;

/**
 *
 * @author MoisesE
 */
public final class Bill implements IPrintable{
    
    public Bill (int ID,int turno,String identifier,Calendar date, double duration){
        this.ID = ID;
        this.identifier = identifier;
        this.date = date;
        this.turno = turno;
        this.duration = duration;
        this.productoList = new ArrayList<>();
        this.consumo = 0.0;
        this.down = null;
    }      
    public Bill (IClientable cliente, int idFactura){
        ID = idFactura;
        turno = (int) ConfigurationDTO.getConfigurationValue(Label.TurnoActual);
        identifier = cliente.getIdentifier();
        date = cliente.getHoraSalida();
        duration = cliente.getDuracionInSeconds()*1000;
        productoList = new ArrayList(); productoList.addAll(cliente.getProductoList());
        consumo = cliente.getConsumo();
        down = null;
    }
    public Bill (Bill dto){
        ID = dto.getID();
        identifier = dto.getIdentifier();
        date = dto.getDate();
        turno = dto.getTurno();
        duration = dto.getDuracion();
        productoList = new ArrayList(); productoList.addAll(dto.getProductoList());
        consumo = dto.getConsumo();
        if (dto.getDown()!=null)this.down   = new Bill(dto.getDown());
        else                    this.down   = null;
    }
    
    @Override public void setDown(Bill dto)           { this.down = dto;                  }
    
    @Override public int getID()                            { return ID;                        }
    @Override public String getIdentifier()                 { return identifier;                }
    @Override public int getTurno()                         { return turno; }
    @Override public Calendar getDate()                     { return date;                      }
    @Override public double getDuracion()                   { return duration;                  }   
    @Override public Bill getDown()                   { return down;                      }
    @Override public ArrayList<ISellable> getProductoList() { return productoList;              }
    @Override public double getConsumo()                    { 
        consumo = 0;
        for (int i=0; i<getProductoList().size(); i++)
            consumo += (getProductoList().get(i).getPrecio()*getProductoList().get(i).getCantidad());
        return consumo;  
    }
    
    public void openTrack(){
        PRINTEROPTIONS.resetAll();
        PRINTEROPTIONS.initialize();
        PRINTEROPTIONS.finitWithDrawer();
        PrinterOptions.feedPrinter(PRINTEROPTIONS.finalCommandSet().getBytes());
    }
    public void print(){
        String line44;
        int id=6,item=21,prec=6,cant=4,subt=7;
        ID = (int) ConfigurationDTO.getConfigurationValue(Label.ConsecutivoFacturas);
        
        PRINTEROPTIONS.resetAll();
        PRINTEROPTIONS.initialize();
        PRINTEROPTIONS.feedBack((byte)2);
        PRINTEROPTIONS.setFont(4, true);
        PRINTEROPTIONS.setTextLeft(IPrintable.billHeader());
        PRINTEROPTIONS.setFont(2, false);
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextRight(String.valueOf(ID));                    PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextLeft(IPrintable.dateTimeHeader(Calendar.getInstance(),0));
        PRINTEROPTIONS.setTextLeft(getIdentifier());                        PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter(" - Detalles de Compra - ");           PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();
        line44 = rightPadding("Nro",id)+rightPadding("Item",item);
        line44+= leftPadding("Prec",prec)+leftPadding("#",cant)+leftPadding("SubT.",subt);
        PRINTEROPTIONS.setTextLeft(line44);                                 PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();

        ISellable aux;
        for (int i=0; i<productoList.size(); i++){
            aux = productoList.get(i);   
            String numero=String.valueOf(aux.getID());
            if (numero.length()<id)             line44=rightPadding(numero,id);
            else                                line44=leftPadding("!",id).replace(" ", "#").replace("!", " ");
            if (aux.getNombre().length()<item)  line44+=rightPadding(aux.getNombre(),item);
            else                                line44+=aux.getNombre().substring(0,item-1)+" ";
            String precio = String.valueOf((int) aux.getPrecio());
            if (precio.length()<prec)           line44+=leftPadding(precio,prec);
            else                                line44+=rightPadding("!",prec).replace(" ", "#").replace("!", " ");
            String cantidad = String.valueOf((int) aux.getCantidad());
            if (cantidad.length()<cant)         line44+=leftPadding(cantidad,cant);
            else                                line44+=rightPadding("!",cant).replace(" ", "#").replace("!", " ");
            String subT = String.valueOf((int)(aux.getPrecio()*aux.getCantidad()));
            if (subT.length()<subt)             line44+=leftPadding(subT,subt);
            else                                line44+=rightPadding("!",subt).replace(" ", "#").replace("!", " ");
            PRINTEROPTIONS.setTextLeft(line44);                             PRINTEROPTIONS.newLine();
        }
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextRight("Total: "+(int)getConsumo());           PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                  PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Gracias por su Compra");              PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Nuestro Placer es Servirle");
        PRINTEROPTIONS.feed((byte)3);
        PRINTEROPTIONS.finitWithDrawer();
        PrinterOptions.feedPrinter(PRINTEROPTIONS.finalCommandSet().getBytes());
    }
    
    @Override public String toString(){
        String r = "";
        r += "\nFacturaDTO toString():\n";
        r += "ID: "+ ID +". Date: "+date.getTime().toString() + "\n";
        r += "Identifer: "+getIdentifier()+"\n";
        for (int i=0; i<getProductoList().size(); i++){
            ISellable aux = getProductoList().get(i);
            r += aux + "\n";
        }
        r += "Consumo: "+getConsumo();
        if (down!=null) r += down.toString();
        return r;
    }
    
    private int ID;
    private final String identifier;
    private final Calendar date;
    private final int turno;
    private final ArrayList<ISellable> productoList;
    private double consumo;
    private final double duration;
    private Bill down;    
    private static final PrinterOptions PRINTEROPTIONS = new PrinterOptions();

    public static String rightPadding(String str, int num) {
        return String.format("%1$-" + num + "s", str);
    }
    public static String leftPadding(String str, int n) {
        return String.format("%1$" + n + "s", str);
    }
}

