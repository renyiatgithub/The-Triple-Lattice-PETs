package hexagonalpets;

import java.awt.*;

public class PopupGeneralDomain extends PopupPanel {

    CanvasBaseDomain GD;
    RotationAxis_Base A;

    public PopupGeneralDomain(Color color) {
        super("Maximal Domains", 400, 600);
        GD = new CanvasBaseDomain();
        A = new RotationAxis_Base();
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

    public PopupGeneralDomain(boolean b, CanvasBaseDomain F, Color color) {
        super("Maximal Return Domains", 500, 600);
        GD = F;
        GD.setSize(500, 520);
        GD.setBackground(color);
        A = Main.AB;
        A.setBackground(Color.gray);
        A.setBaseDomain(GD);
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
