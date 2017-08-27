
package hexagonalpets;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class PopupGoldenRatio extends PopupPanel{
    CanvasGoldenRatio GR ;
    
    public PopupGoldenRatio(Color color){
        super("Golden Ratio ", 400,400);
        GR = new CanvasGoldenRatio();
        GR.setBackground(color);
        GR.setSize(400, 400);
        add(GR);
        setVisible(true);
    }
    
    public PopupGoldenRatio(boolean b, CanvasGoldenRatio T,  Color color){
        super("Golden Ratio", 500,500);
        GR = T;
        GR.setSize(400,400);
        GR.setBackground(color);
        add(GR);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            GR.setSize(w, h);
        }catch(Exception e){}
    
}
}
