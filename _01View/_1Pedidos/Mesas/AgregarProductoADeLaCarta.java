package _01View._1Pedidos.Mesas;


import _03Model.Facility.ProductsAndSupplies.Inventory;
import _03Model.Facility.ProductsAndSupplies.Inventory.DeInventarioType;
import _03Model.Facility.ProductsAndSupplies.DeLaCartaDTO;
import _03Model.Facility.ProductsAndSupplies.ProductoDTO;
import _03Model.Facility.ProductsAndSupplies.SubProductoDTO;
import _01View.MainFrame;
import _01View.Templates.MiniWindowDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import _04DataAccessObject.generalController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MoisesE
 */
public class AgregarProductoADeLaCarta extends MiniWindowDialog{
    
    public AgregarProductoADeLaCarta(MainFrame contextFrame,javax.swing.JFrame frame, boolean modal) {
        super(contextFrame,frame,modal);
        dto = new DeLaCartaDTO(null,0);
        createObjects();
        createPanels();
        this.btnExit.setVisible(false);
    }
    
    public void startFrame(DeLaCartaDTO dto) {
        this.dto = dto;
        System.out.println("AgregarProductoADeLaCarta.startFrame: "+dto);
        informationLabel.setText(dto.getNombre());
        setDeLaCartaListLabelAndButtons();
        setVisible(true);
    }
    
//------------------------------------------------------------------------------
    private DeLaCartaDTO dto;
    private JPanel mainPanel;
    //------------------------------------------------------------
    private JTextField ingresarCodigoTextField;
    private JTextField notaComandaTextField;
    //------------------------------------------------------------
    private ArrayList<JLabel>   labelList;
    private ArrayList<JButton>  buttonList;
//------------------------------------------------------------------------------
    private void createObjects(){
        mainPanel = new JPanel();
        ingresarCodigoTextField = new JTextField();
        notaComandaTextField = new JTextField();
    }
    private void createPanels(){
        createMainPanel();
        containerPanel.add(mainPanel);
    }
    
    private void createMainPanel(){
        int x = 20, y = 10, w = 450, h = 50, dx = 200, dw=20, dy = 50;
        
        labelList = new ArrayList();
        buttonList = new ArrayList();
        
        JLabel ingresarCodigoTextLabel = new JLabel("Ingrese Producto Por Codigo:");
        ingresarCodigoTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (ingresarCodigoTextField.getText().isEmpty()){
                    dto.setEspecial(notaComandaTextField.getText());
                    exit_hide();
                }
                else{
                    System.out.println("AgregarProductoADeLaCarta.ingresarCodigoTextField.actionPerformed: "+ingresarCodigoTextField.getText());
                    Inventory aux = generalController.getProduct(Integer.parseInt(ingresarCodigoTextField.getText()));
                    System.out.println("AgregarProductoADeLaCarta.ingresarCodigoTextField.actionPerformed: "+aux);
                    if (aux.getClase()==DeInventarioType.SubProducto){
                        ingresarCodigoTextField.setText("");
                        aux = new SubProductoDTO((SubProductoDTO) aux);
                        aux.setCantidad(1);
                        dto.addToSubProductoList((SubProductoDTO) aux);
                    }
                    if (aux.getClase().equals(DeInventarioType.Producto)) {
                        ingresarCodigoTextField.setText("");
                        aux = new ProductoDTO((ProductoDTO) aux);
                        aux.setCantidad(1);
                        dto.addToProductoList((ProductoDTO) aux);
                    }
                    System.out.println("AgregarProductoADeLaCarta.ingresarCodigoTextField.actionPerformed: "+dto);
                    setDeLaCartaListLabelAndButtons(); 
                    mainPanel.repaint();
                }
            }
        });
        ingresarCodigoTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                dto.setEspecial(notaComandaTextField.getText());
                exit_hide();
            }
        });
        this.addWindowListener(new WindowAdapter(){
            @Override public void windowOpened(WindowEvent e){
                ingresarCodigoTextField.requestFocus();
            }
        });
        setDeLaCartaListLabelAndButtons();        
        
        ingresarCodigoTextLabel.setBounds(x,y,w,h/2);
        ingresarCodigoTextField.setBounds(x,y+dy/2,w,h/2);      x+=(w+dw);
                
        mainPanel.add(ingresarCodigoTextLabel);
        mainPanel.add(ingresarCodigoTextField);
        mainPanel.setBounds(Panel_Rectangle);
        mainPanel.setLayout(null);
    }
    
    
    private class buttonEliminarActionListener implements ActionListener{
        public buttonEliminarActionListener(DeLaCartaDTO dto, Inventory composition){
            this.dto = dto;
            this.composition = composition;
        }
        @Override public void actionPerformed(ActionEvent e) {
            if (composition.getClase()==DeInventarioType.SubProducto)
                dto.deleteFromSubProductoList((SubProductoDTO)composition);
            if (composition.getClase()==DeInventarioType.Producto)
                dto.deleteFromProductoList((ProductoDTO)composition);
            setDeLaCartaListLabelAndButtons();
        }
        DeLaCartaDTO dto;
        Inventory composition;
    }
    
    private void setDeLaCartaListLabelAndButtons(){
        int x = 60, y = 100, w = 350, h = 50, dx = 100, w2=50, dy = 50;
        labelList.forEach((label) -> { mainPanel.remove(label); });
        buttonList.forEach((button) -> { mainPanel.remove(button);});
        mainPanel.repaint();
        if (!dto.getComposition().isEmpty()){
            for (Inventory compositionDTO: dto.getComposition()){
                JButton button = new JButton("X");
                button.addActionListener(new buttonEliminarActionListener(dto, compositionDTO));
                button.setBounds(x,y,w2,h);
                buttonList.add(button); mainPanel.add(button);

                JLabel label = new JLabel("<"+compositionDTO.getID()+"> "+compositionDTO.getNombre());
                label.setBounds(x+w2,y,w,h);
                labelList.add(label); mainPanel.add(label);

                y+=dy;
            }
        }
        JLabel notaComandaLabel = new JLabel("Nota de Comanda");
        labelList.add(notaComandaLabel);
        notaComandaLabel.setBounds(x,y,w,h); y+=dy*2/3;
        notaComandaTextField.setBounds(x,y,w,h);
        mainPanel.add(notaComandaLabel);
        mainPanel.add(notaComandaTextField);
        mainPanel.repaint();
    }
}
