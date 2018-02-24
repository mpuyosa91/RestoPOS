/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.VisualSetter;

import Controller.DataBase.IDataBase;
import Model.DataTransferObjects.SellBase.DeInventario;
import Model.DataTransferObjects.SellBase.DeInventario.DeInventarioType;
import Model.DataTransferObjects.SellBase.DeLaCartaDTO;
import Model.DataTransferObjects.SellBase.IngredienteDTO;
import Model.DataTransferObjects.SellBase.ProductoDTO;
import Model.DataTransferObjects.SellBase.SubProductoDTO;
import View.MainFrame;
import View.Templates.WindowDialog;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import Model.DataTransferObjects.SellBase.IRetainerProducto;
import Model.DataTransferObjects.SellBase.IRetainerSubProducto;
import Model.DataTransferObjects.SellBase.IRetainerDeLaCarta;
import javax.swing.JFrame;
import Model.DataTransferObjects.SellBase.IRetainerIngrediente;

/**
 *
 * @author MoisesE
 */
public class DeInventarioVisualSetter extends WindowDialog{
    
    public DeInventarioVisualSetter(MainFrame contextFrame,JFrame frame, boolean modal, DeInventarioType clase) {
        super(contextFrame,frame,modal);
        this.clase = clase;
        createObjects();
        createPanels();
    }
    public <G extends DeInventario> void startframe(G dto){
        System.out.println("DeInventarioVisualSetter.startFrame: \n"+dto);
        consoleFlush();
        this.dto = dto;
        informationLabel.setText("Agrega un "+clase);
        createListItemPanel();
        setVisiblePanel(listItemScroll);
        setVisible(true);
    }
    @Override public void exit_hide(){
        setVisible(false);
        consoleFlush();
    }
    @Override protected void setVisiblePanel(Component panel){
        listItemScroll.setVisible(false);
        panel.setVisible(true);
    }
//------------------------------------------------------------------------------
    private final   DeInventarioType    clase;
    //------------------------------------------------------------
    private DeInventario                dto;
    private JScrollPane                 listItemScroll;
    private IngredienteDTO              ingredienteTree;
    private SubProductoDTO              subProductoTree;
    private ProductoDTO                 productoTree;
    private DeLaCartaDTO                deLaCartaTree;
    //------------------------------------------------------------
    private JPanel                      listItemPanel;
    //------------------------------------------------------------
    private ArrayList<ActionListener>   listenerList;
    private ArrayList<JButton>          buttonList;
    private IngredienteDTO              ingredienteActual;
    private SubProductoDTO              subProductoActual;
    private ProductoDTO                 productoActual;
    private DeLaCartaDTO                deLaCartaActual;
    private ArrayList<IngredienteDTO>   ingredienteList;
    private ArrayList<SubProductoDTO>   subProductoList;
    private ArrayList<ProductoDTO>      productoList;
    private ArrayList<DeLaCartaDTO>     deLaCartaList;
//------------------------------------------------------------------------------    
    private void createObjects(){
        listItemPanel               =   new JPanel();
        listenerList                =   new ArrayList<>();
        buttonList                  =   new ArrayList<>();
        listItemScroll              =   new JScrollPane();
    }
    private void createPanels(){
        createListItemPanel();
    }
    private void createListItemPanel(){
        listItemPanel  =   new JPanel();
        listItemPanel.setBounds(LeftPanel_Rectangle);
        listItemPanel.setLayout(null);
        informationLabel.setText("Lista "+clase);
        switch (clase){
            case Ingrediente:
                ingredienteTree = IDataBase.INGREDIENTEDTO;
                setButtonsListItemPanel(ingredienteTree);
                break;
            case SubProducto:
                subProductoTree = IDataBase.SUBPRODUCTODTO;
                setButtonsListItemPanel(subProductoTree);
                break;
            case Producto:
                productoTree = IDataBase.PRODUCTODTO;
                setButtonsListItemPanel(productoTree);
                break;
            case DeLaCarta:
                deLaCartaTree = IDataBase.DELACARTADTO;
                setButtonsListItemPanel(deLaCartaTree);
                break;
        }
        listItemScroll.setBounds(LeftPanel_Rectangle);
        listItemScroll.setViewportView(listItemPanel);
        listItemScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listItemScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        containerPanel.add(listItemScroll);
    }
    private <G extends DeInventario> void setButtonsListItemPanel(G tree){
        ArrayList<DeInventario> list = new ArrayList<>();
        listItemPanel.removeAll();
        switch (clase){
            case Ingrediente:
                ingredienteActual = (IngredienteDTO) tree;
                ingredienteList = ingredienteActual.getDown();
                list.addAll(ingredienteList);
                break;
            case SubProducto:
                subProductoActual = (SubProductoDTO) tree;
                subProductoList = subProductoActual.getDown();
                list.addAll(subProductoList);
                break;
            case Producto:
                productoActual = (ProductoDTO) tree;
                productoList = productoActual.getDown();
                list.addAll(productoList);
                break;
            case DeLaCarta:
                deLaCartaActual = (DeLaCartaDTO) tree;
                deLaCartaList = deLaCartaActual.getDown();
                list.addAll(deLaCartaList);
                break;
        }
        
        listenerList.clear(); 
        buttonList.clear(); 

        for (int i=0;i<list.size()+1;i++){
            if (i==0)   createButton(listItemPanel, buttonList, "Nuevo <"+clase+">", 0);        
            else        createButton(listItemPanel, buttonList, list.get(i-1).getNombre(), i);
            listenerList.add(new DeInventarioActionListener(i));
            buttonList.get(i).addActionListener(listenerList.get(i));
        }  
        buttonList.get(0).setEnabled(false);

        if (!list.isEmpty()){
            this.consoleFlush(); 
            this.consoleAppend("Encontrados "+list.size()+" "+clase+"(s)");
        }
        else{
            this.consoleAppend("Lista <"+clase+"> Vacia");
        }
        listItemPanel.repaint();
    }
    private class DeInventarioActionListener implements ActionListener{
        public DeInventarioActionListener(int i){ this.i = i; }
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (clase){
                case Ingrediente:
                    if (ingredienteList.get(i-1).isFinal()){
                        System.out.println("DeInventarioVisualSetter.DeInventarioActionListener.actionPerformed: \n"+dto);
                        IRetainerIngrediente dtoI = (IRetainerIngrediente) dto;
                        System.out.println("DeInventarioVisualSetter.DeInventarioActionListener.actionPerformed: \n"+dtoI);
                        IngredienteDTO dto1 = new IngredienteDTO(ingredienteList.get(i-1));
                        dto1.setCantidad(0.0,ingredienteList.get(i-1).getUnidadBase());
                        System.out.println("DeInventarioVisualSetter.DeInventarioActionListener.actionPerformed: \n"+dto1);
                        dtoI.getIngredienteList().add(dto1);
                        exit_hide();
                    }
                    else setButtonsListItemPanel(ingredienteList.get(i-1));
                    break;
                case SubProducto:
                    if (subProductoList.get(i-1).isFinal()){
                        System.out.println("DeInventarioVisualSetter.DeInventarioActionListener.actionPerformed: \n"+dto);
                        IRetainerSubProducto dtoS = (IRetainerSubProducto) dto;
                        System.out.println("DeInventarioVisualSetter.DeInventarioActionListener.actionPerformed: \n"+dtoS);
                        SubProductoDTO dto2 = new SubProductoDTO(subProductoList.get(i-1));
                        dto2.setCantidad(0.0,subProductoList.get(i-1).getUnidadBase());
                        System.out.println("DeInventarioVisualSetter.DeInventarioActionListener.actionPerformed: \n"+dto2);
                        dtoS.getSubProductoList().add(dto2);
                        exit_hide();
                    }
                    else setButtonsListItemPanel(subProductoList.get(i-1));
                    break;
                case Producto:
                    if (productoList.get(i-1).isFinal()){
                        System.out.println("DeInventarioVisualSetter.DeInventarioActionListener.actionPerformed: "+dto.getID());
                        IRetainerProducto dtoP = (IRetainerProducto) dto;
                        ProductoDTO dto3 = new ProductoDTO(productoList.get(i-1));
                        dto3.setCantidad(0.0,productoList.get(i-1).getUnidadBase());
                        System.out.println("DeInventarioVisualSetter.DeInventarioActionListener.actionPerformed: "+dto3.getID());
                        dtoP.getProductoList().add(dto3);
                        exit_hide();
                    }
                    else setButtonsListItemPanel(productoList.get(i-1));
                    break;
                case DeLaCarta:
                    if (deLaCartaList.get(i-1).isFinal()){
                        IRetainerDeLaCarta dtoD = (IRetainerDeLaCarta) dto;
                        DeLaCartaDTO dto4 = new DeLaCartaDTO(deLaCartaList.get(i-1));
                        dto4.setCantidad(0.0,deLaCartaList.get(i-1).getUnidadBase());
                        dtoD.getDeLaCartaList().add(dto4);
                        exit_hide();
                    }
                    else setButtonsListItemPanel(deLaCartaList.get(i-1));
                    break;
            }
        }
        private final int i;
    }
    
}
