/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Customers;

import _03Model.Customers.Customer;
import java.util.ArrayList;

/**
 *
 * @author mpuyosa91
 */
public interface ICustomerDAO {
    
    public ArrayList<Customer> getCustomers();
    public Customer getCustomer(String id);
    public boolean insertCustomer(Customer bill);
    public boolean updateCustomer(Customer bill);
    public boolean deleteCustomer(Customer bill);
    
}
