/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.ProductsAndSupplies;

import static _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType.Ingrediente;
import java.util.ArrayList;
import _03Model.Facility.ProductsAndSupplies.Measure.Measurable;

/**
 *
 * @author MoisesE
 */
public class IngredienteDTO extends Inventory{
    
//******************************************************************************
//********************************Constructores*********************************
//******************************************************************************
    public IngredienteDTO(IngredienteDTO father, int ID){
        super(ID,clase);
        up = father;
        down = new ArrayList<>();
    }
    public IngredienteDTO(IngredienteDTO father, int ID, String nombre){
        super(ID,nombre,clase);
        up = father;
        down = new ArrayList<>();
    }
    public IngredienteDTO(IngredienteDTO father, int ID, String nombre, double cantidad, Measurable.Type type){
        super(ID,nombre,cantidad,type,clase);
        up = father;
        down = new ArrayList<>();
    }
    public IngredienteDTO(IngredienteDTO clone){
        super(clone);
        up = null;
        down = new ArrayList<>();
    }
    
//******************************************************************************
//********************************Getters&Setters*******************************
//******************************************************************************
    public void setAllFrom(IngredienteDTO dto){
        setAllFrom((Inventory)dto);
        setUp(dto.getUp());
        setDown(dto.getDown());
    }
    @Override public ArrayList getDown() {
        return down;
    }
    @Override public void setDown(ArrayList down) {
        this.down = down;
    }


//******************************************************************************
//***********************************Atributos**********************************
//******************************************************************************    
    private ArrayList<IngredienteDTO>   down;
    private static DeInventarioType clase = Ingrediente;  
}
