package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasFiberBundle extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    DrawMe draw = new DrawMe();
    static boolean draw_ph, draw_cell0, draw_cell1, draw_cell2, draw_pt = false;
    static boolean draw_check1, draw_check2, draw_check, draw_point, draw_domain = false;
    static double v1;
    static List<Complex> TV;
    static int sc = 70;
    static int[] ps;
    static Complex[] slope;
    static Complex[][] lattice;
    static LongPolyhedron[] P, Q;
    static PolygonWrapper[] Lines;
    static Color[] color = {Color.BLUE, Color.green, Color.red, Color.yellow, Color.darkGray};
    static List<LongPolyhedron> Cell0, Intersect0, Test0, Cell1, Intersect1,
            Test1, Cell2, Intersect2, Test2;
    static List<LongPolyhedron> Partition0, Partition1, Partition2,
            Image1, Image2, Image, Domain;
    static Complex point = new Complex(0, 0);
    static String string_cfe = "0";
    ListenSquare show_partition0, show_partition1, show_partition2, check_image,
            check1, check2, show_domain;
    Point JX;
    static LongPolyhedron S;

    public CanvasFiberBundle() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        TV = new ArrayList<Complex>();
        ps = new int[]{sc, 250, 260};
        v1 = 0;
        Cell0 = new ArrayList<LongPolyhedron>();
        Intersect0 = new ArrayList<LongPolyhedron>();
        Test0 = new ArrayList<LongPolyhedron>();
        Cell1 = new ArrayList<LongPolyhedron>();
        Intersect1 = new ArrayList<LongPolyhedron>();
        Test1 = new ArrayList<LongPolyhedron>();
        Cell2 = new ArrayList<LongPolyhedron>();
        Intersect2 = new ArrayList<LongPolyhedron>();
        Test2 = new ArrayList<LongPolyhedron>();
        Partition0 = new ArrayList<LongPolyhedron>();
        Partition1 = new ArrayList<LongPolyhedron>();
        Partition2 = new ArrayList<LongPolyhedron>();
        Image1 = new ArrayList<LongPolyhedron>();
        Image2 = new ArrayList<LongPolyhedron>();
        Image = new ArrayList<LongPolyhedron>();
        Domain = new ArrayList<LongPolyhedron>();

        show_partition0 = new ListenSquare(10, 30, 15, 15);
        show_partition1 = new ListenSquare(10, 50, 15, 15);
        show_partition2 = new ListenSquare(10, 70, 15, 15);
        check1 = new ListenSquare(320, 30, 15, 15);
        check2 = new ListenSquare(320, 50, 15, 15);
        check_image = new ListenSquare(320, 70, 15, 15);
        show_domain = new ListenSquare(410, 70, 15, 15);

        Lines = new PolygonWrapper[3];
        for (int i = 0; i < 3; i++) {
            Lines[i] = new PolygonWrapper();
            Lines[i].count = 2;
        }
        Lines[0].z[0] = new Complex(-6, 0);
        Lines[1].z[0] = new Complex(3, 3 * Math.sqrt(3));
        Lines[2].z[0] = new Complex(3, -3 * Math.sqrt(3));
        for (int i = 0; i < 3; i++) {
            Lines[i].z[1] = new Complex(-Lines[i].z[0].x, -Lines[i].z[0].y);
        }

        slope = new Complex[3];
        slope[0] = new Complex(1, Math.sqrt(3));
        slope[1] = new Complex(-1, 0);
        slope[2] = new Complex(1, -Math.sqrt(3));

        P = new LongPolyhedron[3];
        P[0] = new LongPolyhedron();
        P[0].count = 6;
        P[0].V[0] = new LongVector(0, 0, 0);
        P[0].V[1] = new LongVector(2, 0, 0);
        P[0].V[2] = new LongVector(0, 0, 2);
        P[0].V[3] = new LongVector(2, 0, 2);
        P[0].V[4] = new LongVector(1, 1, 2);
        P[0].V[5] = new LongVector(3, 1, 2);

        P[1] = new LongPolyhedron();
        P[1].count = 6;
        P[1].V[0] = new LongVector(0, 0, 0);
        P[1].V[1] = new LongVector(-1, 1, 0);
        P[1].V[2] = new LongVector(0, 0, 2);
        P[1].V[3] = new LongVector(-1, 1, 2);
        P[1].V[4] = new LongVector(-3, 1, 2);
        P[1].V[5] = new LongVector(-2, 0, 2);

        P[2] = new LongPolyhedron();
        P[2].count = 6;
        P[2].V[0] = new LongVector(0, 0, 0);
        P[2].V[1] = new LongVector(-1, -1.0, 0);
        P[2].V[2] = new LongVector(0, 0, 2);
        P[2].V[3] = new LongVector(-1, -1, 2);
        P[2].V[4] = new LongVector(1, - 1, 2);
        P[2].V[5] = new LongVector(0, -2, 2);

        Q = new LongPolyhedron[3];
        Q[0] = new LongPolyhedron();
        Q[0].count = 6;
        Q[0].V[0] = new LongVector(0, 0, 0);
        Q[0].V[1] = new LongVector(-1, 1, 0);
        Q[0].V[2] = new LongVector(0, 0, 2);
        Q[0].V[3] = new LongVector(-1, 1, 2);
        Q[0].V[4] = new LongVector(1, 1, 2);
        Q[0].V[5] = new LongVector(0, 2, 2);

        Q[1] = new LongPolyhedron();
        Q[1].count = 6;
        Q[1].V[0] = new LongVector(0, 0, 0);
        Q[1].V[1] = new LongVector(-1, -1, 0);
        Q[1].V[2] = new LongVector(0, 0, 2);
        Q[1].V[3] = new LongVector(-1, - 1, 2);
        Q[1].V[4] = new LongVector(-2, 0, 2);
        Q[1].V[5] = new LongVector(-3, - 1, 2);

        Q[2] = new LongPolyhedron();
        Q[2].count = 6;
        Q[2].V[0] = new LongVector(0, 0, 0);
        Q[2].V[1] = new LongVector(2, 0, 0);
        Q[2].V[2] = new LongVector(0, 0, 2);
        Q[2].V[3] = new LongVector(2, 0, 2);
        Q[2].V[4] = new LongVector(1, -1, 2);
        Q[2].V[5] = new LongVector(3, -1, 2);

        lattice = new Complex[3][2];
        lattice[0][0] = new Complex(-1, 1);
        lattice[0][1] = new Complex(1, 1);
        lattice[1][0] = new Complex(-1, -1);
        lattice[1][1] = new Complex(-2, 0);
        lattice[2][0] = new Complex(2, 0);
        lattice[2][1] = new Complex(1, -1);

        S = new LongPolyhedron();
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.drawString(string_cfe, 180, 20);

        show_partition0.render(g2, Color.green);
        show_partition1.render(g2, Color.green);
        show_partition2.render(g2, Color.green);
        check1.render(g2, Color.cyan);
        check2.render(g2, Color.cyan);
        check_image.render(g2, Color.cyan);
        show_domain.render(g2, Color.orange);
        g.drawString("partition 0", 35, 45);
        g.drawString("partition 1", 35, 65);
        g.drawString("partition 2", 35, 85);
        g.drawString("check1", 355, 45);
        g.drawString("check2", 355, 65);
        g.drawString("image", 355, 85);
        g.drawString("domain", 435, 85);

        for (PolygonWrapper l : Lines) {
            l = BasicPolygon.trans(l, ps[0], ps[1], ps[2]);
            paint_frame(g, l, Color.lightGray, 2);
        }

        if (draw_ph) {
            for (int i = 0; i < P.length; i++) {
                try {
                    PolygonWrapper p = TakeSlice(P[i], v1);
                    p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                    paint_frame(g, p, Color.white, 2);
                } catch (NullPointerException e) {

                }
            }
            for (int i = 0; i < Q.length; i++) {
                try {
                    PolygonWrapper p = TakeSlice(Q[i], v1);
                    p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                    paint_frame(g, p, Color.white, 2);

                } catch (NullPointerException e) {

                }
            }
        }
        if (draw_cell0) {
            paint_cell(g, Cell0, Test0, Intersect0, Partition0);
        }
        if (draw_cell1) {
            paint_cell(g, Cell1, Test1, Intersect1, Partition1);
        }
        if (draw_cell2) {
            paint_cell(g, Cell2, Test2, Intersect2, Partition2);
        }

        if (draw_check) {
            for (int i = 0; i < Image.size(); i++) {
                try {
                    PolygonWrapper p = TakeSlice(Image.get(i), v1);
                    p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                    paint_frame(g, p, Color.cyan, 2);
                    fill_frame(g, p, new Color(255, 255, 255, 80));
                    for (int j = 0; j < p.count; j++) {
                        Complex q = transform(p.z[j].x, p.z[j].y);
                        draw.drawDots(g, q, Color.cyan, 5);
                    }
                } catch (NullPointerException e) {
                }
            }
        }

        if (draw_domain) {
            for (int i = 0; i < Domain.size(); i++) {
                try {
                    PolygonWrapper p = TakeSlice(Domain.get(i), v1);
                    p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                    paint_frame(g, p, Color.orange, 2);
                    fill_frame(g, p, new Color(255, 255, 255, 80));
                    for (int j = 0; j < p.count; j++) {
                        Complex q = transform(p.z[j].x, p.z[j].y);
                        draw.drawDots(g, q, Color.orange, 5);
                    }
                } catch (NullPointerException e) {
                }
            }
        }

        if (draw_check1) {
            for (int i = 0; i < Image1.size(); i++) {
                try {
                    PolygonWrapper p = TakeSlice(Image1.get(i), v1);
                    p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                    paint_frame(g, p, Color.cyan, 2);
                    fill_frame(g, p, new Color(255, 255, 255, 80));
                    for (int j = 0; j < p.count; j++) {
                        Complex q = transform(p.z[j].x, p.z[j].y);
                        draw.drawDots(g, q, Color.cyan, 5);
                    }
                } catch (NullPointerException e) {
                }
            }
        }

        if (draw_check2) {
            for (int i = 0; i < Image2.size(); i++) {
                try {
                    PolygonWrapper p = TakeSlice(Image2.get(i), v1);
                    p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                    paint_frame(g, p, Color.cyan, 2);
                    fill_frame(g, p, new Color(255, 255, 255, 80));
                    for (int j = 0; j < p.count; j++) {
                        Complex q = transform(p.z[j].x, p.z[j].y);
                        draw.drawDots(g, q, Color.cyan, 5);
                    }
                } catch (NullPointerException e) {
                }
            }
        }

        if (draw_point) {
            Complex p = BasicPolygon.trans(point, ps[0], ps[1], ps[2]);
            p = transform(p.x, p.y);
            draw.drawDots(g, p, Color.red);
            try {
                PolygonWrapper pw = TakeSlice(S, v1);
                pw = BasicPolygon.trans(pw, ps[0], ps[1], ps[2]);
                paint_frame(g, pw, Color.red, 2);
                fill_frame(g, pw, Color.yellow);
            } catch (NullPointerException ee) {

            }
        }
    }

    public static void Partition() {
        Cell0 = FiberBundleMap.move_intersect(P[0], P[1], lattice[0], 0);
        Intersect0 = FiberBundleMap.move_intersect(P[0], P[1], lattice[0], 1);
        Test0 = FiberBundleMap.move_intersect(P[0], P[1], lattice[0], 2);
        Partition0 = FiberBundleMap.preimage(Intersect0, lattice[0]);

        Cell1 = FiberBundleMap.move_intersect(P[1], P[2], lattice[1], 0);
        Test1 = FiberBundleMap.move_intersect(P[1], P[2], lattice[1], 2);
        Intersect1 = FiberBundleMap.move_intersect(P[1], P[2], lattice[1], 1);
        Partition1 = FiberBundleMap.preimage(Intersect1, lattice[1]);

        Cell2 = FiberBundleMap.move_intersect(P[2], P[0], lattice[2], 0);
        Test2 = FiberBundleMap.move_intersect(P[2], P[0], lattice[2], 2);
        Intersect2 = FiberBundleMap.move_intersect(P[2], P[0], lattice[2], 1);
        Partition2 = FiberBundleMap.preimage(Intersect2, lattice[2]);

        Image1 = FiberBundleMap.intersect_list(Partition1, Intersect0);
        Image2 = FiberBundleMap.mapall(Image1, Partition1);
        Image2 = FiberBundleMap.intersect_list(Image2, Partition2);

        Image = FiberBundleMap.mapall(Image2, Partition2);
        Domain = FiberBundleMap.preimage(Image2, lattice[1]);
        Domain = FiberBundleMap.preimage_lattice(Domain, Intersect0);       
    }

    public static void doSlice() {
        v1 = RotationAxis_FB.s;
        draw_ph = true;
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
        try {
            if (e.getButton() == MouseEvent.BUTTON1 && JX.y >= 85) {
                point = unTransform(J.X);

                point = BasicPolygon.untrans(point, ps[0], ps[1], ps[2]);

                System.out.println();
                LongVector z = new LongVector(point.x, point.y, v1);
                System.out.println("Polyhedron check");

                for (int i = 0; i < Image2.size(); i++) {
                    if (LongPolyCombinatorics.inside(z, Image2.get(i))) {
                        System.out.println("The" + i + "th polyhedron");
                        S = Image2.get(i);
                        Image2.get(i).print();
                        break;
                    }
                }
                draw_point = true;
            }
        } catch (NullPointerException ne) {

        }
        if (show_partition0.inside(JX)) {
            draw_cell0 = !draw_cell0;
        }
        if (show_partition1.inside(JX)) {
            draw_cell1 = !draw_cell1;
        }
        if (show_partition2.inside(JX)) {
            draw_cell2 = !draw_cell2;
        }
        if (check1.inside(JX)) {
            draw_check1 = !draw_check1;
        }

        if (check2.inside(JX)) {
            draw_check2 = !draw_check2;
        }
        if (check_image.inside(JX)) {
            draw_check = !draw_check;
        }
        if (show_domain.inside(JX)) {
            draw_domain = !draw_domain;
        }

        repaint();
    }

    public void mouseMoved(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = new Point(J.X);
    }

    public void mouseEntered(MouseEvent e) {
        requestFocus();
    }

    public void doMouseClick(int mode) {
        if (mode == 1) {
            scaleUp(JX, 0);
        }
        if (mode == 2) {
            scaleUp(JX, 1);
        }
        repaint();
    }

    public void paint_frame(Graphics2D g, PolygonWrapper P, Color c, int n) {
        GeneralPath gp = P.toGeneralPath();
        gp = transform(gp);
        g.setColor(c);
        g.setStroke(new BasicStroke(n));
        g.draw(gp);
    }

    public void fill_frame(Graphics2D g, PolygonWrapper P, Color c) {
        GeneralPath gp = P.toGeneralPath();
        gp = transform(gp);
        g.setColor(c);
        g.fill(gp);
    }

    public static PolygonWrapper TakeSlice(LongPolyhedron P, double v) {
        P.SCALE = 1;
        Polyhedron PP = P.toPolyhedron();
        PolygonWrapper PW = PolyhedronSlicer.basicSlice(PP, v);
        for (int i = 0; i < PW.count; i++) {
            PW.z[i].y = PW.z[i].y * Math.sqrt(3);
        }
        return (PW);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        int test = 0;
        if (ch == 'd') {
            test = 2;
        }
        if (ch == 'f') {
            test = 1;
        }
        if (test > 0) {
            doMouseClick(test);
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void paint_cell(Graphics g2, List<LongPolyhedron> Cell,
            List<LongPolyhedron> Test, List<LongPolyhedron> Intersect,
            List<LongPolyhedron> Partition) {
        Graphics2D g = (Graphics2D) g2;
        for (LongPolyhedron C : Cell) {
            try {
                PolygonWrapper p = TakeSlice(C, v1);
                p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                paint_frame(g, p, Color.cyan, 1);
            } catch (NullPointerException e) {

            }
        }
        for (int i = 0; i < Test.size(); i++) {
            try {
                LongPolyhedron D = new LongPolyhedron(Test.get(i));
                PolygonWrapper p = TakeSlice(D, v1);
                p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                paint_frame(g, p, Color.red, 1);
            } catch (NullPointerException e) {

            }
        }

        for (int i = 0; i < Intersect.size(); i++) {
            try {
                LongPolyhedron C = new LongPolyhedron(Intersect.get(i));
                C = LongPolyIntersect.isnull(C);
                PolygonWrapper p = TakeSlice(C, v1);
                p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                paint_frame(g, p, Color.green, 2);
                for (int j = 0; j < p.count; j++) {
                    draw.drawDots(g, p.z[j], Color.green, 8);
                }
            } catch (NullPointerException e) {
            }
        }
        for (LongPolyhedron P : Partition) {
            try {
                PolygonWrapper p = TakeSlice(P, v1);
                p = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                paint_frame(g, p, Color.orange, 2);
            } catch (NullPointerException ne) {

            }
        }

    }

}
