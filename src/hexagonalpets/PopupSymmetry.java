
package hexagonalpets;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class PopupSymmetry extends PopupPanel{
    CanvasSymmetry CSym ;
    
    public PopupSymmetry(Color color){
        super("Rotational Symmetry ", 1000,400);
        CSym = new CanvasSymmetry();
        CSym.setBackground(color);
        CSym.setSize(1000, 400);
        add(CSym);
        setVisible(true);
    }
    
    public PopupSymmetry(boolean b, CanvasSymmetry T,  Color color){
        super("Rotational Symmetry", 1000,500);
        CSym = T;
        CSym.setSize(1000,400);
        CSym.setBackground(color);
        add(CSym);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            CSym.setSize(w, h);
        }catch(Exception e){}
    
}
}