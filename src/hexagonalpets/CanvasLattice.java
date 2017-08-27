package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasLattice extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    DrawMe draw = new DrawMe();
    static PolygonWrapper[] F, Lines;
    static List<PolygonWrapper> M, ch1, ch2, image, domain;
    static List<List<PolygonWrapper>> PART0, PART1, PART2, AST0, AST1, AST2;
    static List<Complex> TV0, TV1, TV2;
    static Complex[] c, slope, lattice0, lattice1, lattice2;
    Graphics2D g3;
    static double s = 0;
    static boolean drawquad, draw_lattices, draw_partition0 = false;
    static boolean draw_partition1, draw_partition2 = false;
    static boolean draw_check0, draw_check1, draw_check2, draw_domain = false;
    static boolean draw_ast0, draw_ast1, draw_ast2 = false;
    ListenSquare display_intersection, control, display_partition0,
            display_partition1, display_partition2, check_partition0,
            check_partition1, check_partition2, display_domain;
    Point JX;

    public CanvasLattice() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setScales(0, 0, 1);

        display_intersection = new ListenSquare(10, 10, 15, 15);
        display_partition0 = new ListenSquare(10, 30, 15, 15);
        display_partition1 = new ListenSquare(10, 50, 15, 15);
        display_partition2 = new ListenSquare(10, 70, 15, 15);
        check_partition0 = new ListenSquare(300, 30, 15, 15);
        check_partition1 = new ListenSquare(300, 50, 15, 15);
        check_partition2 = new ListenSquare(300, 70, 15, 15);
        display_domain = new ListenSquare(300, 10, 15, 15);
        control = new ListenSquare(10, 10, 400, 100);

        F = new PolygonWrapper[3];
        for (int i = 0; i < 3; i++) {
            F[i] = new PolygonWrapper();
        }
        M = new ArrayList<PolygonWrapper>();

        PART0 = new ArrayList<List<PolygonWrapper>>();
        PART1 = new ArrayList<List<PolygonWrapper>>();
        PART2 = new ArrayList<List<PolygonWrapper>>();
        AST0 = new ArrayList<List<PolygonWrapper>>();
        AST1 = new ArrayList<List<PolygonWrapper>>();
        AST2 = new ArrayList<List<PolygonWrapper>>();
        TV0 = new ArrayList<Complex>();
        TV1 = new ArrayList<Complex>();
        TV2 = new ArrayList<Complex>();
        lattice0 = new Complex[2];
        lattice1 = new Complex[2];
        lattice2 = new Complex[2];

        c = new Complex[3];
        Lines = new PolygonWrapper[3];
        for (int i = 0; i < 3; i++) {
            Lines[i] = new PolygonWrapper();
            Lines[i].count = 2;
        }
        Lines[0].z[0] = new Complex(-4, 0);
        Lines[1].z[0] = new Complex(2, 2 * Math.sqrt(3));
        Lines[2].z[0] = new Complex(2, -2 * Math.sqrt(3));
        for (int i = 0; i < 3; i++) {
            Lines[i].z[1] = new Complex(-Lines[i].z[0].x, -Lines[i].z[0].y);
        }
        slope = new Complex[3];
        slope[0] = new Complex(0, 0);
        slope[1] = new Complex(-Math.sqrt(3), 0);
        slope[2] = new Complex(Math.sqrt(3), 0);

    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        display_intersection.render(g2, new Color(65, 105, 225));
        display_partition0.render(g2, Color.green);
        display_partition1.render(g2, Color.green);
        display_partition2.render(g2, Color.green);
        check_partition0.render(g2, Color.orange);
        check_partition1.render(g2, Color.orange);
        check_partition2.render(g2, Color.orange);
        display_domain.render(g2, new Color(65, 105, 225));

        Color[] color_fund = {Color.white, Color.orange, Color.green};
        g.setColor(Color.white);
        g.drawString("see intersection", 35, 25);
        g.drawString("partition 1", 35, 45);
        g.drawString("partition 2", 35, 65);
        g.drawString("partition 3", 35, 85);
        g.drawString("check 1", 325, 45);
        g.drawString("check 2", 325, 65);
        g.drawString("image", 325, 85);
        g.drawString("domain", 325, 25);
        g.drawString("'f/d': zoom in/out", 280, 340);
        g.drawString("'1,2,3': dispflay tilings", 280, 360);

        if (drawquad) {
            for (int i = 0; i < F.length; i++) {
                PolygonWrapper P = BasicPolygon.trans(F[i], 60, 200, 200);
                frame_polygon(g, P, color_fund[i], 2);
            }
        }

        if (draw_lattices) {
            for (PolygonWrapper l : Lines) {
                l = BasicPolygon.trans(l, 100, 200, 200);
                frame_polygon(g, l, Color.lightGray, 2);
//                frame_polygon(g, l, Color.black, 2);
            }
            for (PolygonWrapper m : M) {
                PolygonWrapper m0 = BasicPolygon.trans(m, 60, 200, 200);
                frame_polygon(g, m0, Color.white, 2);
//                frame_polygon(g, m0, Color.black, 2);
            }
        }
        if (draw_partition0) {
            for (PolygonWrapper p : PART0.get(0)) {
                PolygonWrapper p0 = BasicPolygon.trans(p, 60, 200, 200);
                frame_polygon(g, p0, Color.orange, 2);
            }
            for (PolygonWrapper p : PART0.get(1)) {
                PolygonWrapper p0 = BasicPolygon.trans(p, 60, 200, 200);
                frame_polygon(g, p0, Color.green, 2);
            }
        }
        if (draw_ast0) {
            for (PolygonWrapper p : AST0.get(1)) {
                PolygonWrapper p0 = BasicPolygon.trans(p, 60, 200, 200);
                frame_polygon(g, p0, Color.cyan, 1);
            }
        }

        if (draw_ast1) {
            for (PolygonWrapper p : AST1.get(1)) {
                PolygonWrapper p0 = BasicPolygon.trans(p, 60, 200, 200);
                frame_polygon(g, p0, Color.cyan, 1);
            }
        }

        if (draw_partition1) {
            for (PolygonWrapper p : PART1.get(0)) {
                PolygonWrapper p0 = BasicPolygon.trans(p, 60, 200, 200);
                frame_polygon(g, p0, Color.orange, 3);
            }
            for (PolygonWrapper p : PART1.get(1)) {
                PolygonWrapper p0 = BasicPolygon.trans(p, 60, 200, 200);
                frame_polygon(g, p0, Color.green, 3);
            }
        }
        if (draw_partition2) {
            for (PolygonWrapper p : PART2.get(0)) {
                PolygonWrapper p0 = BasicPolygon.trans(p, 60, 200, 200);
                frame_polygon(g, p0, Color.orange, 3);
            }
            for (PolygonWrapper p : PART2.get(1)) {
                PolygonWrapper p0 = BasicPolygon.trans(p, 60, 200, 200);
                frame_polygon(g, p0, Color.green, 3);
            }
        }

        if (draw_ast2) {
            for (PolygonWrapper p : AST2.get(1)) {
                PolygonWrapper p0 = BasicPolygon.trans(p, 60, 200, 200);
                frame_polygon(g, p0, Color.cyan, 1);
            }
        }
        if (draw_check0) {
            for (PolygonWrapper c : image) {
                PolygonWrapper p0 = BasicPolygon.trans(c, 60, 200, 200);
                GeneralPath gp = p0.toGeneralPath();
                gp = transform(gp);
                g.setColor(new Color(255, 255, 255, 80));
                g.fill(gp);
                g.setColor(Color.black);
                g.setStroke(new BasicStroke(2));
                g.draw(gp);

            }
        }

        if (draw_check1) {
            for (PolygonWrapper c : ch1) {
                PolygonWrapper p0 = BasicPolygon.trans(c, 60, 200, 200);
                GeneralPath gp = p0.toGeneralPath();
                gp = transform(gp);
                g.setColor(new Color(255, 255, 255, 80));
                g.fill(gp);
                g.setColor(Color.black);
                g.setStroke(new BasicStroke(2));
                g.draw(gp);

            }
        }
        if (draw_check2) {
            for (PolygonWrapper c : ch2) {
                PolygonWrapper p0 = BasicPolygon.trans(c, 60, 200, 200);
                GeneralPath gp = p0.toGeneralPath();
                gp = transform(gp);
                g.setColor(new Color(255, 255, 255, 80));
                g.fill(gp);
                g.setColor(Color.black);
                g.setStroke(new BasicStroke(2));
                g.draw(gp);

            }
        }
        if (draw_domain) {
            for (PolygonWrapper c : domain) {
                PolygonWrapper p0 = BasicPolygon.trans(c, 60, 200, 200);
                GeneralPath gp = p0.toGeneralPath();
                gp = transform(gp);
                g.setColor(new Color(255, 255, 255, 80));
                g.fill(gp);
                g.setColor(Color.red);
                g.setStroke(new BasicStroke(2));
                g.draw(gp);

            }
        }
    }

    public static void Evaluate(double s) {
        M.clear();
        System.out.println("value: " + s);
        c[0] = new Complex(0.5 * (s + 2), Math.sqrt(3) * s / 2);
        F[0].count = 4;
        F[0].z[0] = Complex.minus(new Complex(s, Math.sqrt(3) * s), c[0]);
        F[0].z[1] = Complex.minus(new Complex(2 + s, Math.sqrt(3) * s), c[0]);
        F[0].z[2] = Complex.minus(new Complex(2, 0), c[0]);
        F[0].z[3] = Complex.minus(new Complex(0, 0), c[0]);

        c[1] = new Complex(0.5 * (-1 - 2 * s), Math.sqrt(3) / 2);
        F[1].count = 4;
        F[1].z[0] = Complex.minus(new Complex(-1 - 2 * s, Math.sqrt(3)), c[1]);
        F[1].z[1] = Complex.minus(new Complex(-1, Math.sqrt(3)), c[1]);
        F[1].z[2] = Complex.minus(new Complex(0, 0), c[1]);
        F[1].z[3] = Complex.minus(new Complex(-2 * s, 0), c[1]);

        c[2] = new Complex(0.5 * (s - 1), -0.5 * Math.sqrt(3) * (s + 1));
        F[2].count = 4;
        F[2].z[0] = Complex.minus(new Complex(0, 0), c[2]);
        F[2].z[1] = Complex.minus(new Complex(s, -Math.sqrt(3) * s), c[2]);
        F[2].z[2] = Complex.minus(new Complex(s - 1, -Math.sqrt(3) * (1 + s)), c[2]);
        F[2].z[3] = Complex.minus(new Complex(-1, -Math.sqrt(3)), c[2]);

        for (int i = 0; i < F.length; i++) {
            PolygonWrapper m = new PolygonWrapper(F[i]);
            for (int j = 0; j < m.count; j++) {
                m.z[j] = Complex.plus(m.z[j], c[i]);
            }
            M.add(m);
        }
        draw_lattices = true;
        drawquad = false;

        for (int i = 0; i < 3; i++) {
            PolygonWrapper m = new PolygonWrapper(M.get(i));
            for (int j = 0; j < m.count; j++) {
                m.z[j] = BasicGeometry.pt_reflt_line(m.z[j], slope[i]);
            }
            M.add(m);
        }

        lattice0[0] = new Complex(s, Math.sqrt(3) * s);
        lattice0[1] = new Complex(-1, Math.sqrt(3));
        lattice1[0] = new Complex(-2 * s, 0);
        lattice1[1] = new Complex(- 1, -Math.sqrt(3));
        lattice2[0] = new Complex(s, -Math.sqrt(3) * s);
        lattice2[1] = new Complex(2, 0);

        PART0 = Pet.move_lattice(s, M.get(0), M.get(1), lattice0, 0);
        AST0 = Pet.move_lattice(s, M.get(0), M.get(1), lattice0, 1);

        TV0 = Pet.Get_TV(PART0);
        PART1 = Pet.move_lattice(s, M.get(1), M.get(2), lattice1, 0);
        AST1 = Pet.move_lattice(s, M.get(1), M.get(2), lattice1, 1);
        TV1 = Pet.Get_TV(PART1);
        PART2 = Pet.move_lattice(s, M.get(2), M.get(0), lattice2, 0);
        TV2 = Pet.Get_TV(PART2);
        AST2 = Pet.move_lattice(s, M.get(2), M.get(0), lattice2, 1);

        ch1 = Pet.Partition_Image(PART0.get(1), PART1.get(0));
        ch2 = Pet.MapAll(ch1, PART1.get(0), TV1);
        ch2 = Pet.Partition_Image(ch2, PART2.get(0));
        image = Pet.MapAll(ch2, PART2.get(0), TV2);
        List<PolygonWrapper> domain1 = Pet.GetDomain(ch2, PART1.get(1), TV1);
        domain = Pet.GetDomain(domain1, PART0.get(1), TV0);
    }

    public static List<PolygonWrapper> Setup(double s) {
        List<PolygonWrapper> MM = new ArrayList<PolygonWrapper>();
        Complex[] cc = new Complex[3];
        cc[0] = new Complex(0.5 * (s + 2), Math.sqrt(3) * s / 2);
        cc[1] = new Complex(0.5 * (-1 - 2 * s), Math.sqrt(3) / 2);
        cc[2] = new Complex(0.5 * (s - 1), -0.5 * Math.sqrt(3) * (s + 1));

        Complex[] slope_setup = new Complex[3];
        slope_setup[0] = new Complex(0, 0);
        slope_setup[1] = new Complex(-Math.sqrt(3), 0);
        slope_setup[2] = new Complex(Math.sqrt(3), 0);

        PolygonWrapper[] FF = new PolygonWrapper[3];

        FF[0] = new PolygonWrapper();
        FF[0].count = 4;
        FF[0].z[0] = Complex.minus(new Complex(s, Math.sqrt(3) * s), cc[0]);
        FF[0].z[1] = Complex.minus(new Complex(2 + s, Math.sqrt(3) * s), cc[0]);
        FF[0].z[2] = Complex.minus(new Complex(2, 0), cc[0]);
        FF[0].z[3] = Complex.minus(new Complex(0, 0), cc[0]);

        FF[1] = new PolygonWrapper();
        FF[1].count = 4;
        FF[1].z[0] = Complex.minus(new Complex(-1 - 2 * s, Math.sqrt(3)), cc[1]);
        FF[1].z[1] = Complex.minus(new Complex(-1, Math.sqrt(3)), cc[1]);
        FF[1].z[2] = Complex.minus(new Complex(0, 0), cc[1]);
        FF[1].z[3] = Complex.minus(new Complex(-2 * s, 0), cc[1]);

        FF[2] = new PolygonWrapper();
        FF[2].count = 4;
        FF[2].z[0] = Complex.minus(new Complex(0, 0), cc[2]);
        FF[2].z[1] = Complex.minus(new Complex(s, -Math.sqrt(3) * s), cc[2]);
        FF[2].z[2] = Complex.minus(new Complex(s - 1, -Math.sqrt(3) * (1 + s)), cc[2]);
        FF[2].z[3] = Complex.minus(new Complex(-1, -Math.sqrt(3)), cc[2]);

        for (int i = 0; i < FF.length; i++) {
            PolygonWrapper m = new PolygonWrapper(FF[i]);
            MM.add(BasicPolygon.move(m, cc[i]));
        }

        for (int i = 0; i < 3; i++) {
            PolygonWrapper m = new PolygonWrapper(MM.get(i));
            for (int j = 0; j < m.count; j++) {
                m.z[j] = BasicGeometry.pt_reflt_line(m.z[j], slope_setup[i]);
            }
            MM.add(m);
        }
        return (MM);
    }

    public static List<PolygonWrapper> GetCell(double s, List<PolygonWrapper> MM) {
        List<PolygonWrapper> cell = new ArrayList<PolygonWrapper>();
        Complex[] L0 = new Complex[2];
        L0[0] = new Complex(s, Math.sqrt(3) * s);
        L0[1] = new Complex(-1, Math.sqrt(3));
        Complex[] L1 = new Complex[2];
        L1[0] = new Complex(-2 * s, 0);
        L1[1] = new Complex(- 1, -Math.sqrt(3));
        Complex[] L2 = new Complex[2];
        L2[0] = new Complex(s, -Math.sqrt(3) * s);
        L2[1] = new Complex(2, 0);

        List<List<PolygonWrapper>> partition0 = Pet.move_lattice(s, MM.get(0), MM.get(1), L0, 0);
        List<Complex> vectors0 = Pet.Get_TV(partition0);
        List<List<PolygonWrapper>> partition1 = Pet.move_lattice(s, MM.get(1), MM.get(2), L1, 0);
        List<Complex> vectors1 = Pet.Get_TV(partition1);
        List<List<PolygonWrapper>> partition2 = Pet.move_lattice(s, MM.get(2), MM.get(0), L2, 0);
        List<Complex> vectors2 = Pet.Get_TV(partition2);

        List<PolygonWrapper> image1 = Pet.Partition_Image(partition0.get(1), partition1.get(0));
        List<PolygonWrapper> image2 = Pet.MapAll(image1, partition1.get(0), vectors1);
        image2 = Pet.Partition_Image(image2, partition2.get(0));
        List<PolygonWrapper> image = Pet.MapAll(image2, partition2.get(0), vectors2);
        List<PolygonWrapper> domain1 = Pet.GetDomain(image2, partition1.get(1), vectors1);
        cell = Pet.GetDomain(domain1, partition0.get(1), vectors0);
        for (int i = 0; i < cell.size(); i++) {
            PolygonWrapper P = cell.get(i);
            PolygonWrapper Q = image.get(i);
            cell.get(i).vector = Complex.minus(Q.center(), P.center());
        }

        return (cell);
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        if (control.inside(J.X) == false) {
            if (e.getButton() == MouseEvent.BUTTON1) {

            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                scaleUp(J.X, 1);
            }
        } else {
            if (display_intersection.inside(J.X)) {
                drawquad = !drawquad;
                draw_lattices = !draw_lattices;
                draw_partition0 = false;
                draw_partition1 = false;
                draw_partition2 = false;
            }
            if (display_partition0.inside(J.X)) {
                draw_partition0 = !draw_partition0;
                drawquad = false;
            }
            if (display_partition1.inside(J.X)) {
                draw_partition1 = !draw_partition1;
                drawquad = false;
            }
            if (display_partition2.inside(J.X)) {
                draw_partition2 = !draw_partition2;
                drawquad = false;
            }
            if (check_partition0.inside(J.X)) {
                draw_check1 = !draw_check1;
            }
            if (check_partition1.inside(J.X)) {
                draw_check2 = !draw_check2;
            }
            if (check_partition2.inside(J.X)) {
                draw_check0 = !draw_check0;
            }
            if (display_domain.inside(J.X)) {
                draw_domain = !draw_domain;
            }

        }
        repaint();
    }

    public void doMouseClick(int mode) {
        if (mode == 1) {
            draw_ast0 = !draw_ast0;
        }
        if (mode == 2) {
            draw_ast1 = !draw_ast1;
        }
        if (mode == 3) {
            draw_ast2 = !draw_ast2;
        }
        if (mode == 4) {
            scaleUp(JX, 0);
        }
        if (mode == 5) {
            scaleUp(JX, 1);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = new Point(J.X);
    }

    public void mouseEntered(MouseEvent e) {
        requestFocus();
    }

    public void frame_polygon(Graphics2D g, PolygonWrapper P, Color c, int n) {
        GeneralPath gp = P.toGeneralPath();
        gp = transform(gp);
        g.setColor(c);
        g.setStroke(new BasicStroke(n));
        g.draw(gp);
    }

    public void fill_polygon(Graphics2D g, PolygonWrapper P, Color[] c, int[] n) {
        GeneralPath gp = P.toGeneralPath();
        gp = transform(gp);
        g.setColor(c[0]);
        g.setStroke(new BasicStroke(n[0]));
        g.draw(gp);
        if (n[1] == 1) {
            g.setColor(c[1]);
            g.fill(gp);
        }
    }

    public void fill_frame(Graphics2D g, PolygonWrapper P, Color c) {
        GeneralPath gp = P.toGeneralPath();
        gp = transform(gp);
        g.setColor(c);
        g.fill(gp);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        int test = 0;
        if (ch == '1') {
            test = 1;
        }
        if (ch == '2') {
            test = 2;
        }
        if (ch == '3') {
            test = 3;
        }
        if (ch == 'f') {
            test = 4;
        }
        if (ch == 'd') {
            test = 5;
        }

        if (test > 0) {
            doMouseClick(test);
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
