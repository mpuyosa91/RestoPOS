/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataTransferObjects.SellBase;

import Controller.DataBase.IDataBase;
import java.util.ArrayList;

/**
 *
 * @author MoisesE
 */
public interface IRetainerIngrediente {
    public int          getID();
    public ArrayList<IngredienteDTO> getIngredienteList();
    public void setIngredienteList(ArrayList<IngredienteDTO> ingredienteList);
    public void addToIngredienteList(IngredienteDTO ingredienteDTO);
       
    public static void addToIngredienteList (IngredienteDTO ingredienteDTO, ArrayList<IngredienteDTO> ingredienteList){
        ingredienteList.add(ingredienteDTO);
        IngredienteDTO dbDTO = (IngredienteDTO) IDataBase.searchByIdInDB(ingredienteDTO.getID());
        ingredienteDTO.getMeasure().setMeasureBase(dbDTO.getMeasure());
    }
    
    /**
     * Modifica la cantidad de la Lista de Ingredientes del objeto dto 
     * @param <DTO> Cualquier DTO que implemente la interfaz IRetainerIngrediente
     * @param dto Data Transfer Object que contiene la Lista de Ingredientes a Modificar
     * @param rate Factor de Conversion de toda la Lista de Ingredientes
     */
    public static <DTO extends IRetainerIngrediente> void rateIngredienteList(DTO dto, double rate){
        boolean showMesgSys = false;
        ArrayList<IngredienteDTO> ingredienteList = dto.getIngredienteList();
        for (int i=0; i<ingredienteList.size(); i++){
            IngredienteDTO dtoAux = ingredienteList.get(i);            
            DTO dbDTO1 = (DTO) IDataBase.searchByIdInDB(dto.getID());
            IngredienteDTO dbDTO = dbDTO1.getIngredienteList().get(i);
            dtoAux.getMeasure().setMeasureBase(dbDTO.getMeasure());
            if (showMesgSys) System.out.print("\n rateIngredienteList().dto:"+dtoAux.getDescription()+"*, rate="+rate);
            dtoAux.setCantidad(dtoAux.getCantidad()*rate);
            if (showMesgSys) System.out.print("\n rateIngredienteList().setCantidad():"+dtoAux.getDescription());
        }
    }
}
