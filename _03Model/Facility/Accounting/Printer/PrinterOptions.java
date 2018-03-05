package _03Model.Facility.Accounting.Printer;

//------------------------

import static java.lang.Math.pow;
import java.util.Calendar;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;


//-- PrinterOptions.java
//------------------------

public class PrinterOptions {
    String commandSet = "";
    int font = 0;
           
    public void printAllFonts(){
        for (int i=0; i<8; i++){
            setFont(i,false);
            addLineSeperator(); newLine();
        }
    }
    
    public void setFont(int option, boolean bold){
        int aux = 0;
        font = option;
        final byte[] test = {27, 33, 0};
        if (bold)                   test[2]+=8;
        aux = (int) pow(2,0);
        if ((option&aux)==aux)      test[2]+=16;
        aux = (int) pow(2,1);
        if ((option&aux)!=aux)      test[2]+=1;
        aux = (int) pow(2,2);
        if ((option&aux)==aux)      test[2]+=32;
        commandSet += new String(test);
    }
        
    public String initialize() {
        final byte[] Init = {27, 64};
        commandSet += new String(Init);
        return new String(Init);
    }

    public String feedBack(byte lines) {
        final byte[] Feed = {27,101,lines};
        String s = new String(Feed);
        commandSet += s;
        return s;
    }

    public String feed(byte lines) {
        final byte[] Feed = {27,100,lines};
        String s = new String(Feed);
        commandSet += s;
        return s;
    }

    public String setTextLeft(String text){
        final byte[] AlignLeft = {27,97,48};
        String s = new String(AlignLeft);
        commandSet += s + text;
        return s;
    }
    
    public String setTextCenter(String text){
        final byte[] AlignCenter = {27,97,49};
        String s = new String(AlignCenter);
        commandSet += s + text;
        return s;
    }
    
    public String setTextRight(String text){
        final byte[] AlignRight = {27, 97,50};
        String s = new String(AlignRight);
        commandSet += s + text;
        return s;
    }

    public String newLine() {
        final  byte[] LF = {10};
        String s = new String(LF);
        commandSet += s;
        return s;
   }

    public String reverseColorMode(boolean enabled) {
        final byte[] ReverseModeColorOn = {29, 66, 1};
        final byte[] ReverseModeColorOff = {29, 66, 0};

        String s = "";
        if(enabled)
            s = new String(ReverseModeColorOn);
        else
            s = new String(ReverseModeColorOff);

        commandSet += s;
        return s;
    } 

    public String underLine(int Options) {
        final byte[] UnderLine2Dot = {27, 45, 50};
        final byte[] UnderLine1Dot = {27, 45, 49};
        final byte[] NoUnderLine = {27, 45, 48};

        String s = "";
        switch(Options) {
            case 0:
            s = new String(NoUnderLine);
            break;

            case 1:
            s = new String(UnderLine1Dot);
            break;

            default:
            s = new String(UnderLine2Dot);
        }
        commandSet += s;
        return s;
    }

    public String finit() {
        final byte[] FeedAndCut = {29, 'V', 66, 0};
        String s = new String(FeedAndCut);

        commandSet+=s;
        return s;
    }
    public String finitWithDrawer(){        
        final byte[] FeedAndCut = {27,100,5,27,109};
        String s = new String(FeedAndCut);

        final byte[] DrawerKick12={27,112,0,55,27,112,1,55};
        s += new String(DrawerKick12);

        commandSet+=s;
        return s;
    }

    public String addLineSeperator() {
        final byte[] AlignLeft = {27,97,48};
        String s = new String(AlignLeft);
        commandSet += s;
        return addLineSeparator(font);
    }
    public String addLineSeparator(int font){
        String lineSpace = "";
        int q = getLineSize();
        for (int i=0; i<q; i++){
            lineSpace += "-";
        }
        commandSet += lineSpace;
        return lineSpace;
    }
    public int getLineSize(){
        return getLineSize(font);
    }
    public int getLineSize(int font){
        int r = 22, aux;
        aux = (int) pow(2,1);
        if ((font&aux)!=aux)      r+=6;
        aux = (int) pow(2,2);
        if ((font&aux)!=aux)      r*=2;
        return r;
    }

    public void resetAll() {
        commandSet = "";
    }

    public String finalCommandSet() {
        return commandSet;
    }
    
    public   static boolean feedPrinter(byte[] b) {
        try {       
            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName("CognitiveTPG Receipt", null)); //EPSON TM-U220 ReceiptE4

            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();       
            //PrintServiceLookup.lookupDefaultPrintService().createPrintJob();  

            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);

            job.print(doc, null);
            System.out.println("Printed !");
        } catch (javax.print.PrintException pex) {
            System.out.println("Printer Error " + pex.getMessage());
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}