package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasCell extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    static PolygonWrapper[] P;
    DrawMe draw = new DrawMe();
    static boolean draw_tiling, draw_map, draw_pt = false;
    static List<PolygonWrapper> image, domain, CELL;
    static List<Complex> TV;
    static Complex pt, pt_image;
    static int C = 95;
    static int[] ps;
    static int[] ps0;
    Point JX;

    public CanvasCell() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        P = new PolygonWrapper[2];
        image = new ArrayList<PolygonWrapper>();
        domain = new ArrayList<PolygonWrapper>();
        CELL = new ArrayList<PolygonWrapper>();
        TV = new ArrayList<Complex>();
        pt = new Complex(0, 0);
        pt_image = new Complex(0, 0);
        ps = new int[]{C, 200, 150};
        ps0 = new int[]{C, 200, 280};

    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);

        if (draw_map) {

            for (int i = 0; i < domain.size(); i++) {
                PolygonWrapper d = new PolygonWrapper(domain.get(i));
                Complex ds = new Complex(d.center());
                d = BasicPolygon.trans(d, ps[0], ps[1], ps[2]);
                paint_frame(g, d, Color.white, 2);
                fill_frame(g, d, new Color(255, 255, 255, 80));
                ds = BasicPolygon.trans(ds, ps[0], ps[1], ps[2]);
                ds = transform(ds.x, ds.y);
                g.setFont(new Font("TimesRoman", Font.BOLD, 11));
                g.setColor(Color.black);
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);

            }
            for (int i = 0; i < image.size(); i++) {
                PolygonWrapper d = new PolygonWrapper(image.get(i));
                Complex ds = new Complex(d.center());
                PolygonWrapper p0 = BasicPolygon.trans(d, ps[0], ps[1], ps[2]+200);
                paint_frame(g, p0, Color.white, 2);
                fill_frame(g, p0, new Color(255, 255, 255, 80));
                ds = BasicPolygon.trans(ds, ps[0], ps[1], ps[2]+200);
                ds = transform(ds.x, ds.y);
                g.setFont(new Font("TimesRoman", Font.BOLD, 11));
                g.setColor(Color.black);
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);
            }
        }
        if (draw_pt) {
            Complex p = BasicPolygon.trans(pt, ps[0], ps[1], ps[2]);
            p = transform(p.x, p.y);
            Complex q = BasicPolygon.trans(pt_image, ps[0], ps[1], ps[2] + 200);
            q = transform(q.x, q.y);
            draw.drawDots(g, p, Color.red, 6);
            draw.drawDots(g, q, Color.green, 6);
        }

    }

    public static void SetDomain(double s) {
        if (s > 0.5) {
            C = (int) (180 * s);
            ps = new int[]{C, 200, 100};
            ps0 = new int[]{C, 200, 250};
        }
        P[0] = CanvasLattice.F[0];
        P[1] = P[0];
        image = new ArrayList<PolygonWrapper>();
        Complex c = CanvasLattice.M.get(0).center();
        for (PolygonWrapper P : CanvasLattice.image) {
            P = BasicPolygon.move(P, new Complex(-c.x, -c.y));
            image.add(P);
        }
        domain = new ArrayList<PolygonWrapper>();
        for (PolygonWrapper P : CanvasLattice.domain) {
            P = BasicPolygon.move(P, new Complex(-c.x, -c.y));
            domain.add(P);
        }
        for (int i = 0; i < domain.size(); i++) {
            Complex c0 = domain.get(i).center();
            Complex c1 = image.get(i).center();
            Complex v = Complex.minus(c1, c0);
            TV.add(v);
        }
        draw_map = true;
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
        pt = unTransform(J.X);
        if (e.getButton() == MouseEvent.BUTTON1) {
            try {
                pt = BasicPolygon.untrans(pt, ps[0], ps[1], ps[2]);
                pt_image = Pet.map(pt, domain, TV);
                draw_pt = true;

            } catch (NullPointerException ee) {

            }
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

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        int test = 0;
        if (ch == 'f') {
            test = 1;
        }
        if (ch == 'd') {
            test = 2;
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
