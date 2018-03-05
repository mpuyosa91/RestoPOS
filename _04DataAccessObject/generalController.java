/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject;

import _04DataAccessObject.DataBases.MySQL.DB_MySQL;
import _03Model.Facility.Accounting.Bill;
import _03Model.Facility.Crew.Printables.ReporteTurno;
import _03Model.ConfigurationDTO;
import _03Model.ConfigurationDTO.Label;
import static _03Model.ConfigurationDTO.Label.ConsecutivoFacturas;
import _03Model.Facility.ProductsAndSupplies.Inventory;
import _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType;
import _03Model.Facility.ProductsAndSupplies.DeLaCartaDTO;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import _03Model.Facility.ProductsAndSupplies.IngredienteDTO;
import _03Model.Facility.ProductsAndSupplies.ProductoDTO;
import _03Model.Facility.ProductsAndSupplies.SubProductoDTO;
import _03Model.Customers.ClienteExterno;
import _03Model.Customers.IClientable;
import _03Model.Customers.Mesa;
import _03Model.Customers.PuntoDeVenta;
import _01View.WindowConsole;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import java.util.ArrayList;

/**
 *
 * @author MoisesE
 */
public interface generalController {
    
    public static boolean syncDataWithServer(){
        boolean r;
        WindowConsole.print("     Getting model data... \n");
        r = generalController.DB.getProductsAndSupply();
        if (r)  WindowConsole.print("     [CORRECT] Model data getted.\n");
        else    WindowConsole.print("     [FAILED] in attempt to get model data.\n");
        WindowConsole.print("     Getting model composition... \n");
        r = generalController.DB.getProductsAndSupplyCompositions();
        if (r)  WindowConsole.print("     [CORRECT] Model composition getted\n");
        else    WindowConsole.print("     [FAILED] in attempt to get model composition.\n");
        generalController.DB.getTodayBills();
        return r;
    }
        
    public static Inventory getModel(DeInventarioType type){
        Inventory r = null;
        switch (type){
            case Ingrediente:
                r = INGREDIENTEDTO;
                break;
            case SubProducto:
                r = SUBPRODUCTODTO;
                break;
            case Producto:
                r = PRODUCTODTO;
                break;
            case DeLaCarta:
                r = DELACARTADTO;
                break;
        }
        return r;
    }
        
    public static <G extends Inventory> void insertProduct(G dto){
        generalController.insertProductInItsOwnTree(dto);
        generalController.DB.insertTreeElement(dto);
    }
    public static <G extends Inventory> void updateProduct(G dto){
        switch (dto.getClase()){
            case Ingrediente:
                IngredienteDTO dto1 = (IngredienteDTO) dto;
                ((IngredienteDTO) generalController.getProduct(dto1.getID())).setAllFrom(dto1);
            break;
            case SubProducto:
                SubProductoDTO dto2 = (SubProductoDTO) dto;
                ((SubProductoDTO) generalController.getProduct(dto2.getID())).setAllFrom(dto2);
            break;
            case Producto:
                ProductoDTO dto3 = (ProductoDTO) dto;
                ((ProductoDTO) generalController.getProduct(dto3.getID())).setAllFrom(dto3);
            break;
            case DeLaCarta:
                DeLaCartaDTO dto4 = (DeLaCartaDTO) dto;
                ((DeLaCartaDTO) generalController.getProduct(dto4.getID())).setAllFrom(dto4);
            break;
        }
        generalController.DB.updateTreeElement(dto);
    }
    public static <G extends Inventory> void delete(G node){
        
    }
    
    public static void getTurnoFacturation(){
        ReporteTurno.flushProductoList();
        Bill aux = DB.getTodayBills();
        while (aux!=null) {   
            ArrayList<ISellable> list = aux.getProductoList();
            ProductoDTO auxProducto; DeLaCartaDTO auxDeLaCarta;
            for (int j=0; j<list.size(); j++){ 
                if (list.get(j).getClass()==ProductoDTO.class) {
                    auxProducto = new ProductoDTO((ProductoDTO) list.get(j));
                    ReporteTurno.addProductoList(auxProducto);
                }
                if (list.get(j).getClass()==DeLaCartaDTO.class){
                    auxDeLaCarta = new DeLaCartaDTO((DeLaCartaDTO) list.get(j));
                    ReporteTurno.addProductoList(auxDeLaCarta);
                }
            }
            aux = aux.getDown();
        }
        ArrayList<ISellable> list = ReporteTurno.getProductoList();
        if (list!=null) for (int j=0; j<list.size()-1; j++){
            for (int k=j+1;k<list.size();k++){
                if (list.get(j).getID()==list.get(k).getID()){
                    list.get(j).setCantidad(list.get(j).getCantidad()+1);
        }   }   }
        if (list!=null) for (int j=0; j<list.size()-1; j++){
            for (int k=list.size()-1;k>j;k--){
                if (list.get(j).getID()==list.get(k).getID()){
                    list.remove(list.get(k));
        }   }   }
        ConfigurationDTO.setConfigurationValueAndPutOnServer(Label.TurnoActual, ConfigurationDTO.getConfigurationValue(Label.TurnoActual)+1);
        ReporteTurno.imprimir();
    }
    
