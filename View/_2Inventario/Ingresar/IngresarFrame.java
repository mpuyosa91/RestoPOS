/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View._2Inventario.Ingresar;

import Controller.DataBase.IDataBase;
import Model.DataTransferObjects.SellBase.DeInventario;
import Model.DataTransferObjects.SellBase.DeInventario.DeInventarioType;
import Model.DataTransferObjects.SellBase.DeLaCartaDTO;
import Model.DataTransferObjects.SellBase.IRetainerDeLaCarta;
import Model.DataTransferObjects.SellBase.IRetainerIngrediente;
import Model.DataTransferObjects.SellBase.IRetainerProducto;
import Model.DataTransferObjects.SellBase.IRetainerSubProducto;
import Model.DataTransferObjects.SellBase.ISellable;
import Model.DataTransferObjects.SellBase.IngredienteDTO;
import Model.DataTransferObjects.SellBase.ProductoDTO;
import Model.DataTransferObjects.SellBase.SubProductoDTO;
import View.MainFrame;
import View.Templates.WindowFrame;
import View.VisualSetter.DeInventarioVisualSetter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import Model.DataTransferObjects.SellBase.Measure.Measurable;
import static java.lang.Math.round;
import java.util.Arrays;

/**
 *
 * @author MoisesE
 */
public class IngresarFrame extends WindowFrame{
    public boolean showMesgSys = false;
        
    public IngresarFrame(MainFrame contextFrame, DeInventarioType clase) {
        super(contextFrame);
        this.thisFrame = this;
        this.clase = clase;
        createObjects();
        createPanels();
    }
    
