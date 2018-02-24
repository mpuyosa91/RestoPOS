/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package View._1Pedidos.Mesas;
import Controller.DataBase.IDataBase;
import Model.DataTransferObjects.SellBase.DeInventario;
import Model.DataTransferObjects.SellBase.DeInventario.DeInventarioType;
import Model.DataTransferObjects.SellBase.DeLaCartaDTO;
import Model.DataTransferObjects.SellBase.ProductoDTO;
import Model.DataTransferObjects.ConfigurationDTO;
import Model.DataTransferObjects.ConfigurationDTO.Label;
import View.MainFrame;
import View.Templates.WindowFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import Model.DataTransferObjects.SellBase.ISellable;
import Model.ZonaBObjects.Cliente.ClienteTypes;
import Model.ZonaBObjects.ClienteExterno;
import Model.ZonaBObjects.IClientable;
import Model.ZonaBObjects.PuntoDeVenta;
import View._1Pedidos.PedidosPanel;
import View._1Pedidos.SeleccionFrame;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;

/**
 *
 * @author MoisesE
 */
public class PedidosFrame extends WindowFrame{
    
    final int initialX=60, initialY=60, stepX=250, stepY=150, cant=2;
        
    public PedidosFrame(MainFrame contextFrame,IClientable cliente){
        super(contextFrame);
        this.cliente        = cliente;
        this.thisFrame = this;
        createObjects();
        createPanels();
        statusRefresh();
    }
    
