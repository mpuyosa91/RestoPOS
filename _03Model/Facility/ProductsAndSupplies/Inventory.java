/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.ProductsAndSupplies;

import _03Model.Facility.ProductsAndSupplies.Measure.Measure;
import _03Model.Facility.ProductsAndSupplies.Measure.Measurable;
import java.util.ArrayList;

/**
 *
 * @author MoisesE
 */
public class Inventory implements IInventariable{
    
    public static enum DeInventarioType{
        Ingrediente,
        SubProducto,
        Producto,
        DeLaCarta,
    }
    
//******************************************************************************
//********************************Constructores*********************************
//******************************************************************************
    public Inventory(int ID, DeInventarioType clase){
        this.ID = ID;
        this.nombre = "NoEstablecido";
        this.measure = new Measure(0,Measurable.Type.Cantidad);
        this.isFinal = ID > 10;
        this.clase = clase;
    }
    public Inventory(int ID, String nombre, DeInventarioType clase){
        this.ID = ID;
        this.nombre = nombre;
        this.measure = new Measure(0,Measurable.Type.Cantidad);
        this.isFinal = ID > 10;
        this.clase = clase;
    }
    public Inventory(int ID, String nombre, double cantidad, Measurable.Type type, DeInventarioType clase){
        this.ID = ID;
        this.nombre = nombre;
        this.measure = new Measure(cantidad,type);
        this.isFinal = ID > 10;
        this.clase = clase;
    }
    public Inventory(Inventory clone){
        this.ID = clone.getID();
        this.nombre = clone.getNombre();
        this.measure = new Measure(clone.getMeasure());
        this.isFinal = clone.isFinal();
        this.clase = clone.getClase();
        this.up = clone.getUp();
    }
//******************************************************************************
//****************************Metodos Independientes****************************
//******************************************************************************
    public String getDescription(){
        String r;
        if (isFinal())
            r = "<"+getID()+"> "+getNombre() +" "+  measure.getString();            
        else
            r = "<" + getID()+"> "+getNombre()+":";
        return r;
    }
    public String getComboString(){
        String r="";
        if (isFinal())
            r = +getID()+" "+getNombre();
        return r;
    }
//******************************************************************************
//********************************Getters&Setters*******************************
//******************************************************************************
    @Override public void setAllFrom(Inventory dto){
        this.setID(dto.getID());
        this.setNombre(dto.getNombre());
        this.setMeasurable(new Measure(dto.getMeasure()));
        this.setFinal(dto.isFinal());
    }
    @Override public int getID() {
        return ID;
    }
    @Override public void setID(int ID) {
        this.ID = ID;
    }
    @Override public String getNombre() {
        return nombre;
    }
    @Override public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    @Override public double getPrecio() {
        return 0.0;
    }
    @Override public double getCantidadFixed() {
        return measure.getFixedQuantity();
    }
    @Override public double getCantidad(String unidad) {
        return measure.getQuantity(unidad);
    }
    @Override public double getCantidad() {
        return measure.getQuantity();
    }
    @Override public void setCantidad(double cantidad){
        setCantidad(cantidad,this.getUnidadBase());
    }
    @Override public void setCantidad(double cantidad, String unidad){
        measure.setQuantity(cantidad,unidad);
    }
    @Override public String getUnidadMedida() {
        return measure.getFixedUnits();
    }    
    @Override public String getUnidadBase() {
        return measure.getUnits();
    }    
    @Override public boolean isFinal() {
        return isFinal;
    }
    @Override public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    @Override public void setMeasurable(Measure measure) {
        this.measure = measure;
    }
    @Override public Inventory getUp() {
        return this.up;
    }
    @Override public <G extends IInventariable> void setUp(G up) {
        this.up = (Inventory) up;
    }
    @Override public ArrayList<Inventory> getDown() {
        return this.down;
    }
    @Override public void setDown(ArrayList down) {
        this.down = down;
    }
    @Override public ArrayList<Inventory> getComposition(){
        return null;
    }
    @Override public Measure getMeasure() {
        return measure;
    }    
    public <G extends Inventory> ArrayList<G> treeToList(){
        ArrayList<G> r = new ArrayList<>();
        r = IInventariable.treeToList((G)this,r);
        return r;
    }
    public <G extends Inventory> String treeToString(){
        return IInventariable.treeToString(this);
    }
    public DeInventarioType getClase(){
        return clase;
    }
    @Override public String toString(){
        String r="";
        try{ r += "<Father: "+up.getID()+"> "; }
        catch(Exception e){r += "<Father: "+null+"> ";}
        r += String.valueOf(ID) + " ";
        r += nombre + " ";
        r += measure.getString() + " ";
        r += clase.name() + " ";
        r += isFinal();
        return r;
    }
        
//******************************************************************************
//***********************************Atributos**********************************
//******************************************************************************        
    private int                         ID;
    private String                      nombre;
    private Measure                     measure;
    protected boolean                   isFinal;
    private final DeInventarioType      clase;
    protected Inventory              up;
    private ArrayList<Inventory>     down;

}
