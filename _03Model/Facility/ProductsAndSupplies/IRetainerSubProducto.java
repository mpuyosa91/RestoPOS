/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.ProductsAndSupplies;

import java.util.ArrayList;
import _04DataAccessObject.generalController;

/**
 *
 * @author MoisesE
 */
public interface IRetainerSubProducto {
    public ArrayList<SubProductoDTO> getSubProductoList();
    public void setSubProductoList(ArrayList<SubProductoDTO> subProductoList);
    public void addToSubProductoList(SubProductoDTO subProductoDTO);
    public void deleteFromSubProductoList(SubProductoDTO subProductoDTO);
    
    public static void addToSubProductoList (SubProductoDTO subProductoDTO, ArrayList<SubProductoDTO> subProductoList){
        subProductoList.add(subProductoDTO);
        SubProductoDTO dbAux = (SubProductoDTO) generalController.getProduct(subProductoDTO.getID());
        subProductoDTO.getMeasure().setMeasureBase(dbAux.getMeasure());
        double rate = subProductoDTO.getCantidad()/dbAux.getCantidad();
        IRetainerIngrediente.rateIngredienteList(subProductoDTO, rate);
        IRetainerSubProducto.rateSubProductoList(subProductoDTO, rate);
    }
    
    
    /**
     * Modifica la cantidad de la Lista de SubProductos del objeto dto 
     * @param <DTO> Cualquier DTO que implemente la interfaz IRetainerSubProducto
     * @param dto Data Transfer Object que contiene la Lista de SubProductos a Modificar
     * @param rate Factor de Conversion de toda la Lista de SubProductos
     */
    public static <DTO extends IRetainerSubProducto> void rateSubProductoList(DTO dto, double rate){
        boolean showMesgSys = false;
        ArrayList<SubProductoDTO> dtoList = dto.getSubProductoList();
        for (int i=0; i<dtoList.size(); i++){
            SubProductoDTO dtoAux = dtoList.get(i);
            SubProductoDTO dbDTO = (SubProductoDTO) generalController.getProduct(dtoAux.getID());
            dtoAux.getMeasure().setMeasureBase(dbDTO.getMeasure());
            if (showMesgSys) System.out.print("\n rateSubProductoList().dto:"+dtoAux.getDescription()+"*, rate="+rate);
            dtoAux.setCantidad(dtoAux.getCantidad()*rate);
            if (showMesgSys) System.out.print("\n rateSubProductoList().setCantidad():"+dtoAux.getDescription());
            IRetainerIngrediente.rateIngredienteList(dtoAux, rate);
            IRetainerSubProducto.rateSubProductoList(dtoAux, rate);
        }
    }
    
}