    @Override public void startframe(){
        consoleFlush();
        informationLabel.setText("Menu "+clase);
        switch(clase){
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
        setVisiblePanel(listItemScroll);
        setVisible(true);
    }
    @Override public void exit_hide(){
        setVisible(false);
        consoleFlush();
    }
    @Override protected void setVisiblePanel(Component panel){
        listItemScroll.setVisible(false);
        singleItemScroll.setVisible(false);
        panel.setVisible(true);
    }
//------------------------------------------------------------------------------
    private final   IngresarFrame       thisFrame;
    private final   DeInventarioType    clase;
    //------------------------------------------------------------
    private DeInventarioVisualSetter    ingredienteVisualSetter;
    private DeInventarioVisualSetter    subProductoVisualSetter;
    private DeInventarioVisualSetter    productoVisualSetter;
    private DeInventarioVisualSetter    deLaCartaVisualSetter;
    private JScrollPane                 listItemScroll;
    //------------------------------------------------------------
    private JPanel                      listItemPanel ;
    private DeInventario                dto;
    private IngredienteDTO              ingredienteTree;
    private SubProductoDTO              subProductoTree;
    private ProductoDTO                 productoTree;
    private DeLaCartaDTO                deLaCartaTree;
    private JButton                     borrarButtonListItemPanel;
    private JButton                     atrasButtonListItemPanel;
    //------------------------------------------------------------
    private ArrayList<ActionListener>   listenerList;
    private ArrayList<JButton>          buttonList;
    //------------------------------------------------------------
    private IngredienteDTO              ingredienteActual;
    private SubProductoDTO              subProductoActual;
    private ProductoDTO                 productoActual;
    private DeLaCartaDTO                deLaCartaActual;
    private ArrayList<IngredienteDTO>   ingredienteList;
    private ArrayList<SubProductoDTO>   subProductoList;
    private ArrayList<ProductoDTO>      productoList;
    private ArrayList<DeLaCartaDTO>     deLaCartaList;
    //------------------------------------------------------------
    private boolean                     isNew;
    private JCheckBox                   isFinalCheckBox;
    private JScrollPane                 singleItemScroll;
    //------------------------------------------------------------
    private ArrayList<JLabel>           componentesLabelList;
    private ArrayList<JTextArea>        componentesTextAreaList;
    private JTextArea                   nombreTextArea;
    private JComboBox                   typeComboBox;
    private JComboBox                   unidadComboBox;
    private JLabel                      cantidadLabel;
    private JTextArea                   cantidadTextArea;
    private JTextArea                   precioTextArea;
    private JButton                     addIngredButton;
    private JButton                     addSubProButton;
    private JButton                     addProducButton;
    private JButton                     addDeLaCaButton;
    private JButton                     cargarButton;
    private JButton                     borrarButton;
    private JButton                     atrasButton;
    private JPanel                      singleItemPanel;
    private final int                   initialX=30, initialY=100;
    private final int                   xElements = 2;
//------------------------------------------------------------------------------
    private void createObjects(){
        listItemPanel               =   new JPanel();
        listenerList                =   new ArrayList<>();
        buttonList                  =   new ArrayList<>();
        listItemScroll              =   new JScrollPane();
        singleItemPanel             =   new JPanel();
        componentesLabelList        =   new ArrayList<>();
        componentesTextAreaList     =   new ArrayList<>();
        nombreTextArea              =   new JTextArea(1,1);
        cantidadLabel               =   new JLabel();
        cantidadTextArea            =   new JTextArea(1,1);
        typeComboBox                =   new JComboBox();
        unidadComboBox              =   new JComboBox();
        precioTextArea              =   new JTextArea(1,1);
        isFinalCheckBox             =   new JCheckBox();
        addIngredButton             =   new JButton("Agregar Ingrediente");
        addSubProButton             =   new JButton("Agregar SubProducto");
        addProducButton             =   new JButton("Agregar Producto");
        addDeLaCaButton             =   new JButton("Agregar DeLaCarta");
        cargarButton                =   new JButton("Cargar");
        atrasButton                 =   new JButton("Atras");
        borrarButton                =   new JButton("Borrar");        
        atrasButtonListItemPanel    =   new JButton("Atras");
        borrarButtonListItemPanel   =   new JButton("Borrar");     
        singleItemScroll            =   new JScrollPane();
    }
    private void createPanels(){
        createListItemPanel();
        createSingleItemPanel();
        ingredienteVisualSetter = new DeInventarioVisualSetter(contextFrame, thisFrame, true, DeInventarioType.Ingrediente);
        subProductoVisualSetter = new DeInventarioVisualSetter(contextFrame, thisFrame, true, DeInventarioType.SubProducto);
        productoVisualSetter    = new DeInventarioVisualSetter(contextFrame, thisFrame, true, DeInventarioType.Producto);
        deLaCartaVisualSetter   = new DeInventarioVisualSetter(contextFrame, thisFrame, true, DeInventarioType.DeLaCarta);
        setVisiblePanel(listItemScroll);
    }
    
    private void createListItemPanel(){
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
        if (showMesgSys) System.out.println("IngresarFrame.setButtonsListItemPanel: "+ tree);
        if (listItemPanel.getComponentCount()==0){
            listItemPanel.add(atrasButtonListItemPanel);
            listItemPanel.add(borrarButtonListItemPanel);
            atrasButtonListItemPanel.setBounds       (  30, 25, 100, 50);
            borrarButtonListItemPanel.setBounds      ( 140, 25, 390, 50);
        }
        atrasButtonListItemPanel.setEnabled(tree.getID()>10);
        borrarButtonListItemPanel.setEnabled(tree.getID()>10);
        switch (clase){
            case Ingrediente:
                ingredienteActual = (IngredienteDTO) tree;
                ingredienteList = ingredienteActual.getDown();

                break;
            case SubProducto:
                subProductoActual = (SubProductoDTO) tree;
                subProductoList = subProductoActual.getDown();
                break;
            case Producto:
                productoActual = (ProductoDTO) tree;
                productoList = productoActual.getDown();
                break;
            case DeLaCarta:
                deLaCartaActual = (DeLaCartaDTO) tree;
                deLaCartaList = deLaCartaActual.getDown();
                break;
        }
        setButtonsListenersListItemPanel(tree);
        if (!tree.getDown().isEmpty()){
            this.consoleFlush(); 
            this.consoleAppend("Encontrados "+tree.getDown().size()+" "+clase+"(s)");
            consoleAppend(tree.treeToString());
        }
        else{
            this.consoleFlush(); 
            this.consoleAppend("Lista <"+clase+"> Vacia");
        }
        listItemPanel.repaint();
    }
    private <G extends DeInventario> void setButtonsListenersListItemPanel(G tree){
        ArrayList<G> list = (ArrayList<G>) tree.getDown();
        final int stepX=(listItemPanel.getWidth()-initialX-2)/xElements, stepY=100;
        for (ActionListener actionListener : atrasButton.getActionListeners())
            atrasButtonListItemPanel.removeActionListener(actionListener);
        for (ActionListener actionListener : borrarButton.getActionListeners())
            borrarButtonListItemPanel.removeActionListener(actionListener);        
        atrasButtonListItemPanel.addActionListener(new AtrasActionListener(clase));
        borrarButtonListItemPanel.addActionListener(new BorrarActionListener(clase));
        if (buttonList.isEmpty()){
            JButton auxButton;            
            auxButton = new JButton("Nuevo <"+tree.getNombre()+">");
            auxButton.setBounds(initialX+stepX*(0%xElements), initialY+stepY*(0/xElements), stepX-initialX, stepY);
            auxButton.addActionListener(new DeInventarioActionListener(0));
            buttonList.add(0,auxButton);
            listenerList.add(0,auxButton.getActionListeners()[0]);
            listItemPanel.add(buttonList.get(0));
            for (int i=0;i<list.size();i++){
                auxButton = new JButton("<html><center>"+list.get(i).getNombre()+"<br>&lt;"+String.valueOf(list.get(i).getID())+"&gt;</center></html>");
                auxButton.setBounds(initialX+stepX*((i+1)%xElements), initialY+stepY*((i+1)/xElements), stepX-initialX, stepY);
                auxButton.addActionListener(new DeInventarioActionListener((i+1)));
                buttonList.add(i+1,auxButton);
                listenerList.add(i+1,auxButton.getActionListeners()[0]);
                listItemPanel.add(buttonList.get(i+1));
            }
        }
        else{
            int buttonPosition;
            boolean found;
            JButton auxButton;
            if (!buttonList.get(0).getText().equals("Nuevo <"+tree.getNombre()+">")||buttonList.size()!=(list.size()+1)){
                listItemPanel.remove(buttonList.get(0));
                auxButton = new JButton("Nuevo <"+tree.getNombre()+">");
                auxButton.setBounds(initialX+stepX*(0%xElements), initialY+stepY*(0/xElements), stepX-initialX, stepY);
                auxButton.addActionListener(new DeInventarioActionListener(0));
                buttonList.set(0,auxButton);
                listenerList.set(0, auxButton.getActionListeners()[0]);
                listItemPanel.add(buttonList.get(0));
            }
            for (int i=0;i<list.size();i++){
                buttonPosition = i; found = false;
                for (int j=1; j<buttonList.size(); j++)
                    if (buttonList.get(j).getText().equals(list.get(i).getNombre())){
                        buttonPosition = j; found = true; j=buttonList.size();                    }
                if (found){
                    if (i!=(buttonPosition-1)) {
                        if (showMesgSys) System.out.println("IngresarFrame.setButtonsListenersListItemPanel.isFoundForReplacement");
                        listItemPanel.remove(buttonList.get(buttonPosition));
                        buttonList.set(i,buttonList.get(buttonPosition));
                        buttonList.remove(buttonPosition);
                        listItemPanel.add(buttonList.get(i));
                }   }
                else{   
                    if (showMesgSys) System.out.println("IngresarFrame.setButtonsListenersListItemPanel.notFoundInButtonListForCreating");
                    auxButton = new JButton("<html><center>"+list.get(i).getNombre()+"<br>&lt;"+String.valueOf(list.get(i).getID())+"&gt;</center></html>");
                    auxButton.setBounds(initialX+stepX*((i+1)%xElements), initialY+stepY*((i+1)/xElements), stepX-initialX, stepY);
                    auxButton.addActionListener(new DeInventarioActionListener(i+1));
                    if ((i+1)<buttonList.size()){    
                        listItemPanel.remove(buttonList.get(i+1));
                        buttonList.set(i+1, auxButton);
                        listenerList.set(i+1, auxButton.getActionListeners()[0]);
                        listItemPanel.add(buttonList.get(i+1));
                    }
                    else{
                        buttonList.add(auxButton);
                        listenerList.add(auxButton.getActionListeners()[0]);
                        listItemPanel.add(buttonList.get(i+1));
                    }
                }
            }
            while (buttonList.size()>(list.size()+1)){
                if (showMesgSys) System.out.println("IngresarFrame.setButtonsListenersListItemPanel.RemovingExcedent");
                listItemPanel.remove(buttonList.get(buttonList.size()-1));
                listenerList.remove(listenerList.get(listenerList.size()-1));
                buttonList.remove(buttonList.get(buttonList.size()-1));
            }
        }        
        listItemPanel.setPreferredSize(new Dimension(listItemPanel.getWidth(),20+initialY+stepY*((list.size()+2)/xElements)));
    }
    private class DeInventarioActionListener implements ActionListener{
        public DeInventarioActionListener(int i){ 
            if (i!=0){
                switch (clase){
                    case Ingrediente:
                        dto = IDataBase.searchByIdInDB(ingredienteList.get(i-1).getID(), ingredienteTree);
                        break;
                    case SubProducto:
                        dto = IDataBase.searchByIdInDB(subProductoList.get(i-1).getID(), subProductoTree);
                        break;
                    case Producto:
                        dto = IDataBase.searchByIdInDB(productoList.get(i-1).getID(), productoTree);
                        break;
                    case DeLaCarta:
                        dto = IDataBase.searchByIdInDB(deLaCartaList.get(i-1).getID(), deLaCartaTree);
                        break;
                    default:
                        dto = null;
                        break;
                }
            }
            else{
                int addLvl = 100;
                switch (clase){
                    case Ingrediente:
                        if (ingredienteActual.getID()<10) addLvl = 10;
                        dto = new IngredienteDTO(ingredienteActual,IDataBase.getAviableID(ingredienteActual.getID()*addLvl),"");
                        break;
                    case SubProducto:
                        if (subProductoActual.getID()<10) addLvl = 10;
                        dto = new SubProductoDTO(subProductoActual,IDataBase.getAviableID(subProductoActual.getID()*addLvl),"");
                        break;
                    case Producto:     
                        if (productoActual.getID()<10) addLvl = 10;
                        dto = new ProductoDTO(productoActual,IDataBase.getAviableID(productoActual.getID()*addLvl),"");
                        break;
                    case DeLaCarta:
                        if (deLaCartaActual.getID()<10) addLvl = 10;
                        dto = new DeLaCartaDTO(deLaCartaActual,IDataBase.getAviableID(deLaCartaActual.getID()*addLvl),"");
                        break;
                    default:
                        dto = null;
                        break;                        
                }
            }
            if (showMesgSys) System.out.println("IngresarFrame.DeInventarioActionListener.Constuctor "+dto);
        }
        @Override public void actionPerformed(ActionEvent e) {
            deInventarioClickExecuter(dto);
        }
        private final DeInventario dto;
    }
    private <G extends DeInventario> void deInventarioClickExecuter (G dto){
        switch (clase){
            case Ingrediente:
                ingredienteActual = (IngredienteDTO) dto;
                setButtonsListenersSingleItemPanel(ingredienteActual);
                break;
            case SubProducto:
                subProductoActual = (SubProductoDTO) dto;
                setButtonsListenersSingleItemPanel(subProductoActual);
                break;
            case Producto:
                productoActual = (ProductoDTO) dto;
                precioTextArea.setText(Integer.toString((int)productoActual.getPrecio()));
                setButtonsListenersSingleItemPanel(productoActual);
                break;
            case DeLaCarta:
                deLaCartaActual = (DeLaCartaDTO) dto;
                precioTextArea.setText(Integer.toString((int)deLaCartaActual.getPrecio()));
                setButtonsListenersSingleItemPanel(deLaCartaActual);
                break;
        }
        if (dto.isFinal()){
            isNew = "".equals(dto.getNombre());
            nombreTextArea.setText(dto.getNombre());
            if (dto.getClase()==DeInventarioType.Producto){
                typeComboBox.setSelectedIndex(0);
                typeComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                unidadComboBox.setSelectedIndex(0);
                cantidadTextArea.setText(Double.toString(dto.getMeasure().getQuantity()));
            }
            else if (dto.getClase()==DeInventarioType.Ingrediente || dto.getClase()==DeInventarioType.SubProducto){
                typeComboBox.setSelectedIndex(dto.getMeasure().getType().ordinal());
                typeComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                unidadComboBox.setSelectedIndex(dto.getMeasure().getFixedPos());
                cantidadTextArea.setText(Double.toString(dto.getMeasure().getFixedQuantity()));
            }
            isFinalCheckBox.setSelected(dto.isFinal());
            actualizeSingleItemPanel(dto);
            setVisiblePanel(singleItemScroll);
        }
        else{
            setButtonsListItemPanel(dto);
            setVisiblePanel(listItemScroll);
        }
    }
    private <G extends DeInventario> void setButtonsListenersSingleItemPanel(G dto){
        for (ActionListener actionListener : atrasButton.getActionListeners())
            atrasButton.removeActionListener(actionListener);
        for (ActionListener actionListener : borrarButton.getActionListeners())
            borrarButton.removeActionListener(actionListener);
        for (ActionListener actionListener : addIngredButton.getActionListeners()) 
            addIngredButton.removeActionListener(actionListener);
        for (ActionListener actionListener : addSubProButton.getActionListeners())
            addSubProButton.removeActionListener(actionListener);
        for (ActionListener actionListener : addProducButton.getActionListeners())
            addProducButton.removeActionListener(actionListener);
        for (ActionListener actionListener : addDeLaCaButton.getActionListeners())
            addDeLaCaButton.removeActionListener(actionListener);
        for (ActionListener actionListener : cargarButton.getActionListeners())
            cargarButton.removeActionListener(actionListener);
        switch(clase){
            case Ingrediente:
                break;
            case SubProducto:
                addIngredButton.addActionListener(new IngredienteAddActionListener((SubProductoDTO) dto));
                addSubProButton.addActionListener(new SubProductoAddActionListener((SubProductoDTO) dto));
                break;
            case Producto:
                addIngredButton.addActionListener(new IngredienteAddActionListener((ProductoDTO) dto));
                addSubProButton.addActionListener(new SubProductoAddActionListener((ProductoDTO) dto));
                addProducButton.addActionListener(new ProductoAddActionListener((ProductoDTO) dto));
                break;
            case DeLaCarta:
                addSubProButton.addActionListener(new SubProductoAddActionListener((DeLaCartaDTO) dto));
                addProducButton.addActionListener(new ProductoAddActionListener((DeLaCartaDTO) dto));
                addDeLaCaButton.addActionListener(new DeLaCartaAddActionListener((DeLaCartaDTO) dto));
                break;
        }
        borrarButton.addActionListener(new BorrarActionListener(clase));
        atrasButton.addActionListener(new AtrasActionListener(clase));
        cargarButton.addActionListener(new CargarActionListener());
    }
    private class AtrasActionListener implements ActionListener{
        public <G extends DeInventario> AtrasActionListener (DeInventarioType clase){
            switch(clase){
                case Ingrediente:
                    dto = ingredienteActual;
                    break;
                case SubProducto:
                    dto = subProductoActual;
                    break;
                case Producto:
                    dto = productoActual;
                    break;
                case DeLaCarta:
                    dto = deLaCartaActual;
                    break;
            }   
        }
        @Override public void actionPerformed(ActionEvent e) {
            if (null!=dto.getUp()) {
                switch(clase){
                    case Ingrediente:
                        IngredienteDTO dto1 = (IngredienteDTO) dto;
                        deInventarioClickExecuter(dto1.getUp());
                        break;
                    case SubProducto:
                        SubProductoDTO dto2 = (SubProductoDTO) dto;
                        deInventarioClickExecuter(dto2.getUp());
                        break;
                    case Producto:
                        ProductoDTO dto3 = (ProductoDTO) dto;
                        deInventarioClickExecuter(dto3.getUp());
                        break;
                    case DeLaCarta:
                        DeLaCartaDTO dto4 = (DeLaCartaDTO) dto;
                        deInventarioClickExecuter(dto4.getUp());
                        break;
                }
            }
        }
        DeInventario dto;
    }
    private class BorrarActionListener implements ActionListener{
        public <G extends DeInventario> BorrarActionListener (DeInventarioType clase){
            switch(clase){
                case Ingrediente:
                    dto = ingredienteActual;
                    break;
                case SubProducto:
                    dto = subProductoActual;
                    break;
                case Producto:
                    dto = productoActual;
                    break;
                case DeLaCarta:
                    dto = deLaCartaActual;
                    break;
        }   }
        @Override
        public void actionPerformed(ActionEvent e) {
            int desition = JOptionPane.showConfirmDialog(null, "Realmente desea borrar el "+clase+" "+dto.getNombre()+"?", "Borrar "+clase, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (desition==JOptionPane.OK_OPTION){
                String mess = "Los siguientes elementos seran modificados: ";
                mess+= IDataBase.getUsages(dto);
                mess+="\nDesea Continuar?";
                desition = JOptionPane.showConfirmDialog(null, mess, "Borrar "+clase, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (desition==JOptionPane.OK_OPTION){
                    JOptionPane.showMessageDialog(null, "Borrado");
                    IDataBase.delete(dto);
                    exit_hide();
        }   }   }
        DeInventario dto;
    }    
    private class IngredienteAddActionListener implements ActionListener{
        public IngredienteAddActionListener(DeInventario ingredienteDTO){
            this.ingredienteDTO = ingredienteDTO;
        }
        @Override public void actionPerformed(ActionEvent e) {
            if (showMesgSys) System.out.println("IngresarFrame.IngredienteAddActionListener.actionPerformed: "+ingredienteDTO);
            loadInActual();
            ingredienteVisualSetter.startframe(ingredienteDTO);
            if (showMesgSys) System.out.println("IngresarFrame.IngredienteAddActionListener.actionPerformed: "+ingredienteDTO);
            actualizeSingleItemPanel(ingredienteDTO);
        }
        private final DeInventario ingredienteDTO;
    }
    private class SubProductoAddActionListener implements ActionListener{
        public SubProductoAddActionListener(DeInventario subProductoDTO){
            this.subProductoDTO = subProductoDTO;
        }
        @Override public void actionPerformed(ActionEvent e) {
            if (showMesgSys) System.out.println("IngresarFrame.SubProductoAddActionListener.actionPerformed: "+subProductoDTO);
            loadInActual();
            subProductoVisualSetter.startframe(subProductoDTO);
            if (showMesgSys) System.out.println("IngresarFrame.SubProductoAddActionListener.actionPerformed: "+subProductoDTO);            
            actualizeSingleItemPanel(subProductoDTO);
        }
        private final DeInventario subProductoDTO;
    }
    private class ProductoAddActionListener implements ActionListener{
        public ProductoAddActionListener(DeInventario productoDTO){
            this.productoDTO = productoDTO;
        }
        @Override public void actionPerformed(ActionEvent e) {
            if (showMesgSys) System.out.println("IngresarFrame.ProductoAddActionListener.actionPerformed: "+productoDTO.getID());
            loadInActual();
            if (showMesgSys) System.out.println("IngresarFrame.ProductoAddActionListener.actionPerformed: "+productoDTO.getID());
            productoVisualSetter.startframe(productoDTO);
            if (showMesgSys) System.out.println("IngresarFrame.ProductoAddActionListener.actionPerformed: "+productoDTO.getID());
            actualizeSingleItemPanel(productoDTO);
            if (showMesgSys) System.out.println("IngresarFrame.ProductoAddActionListener.actionPerformed: "+productoDTO.getID());
        }
        private final DeInventario productoDTO;
    }
    private class DeLaCartaAddActionListener implements ActionListener{
        public DeLaCartaAddActionListener(DeInventario deLaCartaDTO){
            this.deLaCartaDTO = deLaCartaDTO;
        }
        @Override public void actionPerformed(ActionEvent e) {
            loadInActual();
            deLaCartaVisualSetter.startframe(deLaCartaDTO);
            actualizeSingleItemPanel(deLaCartaDTO);
        }
        private final DeInventario deLaCartaDTO;
    }
    private class CargarActionListener implements ActionListener{
        @Override public void actionPerformed(ActionEvent e) { 
            if (loadInActual()){
                cargar();
                int addLvl = 100;
                switch (clase){
                    case Ingrediente:
                        if (ingredienteActual.getID()<100) addLvl = 10;
                        setButtonsListItemPanel(IDataBase.searchByIdInDB(ingredienteActual.getID()/addLvl, IDataBase.INGREDIENTEDTO));
                        break;
                    case SubProducto:
                        if (subProductoActual.getID()<100) addLvl = 10;
                        setButtonsListItemPanel(IDataBase.searchByIdInDB(subProductoActual.getID()/addLvl, IDataBase.SUBPRODUCTODTO));
                        break;
                    case Producto:
                        if (productoActual.getID()<100) addLvl = 10;
                        setButtonsListItemPanel(IDataBase.searchByIdInDB(productoActual.getID()/addLvl, IDataBase.PRODUCTODTO));
                        break;
                    case DeLaCarta:
                        if (deLaCartaActual.getID()<100) addLvl = 10;
                        setButtonsListItemPanel(IDataBase.searchByIdInDB(deLaCartaActual.getID()/addLvl, IDataBase.DELACARTADTO));
                        break;
                }
                setVisiblePanel(listItemScroll);
            }
        }
    }
    private boolean loadInActual(){
        boolean validData = true;
        double precioDoub=0,cantDoub=0;
        int previousItems;
        if (!cantidadTextArea.getText().isEmpty()){
            try { cantDoub = Double.parseDouble(cantidadTextArea.getText()); }
            catch (NumberFormatException ex) {
                validData = false;
                String mess = "Ingrese dato valido en campo Cantidad: \n< ";
                mess = mess.concat("Error: " + ex.getMessage());
                JOptionPane.showMessageDialog(null,mess);
        }   }
        if ((clase.equals(DeInventarioType.Producto)||clase.equals(DeInventarioType.DeLaCarta))&&!precioTextArea.getText().isEmpty()){
            try { precioDoub = Double.parseDouble(precioTextArea.getText()); }
            catch (NumberFormatException ex) {
                validData = false;
                String mess = "Ingrese dato valido en campo Precio: \n< ";
                mess = mess.concat("Error: " + ex.getMessage() + " >\n< ");
                JOptionPane.showMessageDialog(null,mess);
        }   }
        if (validData){
            String nombStrg;
            if (nombreTextArea.getText().length()>30)   nombStrg = nombreTextArea.getText().substring(0,29);
            else                                        nombStrg = nombreTextArea.getText();
            if (nombStrg.length()>2)    
                nombStrg = nombStrg.toUpperCase().charAt(0) + nombStrg.substring(1,nombStrg.length());
            nombStrg = nombStrg.replace(".", "_");
            nombStrg = nombStrg.replace(",", "_");
            nombStrg = nombStrg.replace(":", "_");
            nombStrg = nombStrg.replace("ñ", "nn");
            switch(clase){
                case Ingrediente:
                    dto = ingredienteActual;
                    break;
                case SubProducto:
                    dto = subProductoActual;
                    break;
                case Producto:
                    dto = productoActual;
                    break;
                case DeLaCarta:
                    dto = deLaCartaActual;
                    break;
            }
            dto.setNombre(nombStrg);
            dto.setFinal(isFinalCheckBox.isSelected());
            previousItems = 0;
            if (!clase.equals(DeInventarioType.DeLaCarta))
                dto.setCantidad(cantDoub,unidadComboBox.getSelectedItem().toString());
            if (clase.equals(DeInventarioType.SubProducto)||clase.equals(DeInventarioType.Producto)){
                for (int i=0; i<((IRetainerIngrediente)dto).getIngredienteList().size(); i++){
                    try { cantDoub = Double.parseDouble(componentesTextAreaList.get(i).getText()); }
                    catch (NumberFormatException ex) { 
                        cantDoub = 0.0; String mess = ex.getMessage();
                        JOptionPane.showMessageDialog(null,mess);}
                    ((IRetainerIngrediente)dto).getIngredienteList().get(i).setCantidad(cantDoub);
                } previousItems+=((IRetainerIngrediente)dto).getIngredienteList().size();
            }
            if (clase.equals(DeInventarioType.SubProducto)||clase.equals(DeInventarioType.Producto)||clase.equals(DeInventarioType.DeLaCarta)){
                for (int i=0; i<((IRetainerSubProducto)dto).getSubProductoList().size(); i++){
                    try { cantDoub = Double.parseDouble(componentesTextAreaList.get(i+previousItems).getText()); }
                    catch (NumberFormatException ex) { 
                        cantDoub = 0.0; String mess = ex.getMessage();
                        JOptionPane.showMessageDialog(null,mess);}
                    ((IRetainerSubProducto)dto).getSubProductoList().get(i).setCantidad(cantDoub);
                } previousItems+=((IRetainerSubProducto)dto).getSubProductoList().size();
            }
            if (clase.equals(DeInventarioType.Producto)||clase.equals(DeInventarioType.DeLaCarta)){
                ((ISellable)dto).setPrecio(precioDoub);
                for (int i=0; i<((IRetainerProducto)dto).getProductoList().size(); i++){
                    try { cantDoub = Double.parseDouble(componentesTextAreaList.get(i+previousItems).getText()); }
                    catch (NumberFormatException ex) { 
                        cantDoub = 0.0; String mess = ex.getMessage();
                        JOptionPane.showMessageDialog(null,mess);}
                    ((IRetainerProducto)dto).getProductoList().get(i).setCantidad(cantDoub);
                } previousItems+=((IRetainerProducto)dto).getProductoList().size();
            }
            if (clase.equals(DeInventarioType.DeLaCarta)){
                for (int i=0; i<((IRetainerDeLaCarta)dto).getDeLaCartaList().size(); i++){
                    try { cantDoub = Double.parseDouble(componentesTextAreaList.get(i+previousItems).getText()); }
                    catch (NumberFormatException ex) { 
                        cantDoub = 0.0; String mess = ex.getMessage();
                        JOptionPane.showMessageDialog(null,mess);}
                    ((IRetainerDeLaCarta)dto).getDeLaCartaList().get(i).setCantidad(cantDoub);
                } previousItems+=((IRetainerDeLaCarta)dto).getDeLaCartaList().size();
            }
            this.consoleFlush(); consoleAppend(dto.treeToString());
            if (showMesgSys) System.out.println("IngresarFrame.loadInActual: "+dto);
        }
        else{
            String mess = "Ingrese datos Validos";
            JOptionPane.showMessageDialog(null,mess);
        }
        return validData;
    }
    private void cargar(){
        if (showMesgSys) System.out.println("IngresarFrame.cargar: "+dto);
        switch(clase){
            case Ingrediente:
                if (isNew) IDataBase.insert((IngredienteDTO)dto);
                else       IDataBase.update((IngredienteDTO)dto);
                break;
            case SubProducto:
                if (isNew) IDataBase.insert((SubProductoDTO)dto);
                else       IDataBase.update((SubProductoDTO)dto);
                break;
            case Producto:
                if (isNew) IDataBase.insert((ProductoDTO)dto);
                else       IDataBase.update((ProductoDTO)dto);
                break;
            case DeLaCarta:
                if (isNew) IDataBase.insert((DeLaCartaDTO)dto);
                else       IDataBase.update((DeLaCartaDTO)dto);
                break;
        }        
    }
    
    private class UnidadActionListener implements ActionListener{
        @Override public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox)e.getSource();
            double cant = 0,cant_ant;
            try { cant = Double.parseDouble(cantidadTextArea.getText()); }
            catch (NumberFormatException ex) {}
            try{
            switch (clase) {
                case Ingrediente:
                    cant_ant = ingredienteActual.getCantidad(unidadComboBox.getSelectedItem().toString());
                    cantidadTextArea.setText(String.valueOf(cant_ant));
                    ingredienteActual.setCantidad(cant_ant,unidadComboBox.getSelectedItem().toString());
                    cantidadLabel.setText("Total Inventariado en "+(String)cb.getSelectedItem());
                    break;
                case SubProducto:
                    cant_ant = subProductoActual.getCantidad(unidadComboBox.getSelectedItem().toString());
                    cantidadTextArea.setText(String.valueOf(cant_ant));
                    subProductoActual.setCantidad(cant_ant,unidadComboBox.getSelectedItem().toString());
                    cantidadLabel.setText("Total "+Measurable.Type.values()[typeComboBox.getSelectedIndex()]+" en "+(String)cb.getSelectedItem());
                    break;
                case Producto:
                    cant_ant = productoActual.getCantidad(unidadComboBox.getSelectedItem().toString());
                    cantidadTextArea.setText(String.valueOf(cant_ant));
                    productoActual.setCantidad(cant_ant,unidadComboBox.getSelectedItem().toString());
                    cantidadLabel.setText("Cantidad Existente en "+(String)cb.getSelectedItem());
                    break;
                case DeLaCarta:
                    cant_ant = deLaCartaActual.getCantidad(unidadComboBox.getSelectedItem().toString());
                    cantidadTextArea.setText(String.valueOf(cant_ant));
                    deLaCartaActual.setCantidad(cant_ant,unidadComboBox.getSelectedItem().toString());
                    cantidadLabel.setText("Cantidad Existente en "+(String)cb.getSelectedItem());
                    break;
            }
            }
            catch (NullPointerException ex){}
        }
    }
    private class TypeActionListener implements ActionListener{
        @Override public void actionPerformed(ActionEvent e) {
            Measurable.configureUnitComboBox(Measurable.Type.values()[typeComboBox.getSelectedIndex()], unidadComboBox);
        }
    }
    private void createSingleItemPanel(){
        singleItemPanel.setBounds(LeftPanel_Rectangle);
        singleItemPanel.setLayout(null);
                        
        JLabel  nombreLabel     =   new JLabel("Nombre:");
        JLabel  unidadLabel     =   new JLabel("Unidad de Medida:");
        switch (clase) {
            case Ingrediente:
            case SubProducto:
                Measurable.configureTypeComboBox(typeComboBox);
                typeComboBox.addActionListener(new TypeActionListener());
                Measurable.configureUnitComboBox(Measurable.Type.Peso, unidadComboBox);
                unidadComboBox.addActionListener(new UnidadActionListener());
                unidadComboBox.setSelectedIndex(0);
                typeComboBox.setSelectedIndex(0);
                unidadComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                typeComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                break;
            case Producto:
                typeComboBox.addItem(Measurable.Type.Cantidad);
                Measurable.configureUnitComboBox(Measurable.Type.Cantidad, unidadComboBox);
                unidadComboBox.addActionListener(new UnidadActionListener());
                typeComboBox.setSelectedIndex(0);
                unidadComboBox.setSelectedIndex(0);
                unidadComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                typeComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                break;
        }
        JLabel  precioLabel     =   new JLabel("Precio:");
        JLabel  isFinalLabel    =   new JLabel("¿Es Utilizable?:");
        
        int x = 50, y = 100, w = 200, h = 50, dx = 200, dw=80, dy = 50;
        singleItemPanel.add(atrasButton);      atrasButton.setBounds       (  30, 25, 100, 50);
        singleItemPanel.add(borrarButton);     borrarButton.setBounds      ( 140, 25, 390, 50);
        singleItemPanel.add(nombreLabel);      nombreLabel.setBounds       (    x, y,    w, h);
        singleItemPanel.add(nombreTextArea);   nombreTextArea.setBounds    ( dx+x, y, dw+w, h); y+=dy;
        singleItemPanel.add(isFinalLabel);     isFinalLabel.setBounds      (    x, y,    w, h);
        singleItemPanel.add(isFinalCheckBox);  isFinalCheckBox.setBounds   ( dx+x, y, dw+w, h); y+=dy;
        
        if (clase.equals(DeInventarioType.Ingrediente)||clase.equals(DeInventarioType.SubProducto)||clase.equals(DeInventarioType.Producto)){
            singleItemPanel.add(unidadLabel);      unidadLabel.setBounds       (    x, y,    w, h);
            singleItemPanel.add(typeComboBox);     typeComboBox.setBounds      ( dx+x, y,(dw+w)/2, h);
            singleItemPanel.add(unidadComboBox);   unidadComboBox.setBounds    ( dx+x+(dw+w)/2, y,(dw+w)/2, h); y+=dy;
            singleItemPanel.add(cantidadLabel);    cantidadLabel.setBounds     (    x, y,    w, h);
            singleItemPanel.add(cantidadTextArea); cantidadTextArea.setBounds  ( dx+x, y, dw+w, h); y+=dy;
        }
        if (clase.equals(DeInventarioType.Producto)||clase.equals(DeInventarioType.DeLaCarta)){
            singleItemPanel.add(precioLabel);      precioLabel.setBounds       (    x, y,    w, h);
            singleItemPanel.add(precioTextArea);   precioTextArea.setBounds    ( dx+x, y, dw+w, h); y+=dy;
        }
        if (clase.equals(DeInventarioType.SubProducto)||clase.equals(DeInventarioType.Producto))    singleItemPanel.add(addIngredButton);     
        if (!clase.equals(DeInventarioType.Ingrediente))                                            singleItemPanel.add(addSubProButton);
        if (clase.equals(DeInventarioType.Producto)||clase.equals(DeInventarioType.DeLaCarta))      singleItemPanel.add(addProducButton);     
        if (clase.equals(DeInventarioType.DeLaCarta))                                               singleItemPanel.add(addDeLaCaButton);
        singleItemPanel.add(cargarButton);
        moveSingleItemLastButtons();
                
        singleItemScroll.setBounds(LeftPanel_Rectangle);
        singleItemScroll.setViewportView(singleItemPanel);
        singleItemScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        singleItemScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        containerPanel.add(singleItemScroll);
    }
    private <G extends DeInventario> void actualizeSingleItemPanel(G dto){
        if (showMesgSys) System.out.println("IngresarFrame.actualizeSingleItemPanel: \n"+dto);
        this.consoleFlush();
        if ("".equals(dto.getNombre())) consoleAppend("Nuevo "+clase);
        consoleAppend(dto.treeToString());
        for (int i=0; i<componentesLabelList.size(); i++){
            singleItemPanel.remove(componentesLabelList.get(i));
            singleItemPanel.remove(componentesTextAreaList.get(i));
        }
        componentesLabelList.clear();
        componentesTextAreaList.clear();
        switch (clase){
            case Ingrediente:
                break;
            case SubProducto:
                setLabelAndTextArea(subProductoActual.getComposition());
                break;
            case Producto:
                setLabelAndTextArea(productoActual.getComposition());
                break;
            case DeLaCarta:
                setLabelAndTextArea(deLaCartaActual.getComposition());
                break;
        }
        moveSingleItemLastButtons();
        singleItemScroll.revalidate(); 
    }
    private <G extends DeInventario>void setLabelAndTextArea(ArrayList<G> dtoList){
        String msg;
        JLabel aux1;
        JTextArea aux2;
        int previousComponents = 5, dy = 50;
        switch (clase){
            case Ingrediente:
                previousComponents = 5; break;
            case SubProducto:
                previousComponents = 5; break;
            case Producto:
                previousComponents = 6; break;
            case DeLaCarta:
                previousComponents = 5; break;
        }
        for (int i=0; i<dtoList.size(); i++){
            msg = " Cont. de "+dtoList.get(i).getNombre()+" en ";
            msg = msg +dtoList.get(i).getUnidadBase();
            aux1 = new JLabel(msg);
            componentesLabelList.add(aux1);
            aux1.setBounds( 50, (previousComponents+componentesLabelList.size())*dy, 200 , 50);
            singleItemPanel.add(aux1);
            aux2 = new JTextArea(String.valueOf(round(dtoList.get(i).getCantidad()))); 
            aux2.setBounds(250, (previousComponents+componentesLabelList.size())*dy, 280 , 50);
            componentesTextAreaList.add(aux2);
            singleItemPanel.add(aux2);
        }
    }
    private void moveSingleItemLastButtons(){
        int previousComponents = 0;
        switch (clase){
            case Ingrediente:
                previousComponents = 6; break;
            case SubProducto:
                previousComponents = 6; break;
            case Producto:
                previousComponents = 6; break;
            case DeLaCarta:
                previousComponents = 6; break;
        }
        int dy = 50, x = 250, y = (previousComponents+componentesLabelList.size())*dy, w = 280, h = 50;
        
        if (clase.equals(DeInventarioType.Producto))  y+=dy;
        if (clase.equals(DeInventarioType.DeLaCarta))  y-=dy;
        if (clase.equals(DeInventarioType.SubProducto)||clase.equals(DeInventarioType.Producto)){
            addIngredButton.setBounds      ( x, y, w, h); y+=dy;}
        if (clase.equals(DeInventarioType.SubProducto)||clase.equals(DeInventarioType.Producto)||clase.equals(DeInventarioType.DeLaCarta)){
            addSubProButton.setBounds      ( x, y, w, h); y+=dy;}
        if (clase.equals(DeInventarioType.Producto)||clase.equals(DeInventarioType.DeLaCarta)){
            addProducButton.setBounds      ( x, y, w, h); y+=dy;}
        if (clase.equals(DeInventarioType.DeLaCarta)){
            addDeLaCaButton.setBounds      ( x, y, w, h); y+=dy;}
        cargarButton.setBounds      ( x-145, y, w+100, h); y+=dy;
        
        switch (clase){
            case Ingrediente:
                singleItemPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width,(9+componentesLabelList.size())*dy));
                break;
            case SubProducto:
                singleItemPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width,(10+componentesLabelList.size())*dy));
                break;
            case Producto:
                singleItemPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width,(12+componentesLabelList.size())*dy));
                break;
            case DeLaCarta:
                singleItemPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width,(11+componentesLabelList.size())*dy));
                break;
        }
        singleItemPanel.repaint();
    }
}
