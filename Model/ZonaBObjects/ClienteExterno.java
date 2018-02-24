/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.ZonaBObjects;

import Controller.DataBase.IDataBase;
import Model.DataTransferObjects.ConfigurationDTO;
import static Model.DataTransferObjects.ConfigurationDTO.Label.ConsecutivoExterno;

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
        IDataBase.createAndInsertBillInServer(this, (int) ConfigurationDTO.getConfigurationValueAndIncrement(ConsecutivoExterno,1),true);
    }
    @Override public void     saveBill()                                 {
        IDataBase.createAndInsertBillInServer(this, (int) ConfigurationDTO.getConfigurationValueAndIncrement(ConsecutivoExterno,1),false);
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
