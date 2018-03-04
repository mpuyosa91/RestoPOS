/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Surveillance.SurveillanceReport;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author MoisesE
 */
public class WindowConsole {
 
    public static void print(String str){
        String msg = "["+Calendar.getInstance().getTime()+"]     "+str; 
        TEXT.append(msg);
        SurveillanceReport.log(msg.replace("\n", ""));
        JScrollBar vertical = SCROLLPANE.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    } 
    
    public static void restore(){
        FRAME.setState(JFrame.NORMAL);
    }

    public static void minimize(){
        FRAME.setState(JFrame.ICONIFIED);
    }
    
    public static void show(){
        FRAME.setVisible(true);
    }
    
    public static void hide(){
        FRAME.setVisible(false);
    }
    
    private static final String     GREETINGS   = getGreetings();
    private static final JTextArea  TEXT        = new JTextArea(GREETINGS);
    private static final JScrollPane SCROLLPANE = new JScrollPane();
    private static final JPanel     PANEL       = new JPanel(new BorderLayout());
    private static final JFrame     FRAME       = createFrame();
    
    private static JFrame createFrame(){
        JFrame r = new JFrame("Console");
        r.setLocation(0, 100);
        TEXT.repaint();
        PANEL.add(TEXT);
        PANEL.repaint();
        SCROLLPANE.setViewportView(PANEL);
        SCROLLPANE.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        SCROLLPANE.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        SCROLLPANE.setPreferredSize(new Dimension(1000,500));
        r.add(SCROLLPANE);
        r.pack();
        r.validate();
        r.setAlwaysOnTop(false);
        r.setVisible(false);
        return r;
    }
    private static String getGreetings(){
        String r = "";
        r += "----------------------------------------------------------------------------------------------------\n";
        r += "Date: "+Calendar.getInstance().getTime().toString()+ "\n";
        r += "Establishment: ThePaneraBakeryAndFood \n";
        r += "NIT: 15.444.730-9\n";
        r += "Direccion: Cr.81 #43-19 Local 108 (Rionegro,Antioquia)\n";
        r += "----------------------------------------------------------------------------------------------------\n";
        return r;
    }
}
