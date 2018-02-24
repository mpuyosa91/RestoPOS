/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataTransferObjects.SellBase;

import static Model.DataTransferObjects.SellBase.DeInventario.DeInventarioType.Ingrediente;
import java.util.ArrayList;
import Model.DataTransferObjects.SellBase.Measure.Measurable;

/**
 *
 * @author MoisesE
 */
public class IngredienteDTO extends DeInventario{
    
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
        setAllFrom((DeInventario)dto);
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
