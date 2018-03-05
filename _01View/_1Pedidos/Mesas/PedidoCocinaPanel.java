/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _01View._1Pedidos.Mesas;

import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 *
 * @author MoisesE
 */
public class PedidoCocinaPanel extends JPanel{
    
    public PedidoCocinaPanel (){
        super();
        pedidoCocinaTextArea = new JTextArea(27,55);
        add(pedidoCocinaTextArea);
        pedidoCocinaTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        pedidoCocinaTextArea.setEditable(false);
        pedidoCocinaTextArea.setLineWrap(true);         //Ajusta el objeto al tamaño inicial, corta las palabras hasta la proxima linea
        pedidoCocinaTextArea.setWrapStyleWord(true);    //Da enter a la palabra para no cortarla con el ajuste de linea
        
    }
    
    public void append(String text){
        pedidoCocinaTextArea.append(text+"\n");
        
    }
    
    public void flush(){
        pedidoCocinaTextArea.setText("");
    }
    
    //ArrayList y JTable
    private JTextArea pedidoCocinaTextArea;
}
