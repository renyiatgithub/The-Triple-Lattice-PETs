
package hexagonalpets;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class PopupScissor extends PopupPanel{
    CanvasScissor CS;
    CanvasGraph G;
    
    public PopupScissor(Color color){
        super("Rearrangement", 400,450);
        CS = new CanvasScissor();
        CS.setBackground(color);
        CS.setSize(400, 450);
        CS.setCanvasGraph(G);
        add(CS);
        setVisible(true);
    }
    
    public PopupScissor(boolean b, CanvasScissor T, CanvasGraph CG, 
             Color color){
        super("Rearrangement", 400,450);
        CS = T;
        G = CG;
        CS.setSize(400,450);
        CS.setBackground(color);
        CS.setCanvasGraph(G);
        add(CS);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            CS.setSize(w, h);
        }catch(Exception e){}
    
}
}

