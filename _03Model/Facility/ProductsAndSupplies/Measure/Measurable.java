/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.ProductsAndSupplies.Measure;

import javax.swing.JComboBox;

/**
 *
 * @author MoisesE
 */
public interface Measurable {
        
    public static enum Type     { Peso      , Volumen       , Cantidad  }
    //public static enum Units    { MassUnits , VolumeUnits   , UnitsUnits}
    
    public static enum UnitsUnits        { 
        Unidades("Unid",1), Docenas("Docen",12);
        
        private final String shortName; private final double value;
        UnitsUnits(String short_n,double v){ shortName = short_n; value = v; }
        double getValue(){ return value; }
        String getShort(){ return shortName; }
    }
    public static enum MassUnits        { 
        Gramos("gr.",1), Libras("Lbr.",500), Kilogramos("Kg.",1000);
        
        private final String shortName; private final double value;
        MassUnits(String short_n,double v){ shortName = short_n; value = v; }
        double getValue(){ return value; }
        String getShort(){ return shortName; }
    }
    public static enum VolumeUnits        { 
        Mililitros("ml.",1), Cucharadas("cdas.",14.787), Onzas("oz.",29.5735), Litros("Ltrs.",1000), Galones("Gal.",3785.41);
        
        private final String shortName; private final double value;
        VolumeUnits(String short_n,double v){ shortName = short_n; value = v; }
        double getValue(){ return value; }
        String getShort(){ return shortName; }
    }
    
    public Type     getType();                   //Masa o Peso
    public static String getShortUnits(UnitsUnits unit){
        String r="";
        r = UnitsUnits.values()[unit.ordinal()].getShort();
        return r;
    }
    public static String getShortUnits(MassUnits unit){
        String r="";
        r = MassUnits.values()[unit.ordinal()].getShort();
        return r;
    } 
    public static String getShortUnits(VolumeUnits unit){
        String r="";
        r = VolumeUnits.values()[unit.ordinal()].getShort();
        return r;
    }
    public int      getFixedPos();
    public double   getQuantity();
    public double   getQuantity(String unit);
    public String   getUnits();
    public double   getFixedQuantity();
    public String   getFixedUnits();
    public String   getString();
    public double   getDensity();
    public Measure  getMeasureBase();
    
    public void setDefaultType(Type type);
    public void setQuantity(double quantity);
    public void setQuantity(double quantity, String unidad);
    public void setDensity(double density, MassUnits mUnit, VolumeUnits vUnit);  
    public void setMeasureBase(Measure measureBase);
        
    public static double getFixedMultByName(String name){
        double r=0;
        for (Type value: Type.values()){
            switch (value){
                case Peso:
                    for (MassUnits value1 : MassUnits.values()) {
                        if (name.equals(value1.toString())) {
                            r = value1.value;
                        }
                    }
                break;
                case Volumen:
                    for (VolumeUnits value1 : VolumeUnits.values()) {
                        if (name.equals(value1.toString())) {
                            r = value1.value;
                        }
                    }
                break;
                case Cantidad:
                    for (UnitsUnits value1 : UnitsUnits.values()) {
                        if (name.equals(value1.toString())) {
                            r = value1.value;
                        }
                    }
                break;
            }
        }
        return r;
    }
    public static void configureTypeComboBox(JComboBox comboBox){
        comboBox.removeAllItems();
        for (Type value : Type.values()) {
            comboBox.addItem(value.name());
        }
    }
    public static void configureUnitComboBox(Type type, JComboBox comboBox){
        comboBox.removeAllItems();
        switch (type){
            case Peso:
                for (MassUnits value : MassUnits.values()) {
                    comboBox.addItem(value.name());
                }
            break;
            case Volumen:
                for (VolumeUnits value : VolumeUnits.values()) {
                    comboBox.addItem(value.name());
                }
            break;
            case Cantidad:
                for (UnitsUnits value : UnitsUnits.values()) {
                    comboBox.addItem(value.name());
                }
            break;
        }
    }
}