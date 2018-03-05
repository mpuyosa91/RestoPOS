/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _01View._2Inventario.Listar;

import _03Model.ConfigurationDTO.Label;
import _03Model.Facility.ProductsAndSupplies.Inventory;
import _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType;
import _01View.MainFrame;
import _01View.Templates.IFrame;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import static java.lang.Math.round;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import _04DataAccessObject.generalController;

/**
 *
 * @author MoisesE
 */
public class ListFrame extends javax.swing.JFrame implements IFrame{

    public boolean          showMesgInConsole       = false;
    public final float      xFrameContextRatio      = (float) (10.0/10.0);
    public final float      yFrameContextRatio      = (float) (10.0/10.0);
    public final int        navigationPanel_ySize   = 60;
    public final int        btnExit_xySize          = 50;
    public final int        xSize;
    public final int        ySize;    
    public final int        xPosition;
    public final int        yPosition;
    public final Rectangle  LeftPanel_Rectangle;
    public final MainFrame   contextFrame;
    
    public ListFrame(MainFrame contextFrame, DeInventarioType identifier){
        super();
        this.contextFrame           = contextFrame; 
        this.xSize                  = (int) round(contextFrame.xSize * xFrameContextRatio);
        this.ySize                  = (int) round(contextFrame.ySize * yFrameContextRatio);
        this.xPosition              = (int) round(contextFrame.xSize * ( 1 - xFrameContextRatio )/2.0);
        this.yPosition              = (int) round(contextFrame.ySize * ( 1 - yFrameContextRatio )/2.0);
        this.LeftPanel_Rectangle    = new Rectangle(0, navigationPanel_ySize, (int)(xSize-8), ySize - navigationPanel_ySize);
        this.identifier             = identifier;
        configureFrame(); 
        configureContainerPanels();
    }
    @Override
    public void startframe(){
        consoleTextArea.setText("");
//        switch(identifier){
//            case Ingrediente:
//                consoleTextArea.setText(generalController.INGREDIENTEDTO.treeToString());
//                break;
//            case SubProducto:
//                consoleTextArea.setText(generalController.SUBPRODUCTODTO.treeToString());
//                break;
//            case Producto:
//                consoleTextArea.setText(generalController.PRODUCTODTO.treeToString());
//                break;
//            case DeLaCarta:
//                consoleTextArea.setText(generalController.DELACARTADTO.treeToString());
//                break;
//        }
        consoleTextArea.setText(generalController.getModel(identifier).treeToString());
        listScroll.getVerticalScrollBar().setValue(listScroll.getVerticalScrollBar().getMinimum());
        listScroll.getHorizontalScrollBar().setValue(listScroll.getHorizontalScrollBar().getMinimum());
        setVisible(true);
    }
    
    @Override
    public void exit_hide(){
        setVisible(false);
    }
    
    protected ArrayList<JButton>    btnList; 
    protected ArrayList<JPanel>     panelList;
    protected JLabel                informationLabel;
    protected JScrollPane           listScroll;                
    protected JPanel                navigationPanel;
    protected JPanel                listPanel;
    protected Container             containerPanel;
    protected DeInventarioType      identifier;

    
    private         JButton     btnExit;
    private         JTextArea   consoleTextArea;
    
    private void configureFrame(){
        this.setBounds(xPosition, yPosition, xSize, ySize);
        this.setResizable(false);this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color color = UIManager.getColor("activeCaptionBorder");
        this.getRootPane().setBorder(BorderFactory.createLineBorder(color, 4));
    }

    private void configureContainerPanels(){
        containerPanel = new JPanel();
        this.add(containerPanel); 
        containerPanel.setLayout(null);
        createNavigationPanel();    containerPanel.add(navigationPanel);
        createContentPanel();       containerPanel.add(listScroll);
    }   
    
    private void createNavigationPanel(){
        navigationPanel = new JPanel();
        navigationPanel.setBounds (0, 0,xSize, navigationPanel_ySize );
        navigationPanel.setLayout(null);
        btnExit = new JButton("X"); navigationPanel.add(btnExit);
        btnExit.setBounds(xSize-navigationPanel_ySize, 0, btnExit_xySize, btnExit_xySize);
        btnExit.addActionListener((ActionEvent e) -> {
            exit_hide(); 
        });
        informationLabel = new JLabel();
        informationLabel.setText("Lista "+identifier.name());
        navigationPanel.add(informationLabel);
        informationLabel.setBounds(0,0,xSize-btnExit_xySize,navigationPanel_ySize);
    }  
    
    private void createContentPanel(){
        String aux  = "";
//        switch(identifier){
//            case Ingrediente:
//                aux = generalController.INGREDIENTEDTO.treeToString();
//                break;
//            case SubProducto:
//                aux = generalController.SUBPRODUCTODTO.treeToString();
//                break;
//            case Producto:
//                aux = generalController.PRODUCTODTO.treeToString();
//                break;
//            case DeLaCarta:
//                aux = generalController.DELACARTADTO.treeToString();
//                break;
//        }
        aux = generalController.getModel(identifier).treeToString();
        consoleTextArea = new JTextArea(aux);
        consoleTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        consoleTextArea.setEditable(false);
        consoleTextArea.setLineWrap(true);         //Ajusta el objeto al tamaño inicial, corta las palabras hasta la proxima linea
        consoleTextArea.setWrapStyleWord(true);    //Da enter a la palabra para no cortarla con el ajuste de linea       
        consoleTextArea.setVisible(true);
        consoleTextArea.setBounds(new Rectangle(0, 0, (int)(xSize-14), ySize - navigationPanel_ySize - 14));
        Dimension d;
        if (consoleTextArea.getLineCount()<43)  d = new Dimension (consoleTextArea.getWidth(),((43)*16));
        else                                    d = new Dimension (consoleTextArea.getWidth(),((consoleTextArea.getLineCount()+1)*16));
        consoleTextArea.setBounds(0,0,d.width,d.height);
        
        listPanel = new JPanel();
        listPanel.add(consoleTextArea);
        listPanel.setLayout(null);              
        listPanel.setPreferredSize(d);
        listPanel.repaint();
       
        
        listScroll = new JScrollPane(listPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScroll.setBounds(new Rectangle(0, navigationPanel_ySize, (int)(xSize-8), ySize - navigationPanel_ySize - 8));       
        listScroll.getVerticalScrollBar().setUnitIncrement(10);        
        listScroll.repaint();
    }
}
