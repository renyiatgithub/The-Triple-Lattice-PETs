
package hexagonalpets;
import java.awt.*;

public class PopupFiberBundle extends PopupPanel{
    CanvasFiberBundle FB;
    RotationAxis_FB A;
    
    public PopupFiberBundle(Color color){
        super("Fiber Bundle Canvas", 400,600);
        FB = new CanvasFiberBundle();
        A = new RotationAxis_FB();
        FB.setBackground(color);
        A.setBackground(color);
        FB.setSize(400, 520);
        A.setSize(400,80);
        A.setLocation(0,0);
        add(A);
        FB.setLocation(0, 90);
        add(FB);
        setVisible(true);
    }
    
    public PopupFiberBundle(boolean b, CanvasFiberBundle F,  Color color){
        super("Fiber Bundle Canvas", 500,600);
        FB = F;
        FB.setSize(500,520);
        FB.setBackground(color);
        A = new RotationAxis_FB();
        A.setBackground(Color.gray);
        A.setFiberBundle(FB);
        A.setSize(500, 80);
        A.setLocation(0, 0);
        add(A);
        add(FB);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            A.setSize(w,80);
            FB.setSize(w, h);
        }catch(Exception e){}
    
}
}
