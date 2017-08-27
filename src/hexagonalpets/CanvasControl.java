package hexagonalpets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;

public class CanvasControl extends ScaleCanvas implements MouseListener, MouseMotionListener {

    DrawMe draw = new DrawMe();
    Graphics2D g3;
    ListenSquare pop_triplelattice, pop_cellcanvas, pop_tilingcanvas,
            pop_fiber, pop_scissor, pop_action, pop_graph, pop_baby_proof,
            pop_symmetry, pop_reflection, pop_max, pop_return, pop_base0, 
            pop_base1;

    double s = 0;
    int[] cfe;
    static String string_cfe = "0";

    public CanvasControl() {
        addMouseListener(this);
        addMouseMotionListener(this);
        pop_triplelattice = new ListenSquare(30, 70, 100, 60);
        pop_cellcanvas = new ListenSquare(180, 70, 100, 60);
        pop_tilingcanvas = new ListenSquare(330, 70, 100, 60);
        pop_scissor = new ListenSquare(30, 150, 100, 60);
        pop_action = new ListenSquare(180, 150, 100, 60);
        pop_graph = new ListenSquare(330, 150, 100, 60);

        pop_baby_proof = new ListenSquare(30, 230, 100, 60);
        pop_symmetry = new ListenSquare(180, 230, 100, 60);
        pop_reflection = new ListenSquare(330, 230, 100, 60);
        pop_fiber = new ListenSquare(30, 310, 100, 60);
        pop_max = new ListenSquare(180, 310, 100, 60);
        pop_return = new ListenSquare(330, 310, 100, 60);

        pop_base0 = new ListenSquare(30, 390, 100, 60);
        pop_base1 = new ListenSquare(180,390,100,60);
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, getWidth(), getHeight());
        pop_triplelattice.render(g2, new Color(70, 130, 180));
        pop_cellcanvas.render(g2, new Color(70, 130, 180));
        pop_tilingcanvas.render(g2, new Color(70, 130, 180));
        pop_action.render(g2, new Color(70, 130, 180));
        pop_graph.render(g2, new Color(70, 130, 180));
        pop_scissor.render(g2, new Color(70, 130, 180));
        pop_baby_proof.render(g2, new Color(25, 25, 112));
        pop_symmetry.render(g2, new Color(25, 25, 112));
        pop_reflection.render(g2, new Color(25, 25, 112));
        pop_fiber.render(g2, new Color(25, 25, 112));
        pop_max.render(g2, new Color(25, 25, 112));
        pop_return.render(g2, new Color(25, 25, 112));
        pop_base0.render(g2, Color.black);
        pop_base1.render(g2, Color.black);

        g.setColor(new Color(204, 255, 255));
        g.setFont(new Font("Helvetica", Font.PLAIN, 12));
        g.drawString(string_cfe, 200, 60);
        g.setFont(new Font("Helvetica", Font.PLAIN, 20));
        g.setColor(Color.white);
        g.drawString("Settings", 43, 105);
        g.drawString("Map", 210, 105);
        g.drawString("Tiling", 360, 105);
        g.drawString("Cut,paste", 37, 185);
        g.drawString("Action", 200, 185);
        g.drawString("Ar.graph", 340, 185);
        g.drawString("Ex. φ", 52, 265);
        g.drawString("Symmetry", 185, 265);
        g.drawString("Reflection", 336, 265);
        g.drawString("Polyhedra", 36, 345);
        g.drawString("Max.dom.", 186, 345);
        g.drawString("Ret.dom.", 340, 345);
        g.drawString("Base0", 50, 425);
        g.drawString("Base1", 200, 425);
    }

    public void parameter() {
        s = Main.value;
        int[] frac = MathRational.approximate(s, Math.pow(10, -12));
        cfe = MathRational.cfe(frac);
        string_cfe = new String();
        for (int i = 0; i < cfe.length - 1; i++) {
            string_cfe = string_cfe + Integer.toString(cfe[i]) + ",";
        }
        string_cfe = string_cfe + Integer.toString(cfe[cfe.length - 1]);
        string_cfe = string_cfe + "   " + Integer.toString(frac[0])
                + "/" + Integer.toString(frac[1]);
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        if (pop_triplelattice.inside(J.X)) {
            PopupLatticeCanvas POPTRIPLE = new PopupLatticeCanvas(true, Main.TL,
                    new Color(70, 130, 180));
        }
        if (pop_cellcanvas.inside(J.X)) {
            PopupCellCanvas POPCELL = new PopupCellCanvas(true, Main.CC,
                    new Color(70, 130, 180));
        }
        if (pop_tilingcanvas.inside(J.X)) {
            PopupTilingCanvas POPTIG = new PopupTilingCanvas(true, Main.TIG,
                    new Color(70, 130, 180));
        }

        if (pop_scissor.inside(J.X)) {
//            PopupScissor PopCS = new PopupScissor(true, Main.CS, Main.G,
//                    new Color(70, 130, 180));
            PopupScissor PopCS = new PopupScissor(true, Main.CS, Main.G,
                    Color.white);
        }
        if (pop_graph.inside(J.X)) {
            PopupGraph PopG = new PopupGraph(true, Main.G, Color.black);
        }

        if (pop_action.inside(J.X)) {
            PopupAction PopA = new PopupAction(true, Main.A, new Color(70, 130, 180));
            PopA.A.Tiling(Main.value);
            PopA.A.repaint();
        }
        if (pop_baby_proof.inside(J.X)) {
            PopupGoldenRatio PopGR = new PopupGoldenRatio(true, Main.GR, new Color(25, 25, 112));
            PopGR.GR.Setup();
        }

        if (pop_symmetry.inside(J.X)) {
            PopupSymmetry PopSYM = new PopupSymmetry(true, Main.SYM, new Color(25, 25, 112));
            PopSYM.CSym.Setup();
        }

        if (pop_reflection.inside(J.X)) {
            PopupReflection PopREF = new PopupReflection(true, Main.R, new Color(25, 25, 112));
            PopREF.R.Setup();
        }

        if (pop_fiber.inside(J.X)) {
            PopupFiberBundle PopFB = new PopupFiberBundle(true, Main.FB, new Color(25, 25, 112));
            Main.FB.Partition();
        }
        if (pop_max.inside(J.X)) {
//            PopupMaximalDomain PopMD = new PopupMaximalDomain(true, Main.MD, 
//                    Main.AR, Main.AB, Main.AB1, new Color(25, 25, 112));
            PopupMaximalDomain PopMD = new PopupMaximalDomain(true, Main.MD, 
                    Main.AR, Main.AB, Main.AB1, Color.white);
            Main.MD.Partition();
        }

        if (pop_return.inside(J.X)) {
            PopupReturnDomain PopRD = new PopupReturnDomain(true, Main.RD, new Color(25, 25, 112));
            Main.RD.Partition();
        }

        if (pop_base0.inside(J.X)) {
            PopupGeneralDomain PopGD = new PopupGeneralDomain(true, Main.GD, Color.black);
            Main.GD.Partition();
        }
        
        if(pop_base1.inside(J.X)){
            PopupBaseCase1 PopBase = new PopupBaseCase1(true, Main.Base1, Color.black);
            Main.Base1.Partition();
        }
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void mouseMoved(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
