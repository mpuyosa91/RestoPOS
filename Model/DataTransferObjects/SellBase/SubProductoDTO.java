/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataTransferObjects.SellBase;

import static Model.DataTransferObjects.SellBase.DeInventario.DeInventarioType.SubProducto;
import java.util.ArrayList;
import Model.DataTransferObjects.SellBase.Measure.Measurable;

/**
 *
 * @author MoisesE
 */
public class SubProductoDTO extends DeInventario implements IRetainerIngrediente, IRetainerSubProducto{
    
//******************************************************************************
//********************************Constructores*********************************
//******************************************************************************
    public SubProductoDTO(SubProductoDTO father, int ID){
        super(ID,clase);
        up = father;
        down = new ArrayList<>();
        ingredienteList = new ArrayList<>();
        subProductoList = new ArrayList<>();
    }
    public SubProductoDTO(SubProductoDTO father, int ID, String nombre){
        super(ID,nombre,clase);
        up = father;
        down = new ArrayList<>();
        ingredienteList = new ArrayList<>();
        subProductoList = new ArrayList<>();
    }
    public SubProductoDTO(SubProductoDTO father, int ID, String nombre, double cantidad, Measurable.Type type) {
        super(ID,nombre,cantidad,type,clase);
        up = father;
        down = new ArrayList<>();
        ingredienteList = new ArrayList<>();
        subProductoList = new ArrayList<>();
    }
    public SubProductoDTO(SubProductoDTO clone){
        super(clone);
        up = null;
        down = new ArrayList<>();
        ingredienteList = new ArrayList<>();
        for (int i=0; i<clone.getIngredienteList().size(); i++){
            IngredienteDTO ingredienteDTO = new IngredienteDTO(clone.getIngredienteList().get(i));
            ingredienteList.add(ingredienteDTO);
        }
        subProductoList = new ArrayList<>();
        for (int i=0; i<clone.getSubProductoList().size(); i++){
            SubProductoDTO subProductoDTO = new SubProductoDTO(clone.getSubProductoList().get(i));
            subProductoList.add(subProductoDTO);
        }
    }

//******************************************************************************
//********************************Getters&Setters*******************************
//******************************************************************************    
    public void setAllFrom(SubProductoDTO dto){
        setAllFrom((DeInventario)dto);
        setUp(dto.getUp());
        setDown(dto.getDown());
        setIngredienteList(dto.getIngredienteList());
        setSubProductoList(dto.getSubProductoList());
    }

    @Override public ArrayList getDown() {
        return down;
    }
    @Override public void setDown(ArrayList down) {
        this.down = down;
    }

    @Override public ArrayList<IngredienteDTO> getIngredienteList() {
        return ingredienteList;
    }
    @Override public void setIngredienteList(ArrayList<IngredienteDTO> ingredienteList) {
        this.ingredienteList = ingredienteList;
    }
    @Override public void addToIngredienteList(IngredienteDTO ingredienteDTO) {
        IRetainerIngrediente.addToIngredienteList(ingredienteDTO,ingredienteList);
    }
    
    @Override public ArrayList<SubProductoDTO> getSubProductoList() {
        return subProductoList;
    }
    @Override public void setSubProductoList(ArrayList<SubProductoDTO> subProductoList) {
        this.subProductoList = subProductoList;
    }
    @Override public void addToSubProductoList(SubProductoDTO subProductoDTO) {
        IRetainerSubProducto.addToSubProductoList(subProductoDTO,subProductoList);
    }
    @Override public void deleteFromSubProductoList(SubProductoDTO subProductoDTO) {
        subProductoList.remove(subProductoDTO);
    }
    
    @Override public ArrayList<DeInventario> getComposition(){
        ArrayList<DeInventario> r = new ArrayList<>();
        r.addAll(getIngredienteList());
        r.addAll(getSubProductoList());
        return r;
    }
        
    @Override public String toString(){
        String r = super.toString();
        for (int i=0; i<getComposition().size();i++){
            r += "\n  ";
            r += getComposition().get(i).toString();
        }
        return r;
    } 
   
//******************************************************************************
//***********************************Atributos**********************************
//******************************************************************************    
    private ArrayList<IngredienteDTO> ingredienteList;
    private ArrayList<SubProductoDTO> subProductoList;
    private ArrayList<SubProductoDTO> down;
    private static DeInventarioType clase = SubProducto;

}
