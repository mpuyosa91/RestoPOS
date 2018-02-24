/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.DataBase;

import Model.DataTransferObjects.Bills.FacturaDTO;
import Model.DataTransferObjects.SellBase.DeInventario;
import java.sql.Statement;

/**
 *
 * @author mpuyosa91
 */
public interface DBDataHandler {
    
    public Statement                        getStatement();
    public String                           getDBName();
    public boolean                          getAndLoadConfigurationFromServer();
    public void                             updateConfigurationToServer();
    public void                             updateConfigurationToServer(int i);
    public boolean                          getAndLoadModel();
    public boolean                          getAndLoadModelComposition();
    public FacturaDTO                       getAndLoadTodaysBills();
    public <G extends DeInventario> void    insertTreeElement(G dto);
    public <G extends DeInventario> void    updateTreeElement(G dto);
    public void                             insertBill(FacturaDTO bill);
   
}
