
package hexagonalpets;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class PopupAction extends PopupPanel{
    CanvasAction A;
    
    public PopupAction(Color color){
        super("Action by Translation", 400,400);
        A = new CanvasAction();
        A.setBackground(color);
        A.setSize(400, 400);
        add(A);
        setVisible(true);
    }
    
    public PopupAction(boolean b, CanvasAction T,  Color color){
        super("Action by Translation", 400,400);
        A = T;
        A.setSize(400,400);
        A.setBackground(color);
        add(A);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            A.setSize(w, h);
        }catch(Exception e){}
    
}
}

