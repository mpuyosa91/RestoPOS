/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View._4Ventas;

import Controller.DataBase.IDataBase;
import View._1Pedidos.Mesas.PedidosFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;

/**
 *
 * @author MoisesE
 */
public class VentasPanel extends javax.swing.JPanel{
    
    public VentasPanel(){
        super();
        initComponents();
        moveComponents();
        createListenerList();
    }
    
    public class PedidosFrameButton implements ActionListener{
        public PedidosFrameButton(PedidosFrame pedidosFrame){
            this.pedidosFrame = pedidosFrame;
        }
        @Override public void actionPerformed(ActionEvent e) {
            pedidosFrame.startframe();
        }
        private final PedidosFrame pedidosFrame;
    }
    
    private ArrayList<JButton> buttonList;
    private ArrayList<ActionListener> listenList;
    
    private void initComponents(){
        int i=0;
        JButton btnAux;
        buttonList = new ArrayList<>();
        btnAux = new JButton("<html><center>Generar <br>Corte De Turno</center></html>");   buttonList.add(btnAux);
        this.add(buttonList.get(i)); i++; btnAux.setEnabled(false);
        btnAux = new JButton("<html><center>Generar <br>Corte Diario</center></html>");   buttonList.add(btnAux);
        this.add(buttonList.get(i)); i++;
    }
    
    private void moveComponents(){
        int initialX=10,initialY=10;
        int stepX=190,stepY=190;
        for (int i=0;i<buttonList.size();i++){
            buttonList.get(i).setBounds(stepX*(i%4)+initialX,stepY*(i/4)+initialY,150,150);
        }
    }
    
    private void createListenerList(){
        int i = 0;
        listenList = new ArrayList<>();
        ActionListener actionListener;
        actionListener = (ActionEvent e) -> { IDataBase.getTurnoFacturation(); };
        listenList.add(actionListener); buttonList.get(i).addActionListener(actionListener); i++;
        actionListener = (ActionEvent e) -> { IDataBase.getDayFacturation(); };
        listenList.add(actionListener); buttonList.get(i).addActionListener(actionListener); i++;
    }
}
