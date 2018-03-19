/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _01View._6Configuracion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import _04DataAccessObject.generalController;

/**
 *
 * @author MoisesE
 */
public class ConfiguracionPanel extends javax.swing.JPanel{
    
    public ConfiguracionPanel(){
        super();
        initComponents();
        moveComponents();
        createListenerList();
    }
    
    /*
    
    Rutina para iniciar otro frame como reaccion a un boton
    
    public class ConfiguracionFrameButton implements ActionListener{
        public ConfiguracionFrameButton(PedidosFrame pedidosFrame){
            this.pedidosFrame = pedidosFrame;
        }
        @Override public void actionPerformed(ActionEvent e) {
            pedidosFrame.startframe();
        }
        private final PedidosFrame pedidosFrame;
    }
    */
    
    private ArrayList<JButton> buttonList;
    private ArrayList<ActionListener> listenList;
    
    private void initComponents(){
        int i=0;
        JButton btnAux;
        buttonList = new ArrayList<>();
        
        btnAux = new JButton("<html><center>Arreglo<br>de Mesas</center></html>");   buttonList.add(btnAux);
        this.add(buttonList.get(i)); i++; btnAux.setEnabled(false);
        btnAux = new JButton("<html><center>Encabezado<br>de Facturas</center></html>");   buttonList.add(btnAux);
        this.add(buttonList.get(i)); i++; btnAux.setEnabled(false);
        
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
        ActionListener actionListener;
        listenList = new ArrayList<>();
        
        actionListener = (ActionEvent e) -> {  };
        listenList.add(actionListener); buttonList.get(i).addActionListener(actionListener); i++;
        actionListener = (ActionEvent e) -> {  };
        listenList.add(actionListener); buttonList.get(i).addActionListener(actionListener); i++;
                
    }
}
