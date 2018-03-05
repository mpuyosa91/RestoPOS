/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Customers;

import _03Model.ConfigurationDTO;
import static _03Model.ConfigurationDTO.Label.ConsecutivoExterno;
import _04DataAccessObject.generalController;

/**
 *
 * @author MoisesE
 */
public final class ClienteExterno extends PuntoDeVenta{
        
    public ClienteExterno (){
        super();
    }
    public ClienteExterno(ClienteExterno clienteExterno){
        super(clienteExterno);
    }
    
    @Override public String   getIdentifier()                               { return ClienteTypes.ClienteExterno.getShowableName();     }
    @Override public void     printBill()                                 {
        generalController.insertBill(this,true);
    }
    @Override public void     saveBill()                                 {
        generalController.insertBill(this,false);
    }
    @Override public String toString(){
        String r="";
        r = "\n"+getIdentifier()+":";
        for (int i=0; i<despachadosProductoList.size();i++){
            r += "\n    "+i+" "+despachadosProductoList.get(i).getNombre();
            r += " x"+despachadosProductoList.get(i).getCantidad();
            r += ", $"+despachadosProductoList.get(i).getPrecio();
        }
        return r;
    }
}
