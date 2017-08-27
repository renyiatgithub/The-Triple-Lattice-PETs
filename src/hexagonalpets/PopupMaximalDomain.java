

package hexagonalpets;
import java.awt.*;

public class PopupMaximalDomain extends PopupPanel{
    CanvasMaxDomain MD;
    RotationAxis_Max A;
    
    public PopupMaximalDomain(Color color){
        super("Maximal Domains", 400,600);
        MD = new CanvasMaxDomain();
        A = new RotationAxis_Max();
        MD.setBackground(color);
        A.setBackground(color);
        MD.setSize(400, 520);
        A.setSize(400,80);
        A.setLocation(0,0);
        add(A);
        MD.setLocation(0, 90);
        add(MD);
        setVisible(true);
    }
    
    public PopupMaximalDomain(boolean b, CanvasMaxDomain F, RotationAxis_Return R,
            RotationAxis_Base B, RotationAxis_Base1 B1, Color color){
        super("Maximal Domains", 500,600);
        MD = F;
        MD.setSize(500,520);
        MD.setBackground(color);
        A = new RotationAxis_Max();
        A.setBackground(Color.gray);
        A.setMaxDomain(MD);
        A.setRotationAxis_Return(R);
        A.setRotationAxis_Base(B);
        A.setRotationAxis_Base1(B1);
        A.setSize(500, 80);
        A.setLocation(0, 0);
        add(A);
        add(MD);
        setVisible(b);        
    }
    
    public void resized(int w, int h){
        try{
            A.setSize(w,80);
            MD.setSize(w, h);
        }catch(Exception e){}
    
}
}
