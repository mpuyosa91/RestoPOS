/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _04DataAccessObject;

import _01View.WindowConsole;
import _03Model.ConfigurationDTO;
import static _03Model.ConfigurationDTO.Label.ConsecutivoFacturas;
import _03Model.Customers.IClientable;
import _03Model.Facility.Accounting.Bill;
import _03Model.Facility.Crew.CrewMember;
import _03Model.Facility.ProductsAndSupplies.Inventory;
import _03Model.Facility.ProductsAndSupplies.IngredienteDTO;
import _03Model.Facility.ProductsAndSupplies.SubProductoDTO;
import _03Model.Facility.ProductsAndSupplies.ProductoDTO;
import _03Model.Facility.ProductsAndSupplies.DeLaCartaDTO;
import _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType;
import static _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType.DeLaCarta;
import static _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType.Ingrediente;
import static _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType.Producto;
import static _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType.SubProducto;
import _04DataAccessObject.DataBases.MySQL.DB_MySQL;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import java.util.ArrayList;

/**
 *
 * @author mpuyosa91
 */
public class FacilityDAO{
    
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

    public ArrayList<Bill> getTodayBills() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Bill getBill(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static boolean insertBill(IClientable cliente, boolean printBill){
        return insertBill(cliente, (int) ConfigurationDTO.getConfigurationValueAndIncrement(ConsecutivoFacturas,1),printBill);
    }

    public ArrayList<CrewMember> getCrewMembers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public CrewMember getCrewMember(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean insertCrewMember(CrewMember crewMember) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean updateCrewMember(CrewMember crewMember) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean deleteCrewMember(CrewMember crewMember) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static int getAviableID(int ID){
        int r = 0;
        int l = (int) floor(log10(ID));
        int first = (int) (ID/(int)pow(10,l));
        switch (first){
            case 1:
                r = getAviableID(INGREDIENTEDTO,ID);
                break;
            case 2:
                r = getAviableID(SUBPRODUCTODTO,ID);
                break;
            case 3:
                r = getAviableID(PRODUCTODTO,ID);
                break;
            case 4:
                r = getAviableID(DELACARTADTO,ID);
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
    
    public static Inventory getProduct(int id){
        Inventory r = null;
        int l = (int) floor(log10(id));
        int first = (int) (id/(int)pow(10,l));
        switch (first){
            case 1:
                r = getProductById(id, getProductTreeByType(Ingrediente));
                break;
            case 2:
                r = getProductById(id, getProductTreeByType(SubProducto));
                break;
            case 3:
                r = getProductById(id, getProductTreeByType(Producto));
                break;
            case 4:
                r = getProductById(id, getProductTreeByType(DeLaCarta));
                break;
        }
        return r;
    }
    
    public static <G extends Inventory> void insertProduct(G dto){
        insertProductInItsOwnTree(dto);
        DAO.insertTreeElement(dto);
    }

    public static <G extends Inventory> boolean updateProduct(G dto){
        switch (dto.getClase()){
            case Ingrediente:
                IngredienteDTO dto1 = (IngredienteDTO) dto;
                ((IngredienteDTO) getProduct(dto1.getID())).setAllFrom(dto1);
            break;
            case SubProducto:
                SubProductoDTO dto2 = (SubProductoDTO) dto;
                ((SubProductoDTO) getProduct(dto2.getID())).setAllFrom(dto2);
            break;
            case Producto:
                ProductoDTO dto3 = (ProductoDTO) dto;
                ((ProductoDTO) getProduct(dto3.getID())).setAllFrom(dto3);
            break;
            case DeLaCarta:
                DeLaCartaDTO dto4 = (DeLaCartaDTO) dto;
                ((DeLaCartaDTO) getProduct(dto4.getID())).setAllFrom(dto4);
            break;
        }
        DAO.updateTreeElement(dto);
        return true;
    }

    public boolean deleteProduct(Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static final DB_MySQL       DAO                 = new DB_MySQL();
    private static final IngredienteDTO INGREDIENTEDTO      = new IngredienteDTO(null,1,"Ingredientes");
    private static final SubProductoDTO SUBPRODUCTODTO      = new SubProductoDTO(null,2,"SubProductos");
    private static final ProductoDTO    PRODUCTODTO         = new ProductoDTO(null,3,"Productos");
    private static final DeLaCartaDTO   DELACARTADTO        = new DeLaCartaDTO(null,4,"DeLaCarta");  
    
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
    
    private static Inventory getProductTreeByType(DeInventarioType type){
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
    private static <G extends Inventory> G getProductById(int id, G tree){
        boolean showMesgSys = false;
        G r = null;
        int la = (int) floor(log10(id))+1,lb;
        if (showMesgSys) System.out.print("\n-->"+String.valueOf(id)+" vs "+String.valueOf(tree.getID()));
        if (tree.getID()==id){
            if (showMesgSys) System.out.print("  FINDED");
            r = tree; }
        else {
            if (showMesgSys) System.out.print("  GETING DOWN");
            if (!tree.getDown().isEmpty())  
            for (int i=0; i<tree.getDown().size(); i++){
                G aux = (G) tree.getDown().get(i);
                lb = (int) ceil(log10(aux.getID()));
                if (aux.getID()==(int)(id/pow(10,la-lb)))
                    r = getProductById(id,aux);
                if (r!=null) i = tree.getDown().size();
        }   }
        return r;
    }
    private static <G extends Inventory> void insertProductInItsOwnTree(G node){
        int ID = node.getID();
        int l = (int) floor(log10(ID));
        int first = (int) (ID/(int)pow(10,l));
        switch (first){
            case 1:
                insertProductInTree(node, getProductTreeByType(DeInventarioType.Ingrediente));
                break;
            case 2:
                insertProductInTree(node, getProductTreeByType(DeInventarioType.SubProducto));
                break;
            case 3:
                insertProductInTree(node, getProductTreeByType(DeInventarioType.Producto));
                break;
            case 4:
                insertProductInTree(node, getProductTreeByType(DeInventarioType.DeLaCarta));
                break;
        }
    }
    private static <G extends Inventory> void insertProductInTree(G node, G tree){
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
                    insertProductInTree(node,aux.get(i));
                    if (showMesgSys) System.out.print(", NEXT");
                }
                if (showMesgSys) System.out.print(",  UP "+tree.getID());
        }   }
    }
        
    private static boolean insertBill(IClientable cliente, int factura, boolean printBill){
        Bill bill = new Bill(cliente,factura);
        if (printBill) bill.print();
        else bill.openTrack();
        DAO.insertBill(bill);
        return true;
    }
}
