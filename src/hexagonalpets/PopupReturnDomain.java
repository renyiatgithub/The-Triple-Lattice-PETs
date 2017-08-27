package hexagonalpets;

import java.awt.*;

public class PopupReturnDomain extends PopupPanel {

    CanvasReturnDomain RD;
    RotationAxis_Return A;

    public PopupReturnDomain(Color color) {
        super("Maximal Domains", 400, 600);
        RD = new CanvasReturnDomain();
        A = new RotationAxis_Return();
        RD.setBackground(color);
        A.setBackground(color);
        RD.setSize(400, 520);
        A.setSize(400, 80);
        A.setLocation(0, 0);
        add(A);
        RD.setLocation(0, 90);
        add(RD);
        setVisible(true);
    }

    public PopupReturnDomain(boolean b, CanvasReturnDomain F, Color color) {
        super("Maximal Return Domains", 500, 600);
        RD = F;
        RD.setSize(500, 520);
        RD.setBackground(color);
//        A = new RotationAxis_Return();
        A = Main.AR;
        A.setBackground(Color.gray);
        A.setReturnDomain(RD);
        A.setSize(500, 80);
        A.setLocation(0, 0);
        add(A);
        add(RD);
        setVisible(b);
    }

    public void resized(int w, int h) {
        try {
            A.setSize(w, 80);
            RD.setSize(w, h);
        } catch (Exception e) {
        }

    }
}
