package _03Model.Facility.Accounting.Printer;

//------------------------

import _02Controller.ProgramIntegritySurveillance.SurveillanceReport;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;


//-- PointOfServicePrinter.java
//------------------------------------------------------------------------------

public class PointOfServicePrinter {
    public static boolean showMesgSys = false;
    private final byte NULL = 0;
    private final byte ESC = 27;


    public PointOfServicePrinter(){
        commandSet = "";
        font = 0;
        setFont(2, false);
    }

    public void setFont(int option, boolean bold){
        font = option;
        final byte[] fontCommand = {ESC, '!', 0};
        if (bold)           fontCommand[2]+=8;
        if ((font&1)==1)    fontCommand[2]+=16;
        if ((font&2)!=2)    fontCommand[2]+=1;
        if ((font&4)==4)    fontCommand[2]+=32;
        commandSet += new String(fontCommand);
    }
        
    public void initialize() {
        commandSet += new String(new byte[]{ESC, '@'});
    }

    public void feedBack(byte lines) {
        commandSet += new String(new byte[]{ESC,'e',lines});
    }

    public void feed(byte lines) {
        commandSet += new String(new byte[]{ESC,'d',lines});
    }

    public void setTextLeft(String text){
        commandSet += new String(new byte[]{ESC,'a','0'}) + text;
    }
    
    public void setTextCenter(String text){
        commandSet += new String(new byte[]{ESC,'a','1'}) + text;
    }
    
    public void setTextRight(String text){
        commandSet += new String(new byte[]{ESC,'a','2'}) + text;
    }

    public void newLine() {
        byte LF = 10;
        commandSet += new String(new byte[]{LF});
    }

    public void feedAndCut() {
        byte GS = 29;
        commandSet+=new String(new byte[]{GS, 'V', 'B', NULL});
    }
    public void feedCutAndDrawerKick(){
        byte ENQ = 5;
        commandSet += new String(new byte[]{ESC,'d', ENQ,ESC,'m'});
        drawerKick();
    }
    public void drawerKick(){
        byte SOH = 1;
        commandSet += new String(new byte[]{ESC,'p',NULL,'7',ESC,'p',SOH, '7'});
    }

    public void addLineSeperator() {
        commandSet += new String(new byte[]{ESC,'a','0'}); //AlignLeft
        addLine();
    }

    public void resetAll() {
        commandSet = "";
    }

    public String finalCommandSet() {
        return commandSet;
    }

    public void printAll() {
        try {

            Doc doc = new SimpleDoc(
                    commandSet.getBytes(),
                    DocFlavor.BYTE_ARRAY.AUTOSENSE,
                    null);

            //EPSON TM-U220 ReceiptE4
            AttributeSet attributeSet =
                    new HashPrintServiceAttributeSet(
                            new PrinterName (
                                    "CognitiveTPG Receipt",
                                    null));

            PrintServiceLookup
                    .lookupPrintServices(null,attributeSet)[0]
                    .createPrintJob()
                    .print(doc, null);

            SurveillanceReport.log("Bill Print.");
        } catch(Exception e) {
            SurveillanceReport.generic(Thread.currentThread().getStackTrace(), e);
        }
    }

    private String commandSet;
    private int font;

    private int getLineSize(){
        int r = 22;
        if ((font&2)!=2)    r+=6;
        if ((font&4)!=4)    r*=2;
        return r;
    }

    private void addLine(){
        StringBuilder lineSpace = new StringBuilder();
        for (int i=0; i<getLineSize(); i++)
            lineSpace.append("-");
        commandSet += lineSpace.toString();
    }

    /*
    private void printAllFonts(){
        for (int i=0; i<8; i++){
            setFont(i,false);
            addLineSeperator(); newLine();
        }
    }

        public void reverseColorMode(boolean enabled) {
        commandSet += new String(new byte[]{GS, 'B', (enabled)? SOH: NULL});
    }

    public void underLine(int Options) {
        String s;
        switch(Options) {
            case 0:
            s = new String(new byte[]{ESC, '-', '0'}); //NoUnderLine
            break;

            case 1:
            s = new String(new byte[]{ESC, '-', '1'}); //UnderLine1Dot
            break;

            default:
            s = new String(new byte[]{ESC, '-', '2'}); //UnderLine2Dot
        }
        commandSet += s;
    */

}