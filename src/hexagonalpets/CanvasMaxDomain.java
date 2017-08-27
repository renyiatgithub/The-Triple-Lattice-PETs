package hexagonalpets;

import static hexagonalpets.CanvasFiberBundle.Domain;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasMaxDomain extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    DrawMe draw = new DrawMe();
    static boolean draw_domain, draw_pointer = false;
    static double v1;
    static int[] ps;
    static List<LongPolyhedron> Image, Domain, Domain_ref, Image_ref;
    static LongPolyhedron POINTER;
    static List<double[]> coeffs;
    static LongPolyhedron CUT, D;
    static Complex point = new Complex(0, 0);
    Point JX;

    public CanvasMaxDomain() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        ps = new int[]{95, 130, 100};
        v1 = 0;
        Image = new ArrayList<LongPolyhedron>();
        Domain = new ArrayList<LongPolyhedron>();
        Image_ref = new ArrayList<LongPolyhedron>();
        Domain_ref = new ArrayList<LongPolyhedron>();
        List<double[]> coeffs = new ArrayList<double[]>();

        D = new LongPolyhedron();
        D.count = 8;
        D.V[0] = new LongVector(0, 0, 1);
        D.V[1] = new LongVector(2, 0, 1);
        D.V[2] = new LongVector(0.5, 0.5, 1);
        D.V[3] = new LongVector(2.5, 0.5, 1);
        D.V[4] = new LongVector(0, 0, 2);
        D.V[5] = new LongVector(2, 0, 2);
        D.V[6] = new LongVector(1, 1, 2);
        D.V[7] = new LongVector(3, 1, 2);
        D.FACE = LongPolyFace.faceList(D);

        D = FiberBundleMap.move(D, new Complex(-0.5, -0.5), 1);
        D = FiberBundleMap.move(D, new Complex(-1, 0), 0);

        CUT = new LongPolyhedron();
        CUT.count = 6;
        CUT.V[0] = new LongVector(0, 0, 1);
        CUT.V[1] = new LongVector(1, 0, 1);
        CUT.V[2] = new LongVector(0.5, 0.5, 1);
        CUT.V[3] = new LongVector(1.5, 0.5, 1);
        CUT.V[4] = new LongVector(0, 0, 2);
        CUT.V[5] = new LongVector(1, 1, 2);
        CUT = FiberBundleMap.move(CUT, new Complex(-0.5, -0.5), 1);
        CUT = FiberBundleMap.move(CUT, new Complex(-1, 0), 0);
        CUT.FACE = LongPolyFace.faceList(CUT);

        POINTER = new LongPolyhedron(CUT);
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);

        if (draw_domain) {
            PolygonWrapper[] cut = new PolygonWrapper[2];
            LongPolyhedron CUT0 = FiberBundleMap.move(CUT, new Complex(2, 0), 1);
            cut[0] = TakeSlice(CUT, v1);
            cut[1] = TakeSlice(CUT0, v1);

            PolygonWrapper[][] polys = new PolygonWrapper[2][2];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    polys[i][j] = BasicPolygon.trans(cut[j], ps[0], 
                            ps[1] + i * 300, ps[2] + j * 220);
//                    fill_frame(g, polys[i][j], new Color(255, 0, 0, 80));
                }
            }

            paint_general(g, Domain, ps[0], ps[1], ps[2] + 100, Color.black);
            paint_general(g, Image, ps[0], ps[1] + 250, ps[2] + 100, Color.black);

            PolygonWrapper d0 = TakeSlice(D, v1);
        }

        if (draw_pointer) {
            PolygonWrapper pointer = TakeSlice(POINTER, v1);
            pointer = BasicPolygon.trans(pointer, ps[0], ps[1], ps[2] + 220);
            fill_frame(g, pointer, new Color(0, 0, 0, 80));
        }
    }

    public static void Partition() {
        List<LongPolyhedron> Image0 = CanvasFiberBundle.Image;
        List<LongPolyhedron> Domain0 = CanvasFiberBundle.Domain;
        for (int i = 0; i < Domain0.size(); i++) {
            try {
                LongPolyhedron Q = LongPolyIntersect.chop(Domain0.get(i), 1, 1, 0);
                LongPolyhedron W = LongPolyIntersect.chop(Image0.get(i), 1, 1, 0);

                if (Q != null) {
                    LongPolyhedron Q0 = new LongPolyhedron(Q);
                    Q0 = FiberBundleMap.move(Q0, new Complex(-0.5, -0.5), 1);
                    Q0 = FiberBundleMap.move(Q0, new Complex(-1, 0), 0);
                    Domain_ref.add(Q0);

                    LongPolyhedron W0 = new LongPolyhedron(W);
                    W0 = FiberBundleMap.move(W0, new Complex(-0.5, -0.5), 1);
                    W0 = FiberBundleMap.move(W0, new Complex(-1, 0), 0);
                    Image_ref.add(W0);

                    if (LongPolyCombinatorics.inside(Q0.center(), CUT)) {
                        Q0 = FiberBundleMap.move(Q0, new Complex(2, 0), 0);
                    }
                    if (LongPolyCombinatorics.inside(W0.center(), CUT)) {
                        W0 = FiberBundleMap.move(W0, new Complex(2, 0), 0);
                    }
                    Q0 = FiberBundleMap.move(Q0, new Complex(-2, 0), 0);
                    Q0 = FiberBundleMap.move(Q0, new Complex(2, 0), 1);
                    W0 = FiberBundleMap.move(W0, new Complex(-2, 0), 0);
                    W0 = FiberBundleMap.move(W0, new Complex(2, 0), 1);

                    Q0 = Q0.sort();
                    Domain.add(Q0);
                    W0 = W0.sort();
                    Image.add(W0);
                }
            } catch (NullPointerException e) {
            }
        }

        coeffs = FiberBundleMap.coding(Domain, Image);
        int count = 0;
        for (int j = 0; j < Domain.size(); j++) {
            System.out.println("The " + count + " th polyhedron in the domain: ");
            Domain.get(j).print();
            count = count + 1;
        }

        List<PolygonWrapper> slice_top_list = new ArrayList<PolygonWrapper>();
        List<PolygonWrapper> slice_bot_list = new ArrayList<PolygonWrapper>();
        double s0 = 7.0 / 10;
        double s1 = 9.0 / 10;

        for (int i = 0; i < Domain.size(); i++) {
            try {
                LongPolyhedron domain = Domain.get(i);
                PolygonWrapper T = TakeSlice(domain, s0 * 2);
                T.index = i;
                PolygonWrapper B = TakeSlice(domain, s1 * 2);
                B.index = i;
                if ((T != null) && (B != null)) {
                    slice_top_list.add(T);
                    slice_bot_list.add(B);

                    System.out.println();
                    System.out.println("index " + i);
                    double[][][] coeff = CanvasSymmetry.rationalize_polygon(T, B, s0, s1);
                    double[] trans = domain.trans;
                    System.out.println("translation vector: (" + trans[0] + ", " + trans[1]
                            + ", " + trans[2] + ", " + trans[3] + ")");

                }
            } catch (NullPointerException ne) {
            }

        }
        PolygonWrapper[] slice_top = BasicPolygon.copy_list(slice_top_list);
        PolygonWrapper[] slice_bot = BasicPolygon.copy_list(slice_bot_list);

    }

    public static void doSlice() {
        v1 = RotationAxis_Max.s;
        draw_domain = true;
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
        try {
            if (e.getButton() == MouseEvent.BUTTON1 & J.X.y > 240) {
                point = unTransform(J.X);
                point = BasicPolygon.untrans(point, ps[0], ps[1], ps[2] + 220);
                System.out.println();
                LongVector z = new LongVector(point.x, 1.0 * point.y / Math.sqrt(3), v1);
                int region = LongPolyReturnMap.region(z, Domain);
                System.out.println(region);
                LongPolyhedron d = Domain.get(region);
                d.print();
                Image.get(region).print();
                POINTER = d;
                draw_pointer = true;
            }
        } catch (NullPointerException ne) {

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

    public void paint_general(Graphics2D g, List<LongPolyhedron> PList, int n0,
            int n1, int n2, Color c) {
        for (int i = 0; i < PList.size(); i++) {
            try {
                PolygonWrapper p = TakeSlice(PList.get(i), v1);
                p = BasicPolygon.trans(p, n0, n1, n2);
                paint_frame(g, p, c, 1);
                fill_frame(g, p, new Color(255, 255, 255, 80));
                for (int j = 0; j < p.count; j++) {
                    g.setFont(new Font("TimesRoman", Font.BOLD, 10));
                    g.setColor(c);
                    Complex p0 = transform(p.center().x, p.center().y);
                    g.drawString(Integer.toString(i), (int) p0.x, (int) p0.y);
                }
            } catch (NullPointerException e) {
            }
        }
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

}