    public void startframe(PedidosPanel father){
        this.father = father;
        boolean valid = true;
        if (cliente.getIdentifier().contains(ClienteTypes.ClienteExterno.getShowableName())){
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Contraseña Para Cliente Externo:");
            JPasswordField pass = new JPasswordField(10);
            panel.add(label);
            panel.add(pass);
            String[] options = new String[]{"OK", "Cancel"};
            int option = JOptionPane.showOptionDialog(null, panel, "Autenticacion de Seguridad",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
            if(option == 0){
                char[] password = pass.getPassword();
                char[] autent = (""+(int)ConfigurationDTO.getConfigurationValue(Label.ClienteExternoAutent)).toCharArray();
                valid = Arrays.equals(password, autent);
            }
            else { valid = false; }
        }
        if (valid){
            consoleFlush();
            printActual();
            setVisible(true);
        }
    }
    
    @Override public void exit_hide(){
        father.setButtonsColors();
        setVisible(false);
        consoleFlush();
    }
    
    @Override
    protected void setVisiblePanel(Component panel){
        clientePanel.setVisible(false);
        panel.setVisible(true);
    }
    
    private PedidosPanel father;
    private final IClientable cliente;
    private JPanel clientePanel;
    private final PedidosFrame thisFrame;
    //------------------------------------------------------------
    private JTextField  ingresarCodigoTextField;
    private JButton     cerrarClienteButton;
    private JButton     notificarCocinaButton;
    private JButton     seleccionProductoButton;
    private JButton     seleccionDeLaCartaButton;
    //------------------------------------------------------------
    private ArrayList<JLabel>   labelList;
    private ArrayList<JButton>  buttonList;
    private JPanel              productosPanel;
    private JScrollPane         productosScrollPane;
//------------------------------------------------------------------------------
    
    private void createObjects(){
        clientePanel = new JPanel();
        notificarCocinaButton = new JButton("Enviar Nuevas Comandas");
        notificarCocinaButton.setEnabled(false);
        cerrarClienteButton = new JButton("Cerrar "+cliente.getIdentifier());
        cerrarClienteButton.setEnabled(false);
        ingresarCodigoTextField = new JTextField();
        seleccionProductoButton = new JButton("Productos");
        seleccionDeLaCartaButton = new JButton("De La Carta");
        productosPanel = new JPanel();
        productosScrollPane = new JScrollPane(productosPanel);
        productosScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        productosScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    private void createPanels(){
        createClientePanel();
        containerPanel.add(clientePanel);
    }
        
    private void statusRefresh(){
        if (cliente.isOcupada()){
            informationLabel.setText("Ocupada");
            navigationPanel.setBackground(Color.LIGHT_GRAY);
        }
        else{
            informationLabel.setText("Disponible");
            navigationPanel.setBackground(Color.green);
        }
    }
    private void createClientePanel(){
        int x = 20, y = 100, w = 350, h = 50, dx = 200, dw=20, dy = 50;
        
        labelList = new ArrayList();
        buttonList = new ArrayList();
        
        JLabel ingresarCodigoTextLabel = new JLabel("Ingrese Producto Por Codigo:");
        ingresarCodigoTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (ingresarCodigoTextField.getText().isEmpty()){
                    cliente.sendToKitchen();
                    setPedidoListLabelAndButtons();
                    ingresarCodigoTextField.setText("");
                }
                else{
                    DeInventario aux = IDataBase.searchByIdInDB(Integer.parseInt(ingresarCodigoTextField.getText()));
                    System.out.println("PedidosFrame.createClientePanel.actionPerformed: "+aux);
                    if (aux!=null) ingresarProducto(aux);
                }
            }
        });
        this.addWindowListener(new WindowAdapter(){
            @Override public void windowOpened(WindowEvent e){
                ingresarCodigoTextField.requestFocus();
            }
        });
        cerrarClienteButton.addActionListener((ActionEvent e) -> {
            boolean valid=false;
            JPanel panel                   = new JPanel(new GridBagLayout()); //SpringLayout
            GridBagConstraints gridBagConstraints = new GridBagConstraints();         
            JLabel totalLabel              = new JLabel("Total:  ");
            JTextField totalTextField      = new JTextField(10);       
            JLabel pagoLabel               = new JLabel("Pago:   ");
            JTextField pagoTextField       = new JTextField(10);
            JLabel devolutionLabel         = new JLabel("Devol:  ");
            JTextField devolutionTextField = new JTextField(10);

            totalTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(cliente.getConsumo()));
            long a,b;
            try{ a = (long) NumberFormat.getNumberInstance(Locale.GERMAN).parse(pagoTextField.getText()); }
            catch (ParseException ex) { a = 0; }
            b = (int) cliente.getConsumo();
            totalTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(b));
            pagoTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(a));
            devolutionTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(a-b));    

            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            panel.add(totalLabel,gridBagConstraints);
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            panel.add(totalTextField,gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            panel.add(pagoLabel,gridBagConstraints);
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            panel.add(pagoTextField,gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            panel.add(devolutionLabel,gridBagConstraints);
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 3;
            panel.add(devolutionTextField,gridBagConstraints);

            totalTextField.setEditable(false);
            devolutionTextField.setEditable(false);
            AbstractAction abstractAction = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    long a,b;
                    try{ a = (long) NumberFormat.getNumberInstance(Locale.GERMAN).parse(pagoTextField.getText()); }
                    catch (ParseException ex) { a = 0; }
                    b = (int) cliente.getConsumo();
                    totalTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(b));
                    pagoTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(a));
                    devolutionTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(a-b));                       
                }
            };
            for (int i=48;i<58;i++) pagoTextField.getInputMap().put(KeyStroke.getKeyStroke(i,0,true),abstractAction);
            for (int i=96;i<106;i++) pagoTextField.getInputMap().put(KeyStroke.getKeyStroke(i,0,true),abstractAction);
            pagoTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,0,true),abstractAction);

            String[] options = new String[]{"Ok","Factura", "Cancel"};
            int option = JOptionPane.showOptionDialog(null, panel, cliente.getIdentifier(),
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
            System.out.println("----------------------------------------\n***********"+option);
            switch (option) {
                case 0:
                    cliente.saveBill();
                    setPedidoListLabelAndButtons();
                    ingresarCodigoTextField.setText("");
                    cerrarClienteButton.setEnabled(false);
                    notificarCocinaButton.setEnabled(false);
                    clientePanel.setVisible(true);
                    consoleAppend("<<"+cliente.getIdentifier()+" Cerrada>> Consumo: "+cliente.desocupar());
                    break;
                case 1:
                    cliente.printBill();
                    setPedidoListLabelAndButtons();
                    ingresarCodigoTextField.setText("");
                    cerrarClienteButton.setEnabled(false);
                    notificarCocinaButton.setEnabled(false);
                    clientePanel.setVisible(true);
                    consoleAppend("<<"+cliente.getIdentifier()+" Cerrada>> Consumo: "+cliente.desocupar());
                    break;
                default:
                    setPedidoListLabelAndButtons();
                    ingresarCodigoTextField.setText("");
                    break;
            }
        });
        notificarCocinaButton.addActionListener((ActionEvent e) -> {
            cliente.sendToKitchen();
            setPedidoListLabelAndButtons();
            ingresarCodigoTextField.setText("");
        });
        seleccionProductoButton.addActionListener((ActionEvent e) -> {
            SeleccionFrame seleccionador = new SeleccionFrame(contextFrame,thisFrame,true,IDataBase.PRODUCTODTO);
            DeInventario externalDto = new DeInventario(0,DeInventario.DeInventarioType.Producto);
            seleccionador.startDialog();
            if (!"NoEstablecido".equals(seleccionador.externalDto.getNombre()))
                ingresarProducto(seleccionador.externalDto);
        });
        seleccionDeLaCartaButton.addActionListener((ActionEvent e) -> {
            SeleccionFrame seleccionador = new SeleccionFrame(contextFrame,thisFrame,true,IDataBase.DELACARTADTO);
            DeInventario externalDto = new DeInventario(0,DeInventario.DeInventarioType.DeLaCarta);
            seleccionador.startDialog();
            if (!"NoEstablecido".equals(seleccionador.externalDto.getNombre()))
                ingresarProducto(seleccionador.externalDto);
        });
        seleccionProductoButton.setEnabled(true);
        seleccionDeLaCartaButton.setEnabled(true);
        cerrarClienteButton.setEnabled(false);
        notificarCocinaButton.setEnabled(false);
        
        ingresarCodigoTextLabel.setBounds(x,y,w,h/2);
        ingresarCodigoTextField.setBounds(x,y+dy/2,w,h/2);      x+=(w+dw);
        notificarCocinaButton.setBounds(x,y,w/2,h);             x = 20; y-=dy;
        cerrarClienteButton.setBounds(x,y,w/2,h);               x += w*2/4 + 10;
        seleccionProductoButton.setBounds(x,y,w/2,h);           x += w*2/4 + 10;
        seleccionDeLaCartaButton.setBounds(x,y,w/2,h);          x += w*2/4 + 10;
        productosScrollPane.setBounds(0,150,LeftPanel_Rectangle.width,LeftPanel_Rectangle.height-150);
        productosPanel.setLayout(null);
        productosScrollPane.setBackground(Color.red);
        
        clientePanel.add(ingresarCodigoTextLabel);
        clientePanel.add(ingresarCodigoTextField);
        clientePanel.add(seleccionProductoButton);
        clientePanel.add(seleccionDeLaCartaButton);
        clientePanel.add(cerrarClienteButton);
        clientePanel.add(notificarCocinaButton); 
        clientePanel.add(productosScrollPane);
        //clientePanel.add(productosPanel);
        
        clientePanel.setBounds(LeftPanel_Rectangle);
        clientePanel.setLayout(null);

    }
    
    private class buttonProductoActionListener implements ActionListener{
        public buttonProductoActionListener(ISellable dto, IClientable mesa){
            this.dto = dto;
            this.cliente = mesa;
        }
        @Override public void actionPerformed(ActionEvent e) {
            cliente.borrarProducto(dto);
            setPedidoListLabelAndButtons();
        }
        ISellable dto;
        IClientable cliente;
    }
    
    private void setPedidoListLabelAndButtons(){
        int x = 60, y = 20, w = 300, h = 50, dx = 100, w2=50, dy = 50;
        labelList.forEach((label) -> { productosPanel.remove(label);}); //clientePanel.remove(label); });
        buttonList.forEach((button) -> { productosPanel.remove(button); }); //clientePanel.remove(button);});
        for (ISellable dto: cliente.getNuevosProductoList()){
            JButton button = new JButton("X");
            button.addActionListener(new buttonProductoActionListener(dto,cliente));
            button.setBounds(x,y,w2,h);
            buttonList.add(button); productosPanel.add(button); //clientePanel.add(button);
            
            JLabel label = new JLabel("<"+dto.getID()+"> x"+(int) dto.getCantidad()+" "+dto.getNombre());
            label.setBounds(x+w2,y,w,h);
            labelList.add(label); productosPanel.add(label); //clientePanel.add(label);
            
            y+=dy;
        }
        productosPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width,y));
        productosPanel.repaint();
        
        consoleFlush();
        printActual();
        statusRefresh();
    }
    
    private void printActual(){
        ISellable aux;
        if (cliente.isOcupada()){
            consoleAppend(cliente.getIdentifier());
            for (int j=0;j<cliente.getProductoList().size();j++){
                aux = cliente.getProductoList().get(j);
                consoleAppend("    <"+aux.getID()+"> x"+String.format("%03d",(int)aux.getCantidad())+" "+aux.getNombre()+" "+(int)(aux.getPrecio()*aux.getCantidad()));
            }
            for (int j=0;j<cliente.getNuevosProductoList().size();j++){
                aux = cliente.getNuevosProductoList().get(j);
                consoleAppend("  -><"+aux.getID()+"> x"+String.format("%03d",(int)aux.getCantidad())+" "+aux.getNombre()+" "+(int)(aux.getPrecio()*aux.getCantidad()));
            }
            consoleAppend("\n****** TOTAL: "+(int)cliente.getConsumo()+" *******");
        }
        else consoleAppend(cliente.getIdentifier()+" Disponible");
    }              
    
    class EnterableTextField extends JTextField {
        EnterableTextField(int len) {
            super(len);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent evt) {
                    int key = evt.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) transferFocus();
                }
            });
        }
    }
    
    private void ingresarProducto(DeInventario dto){
        boolean valid, addNew;
        int cantidad=1;
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Ingrese Cantidad de "+dto.getNombre()+":");
        EnterableTextField quantity = new EnterableTextField(10);
        panel.add(label);
        panel.add(quantity);
        String[] options = new String[]{"Agregar", "Cancelar"};
        do {
            addNew = false;
            int option = JOptionPane.showOptionDialog(null, panel, "Cantidad de "+dto.getNombre(),
            JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
            if(option == 0){
                if (!quantity.getText().isEmpty()){
                    try{
                        cantidad = Integer.valueOf(quantity.getText()); valid = true;
                    }
                    catch (NumberFormatException e){ valid = false; }
                }
                else { 
                    cantidad = 1; 
                    valid = true; 
                }
                addNew = true;
            }
            else { 
                cantidad = 0; 
                valid = true;
                addNew = false;
            }
            quantity.setText("");
        }while (!valid);
        if (addNew){
            if (dto.getClase()==DeInventarioType.Producto)   dto = new ProductoDTO((ProductoDTO) dto);
            if (dto.getClase()==DeInventarioType.DeLaCarta) {
                dto = new DeLaCartaDTO((DeLaCartaDTO) dto);
                System.out.println("PedidosFrame.createClientePanel.actionPerformed.DeLaCarta: "+dto);
                AgregarProductoADeLaCarta auxFrame = new AgregarProductoADeLaCarta(contextFrame,thisFrame,true);
                auxFrame.startFrame((DeLaCartaDTO)dto);
            }
            dto.setCantidad(cantidad);
            try{
                if (dto.getClass()==ProductoDTO.class || dto.getClass()==DeLaCartaDTO.class) {       
                    if (!cliente.isOcupada()){
                        cliente.ocupar();
                        statusRefresh();
                        cerrarClienteButton.setEnabled(true);
                        if (!cliente.getClass().equals(PuntoDeVenta.class)&&!cliente.getClass().equals(ClienteExterno.class))
                            notificarCocinaButton.setEnabled(true);
                    }
                    cliente.agregarProducto((ISellable) dto);
                    setPedidoListLabelAndButtons();
                    ingresarCodigoTextField.setText("");
                }
                else{
                    JOptionPane.showMessageDialog(null,"Tipo Invalido, Ingresado: \n"+dto.getClase().toString());
                }
            }
            catch (NullPointerException ex){

            }
        }
    }
    
}