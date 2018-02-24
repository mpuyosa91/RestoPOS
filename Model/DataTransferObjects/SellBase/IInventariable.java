/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataTransferObjects.SellBase;

import Model.DataTransferObjects.SellBase.Measure.Measure;
import Model.Printer.PrinterOptions;
import java.util.ArrayList;


/**
 *
 * @author MoisesE
 */
public interface IInventariable {
    
    public int          getID();
    public String       getNombre();
    public double       getPrecio();
    public String       getUnidadMedida();
    public String       getUnidadBase();
    public double       getCantidadFixed();
    public double       getCantidad(String unidad);
    public double       getCantidad();
    public Measure      getMeasure();
    public boolean      isFinal();
    public DeInventario getUp();
    public ArrayList                getDown();
    public ArrayList<DeInventario>  getComposition();
    
    public void         setAllFrom(DeInventario dto);
    public void         setID(int ID);   
    public void         setNombre(String nombre);
    public void         setCantidad(double cantidad);
    public void         setCantidad(double cantidad, String unidad);
    public void         setMeasurable(Measure measure);
    public void         setFinal(boolean b);
    public <G extends IInventariable> void setUp(G up);
    public void         setDown(ArrayList down);
    
    public static <G extends IInventariable, DeInventario> ArrayList<G> treeToList(G dto, ArrayList<G> list){
        ArrayList<G> aux;
        for (int i=0; i<dto.getDown().size(); i++){
            aux = (ArrayList<G>) dto.getDown();
            if (aux.get(i).isFinal()) list.add(aux.get(i));
            else                      treeToList(aux.get(i),list);}
        return list;
    }
    
    public static String spaces(int spcs){
        String r = "";
        for (int i=0; i<spcs; i++)
            r = r + " ";
        return r;
    }
    public static <G extends DeInventario> String treeToString(G dto){
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
    public static String treeToString(IngredienteDTO tree, String r, int lvl){
        r = r +"\n"+ spaces(lvl)+ tree.getDescription();
        for (int i=0; i<tree.getDown().size(); i++){
            IngredienteDTO aux = (IngredienteDTO) tree.getDown().get(i); 
            r = treeToString(aux,r,String.valueOf(tree.getID()).length()+3+lvl); 
        }
        return r;
    }
    public static String treeToString(SubProductoDTO tree, String r, int lvl){
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
    public static String treeToString(ProductoDTO tree, String r,int lvl){
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
    public static String treeToString(DeLaCartaDTO tree, String r, int lvl){
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
        
    public static <G extends IInventariable> void toThermalPrinter(G dto){
        PRINTEROPTIONS.resetAll();
        PRINTEROPTIONS.initialize();
        PRINTEROPTIONS.feedBack((byte)2);
        PRINTEROPTIONS.setFont(4, true);
        PRINTEROPTIONS.setTextCenter("The Panera");                          PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Bakery and Food");                     PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setFont(2, false);
        PRINTEROPTIONS.setTextCenter("NIT: 15.444.730-9");                   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Direccion: Cr.81 #43-19 Local 108");   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Rionegro, Antioquia");                 PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter("Domicilios: 562 9979");                PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextCenter(" - Codigos de Producto - ");           PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.setTextLeft("Nro.     Item                         Precio");
        PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.addLineSeperator();                                   PRINTEROPTIONS.newLine();
        ArrayList<G> list = new ArrayList<>();
        list = IInventariable.treeToList(dto, list);
        G aux;
        for (int i=0; i<list.size(); i++){
            aux = list.get(i);
            
            String spaces = "";
            String ID = String.valueOf(aux.getID());
            String nombre = aux.getNombre();
            String precio = String.valueOf((int) aux.getPrecio());
            
            spaces = ""; for (int j=0; j<8-ID.length(); j++) {spaces +=" ";}
            ID = spaces.concat(ID+" ");
            
            spaces = "";
            if (nombre.length()<28) for (int j=0;j<29-nombre.length();j++){ spaces += " "; }
            else                    nombre = aux.getNombre().substring(0, 28)+" ";
            nombre = nombre.concat(spaces); 
            
            spaces = ""; for (int j=0;j<6-precio.length();j++){ spaces += " "; }
            precio = spaces.concat(precio);
            
            PRINTEROPTIONS.setTextLeft(ID+nombre+precio);
            PRINTEROPTIONS.newLine();
        }
        PRINTEROPTIONS.addLineSeperator();                                   PRINTEROPTIONS.newLine();
        PRINTEROPTIONS.feed((byte)3);
        PRINTEROPTIONS.finit();
        PrinterOptions.feedPrinter(PRINTEROPTIONS.finalCommandSet().getBytes());
        //System.out.println(printerOptions.finalCommandSet());
    }
    public static final PrinterOptions PRINTEROPTIONS = new PrinterOptions();
}
