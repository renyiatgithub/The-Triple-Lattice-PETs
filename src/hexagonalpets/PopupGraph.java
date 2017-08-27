package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class PopupGraph extends PopupPanel{
    CanvasGraph G;
    RotationAxis A;
    
    
    public PopupGraph(Color color){
        super("Airhtmetic Graph", 400,400);
        G = new CanvasGraph();
        A = new RotationAxis();
        A.setCanvasGraph(G);
        G.setBackground(color);
        A.setBackground(color);
        G.setSize(400, 400);
        A.setSize(400,80);
        A.setLocation(0, 0);
        add(A);
        G.setLocation(0,90);
        add(G);
        setVisible(true);
    }
    
    public PopupGraph(boolean b, CanvasGraph T,  Color color){
        super("Arithmetic Graph", 400,480);
        G = T;
        G.setSize(400,400);
        G.setBackground(color);
        A = new RotationAxis();
        A.setBackground(Color.gray);
        A.setCanvasGraph(T);
        A.setSize(400, 80);
        A.setLocation(0, 0);
        G.setLocation(0, 90);
        add(A);
        add(G);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            A.setSize(w, 80);
            G.setSize(w, h);
        }catch(Exception e){}
    }
}
