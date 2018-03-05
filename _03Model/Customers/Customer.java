/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Customers;

import _03Model.Facility.Crew.Printables.Command;
import _03Model.ConfigurationDTO;
import _03Model.ConfigurationDTO.Label;
import java.util.ArrayList;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import java.util.Calendar;
import _04DataAccessObject.generalController;

/**
 *
 * @author MoisesE
 */
public class Customer implements IClientable{
    
    public static boolean showMesgSys = false;
    
    public static enum ClienteTypes{
        ClienteGenerico("Cliente Generico"),
        Mesa("Mesa "),
        PuntoDeVenta("Compra por Taquilla"),
        ClienteExterno("Cliente Externo");
        
        private final String showableName;
        ClienteTypes(String showable){showableName = showable;}
        public String getShowableName(){return showableName;}
    }
    
    public Customer (){
        this.consumo = 0.0;
        this.despachadosProductoList = new ArrayList<>();
        nroOrden = 0;
        if (showMesgSys){
            mess = ClienteTypes.ClienteGenerico.getShowableName().concat(" creado.");
            System.out.println(mess);
        }
    }
    public Customer(Customer cliente){
        this.consumo = cliente.getConsumo();
        this.despachadosProductoList = new ArrayList<>();
        this.despachadosProductoList.addAll(cliente.getProductoList());
        nroOrden = 0;
        if (showMesgSys){
            mess = ClienteTypes.ClienteGenerico.getShowableName().concat(" copiada.");
            System.out.println(mess);
        }
    }
    
    @Override public void           ocupar()                                    {
        if (showMesgSys){
            mess = this.getIdentifier().concat(" ha sido ocupada el "+Calendar.getInstance().getTime());
            System.out.println(mess);
        }
    }
    @Override public double         desocupar()                                 {
        double r = getConsumo();
        if (showMesgSys){
            mess = this.getIdentifier().concat(" ha sido desocupada el "+Calendar.getInstance().getTime()+" con un consumo de $"+consumo+"");
            System.out.println(mess);
        }
        despachadosProductoList = new ArrayList<>();
        consumo = 0.0;
        nroOrden = 0;
        return r;
    }
    @Override public void           agregarProducto (ISellable producto)        {
        despachadosProductoList.add(producto);
        consumo += (producto.getPrecio()*producto.getCantidad());
    }
    @Override public boolean        borrarProducto (ISellable producto)         {
        boolean r;
        r = despachadosProductoList.remove(producto);
        if (r) consumo -= (producto.getPrecio()*producto.getCantidad());
        return r;
    }
    @Override public void           sendToKitchen()                             { 
        nroOrden+=1; 
        nroComanda +=1;
        ConfigurationDTO.setConfigurationValueAndPutOnServer(Label.ConsecutivoComandas, nroComanda);
        Command.imprimir(this,nroComanda);          }
    @Override public void           printBill()     {
        desocupar();
        generalController.insertBill(this,true);
    }
    @Override public void          saveBill()      {
        desocupar();
        generalController.insertBill(this,false);
    }
    
    @Override public boolean        isOcupada()                                 { return consumo!=0.0;                                      }
    @Override public String         getIdentifier()                             { return ClienteTypes.ClienteGenerico.getShowableName();    }
    @Override public Calendar       getHoraSalida()                             { return Calendar.getInstance();                            }
    @Override public double         getDuracionInSeconds()                      { return 5;                                                 }
    @Override public int            getNroOrden()                               { return nroOrden;                                          }
    @Override public ArrayList<ISellable> getNuevosProductoList()               { return new ArrayList<>();                                 }
    @Override public ArrayList<ISellable> getProductoList()                     { return despachadosProductoList;                           }
    @Override public double         getConsumo()                                { 
        consumo = 0;
        for (int i=0; i<getProductoList().size(); i++)
            consumo += (getProductoList().get(i).getPrecio()*getProductoList().get(i).getCantidad());
        return consumo;                           
    }
        
    @Override public String toString(){
        String r="";
        for (int i=0; i<despachadosProductoList.size();i++){
            r += "\n    "+i+" "+despachadosProductoList.get(i).getNombre();
            r += " x"+despachadosProductoList.get(i).getCantidad();
            r += ", $"+despachadosProductoList.get(i).getPrecio();
        }
        return r;
    }
    
    private String mess;
    protected ArrayList<ISellable> despachadosProductoList;
    protected double consumo;
    protected int nroOrden;
    protected static int nroComanda = getNroComanda() ;

    private static int getNroComanda(){
        int r = 0;
        Calendar today = Calendar.getInstance();
        int todayDate = today.get(Calendar.YEAR)*10000+(today.get(Calendar.MONTH)+1)*100+today.get(Calendar.DATE);
        if (ConfigurationDTO.getConfigurationValue(Label.FechaUltimaFactura)!=todayDate)
            ConfigurationDTO.setConfigurationValueAndPutOnServer(Label.ConsecutivoComandas, r);
        else
            r = (int) ConfigurationDTO.getConfigurationValue(ConfigurationDTO.Label.ConsecutivoComandas);
        return r;
    }
}
