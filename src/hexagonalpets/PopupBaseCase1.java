package hexagonalpets;

import java.awt.*;

public class PopupBaseCase1 extends PopupPanel {

    CanvasBase1 GD;
    RotationAxis_Base1 A;

    public PopupBaseCase1(Color color) {
        super("BaseCase 1", 400, 600);
        GD = new CanvasBase1();
        A = new RotationAxis_Base1();
        GD.setBackground(color);
        A.setBackground(color);
        GD.setSize(400, 520);
        A.setSize(400, 80);
        A.setLocation(0, 0);
        add(A);
        GD.setLocation(0, 90);
        add(GD);
        setVisible(true);
    }

    public PopupBaseCase1(boolean b, CanvasBase1 F, Color color) {
        super("BaseCase 1", 500, 600);
        GD = F;
        GD.setSize(500, 520);
        GD.setBackground(color);
        A = Main.AB1;;
        A.setBackground(Color.gray);
        A.setReturnDomain(GD);
        A.setSize(500, 80);
        A.setLocation(0, 0);
        add(A);
        add(GD);
        setVisible(b);
    }

    public void resized(int w, int h) {
        try {
            A.setSize(w, 80);
            GD.setSize(w, h);
        } catch (Exception e) {
        }

    }
}