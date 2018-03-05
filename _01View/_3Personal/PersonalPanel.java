/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _01View._3Personal;

import _01View.MainFrame;
import _01View.Templates.IFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import _04DataAccessObject.generalController;

/**
 *
 * @author mpuyosa91
 */
public class PersonalPanel extends javax.swing.JPanel{
    
    public PersonalPanel(MainFrame mainFrame){
        super();
        this.contextFrame = mainFrame;
        initComponents();
        moveComponents();
        createListenerList();
    }
    
    private final MainFrame contextFrame;
    private ArrayList<JButton>  buttonList;    
    private ArrayList<IFrame>   frameList;
    private ArrayList<ActionListener> listenList;
    
    private void initComponents(){
        int i=0;
        JButton btnAux;
        buttonList = new ArrayList<>();
        btnAux = new JButton("<html><center>Ajustar<br>Grupo de Trabajo</center></html>");   buttonList.add(btnAux);
        this.add(buttonList.get(i)); i++; btnAux.setEnabled(false);
        btnAux = new JButton("<html><center>Declarar<br>Ingreso/Salida</center></html>");   buttonList.add(btnAux);
        this.add(buttonList.get(i)); i++; btnAux.setEnabled(false);
    }
    
    private void moveComponents(){
        int initialX=10,initialY=10;
        int stepX=190,stepY=190;
        for (int i=0;i<buttonList.size();i++){
            buttonList.get(i).setBounds(stepX*(i%4)+initialX,stepY*(i/4)+initialY,150,150);
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
        int i = 0;
        listenList = new ArrayList<>();
        ActionListener actionListener;
        actionListener = (ActionEvent e) -> {  };
        listenList.add(actionListener); buttonList.get(i).addActionListener(actionListener); i++;
        actionListener = (ActionEvent e) -> {  };
        listenList.add(actionListener); buttonList.get(i).addActionListener(actionListener); i++;
    }
    
}
