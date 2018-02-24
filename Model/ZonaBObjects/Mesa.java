/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ZonaBObjects;

import Controller.DataBase.IDataBase;
import Model.DataTransferObjects.Bills.Comanda;
import Model.DataTransferObjects.ConfigurationDTO;
import java.util.ArrayList;
import java.util.Calendar;
import Model.DataTransferObjects.SellBase.ISellable;
import static Model.ZonaBObjects.Cliente.nroComanda;

/**
 *
 * @author MoisesE
 */
public class Mesa extends Cliente{
        
    public Mesa (int numero){
        super();
        this.numero = numero;
        this.nuevosProductoList = new ArrayList<>();
    }
    public Mesa (Mesa mesa){
        super(mesa);
        this.numero = mesa.getNumero();
        this.nuevosProductoList = new ArrayList<>();      this.nuevosProductoList.addAll(mesa.getNuevosProductoList());
        this.tiempoInicio = mesa.getHoraInicio();
        this.tiempoSalida = mesa.getHoraSalida();
        this.nroOrden = (int) mesa.getNroOrden();
    }
    
    @Override public void               ocupar()                                    {
        tiempoInicio = Calendar.getInstance();
        if (showMesgSys){
            mess = this.getIdentifier().concat(" ha sido ocupada el "+tiempoInicio.getTime());
            System.out.println(mess);
        }
    }
    @Override public double             desocupar()                                 {
        double r = getConsumo();
        tiempoSalida = Calendar.getInstance();
        if (showMesgSys){
            mess = this.getIdentifier().concat(" ha sido desocupada el "+tiempoSalida.getTime()+" con un consumo de $"+consumo+"");
            System.out.println(mess);
        }
        nuevosProductoList      = new ArrayList<>();
        despachadosProductoList = new ArrayList<>();
        consumo = 0.0;
        nroOrden = 1;
        return r;
    }
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
    @Override public void               sendToKitchen()                             {         
        if (!nuevosProductoList.isEmpty()){
            nroOrden+=1; 
            nroComanda +=1;
            ConfigurationDTO.setConfigurationValueAndPutOnServer(ConfigurationDTO.Label.ConsecutivoComandas, nroComanda);
            despachadosProductoList.addAll(nuevosProductoList);
            Comanda.imprimir(this,nroComanda); 
            nuevosProductoList = new ArrayList<>();
        }
    }
    @Override public void               printBill()                                 {
        if (!nuevosProductoList.isEmpty()){
            sendToKitchen();
        }
        IDataBase.createAndInsertBillInServer(this,true);
    }
    @Override public void               saveBill()                                 {
        if (!nuevosProductoList.isEmpty()){
            sendToKitchen();
        }
        IDataBase.createAndInsertBillInServer(this,false);
    }
    
    public Calendar                     getHoraInicio()                             { return tiempoInicio;                      }
    @Override public Calendar           getHoraSalida()                             { return (tiempoSalida==null)?Calendar.getInstance():tiempoSalida;}
    public int                          getNumero()                                 { return numero;                            }
    @Override public final String       getIdentifier()                             { return (ClienteTypes.Mesa.getShowableName()+this.numero); }
    @Override public double             getDuracionInSeconds()                      {
        double r = 0;
        if (this.tiempoSalida!=null)
            r =(this.tiempoSalida.getTimeInMillis()-this.tiempoInicio.getTimeInMillis())/1000; 
        else
            r =(Calendar.getInstance().getTimeInMillis()-this.tiempoInicio.getTimeInMillis())/1000; 
        return r;
    }
    @Override public int                getNroOrden()                               { return nroOrden;                          }
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
    
    private final int numero;
    private Calendar tiempoInicio;
    private Calendar tiempoSalida;
    private ArrayList<ISellable> nuevosProductoList;
    private String mess;

}
