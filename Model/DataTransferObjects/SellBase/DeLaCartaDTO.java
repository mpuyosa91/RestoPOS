/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataTransferObjects.SellBase;

import static Model.DataTransferObjects.SellBase.DeInventario.DeInventarioType.DeLaCarta;
import java.util.ArrayList;
import Model.DataTransferObjects.SellBase.Measure.Measurable;

/**
 *
 * @author MoisesE
 */
public class DeLaCartaDTO extends DeInventario implements ISellable, IRetainerSubProducto, IRetainerProducto, IRetainerDeLaCarta{
    
//******************************************************************************
//********************************Constructores*********************************
//******************************************************************************
    public DeLaCartaDTO(DeLaCartaDTO father, int ID){
        super(ID,clase);
        precio = 0;
        especial = "";
        up = father;
        down = new ArrayList<>();
        subProductoList = new ArrayList<>();
        productoList = new ArrayList<>();
        deLaCartaList = new ArrayList<>();
    }
    public DeLaCartaDTO(DeLaCartaDTO father, int ID, String nombre){
        super(ID,nombre,clase);
        precio = 0;
        especial = "";
        up = father;
        down = new ArrayList<>();
        subProductoList = new ArrayList<>();
        productoList = new ArrayList<>();
        deLaCartaList = new ArrayList<>();
    }
    public DeLaCartaDTO(DeLaCartaDTO father, int ID, String nombre, double cantidad, double precio) {
        super(ID,nombre,cantidad,Measurable.Type.Cantidad,clase);
        this.precio = precio;
        especial = "";
        up = father;
        down = new ArrayList<>();    
        subProductoList = new ArrayList<>();
        productoList = new ArrayList<>();
        deLaCartaList = new ArrayList<>();
    }
    public DeLaCartaDTO(DeLaCartaDTO clone){
        super(clone);
        this.precio = clone.getPrecio();
        this.especial = clone.getEspecial();
        up = clone.getUp();
        down = new ArrayList<>();
        subProductoList = new ArrayList<>();
        productoList = new ArrayList<>();
        deLaCartaList = new ArrayList<>();
        for (int i=0; i<clone.getSubProductoList().size(); i++){
            SubProductoDTO subProductoDTO = new SubProductoDTO(clone.getSubProductoList().get(i));
            subProductoList.add(subProductoDTO);
        }
        for (int i=0; i<clone.getProductoList().size(); i++){
            ProductoDTO productoDTO = new ProductoDTO(clone.getProductoList().get(i));
            productoList.add(productoDTO);
        }
        for (int i=0; i<clone.getDeLaCartaList().size(); i++){
            DeLaCartaDTO deLaCartaDTO = new DeLaCartaDTO(clone.getDeLaCartaList().get(i));
            deLaCartaList.add(deLaCartaDTO);
        }
    }

//******************************************************************************
//****************************Metodos Independientes****************************
//******************************************************************************
    @Override
    public String getDescription(){
        String r;
        if (isFinal())
            r = "<"+getID()+"> "+getNombre() +" $"+ String.valueOf((int) getPrecio());
        else
            r = "<" + getID()+"> "+getNombre()+":";
        return r;
    }
//******************************************************************************
//********************************Getters&Setters*******************************
//******************************************************************************
    public void setAllFrom(DeLaCartaDTO dto){
        setAllFrom((DeInventario)dto);
        setUp(dto.getUp());
        setDown(dto.getDown());
        setPrecio(dto.getPrecio());
        setSubProductoList(dto.getSubProductoList());
        setProductoList(dto.getProductoList());
        setDeLaCartaList(dto.getDeLaCartaList());
    }
    
    @Override public ArrayList getDown() {
        return down;
    }
    @Override public void setDown(ArrayList down) {
        this.down = down;
    }

    @Override public double getPrecio() {
        return precio;
    }
    @Override public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    @Override public ArrayList<SubProductoDTO> getSubProductoList() {
        return subProductoList;
    }
    @Override public void setSubProductoList(ArrayList<SubProductoDTO> subProductoList) {
        this.subProductoList = subProductoList;
    }
    @Override public void addToSubProductoList(SubProductoDTO subProductoDTO) {
        IRetainerSubProducto.addToSubProductoList(subProductoDTO, subProductoList);
    }
    @Override public void deleteFromSubProductoList(SubProductoDTO subProductoDTO) {
        subProductoList.remove(subProductoDTO);
    }
    
    @Override public ArrayList<ProductoDTO> getProductoList() {
        return productoList;
    }
    @Override public void setProductoList(ArrayList<ProductoDTO> productoList) {
        this.productoList = productoList;
    }
    @Override public void addToProductoList(ProductoDTO productoDTO) {
        IRetainerProducto.addToProductoList(productoDTO, productoList);
    }
    @Override public void deleteFromProductoList(ProductoDTO productoDTO) {
        productoList.remove(productoDTO);
    }
    
    @Override public ArrayList<DeLaCartaDTO> getDeLaCartaList() {
        return deLaCartaList;
    }
    @Override public void setDeLaCartaList(ArrayList<DeLaCartaDTO> deLaCartaList) {
        this.deLaCartaList = deLaCartaList;
    }
    @Override public void addToDeLaCartaList(DeLaCartaDTO deLaCartaDTO) {
        IRetainerDeLaCarta.addToDeLaCartaList(deLaCartaDTO, deLaCartaList);
    }
    
    @Override public ArrayList<DeInventario> getComposition(){
        ArrayList<DeInventario> r = new ArrayList<>();
        r.addAll(getSubProductoList());
        r.addAll(getProductoList());
        r.addAll(getDeLaCartaList());
        return r;
    }
    
    public String getEspecial() {
        return especial;
    }
    public void setEspecial(String especial) {
        this.especial = especial;
    }
    
    @Override public String toString(){
        String r = super.toString()+ " " + precio;
        for (int i=0; i<getComposition().size();i++){
            r += "\n  ";
            r += getComposition().get(i).toString();
        }
        return r;
    }

//******************************************************************************
//***********************************Atributos**********************************
//******************************************************************************      
    private String                  especial;
    private double                  precio;
    private ArrayList<SubProductoDTO>   subProductoList;
    private ArrayList<ProductoDTO>  productoList;
    private ArrayList<DeLaCartaDTO> deLaCartaList;
    private ArrayList<DeLaCartaDTO> down;
    private static DeInventarioType clase = DeLaCarta;

}
