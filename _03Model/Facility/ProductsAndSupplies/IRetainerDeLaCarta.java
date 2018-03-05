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
public interface IRetainerDeLaCarta {
    public ArrayList<DeLaCartaDTO> getDeLaCartaList();
    public void setDeLaCartaList(ArrayList<DeLaCartaDTO> deLaCartaList);
    public void addToDeLaCartaList(DeLaCartaDTO deLaCartaDTO);
    
    public static void addToDeLaCartaList (DeLaCartaDTO newDTO, ArrayList<DeLaCartaDTO> deLaCartaList){
        deLaCartaList.add(newDTO);
        DeLaCartaDTO dbAux = (DeLaCartaDTO) generalController.getProduct(newDTO.getID());
        double rate = newDTO.getCantidad()/dbAux.getCantidad();
        IRetainerProducto.rateProductoList(newDTO, rate);
        IRetainerDeLaCarta.rateDeLaCartaList(newDTO, rate);
    }
    
    /**
     * Modifica la cantidad de la Lista de DeLaCarta del objeto dto 
     * @param <DTO> Cualquier DTO que implemente la interfaz IRetainerDeLaCarta
     * @param dto Data Transfer Object que contiene la Lista de DeLaCarta a Modificar
     * @param rate Factor de Conversion de toda la Lista de DeLaCarta
     */
    public static <DTO extends IRetainerDeLaCarta> void rateDeLaCartaList(DTO dto, double rate){
        boolean showMesgSys = false;
        ArrayList<DeLaCartaDTO> dtoList = dto.getDeLaCartaList();
        for (int i=0; i<dtoList.size(); i++){
            DeLaCartaDTO dtoAux = dtoList.get(i);
            DeLaCartaDTO dbDTO = (DeLaCartaDTO) generalController.getProduct(dtoAux.getID());
            dtoAux.getMeasure().setMeasureBase(dbDTO.getMeasure());
            if (showMesgSys) System.out.print("\n rateDeLaCartaList().dto:"+dtoAux.getDescription()+"*, rate="+rate);
            dtoAux.setCantidad(dtoAux.getCantidad()*rate);
            if (showMesgSys) System.out.print("\n rateDeLaCartaList().setCantidad():"+dtoAux.getDescription());
            IRetainerProducto.rateProductoList(dtoAux, rate);
            IRetainerDeLaCarta.rateDeLaCartaList(dtoAux, rate);
        }
    }
}
