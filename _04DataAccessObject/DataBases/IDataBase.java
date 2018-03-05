/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject.DataBases;

import _03Model.Facility.Accounting.Bill;
import _03Model.Facility.ProductsAndSupplies.Inventory;
import java.sql.Statement;

/**
 *
 * @author mpuyosa91
 */
public interface IDataBase {
    
    public Statement                        getStatement();
    public String                           getDBName();
    public boolean                          getAndLoadConfigurationFromServer();
    public void                             updateConfigurationToServer();
    public void                             updateConfigurationToServer(int i);
    public boolean                          getProductsAndSupply();
    public boolean                          getProductsAndSupplyCompositions();
    public Bill                             getTodayBills();
    public <G extends Inventory> void       insertTreeElement(G dto);
    public <G extends Inventory> void       updateTreeElement(G dto);
    public void                             insertBill(Bill bill);
   
}
