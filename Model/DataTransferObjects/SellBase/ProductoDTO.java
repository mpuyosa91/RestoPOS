/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataTransferObjects.SellBase;

import static Model.DataTransferObjects.SellBase.DeInventario.DeInventarioType.Producto;
import java.util.ArrayList;
import Model.DataTransferObjects.SellBase.Measure.Measurable;

/**
 *
 * @author MoisesE
 */
public class ProductoDTO extends DeInventario implements ISellable, IRetainerIngrediente, IRetainerSubProducto, IRetainerProducto{
    
//******************************************************************************
//********************************Constructores*********************************
//******************************************************************************
    public ProductoDTO(ProductoDTO father, int ID){
        super(ID,clase);
        precio = 0;
        up = father;
        down = new ArrayList<>();
        ingredienteList = new ArrayList<>();
        subProductoList = new ArrayList<>();
        productoList = new ArrayList<>();
    }
    public ProductoDTO(ProductoDTO father, int ID, String nombre){
        super(ID,nombre,clase);
        precio = 0;
        up = father;
        down = new ArrayList<>();
        ingredienteList = new ArrayList<>();
        subProductoList = new ArrayList<>();
        productoList = new ArrayList<>();
    }
    public ProductoDTO(ProductoDTO father, int ID, String nombre, double cantidad, double precio) {
        super(ID,nombre,cantidad,Measurable.Type.Cantidad,clase);
        this.precio = precio;
        up = father;
        down = new ArrayList<>();
        ingredienteList = new ArrayList<>();
        subProductoList = new ArrayList<>();
        productoList = new ArrayList<>();
    }   
    public ProductoDTO(ProductoDTO clone){
        super(clone);
        this.precio = clone.getPrecio();
        up = null;
        down = new ArrayList<>();
        ingredienteList = new ArrayList<>();
        subProductoList = new ArrayList<>();
        productoList = new ArrayList<>();
        for (int i=0; i<clone.getIngredienteList().size(); i++){
            IngredienteDTO ingredienteDTO = new IngredienteDTO(clone.getIngredienteList().get(i));
            ingredienteList.add(ingredienteDTO);
        }
        for (int i=0; i<clone.getSubProductoList().size(); i++){
            SubProductoDTO subProductoDTO = new SubProductoDTO(clone.getSubProductoList().get(i));
            subProductoList.add(subProductoDTO);
        }
        for (int i=0; i<clone.getProductoList().size(); i++){
            ProductoDTO productoDTO = new ProductoDTO(clone.getProductoList().get(i));
            productoList.add(productoDTO);
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
    public void setAllFrom(ProductoDTO dto){
        setAllFrom((DeInventario)dto);
        setUp(dto.getUp());
        setDown(dto.getDown());
        setPrecio(dto.getPrecio());
        setIngredienteList(dto.getIngredienteList());
        setSubProductoList(dto.getSubProductoList());
        setProductoList(dto.getProductoList());
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
    
    @Override public ArrayList<IngredienteDTO> getIngredienteList() {
        return ingredienteList;
    }
    @Override public void setIngredienteList(ArrayList<IngredienteDTO> ingredienteList) {
        this.ingredienteList = ingredienteList;
    }
    @Override public void addToIngredienteList(IngredienteDTO ingredienteDTO) {
        IRetainerIngrediente.addToIngredienteList(ingredienteDTO, ingredienteList);
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
    @Override public void setProductoList(ArrayList<ProductoDTO> vendibleList) {
        this.productoList = vendibleList;
    }
    @Override public void addToProductoList(ProductoDTO productoDTO) {
        IRetainerProducto.addToProductoList(productoDTO, productoList);
    }
    @Override public void deleteFromProductoList(ProductoDTO productoDTO) {
        productoList.remove(productoDTO);
    }
    
    @Override public ArrayList<DeInventario> getComposition(){
        ArrayList<DeInventario> r = new ArrayList<>();
        r.addAll(getIngredienteList());
        r.addAll(getSubProductoList());
        r.addAll(getProductoList());
        return r;
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
    private double                      precio;    
    private ArrayList<IngredienteDTO>   ingredienteList;
    private ArrayList<SubProductoDTO>   subProductoList;
    private ArrayList<ProductoDTO>      productoList;
    private ArrayList<ProductoDTO>      down;
    private static DeInventarioType     clase = Producto;

}
