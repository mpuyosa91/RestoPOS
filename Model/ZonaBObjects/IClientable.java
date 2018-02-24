/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ZonaBObjects;

import java.util.ArrayList;
import Model.DataTransferObjects.SellBase.ISellable;
import java.util.Calendar;

/**
 *
 * @author MoisesE
 */
public interface IClientable {
    
    public void                 ocupar();
    public double               desocupar();
    public void                 agregarProducto (ISellable producto);
    public boolean              borrarProducto (ISellable producto);
    public void                 sendToKitchen();
    public void                 printBill();
    public void                 saveBill();
    
    public boolean              isOcupada();
    public String               getIdentifier();
    public Calendar             getHoraSalida();
    public double               getDuracionInSeconds();
    public int                  getNroOrden();
    public ArrayList<ISellable> getNuevosProductoList();
    public ArrayList<ISellable> getProductoList();
    public double               getConsumo();
    
}
