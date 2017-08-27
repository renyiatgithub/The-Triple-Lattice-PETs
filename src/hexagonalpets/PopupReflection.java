
package hexagonalpets;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class PopupReflection extends PopupPanel{
    CanvasReflection R ;
    
    public PopupReflection(Color color){
        super("Reflection ", 400,400);
        R = new CanvasReflection();
        R.setBackground(color);
        R.setSize(400, 400);
        add(R);
        setVisible(true);
    }
    
    public PopupReflection(boolean b, CanvasReflection T,  Color color){
        super("Reflection", 500,500);
        R = T;
        R.setSize(400,400);
        R.setBackground(color);
        add(R);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            R.setSize(w, h);
        }catch(Exception e){}
    
}
}

