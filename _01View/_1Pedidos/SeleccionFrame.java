/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _01View._1Pedidos;

import _03Model.Facility.ProductsAndSupplies.Inventory;
import _03Model.Facility.ProductsAndSupplies.ISellable;
import _01View.MainFrame;
import _01View.Templates.MiniWindowDialog;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import _04DataAccessObject.generalController;

/**
 *
 * @author MoisesE
 */
public class SeleccionFrame extends MiniWindowDialog{
    final boolean showMesgSys = false;
    
    final int itemByRow=2;
    final int stepX=(int)(xSize/itemByRow), stepY=(int) (stepX*9/16);
    final int height = (int) (stepY*0.8), width = (int) (stepX*0.8);
    final int initialX = (int) (stepY*0.1), initialY = (int) (stepY*0.1);
    
    public SeleccionFrame(MainFrame contextFrame, JFrame frame, boolean modal, ISellable clase) {
        super(contextFrame, frame, modal);
        this.clase = clase;
        externalDto = new Inventory(0,Inventory.DeInventarioType.Producto);
        createObjects();
        createPanels();
    }
    
    @Override
    public void startDialog(){
        externalDto = new Inventory(0,Inventory.DeInventarioType.Producto);
        setVisible(true);
    }
    
    @Override
    protected void setVisiblePanel(Component panel){
        seleccionProductoScroll.setVisible(false);
        panel.setVisible(true);
    }

//------------------------------------------------------------------------------    
    public Inventory externalDto;
    private final ISellable clase;
    private ArrayList<JButton> productosButtonList;
    private ArrayList<ActionListener> productosListenerList;
    private JPanel seleccionProductoPanel;
    private JScrollPane seleccionProductoScroll;
    //------------------------------------------------------------
    
//------------------------------------------------------------------------------
    
    private void createObjects(){
        productosButtonList = new ArrayList<>();
        productosListenerList = new ArrayList<>();            
        seleccionProductoPanel = new JPanel();
        seleccionProductoScroll = new JScrollPane();
    }
    private void createPanels(){        
        createSeleccionProductoPanel();
        containerPanel.add(seleccionProductoScroll);
    }
    private void createSeleccionProductoPanel(){
        seleccionProductoPanel.setLayout(null);
        createButtons(clase);
        seleccionProductoPanel.repaint();
        
        seleccionProductoScroll = new JScrollPane(seleccionProductoPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        seleccionProductoScroll.setBounds(Panel_Rectangle);       
        seleccionProductoScroll.getVerticalScrollBar().setUnitIncrement(10);        
        seleccionProductoScroll.repaint();
    }
    
    private class SeleccionadorISellable implements ActionListener{
        public SeleccionadorISellable(ISellable dto){
            this.producto = dto;
        }
        @Override public void actionPerformed(ActionEvent e) {
            if (!producto.isFinal())
                createButtons(producto); 
            else{
                externalDto = generalController.getProduct(producto.getID());
                if (showMesgSys) System.out.println("..................................");
                if (showMesgSys) System.out.println(externalDto);
                exit_hide();
            }
        }
        private final ISellable producto;
    }    
    private void createButtons(ISellable dto){
        int i;
        productosButtonList.forEach((button)->{ seleccionProductoPanel.remove(button); });
        productosButtonList = new ArrayList<>();
        productosListenerList = new ArrayList<>();
        seleccionProductoPanel.repaint();
        
        ArrayList<ISellable> dtoDown = dto.getDown();     
        SeleccionadorISellable auxListener;
        for (i=0;i<dtoDown.size();i++){
            productosButtonList.add(new JButton("<html><center>"+dtoDown.get(i).getNombre()+"<br>&lt;"+String.valueOf(dtoDown.get(i).getID())+"&gt;</center></html>"));
            seleccionProductoPanel.add(productosButtonList.get(i));
            seleccionProductoPanel.setPreferredSize(new Dimension(seleccionProductoPanel.getWidth(),initialY+stepY*((i+1)/itemByRow)));
            productosButtonList.get(i).setBounds(initialX+stepX*(i%itemByRow), initialY+stepY*(i/itemByRow), width, height);
            auxListener = new SeleccionadorISellable(dtoDown.get(i));
            productosListenerList.add(auxListener);
            productosButtonList.get(i).addActionListener(productosListenerList.get(i)); 
        }
        if (showMesgSys) System.out.println("Quantity: "+i);
        seleccionProductoPanel.setPreferredSize(new Dimension(seleccionProductoPanel.getWidth(),initialY+stepY*((i+1)/itemByRow)));
        seleccionProductoScroll.setViewportView(seleccionProductoPanel);
        seleccionProductoScroll.repaint();
    }
}
