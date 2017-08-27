package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class PopupLatticeCanvas extends PopupPanel{
    CanvasLattice TL;
    
    public PopupLatticeCanvas(Color color){
        super("Triple Lattice Canvas", 400,400);
        TL = new CanvasLattice();
        TL.setBackground(color);
        TL.setSize(400, 400);
        add(TL);
        setVisible(true);
    }
    
    public PopupLatticeCanvas(boolean b, CanvasLattice T,  Color color){
        super("Triple Lattice Canvas", 400,400);
        TL = T;
        TL.setSize(400,400);
        TL.setBackground(color);
        add(TL);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            TL.setSize(w, h);
        }catch(Exception e){}
    }
}
