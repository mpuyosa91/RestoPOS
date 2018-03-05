/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _01View._1Pedidos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import _01View.MainFrame;
import _01View._1Pedidos.Mesas.PedidosFrame;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import _04DataAccessObject.generalController;

/**
 *
 * @author MoisesE
 */
public class PedidosPanel extends javax.swing.JPanel{
    
    public PedidosPanel(MainFrame mainFrame){
        super();
        this.mainFrame = mainFrame;
        initComponents();
        moveComponents();
        createListenerList();
    }
    
    //When focus on this, refresh buttons colors by statuc (ocuped/free)
    
    public class PedidosFrameButton implements ActionListener{
        public PedidosFrameButton(PedidosFrame pedidosFrame){
            this.pedidosFrame = pedidosFrame;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            pedidosFrame.startframe(thisPanel);
        }
        private final PedidosFrame pedidosFrame;
    }
    
    public void setButtonsColors(){
        for (int i=1;i<generalController.CLIENTELIST.size()-1;i++){
            if (generalController.CLIENTELIST.get(i).isOcupada()){
                btnMesasList.get(i).setBackground(Color.lightGray);
                btnMesasList.get(i).setText("<html><center>"+generalController.CLIENTELIST.get(i).getIdentifier()+"<br>Ocupado"+"</center></html>");}
            else{
                btnMesasList.get(i).setBackground(new Color(0x5CE25C));
                btnMesasList.get(i).setText("<html><center>"+generalController.CLIENTELIST.get(i).getIdentifier()+"<br>Libre"+"</center></html>");}
        }  
    }
    
    private final PedidosPanel thisPanel = this;
    private ArrayList<JButton> btnMesasList;
    private ArrayList<PedidosFrame> pedidosFrameList;
    private ArrayList<ActionListener> listenList;
    private final MainFrame mainFrame;
    
    private void initComponents(){
        int i;
        btnMesasList = new ArrayList<>();
        for (i=0;i<generalController.CLIENTELIST.size();i++){
            btnMesasList.add(new JButton(generalController.CLIENTELIST.get(i).getIdentifier())); 
            this.add(btnMesasList.get(i));
        }   
        setButtonsColors();
        pedidosFrameList = new ArrayList<>();
        for (i=0;i<generalController.CLIENTELIST.size();i++){
            pedidosFrameList.add(new PedidosFrame(mainFrame,generalController.CLIENTELIST.get(i)));
        }
        this.addFocusListener(new FocusListener(){
            @Override public void focusGained(FocusEvent e) { setButtonsColors(); }
            @Override public void focusLost(FocusEvent e) { }            
        });
    }
    
    private void moveComponents(){
        int initialX=10,initialY=10;
        int stepX=190,stepY=190;
        for (int i=0;i<generalController.CLIENTELIST.size();i++){
            btnMesasList.get(i).setBounds(stepX*(i%4)+initialX,stepY*(i/4)+initialY,150,150);
        }
    }
    
    private void createListenerList(){
        listenList = new ArrayList<>();
        PedidosFrameButton aux;
        for (int i=0;i<generalController.CLIENTELIST.size();i++){
            aux = new PedidosFrameButton(pedidosFrameList.get(i));
            listenList.add(aux);
            btnMesasList.get(i).addActionListener(listenList.get(i));
        }
    }
}
