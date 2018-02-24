/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ZonaBObjects;

import Controller.DataBase.IDataBase;
import Model.DataTransferObjects.SellBase.ISellable;
import java.util.ArrayList;

/**
 *
 * @author MoisesE
 */
public class PuntoDeVenta extends Cliente{
        
    public PuntoDeVenta (){
        super();
        this.nuevosProductoList = new ArrayList<>(); 
    }
    public PuntoDeVenta(PuntoDeVenta puntoDeVenta){
        super(puntoDeVenta);
        this.nuevosProductoList = new ArrayList<>(); 
    }
    
    @Override public String   getIdentifier()                               { return ClienteTypes.PuntoDeVenta.getShowableName();       }
    
     
    @Override public void               agregarProducto (ISellable producto)        {
        nuevosProductoList.add(producto);
        consumo += producto.getPrecio();
    }
    @Override public boolean            borrarProducto (ISellable producto)         {
        boolean r;
        r = nuevosProductoList.remove(producto);
        if (r) consumo -= producto.getPrecio();
        return r;
    }
    @Override public void               sendToKitchen()                             { }
    @Override public void               printBill()                                 {
        if (!nuevosProductoList.isEmpty()){
            despachadosProductoList.addAll(nuevosProductoList);
            nuevosProductoList = new ArrayList<>();
        }
        IDataBase.createAndInsertBillInServer(this,true);
    }
    @Override public void               saveBill()                                 {
        if (!nuevosProductoList.isEmpty()){
            despachadosProductoList.addAll(nuevosProductoList);
            nuevosProductoList = new ArrayList<>();
        }
        IDataBase.createAndInsertBillInServer(this,false);
    }
    
    @Override public ArrayList<ISellable> getNuevosProductoList()                   { return nuevosProductoList;                }
    @Override public ArrayList<ISellable> getProductoList()                         { return despachadosProductoList;           }
    @Override public double             getConsumo()                                {
        consumo = 0;
        for (int i=0; i<getProductoList().size(); i++)
            consumo += (getProductoList().get(i).getPrecio()*getProductoList().get(i).getCantidad());
        for (int i=0; i<getNuevosProductoList().size(); i++)
            consumo += (getNuevosProductoList().get(i).getPrecio()*getNuevosProductoList().get(i).getCantidad());
        return consumo;  
    }
    
    @Override public String toString(){
        String r="";
        r = "\n"+getIdentifier()+":";
        for (int i=0; i<despachadosProductoList.size();i++){
            r += "\n    "+i+" "+despachadosProductoList.get(i).getNombre();
            r += " x"+despachadosProductoList.get(i).getCantidad();
            r += ", $"+despachadosProductoList.get(i).getPrecio();
        }
        for (int i=0; i<nuevosProductoList.size();i++){
            r += "\n  ->"+i+" "+nuevosProductoList.get(i).getNombre();
            r += " x"+nuevosProductoList.get(i).getCantidad();
            r += ", $"+nuevosProductoList.get(i).getPrecio();
        }
        return r;
    }
    
    private ArrayList<ISellable> nuevosProductoList;
}