    public static void getDayFacturation(){
        ReporteTurno.flushProductoList();
        Bill aux = DB.getTodayBills();
        while (aux!=null) {   
            ArrayList<ISellable> list = aux.getProductoList();
            ProductoDTO auxProducto; DeLaCartaDTO auxDeLaCarta;
            for (int j=0; j<list.size(); j++){ 
                if (list.get(j).getClass()==ProductoDTO.class) {
                    auxProducto = new ProductoDTO((ProductoDTO) list.get(j));
                    ReporteTurno.addProductoList(auxProducto);
                }
                if (list.get(j).getClass()==DeLaCartaDTO.class){
                    auxDeLaCarta = new DeLaCartaDTO((DeLaCartaDTO) list.get(j));
                    ReporteTurno.addProductoList(auxDeLaCarta);
                }
            }
            aux = aux.getDown();
        }
        ArrayList<ISellable> list = ReporteTurno.getProductoList();
        if (list!=null) for (int j=0; j<list.size()-1; j++){
            for (int k=j+1;k<list.size();k++){
                if (list.get(j).getID()==list.get(k).getID()){
                    list.get(j).setCantidad(list.get(j).getCantidad()+1);
        }   }   }
        if (list!=null) for (int j=0; j<list.size()-1; j++){
            for (int k=list.size()-1;k>j;k--){
                if (list.get(j).getID()==list.get(k).getID()){
                    list.remove(list.get(k));
        }   }   }
        ReporteTurno.imprimir();
    }
    
//******************************************************************************
//********************************DataTreatement********************************
//****************************************************************************** 
    public static DB_MySQL DB                              = new DB_MySQL();
    public static final ArrayList<IClientable> CLIENTELIST  = createClienteList();
    public static final IngredienteDTO  INGREDIENTEDTO      = new IngredienteDTO(null,1,"Ingredientes");
    public static final SubProductoDTO  SUBPRODUCTODTO      = new SubProductoDTO(null,2,"SubProductos");
    public static final ProductoDTO     PRODUCTODTO         = new ProductoDTO(null,3,"Productos");
    public static final DeLaCartaDTO    DELACARTADTO        = new DeLaCartaDTO(null,4,"DeLaCarta");
    
    public static ArrayList<IClientable> createClienteList(){
        WindowConsole.print("     Creating Client List... \n");
        ArrayList<IClientable> r = new ArrayList<>();
        r.add(new PuntoDeVenta());
        for (int i=0; i<ConfigurationDTO.getConfigurationValue(Label.NroMesas);i++){
            Mesa auxMesa = new Mesa(i+1);
            r.add(auxMesa);
        }
        r.add(new ClienteExterno());
        WindowConsole.print("     [CORRECT] Client List created successfully.\n");
        return r;
    }
    
    public static Inventory getProduct(int ID){
        Inventory r = null;
        int l = (int) floor(log10(ID));
        int first = (int) (ID/(int)pow(10,l));
        switch (first){
            case 1:
                r = generalController.getProduct(ID, getModel(DeInventarioType.Ingrediente));
                break;
            case 2:
                r = generalController.getProduct(ID, getModel(DeInventarioType.SubProducto));
                break;
            case 3:
                r = generalController.getProduct(ID, getModel(DeInventarioType.Producto));
                break;
            case 4:
                r = generalController.getProduct(ID, getModel(DeInventarioType.DeLaCarta));
                break;
        }
        return r;
    }
    public static <G extends Inventory> G getProduct(int ID, G tree){
        boolean showMesgSys = false;
        G r = null;
        int la = (int) floor(log10(ID))+1,lb;
        if (showMesgSys) System.out.print("\n-->"+String.valueOf(ID)+" vs "+String.valueOf(tree.getID()));
        if (tree.getID()==ID){
            if (showMesgSys) System.out.print("  FINDED");
            r = tree; }
        else {
            if (showMesgSys) System.out.print("  GETING DOWN");
            if (!tree.getDown().isEmpty())  
            for (int i=0; i<tree.getDown().size(); i++){
                G aux = (G) tree.getDown().get(i);
                lb = (int) ceil(log10(aux.getID()));
                if (aux.getID()==(int)(ID/pow(10,la-lb)))
                    r = generalController.getProduct(ID,aux);
                if (r!=null) i = tree.getDown().size();
        }   }
        return r;
    }
    
