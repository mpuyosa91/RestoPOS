/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.ProductsAndSupplies;

import _03Model.Facility.Accounting.Printer.PointOfServicePrinter;

import java.util.ArrayList;

/**
 *
 * @author MoisesE
 */
public interface ISellable extends IInventariable{
    
     double getPrecio();
     void setPrecio(double precio);
     ArrayList<ProductoDTO> getProductoList();
    
     static <G extends ISellable> ArrayList<G> treeToList(G dto, ArrayList<G> list){
        ArrayList<G> aux;
        for (int i=0; i<dto.getDown().size(); i++){
            aux = (ArrayList<G>) dto.getDown();
            if (aux.get(i).isFinal()) list.add(aux.get(i));
            else                      treeToList(aux.get(i),list);}
        return list;
    }
}
