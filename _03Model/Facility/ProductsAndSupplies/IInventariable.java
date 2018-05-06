/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _03Model.Facility.ProductsAndSupplies;

import _03Model.Facility.ProductsAndSupplies.Measure.Measure;
import _03Model.Facility.Accounting.Printer.PointOfServicePrinter;
import java.util.ArrayList;


/**
 *
 * @author MoisesE
 */
public interface IInventariable {
    
    int          getID();
    String       getNombre();
    double       getPrecio();
    String       getUnidadMedida();
    String       getUnidadBase();
    double       getCantidadFixed();
    double       getCantidad(String unidad);
    double       getCantidad();
    Measure      getMeasure();
    boolean      isFinal();
    Inventory getUp();
    ArrayList                getDown();
    ArrayList<Inventory>  getComposition();
    
    void         setAllFrom(Inventory dto);
    void         setID(int ID);   
    void         setNombre(String nombre);
    void         setCantidad(double cantidad);
    void         setCantidad(double cantidad, String unidad);
    void         setMeasurable(Measure measure);
    void         setFinal(boolean b);
    <G extends IInventariable> void setUp(G up);
    void         setDown(ArrayList down);
    
    static <G extends IInventariable, Inventory> ArrayList<G> treeToList(G dto, ArrayList<G> list){
        ArrayList<G> childs = (ArrayList<G>) dto.getDown();
        for(G child: childs){
            if (child.isFinal())    list.add(child);
            else                    treeToList(child,list);
        }
        return list;
    }

    static String spaces(int spcs){
        String r = "";
        for (int i=0; i<spcs; i++)
            r = r + " ";
        return r;
    }
    static <G extends Inventory> String treeToString(G dto){
        String r = "";
        switch(dto.getClase()){
            case Ingrediente:
                IngredienteDTO dto1 = (IngredienteDTO) dto;
                r = treeToString(dto1,"",0);
                break;
            case SubProducto:
                SubProductoDTO dto2 = (SubProductoDTO) dto;
                r = treeToString(dto2,"",0);
                break;
            case Producto:
                ProductoDTO dto3 = (ProductoDTO) dto;
                r = treeToString(dto3,"",0);
                break;
            case DeLaCarta:
                DeLaCartaDTO dto4 = (DeLaCartaDTO) dto;
                r = treeToString(dto4,"",0);
                break;
        }
        return r;
    }
    static String treeToString(IngredienteDTO tree, String r, int lvl){
        r = r +"\n"+ spaces(lvl)+ tree.getDescription();
        for (int i=0; i<tree.getDown().size(); i++){
            IngredienteDTO aux = (IngredienteDTO) tree.getDown().get(i); 
            r = treeToString(aux,r,String.valueOf(tree.getID()).length()+3+lvl); 
        }
        return r;
    }
    static String treeToString(SubProductoDTO tree, String r, int lvl){
        r = r +"\n"+ spaces(lvl)+ tree.getDescription();
        if (tree.isFinal()){
            for (int i=0; i<tree.getIngredienteList().size(); i++)
                r = treeToString(tree.getIngredienteList().get(i),r,String.valueOf(tree.getID()).length()+3+lvl);
            for (int i=0; i<tree.getSubProductoList().size(); i++)
                r = treeToString(tree.getSubProductoList().get(i),r,String.valueOf(tree.getID()).length()+3+lvl);
        }
        for (int i=0; i<tree.getDown().size(); i++){
            SubProductoDTO aux = (SubProductoDTO) tree.getDown().get(i); 
            r = treeToString(aux,r,String.valueOf(tree.getID()).length()+3+lvl);}
        return r;
    }
    static String treeToString(ProductoDTO tree, String r,int lvl){
        r = r +"\n"+ spaces(lvl)+ tree.getDescription();
        if (tree.isFinal()){
            for (int i=0; i<tree.getIngredienteList().size(); i++)
                r = treeToString(tree.getIngredienteList().get(i),r,String.valueOf(tree.getID()).length()+3+lvl);
            for (int i=0; i<tree.getSubProductoList().size(); i++)
                r = treeToString(tree.getSubProductoList().get(i),r,String.valueOf(tree.getID()).length()+3+lvl);
            for (int i=0; i<tree.getProductoList().size(); i++)
                r = treeToString(tree.getProductoList().get(i),r,String.valueOf(tree.getID()).length()+3+lvl);
        }
        for (int i=0; i<tree.getDown().size(); i++){
            ProductoDTO aux = (ProductoDTO) tree.getDown().get(i); 
            r = treeToString(aux,r,String.valueOf(tree.getID()).length()+3+lvl);}
        return r;
    }
    static String treeToString(DeLaCartaDTO tree, String r, int lvl){
        r = r +"\n"+ spaces(lvl)+ tree.getDescription();
        if (tree.isFinal()){
            for (int i=0; i<tree.getSubProductoList().size(); i++)
                r = treeToString(tree.getSubProductoList().get(i),r,String.valueOf(tree.getID()).length()+3+lvl);
            for (int i=0; i<tree.getProductoList().size(); i++)
                r = treeToString(tree.getProductoList().get(i),r,String.valueOf(tree.getID()).length()+3+lvl);
            for (int i=0; i<tree.getDeLaCartaList().size(); i++)
                r = treeToString(tree.getDeLaCartaList().get(i),r,String.valueOf(tree.getID()).length()+3+lvl);
        }
        for (int i=0; i<tree.getDown().size(); i++){
            DeLaCartaDTO aux = (DeLaCartaDTO) tree.getDown().get(i); 
            r = treeToString(aux,r,String.valueOf(tree.getID()).length()+3+lvl);}
        return r;
    }
        
