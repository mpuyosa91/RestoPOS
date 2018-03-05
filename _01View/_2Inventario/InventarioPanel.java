/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _01View._2Inventario;

import _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import _03Model.Facility.ProductsAndSupplies.IInventariable;
import _01View._2Inventario.Ingresar.IngresarFrame;
import _01View.MainFrame;
import _01View.Templates.IFrame;
import _01View._2Inventario.Listar.ListFrame;
import _04DataAccessObject.generalController;

/**
 *
 * @author MoisesE
 */
public class InventarioPanel extends javax.swing.JPanel{
    
    public InventarioPanel(MainFrame mainFrame){
        super();
        this.contextFrame = mainFrame;
        initComponents();
        moveComponents();
        createListenerList();
    }
        
    private final MainFrame contextFrame;
    private ArrayList<JButton>  btnList;    
    private ArrayList<IFrame>   frameList;
    private ArrayList<ActionListener> listenList;
    
    private void initComponents(){
        int i;
        i=0;    btnList = new ArrayList<>();
        btnList.add(new JButton("Ingredientes"));           this.add(btnList.get(i));   i++;
        btnList.add(new JButton("SubProductos"));           this.add(btnList.get(i));   i++;
        btnList.add(new JButton("Productos"));              this.add(btnList.get(i));   i++;
        btnList.add(new JButton("De La Carta"));            this.add(btnList.get(i));   i++;
        btnList.add(new JButton("Lista Ingredientes"));     this.add(btnList.get(i));   i++;
        btnList.add(new JButton("Lista Subproductos"));     this.add(btnList.get(i));   i++;
        btnList.add(new JButton("Lista Productos"));        this.add(btnList.get(i));   i++;
        btnList.add(new JButton("Lista DeLaCarta"));        this.add(btnList.get(i));   i++;
        btnList.add(new JButton("Imprimir Ingredientes"));  this.add(btnList.get(i));   i++;
        btnList.add(new JButton("Imprimir SubProductos"));  this.add(btnList.get(i));   i++;
        btnList.add(new JButton("Imprimir Productos"));     this.add(btnList.get(i));   i++;
        btnList.add(new JButton("Imprimir DeLaCarta"));     this.add(btnList.get(i));   i++;
        
        i=0;    frameList = new ArrayList<>();
        frameList.add(new IngresarFrame(contextFrame,DeInventarioType.Ingrediente));    i++;
        frameList.add(new IngresarFrame(contextFrame,DeInventarioType.SubProducto));    i++;
        frameList.add(new IngresarFrame(contextFrame,DeInventarioType.Producto));       i++;
        frameList.add(new IngresarFrame(contextFrame,DeInventarioType.DeLaCarta));      i++;
        frameList.add(new ListFrame(contextFrame,DeInventarioType.Ingrediente));        i++;
        frameList.add(new ListFrame(contextFrame,DeInventarioType.SubProducto));        i++;
        frameList.add(new ListFrame(contextFrame,DeInventarioType.Producto));           i++;
        frameList.add(new ListFrame(contextFrame,DeInventarioType.DeLaCarta));          i++;
        btnList.get(i).setEnabled(false); i++;
        btnList.get(i).addActionListener((ActionEvent e) -> {
            IInventariable.toThermalPrinter(generalController.SUBPRODUCTODTO);
        }); i++;
        btnList.get(i).addActionListener((ActionEvent e) -> {
            ISellable.toThermalPrinter(generalController.PRODUCTODTO);
        }); i++;
        btnList.get(i).addActionListener((ActionEvent e) -> {
            ISellable.toThermalPrinter(generalController.DELACARTADTO);
        }); i++;
    }
    
    private void moveComponents(){
        int initialX=10,initialY=10;
        int stepX=190,stepY=190;
        for (int i=0;i<btnList.size();i++){
            btnList.get(i).setBounds(stepX*(i%4)+initialX,stepY*(i/4)+initialY,150,150);
        }
    }
    
    public class btnActionListener implements ActionListener{
        public btnActionListener(IFrame Frame){
            this.inventarioFrame = Frame;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            inventarioFrame.startframe();
        }
        private final IFrame inventarioFrame;
    }
    private void createListenerList(){
        listenList = new ArrayList<>();
        btnActionListener aux;
        for (int i=0;i<frameList.size();i++){
            aux = new btnActionListener(frameList.get(i));
            listenList.add(aux);
            btnList.get(i).addActionListener(listenList.get(i));
        }
    }
}
