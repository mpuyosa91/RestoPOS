/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.ProductsAndSupplies;

import java.util.ArrayList;

/**
 *
 * @author MoisesE
 */
public interface ITreesable {
    
    public boolean      isFinal();
    public Inventory getUp();
    public ArrayList    getDown();
    
    public void         setFinal(boolean b);
    public void         setUp(Inventory up);
    public void         setDown(ArrayList down);
    
    public static <G extends Inventory> ArrayList<G> treeToList(G dto, ArrayList<G> list){
        ArrayList<G> aux;
        for (int i=0; i<dto.getDown().size(); i++){
            aux = (ArrayList<G>) dto.getDown();
            if (aux.get(i).isFinal()) list.add(aux.get(i));
            else                      treeToList(aux.get(i),list);}
        return list;
    }
}
