
package hexagonalpets;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class PopupCellCanvas extends PopupPanel{
    CanvasCell C;
    
    public PopupCellCanvas(Color color){
        super("PET Canvas", 400, 400);
        C = new CanvasCell();
        C.setBackground(color);
        C.setSize(400, 400);
        add(C);
        setVisible(true);
    }
    
    public PopupCellCanvas(boolean b, CanvasCell T,  Color color){
        super("PET Canvas", 400,400);
        C = T;
        C.setSize(400,400);
        C.setBackground(color);
        add(C);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            C.setSize(w, h);
        }catch(Exception e){}
    
}
}
