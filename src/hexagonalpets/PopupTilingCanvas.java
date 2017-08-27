
package hexagonalpets;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class PopupTilingCanvas extends PopupPanel{
    CanvasTiling TIG;
    
    public PopupTilingCanvas(Color color){
        super("Periodic Tiling", 400,400);
        TIG = new CanvasTiling();
        TIG.setBackground(color);
        TIG.setSize(400, 400);
        add(TIG);
        setVisible(true);
    }
    
    public PopupTilingCanvas(boolean b, CanvasTiling T,  Color color){
        super("Periodic Tiling", 400,400);
        TIG = T;
        TIG.setSize(400,400);
        TIG.setBackground(color);
        add(TIG);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            TIG.setSize(w, h);
        }catch(Exception e){}
    
}
}

