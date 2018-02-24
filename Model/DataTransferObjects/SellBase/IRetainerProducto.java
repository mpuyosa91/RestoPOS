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
public interface IRetainerProducto {
    public ArrayList<ProductoDTO> getProductoList();
    public void setProductoList(ArrayList<ProductoDTO> vendibleList);
    public void addToProductoList(ProductoDTO productoDTO);
    public void deleteFromProductoList(ProductoDTO productoDTO);
    
    public static void addToProductoList (ProductoDTO newDTO, ArrayList<ProductoDTO> productoList){
        productoList.add(newDTO);
        ProductoDTO dbAux = (ProductoDTO) IDataBase.searchByIdInDB(newDTO.getID());
        newDTO.getMeasure().setMeasureBase(dbAux.getMeasure());
        double rate = newDTO.getCantidad()/dbAux.getCantidad();
        IRetainerIngrediente.rateIngredienteList(newDTO, rate);
        IRetainerSubProducto.rateSubProductoList(newDTO, rate);
        IRetainerProducto.rateProductoList(newDTO, rate);
    }
        
    /**
     * Modifica la cantidad de la Lista de Productos del objeto dto 
     * @param <DTO> Cualquier DTO que implemente la interfaz IRetainerProducto
     * @param dto Data Transfer Object que contiene la Lista de Productos a Modificar
     * @param rate Factor de Conversion de toda la Lista de Productos
     */
    public static <DTO extends IRetainerProducto> void rateProductoList(DTO dto, double rate){
        boolean showMesgSys = false;
        ArrayList<ProductoDTO> dtoList = dto.getProductoList();
        for (int i=0; i<dtoList.size(); i++){
            ProductoDTO dtoAux = dtoList.get(i);
            ProductoDTO dbDTO = (ProductoDTO) IDataBase.searchByIdInDB(dtoAux.getID());
            dtoAux.getMeasure().setMeasureBase(dbDTO.getMeasure());
            if (showMesgSys) System.out.print("\n rateProductoList().dto:"+dtoAux.getDescription()+"*, rate="+rate);
            dtoAux.setCantidad(dtoAux.getCantidad()*rate);
            if (showMesgSys) System.out.print("\n rateProductoList().setCantidad():"+dtoAux.getDescription());
            IRetainerIngrediente.rateIngredienteList(dtoAux, rate);
            IRetainerSubProducto.rateSubProductoList(dtoAux, rate);
            IRetainerProducto.rateProductoList(dtoAux, rate);
        }
    }
    
}