    public static int getAviableID(int ID){
        int r = 0;
        int l = (int) floor(log10(ID));
        int first = (int) (ID/(int)pow(10,l));
        switch (first){
            case 1:
                r = generalController.getAviableID(generalController.INGREDIENTEDTO,ID);
                break;
            case 2:
                r = generalController.getAviableID(generalController.SUBPRODUCTODTO,ID);
                break;
            case 3:
                r = generalController.getAviableID(generalController.PRODUCTODTO,ID);
                break;
            case 4:
                r = generalController.getAviableID(generalController.DELACARTADTO,ID);
                break;
        }
        return r;
    }
    public static <G extends Inventory> G getDTOFather(int ID, int pos, G tree){
        boolean showMesgSys = false;
        G r = null, aux; 
        int len = (int) floor(log10(ID));
        if (showMesgSys) System.out.print("\n--> getDTOFather("+String.valueOf(ID)+", "+tree.getDescription()+")");
        if (showMesgSys) System.out.print("\n-->"+tree.getID()+" vs "+String.valueOf((int)(ID/pow(10,len-pos)))+" len-pos="+(len-pos));
        if ((tree.getID()==(int)(ID/pow(10,len-pos)))&&(len-pos>0)){
            r = tree;
            if (len-pos>2){
                if (showMesgSys) System.out.print("\n    Enter");
                for (int i=0; i<tree.getDown().size(); i++){
                    aux = (G) tree.getDown().get(i);
                    if (pos==0) aux = (G) getDTOFather(ID,pos+1,aux);
                    else        aux = (G) getDTOFather(ID,pos+2,aux);
                    if (aux!=null) r = aux;
                }   
            }
        }
        return r;
    }
    public static <G extends Inventory> int getAviableID(G tree, int ID){
        boolean showMesgSys = false, aviable=false;
        int r=0, aux;
        if (showMesgSys) System.out.print("\n--> getAviableID("+tree+","+String.valueOf(ID)+")");
        
        G dtoFather = (G) getDTOFather(ID, 0,tree);
        if (showMesgSys) System.out.print("\n--> Father = "+dtoFather);
        for (int i=0; i<dtoFather.getDown().size() ;i++){
            aux = ID+1+i; aviable = false;
            if (showMesgSys) System.out.print("\n--> Searching AviableID="+aux+"");
            if (aux!=dtoFather.getDown().get(i).getID()) {aviable = true; r = aux; break;}
        }
        if (!aviable) {
            if (dtoFather.getID()<10)  r = dtoFather.getID()*10+dtoFather.getDown().size()+1;
            else                       r = dtoFather.getID()*100+dtoFather.getDown().size()+1;
        }
        if (showMesgSys) System.out.print("\n--> r="+r+"\n");
        return r;
    }
    
    public static <G extends Inventory> String getProductUsages(G dto){
        String r = "";
        switch (dto.getClase()){
            case Ingrediente:
                break;
            case SubProducto:
                break;
            case Producto:
                break;
            case DeLaCarta:
                break;
        }
        return r;
    }
    
    public static <G extends Inventory> void insertProductInItsOwnTree(G node){
        int ID = node.getID();
        int l = (int) floor(log10(ID));
        int first = (int) (ID/(int)pow(10,l));
        switch (first){
            case 1:
                insertProductInList(node, getModel(DeInventarioType.Ingrediente));
                break;
            case 2:
                insertProductInList(node, getModel(DeInventarioType.SubProducto));
                break;
            case 3:
                insertProductInList(node, getModel(DeInventarioType.Producto));
                break;
            case 4:
                insertProductInList(node, getModel(DeInventarioType.DeLaCarta));
                break;
        }
    }
    public static <G extends Inventory> void insertProductInList(G node, G tree){
        boolean showMesgSys = false;
        if (showMesgSys) System.out.print("\nInsertNodeInTree: ");
        String nodeID = String.valueOf(node.getID());
        String treeID = String.valueOf(tree.getID());
        boolean valid;
        int addLvl;
        if (nodeID.length()==2) addLvl = 1;
        else                    addLvl = 2;
        if (showMesgSys) System.out.print("-->"+String.valueOf(node.getID())+" vs "+String.valueOf(tree.getID()));
        if (nodeID.length()==treeID.length()+addLvl){
            valid = true;
            for (int i=0; i<treeID.length(); i++)
                if (treeID.charAt(i)!=nodeID.charAt(i)){
                    valid = false; i = treeID.length();}
            if (valid) {
                if (showMesgSys) System.out.print(",  ADDED");
                node.setUp(tree);
                tree.getDown().add(node); }
            else if (showMesgSys) System.out.print(",  NO MATCH");
        }
        else{
            if (!tree.getDown().isEmpty()){
                if (showMesgSys) System.out.print(",  DOWN "+tree.getID());
                for (int i=0; i<tree.getDown().size(); i++){
                    ArrayList<G> aux = (ArrayList<G>) tree.getDown();
                    insertProductInList(node,aux.get(i));
                    if (showMesgSys) System.out.print(", NEXT");
                }
                if (showMesgSys) System.out.print(",  UP "+tree.getID());
        }   }
    }
       
    
    public static void insertBill(IClientable cliente, int factura, boolean printBill){
        Bill bill = new Bill(cliente,factura);
        if (printBill) bill.print();
        else bill.openTrack();
        DB.insertBill(bill);
    }
    
    public static void insertBill(IClientable cliente, boolean printBill){
        generalController.insertBill(cliente, (int) ConfigurationDTO.getConfigurationValueAndIncrement(ConsecutivoFacturas,1),printBill);
    }

}