    static <G extends IInventariable> void toThermalPrinter(G dto){
        PRINTER.resetAll();
        PRINTER.initialize();
        PRINTER.feedBack((byte)2);
        PRINTER.setFont(4, true);
        PRINTER.setTextCenter("The Panera");                PRINTER.newLine();
        PRINTER.setTextCenter("Bakery and Food");           PRINTER.newLine();
        PRINTER.setFont(2, false);
        PRINTER.setTextCenter("REGIMEN SIMPLIFICADO");     PRINTER.newLine();
        PRINTER.setTextCenter("NIT: 15.444.730-9");         PRINTER.newLine();
        PRINTER.setTextCenter("Direccion: Cr.81 #43-19 Local 108");
        PRINTER.newLine();
        PRINTER.setTextCenter("Rionegro, Antioquia");       PRINTER.newLine();
        PRINTER.setTextCenter("Domicilios: 562 9979");      PRINTER.newLine();
        PRINTER.setTextCenter(" - Codigos de Producto - "); PRINTER.newLine();
        PRINTER.addLineSeperator();                         PRINTER.newLine();
        PRINTER.setTextLeft("Nro.     Item                         Precio");
        PRINTER.newLine();
        PRINTER.addLineSeperator();                         PRINTER.newLine();
        ArrayList<G> productList = new ArrayList<>();
        productList = IInventariable.treeToList(dto, productList);
        StringBuilder spaces;
        for (G product : productList) {

            String ID = String.valueOf(product.getID());
            String nombre = product.getNombre();
            String precio = String.valueOf((int) product.getPrecio());

            spaces = new StringBuilder();
            for (int j = 0; j < 9 - ID.length(); j++) {
                spaces.append(" ");
            }
            ID = spaces.toString().concat(ID + " ");

            if (nombre.length() < 29) {
                spaces = new StringBuilder();
                for (int j = 0; j < 29 - nombre.length(); j++) {
                    spaces.append(" ");
                }
            }
            else nombre = product.getNombre().substring(0, 28) + " ";
            nombre = nombre.concat(spaces.toString());

            spaces = new StringBuilder();
            for (int j = 0; j < 6 - precio.length(); j++) {
                spaces.append(" ");
            }
            precio = spaces.toString().concat(precio);

            PRINTER.setTextLeft(ID + nombre + precio);
            PRINTER.newLine();
        }
        PRINTER.addLineSeperator();                         PRINTER.newLine();
        PRINTER.feed((byte)3);
        PRINTER.feedAndCut();
        PRINTER.printAll();
    }
    PointOfServicePrinter PRINTER = new PointOfServicePrinter();

}
