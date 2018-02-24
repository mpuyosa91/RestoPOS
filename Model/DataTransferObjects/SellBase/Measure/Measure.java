/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataTransferObjects.SellBase.Measure;

import View.WindowConsole;
import java.util.ArrayList;

/**
 *
 * @author MoisesE
 */
public class Measure implements Measurable {

    public Measure(double quantity, Type defaultType){
        this.density = 1;
        this.type = defaultType;
        this.measureBase = null;
        this.rate = quantity;
    }
    public Measure(double quantity, Type defaultType, Measure measureBase){
        this.density = 1;
        this.type = defaultType;
        this.measureBase = measureBase;
        this.rate = quantity/measureBase.getQuantity();
    }
    public Measure(Measurable clone){
        this.density = clone.getDensity();
        this.type = clone.getType();
        this.measureBase = clone.getMeasureBase();
        if (clone.getMeasureBase()!=null)
            this.rate = clone.getQuantity()/clone.getMeasureBase().getQuantity();
        else
            this.rate = clone.getQuantity();
    }

    @Override public int getFixedPos(){
        int r=0;
        switch (type){
            case Peso:
                for (int i=0; i<MassUnits.values().length; i++){
                    if (this.getQuantity()>=MassUnits.values()[i].getValue()){ r = i; }
                }
                break;
            case Volumen:
                for (int i=0; i<VolumeUnits.values().length; i++){
                    if (this.getQuantity()>=VolumeUnits.values()[i].getValue()){ r = i; }
                }
                break;
            case Cantidad:
                for (int i=0; i<UnitsUnits.values().length; i++){
                    if (this.getQuantity()>=UnitsUnits.values()[i].getValue()){ r = i; }
                }
                break;
        }
        return r;
    }
    
    @Override public Type getType() {
        return type;
    }
    @Override public String getUnits() {
        String r="";
        switch (getType()){
            case Peso:
                r = MassUnits.values()[0].toString();
            break;
            case Volumen:
                r = VolumeUnits.values()[0].toString();
            break;
            case Cantidad:
                r = UnitsUnits.values()[0].toString();
            break;
        }
        return r;
    }
    @Override public double getQuantity() {   
        if (measureBase!=null)
            return rate*measureBase.getQuantity();
        else
            return rate;
    }
    @Override public double getQuantity(String unit){
        double r = 0;
        switch (type){
            case Peso:
                for (MassUnits value : MassUnits.values()) {
                    if (unit.equals(value.toString())) {
                        r = getQuantity()/Measurable.getFixedMultByName(value.toString());
                }   }
            break;
            case Volumen:
                for (VolumeUnits value : VolumeUnits.values()) {
                    if (unit.equals(value.toString())) {
                        r = getQuantity()/Measurable.getFixedMultByName(value.toString());
                }   }
            break;
            case Cantidad:
                for (UnitsUnits value : UnitsUnits.values()) {
                    if (unit.equals(value.toString())) {
                        r = getQuantity()/Measurable.getFixedMultByName(value.toString());
                }   }
            break;
        }
        return r;
    }
    @Override public String getFixedUnits(){
        String r = "";
        switch (type){
            case Peso:
                r = MassUnits.values()[getFixedPos()].toString();
                break;
            case Volumen:
                r = VolumeUnits.values()[getFixedPos()].toString();
                break;
            case Cantidad:
                r = UnitsUnits.values()[getFixedPos()].toString();
                break;
        }
        return r;
    }
    @Override public double getFixedQuantity(){
        double r = 0;
        switch (type){
            case Peso:
                r = this.getQuantity() / MassUnits.values()[getFixedPos()].getValue();
                break;
            case Volumen:
                r = this.getQuantity() / VolumeUnits.values()[getFixedPos()].getValue();
                break;
            case Cantidad:
                r = this.getQuantity() / UnitsUnits.values()[getFixedPos()].getValue();
                break;
        }
        return r;
    }
    @Override public double getDensity(){
        return density;
    }
    @Override public Measure getMeasureBase(){
        return measureBase;
    }
    
    @Override public void setDefaultType(Type type){
        this.type=type;
    }
    @Override public void setQuantity(double quantity){
        if (measureBase!=null)
            this.rate = quantity/getMeasureBase().getQuantity();
        else
            this.rate = quantity;
    }
    @Override public void setQuantity(double quantity,String unidad){
        for (Type type_aux: Type.values()){
            switch (type_aux){
                case Peso:
                    for (MassUnits value : MassUnits.values()) {
                        if (unidad.equals(value.toString())) {
                            setQuantity(quantity*Measurable.getFixedMultByName(unidad));
                            setDefaultType(Type.Peso);
                    }   }
                break;
                case Volumen:
                    for (VolumeUnits value : VolumeUnits.values()) {
                        if (unidad.equals(value.toString())) {
                            setQuantity(quantity*Measurable.getFixedMultByName(unidad));
                            setDefaultType(Type.Volumen);
                    }   }
                break;
                case Cantidad:
                    for (UnitsUnits value : UnitsUnits.values()) {
                        if (unidad.equals(value.toString())) {
                            setQuantity(quantity*Measurable.getFixedMultByName(unidad));
                            setDefaultType(Type.Cantidad);
                    }   }
                break;
            }
        }
    }
    @Override public void setDensity(double density, MassUnits mUnit, VolumeUnits vUnit) {
        this.density = density; //D = m/V
        switch (mUnit) {
            case Libras:
                this.density*=453.592; break;
            case Kilogramos:
                this.density*=1000; break;
            case Gramos:
                this.density*=1; break;
        }
        switch (vUnit) {
            case Litros:
                this.density /= 1000; break;
            case Mililitros:
                this.density /= 1; break;
            case Onzas:
                this.density /= 29.5735; break;
            case Galones:
                this.density /= 3785.41; break;
        }   
    }
    @Override public void setMeasureBase(Measure measureBase){
        rate = getQuantity()/measureBase.getQuantity();
        this.measureBase = measureBase;
    }
           
    @Override public String getString() {
        String r = "";
        switch (type){
            case Peso:
                if (getQuantity()>1000) 
                    r += " ("+String.format("%.001f",getQuantity()/1000.0)+" "+
                            MassUnits.Kilogramos.name()+")";
                else
                    r += " ("+String.format("%.001f",getQuantity())+" "+
                            MassUnits.Gramos.name()+")";
                break;
            case Volumen:
                if (getQuantity()>3785.41)
                    r += " ("+String.format("%.001f",getQuantity()/3785.41)+" "+
                            VolumeUnits.Galones.name()+")";
                else if (getQuantity()>1000)
                    r += " ("+String.format("%.001f",getQuantity()/1000)+" "+
                            VolumeUnits.Litros.name()+")";
                else
                    r += " ("+String.format("%.001f",getQuantity())+" "+
                            VolumeUnits.Mililitros.name()+")";
                break;
            case Cantidad:
                if (getQuantity()>12)
                    r += " ("+String.format("%.001f",getQuantity()/12)+" "+
                            UnitsUnits.Docenas.name()+")";
                else
                    r += " ("+String.format("%.001f",getQuantity())+" "+
                            UnitsUnits.Unidades.name()+")";
                break;
        }
        return r;
    }

    private double  rate;
    private Measure measureBase;
    private double  density;
    private Type    type;     
    
}
