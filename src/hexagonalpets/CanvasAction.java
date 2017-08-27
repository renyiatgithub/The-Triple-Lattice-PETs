package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class CanvasAction extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    static PolygonWrapper P, L;
    DrawMe draw = new DrawMe();
    static boolean draw_tile, draw_map, draw_pt,
            draw_orbit, draw_returnSet, draw_largeSet = false;
    static List<PolygonWrapper> domain, image, tiles;
    static List<PolygonWrapper> ReturnSet, LargeSet, LS_Tiling, returncell;
    static List<Complex> TV;
    static List<List<Integer>> list_index, return_index;
    static List<Complex> pt, pt_image;
    static int[] ps = {100, 200, 200};
    static String string_cfe = "0";
    static Complex p_mouse, q_mouse, pt_closest;
    Point JX;
    static int a = 0;
    static double side, s = 0;

    public CanvasAction() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        L = new PolygonWrapper();
        domain = new ArrayList<PolygonWrapper>();
        image = new ArrayList<PolygonWrapper>();
        tiles = new ArrayList<PolygonWrapper>();
        TV = new ArrayList<Complex>();
        list_index = new ArrayList<List<Integer>>();
        ReturnSet = new ArrayList<PolygonWrapper>();
        LargeSet = new ArrayList<PolygonWrapper>();
        LS_Tiling = new ArrayList<PolygonWrapper>();
        returncell = new ArrayList<PolygonWrapper>();
        return_index = new ArrayList<List<Integer>>();
        pt = new ArrayList<Complex>();
        pt_image = new ArrayList<Complex>();
        pt_closest = new Complex();
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.drawString("'f/d': zoom in/out", 280, 340);
        g.drawString("'a,s': return set", 280, 360);
        g.drawString(string_cfe, 180, 20);

        if (draw_tile) {
            for (PolygonWrapper cell : tiles) {
                int c = 3 * cell.colormode + 1;
                Color col = new Color(150 + Math.abs(255 - 41 * c) % 104,
                        170 + Math.abs(255 - 31 * c) % 84,
                        201 + Math.abs(255 - 21 * c) % 44);
                cell = BasicPolygon.trans(cell, ps[0], ps[1], ps[2]);
                paint_frame(g, cell, Color.black, 1);
                fill_frame(g, cell, col);
            }
        }
        if (draw_pt) {
            for (Complex p : pt) {
                Complex p0 = BasicPolygon.trans(p, ps[0], ps[1], ps[2]);
                p0 = transform(p0.x, p0.y);
                draw.drawDots(g, p0, Color.red);
            }

            for (Complex q : pt_image) {
                q = BasicPolygon.trans(q, ps[0], ps[1], ps[2]);
                q = transform(q.x, q.y);
                draw.drawDots(g, q, Color.blue);
            }

            Complex pc = BasicPolygon.trans(pt_closest, ps[0], ps[1], ps[2]);
            pc = transform(pc.x, pc.y);
            draw.drawDots(g, pc, Color.green);
        }

        if (draw_returnSet) {
            draw_orbit = false;
            for (PolygonWrapper rst : ReturnSet) {
                rst = BasicPolygon.trans(rst, ps[0], ps[1], ps[2]);
                fill_frame(g, rst, new Color(255, 255, 204, 90));
                paint_frame(g, rst, Color.red, 2);

            }
        }
        if (draw_largeSet) {
            for (PolygonWrapper lst : LS_Tiling) {
                lst = BasicPolygon.trans(lst, ps[0], ps[1], ps[2]);
                paint_frame(g, lst, Color.gray, 2);
            }
            for (PolygonWrapper lst : LargeSet) {
                lst = BasicPolygon.trans(lst, ps[0], ps[1], ps[2]);
                fill_frame(g, lst, new Color(255, 255, 255, 80));
                paint_frame(g, lst, Color.orange, 2);
            }

        }
    }

    public static void Tiling(double s0) {
        s = s0;
        domain = CanvasScissor.domain;
        TV = CanvasScissor.TV;
        ps[1] = (int) (ps[1] - s * 150);
        a = (int) Math.floor(1.0 / s);
        side = 2 * (1 - a * s);
        L = FirstReturn.largeSet_SS(s);
        for (PolygonWrapper P : CanvasScissor.tiles) {
            Complex center = P.center();
            if (PolygonWrapper.contains(L, center)) {
                for (int i = -2; i < 3; i++) {
                    PolygonWrapper Q = BasicPolygon.move(P, new Complex(i * (2 * s + side), 0));
                    for (int j = -2; j < 3; j++) {
                        PolygonWrapper R = BasicPolygon.move(Q, new Complex(j * s, j * s * Math.sqrt(3)));
                        R.colormode = P.colormode;
                        tiles.add(R);
                    }
                }
            }
        }

        for (int i = -2; i < 3; i++) {
            PolygonWrapper LT = BasicPolygon.move(L, new Complex(i * (2 * s + side), 0));
            for (int j = -2; j < 3; j++) {
                PolygonWrapper RT = BasicPolygon.move(LT, new Complex(j * s, j * s * Math.sqrt(3)));
                LS_Tiling.add(RT);
            }
        }

        LargeSet.add(L);
        draw_largeSet = true;
        draw_tile = true;
    }

    public void mouseClicked(MouseEvent e) {
        pt.clear();
        pt_image.clear();
        MouseData J = MouseData.process(e);
        JX = J.X;
        try {
            p_mouse = unTransform(J.X);
            if (e.getButton() == MouseEvent.BUTTON1) {
                Complex p_mouse_trans = BasicPolygon.untrans(p_mouse, ps[0], ps[1], ps[2]);               
                p_mouse = conjugation(p_mouse_trans, L, s, side);
                
                Complex trans = Complex.minus(p_mouse, p_mouse_trans);              
                if (PolygonWrapper.contains(L, p_mouse)) {
                    q_mouse = Pet.map(p_mouse, domain, TV);
                    if (draw_largeSet == true && draw_returnSet == false) {
                        int n = Pet.region(p_mouse, LargeSet);
                        if (n != -1) {
                            q_mouse = FirstReturn.firstReturn(p_mouse, LargeSet.get(n),
                                    domain, TV);
                        }
                    }
                    if (draw_returnSet == true && draw_largeSet == false) {
                        int n = Pet.region(p_mouse, ReturnSet);
                        if (n != -1) {
                            q_mouse = FirstReturn.firstReturn(p_mouse, ReturnSet.get(n),
                                    domain, TV);
                        }
                    }
                    for (int i = -2; i < 3; i++) {
                        Complex r = Complex.plus(p_mouse, new Complex(i
                                * (2 * s + side), 0));
                        Complex rm = Complex.plus(q_mouse, new Complex(i
                                * (2 * s + side), 0));
                        pt.add(r);
                        pt_image.add(rm);

                        for (int j = -2; j <3; j++) {
                            Complex r0 = Complex.plus(r, new Complex(j * s, j * s * Math.sqrt(3)));
                            Complex rm0 = Complex.plus(rm, new Complex(j * s, j * s * Math.sqrt(3)));
                            pt.add(r0);
                            pt_image.add(rm0);
                        }
                    }
                    pt_closest = CanvasGraph.proj(p_mouse, pt_image, s);
                    pt_closest = Complex.minus(pt_closest, trans);
                    double dist = Complex.dist(pt_closest, p_mouse_trans);
                    int[] app = MathRational.approximate(dist, Math.pow(10, -12));
                    System.out.println("translation distance: " + dist + ", " + app[0]+"/" + app[1]);
                    int[] app_side = MathRational.approximate(side, Math.pow(10, -12));
                    System.out.println("side: " + side + ", " + app_side[0] + "/" + app_side[1]);
                    draw_pt = true;
                }
            }
        } catch (NullPointerException out) {
        }

        repaint();
    }

    public static Complex conjugation(Complex z, PolygonWrapper P, double s,
            double side) {
            for (int i = -2; i < 3; i++) {
                Complex w = Complex.plus(z, new Complex(-i * (2 * s + side), 0));
                if (PolygonWrapper.contains(P, w)) {
                    return (w);
                }
                for (int j = -2; j < 3; j++) {
                    Complex r = Complex.plus(w, new Complex(-j * s, -j * s * Math.sqrt(3)));
                    if (PolygonWrapper.contains(P, r)) {
                        return (r);
                    }
                }
            }        
        return (new Complex(z));
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
        if (mode == 3) {
            draw_returnSet = !draw_returnSet;
            draw_orbit = false;
        }
        if (mode == 4) {
            draw_largeSet = !draw_largeSet;
            draw_orbit = false;
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
        if (ch == 's') {
            test = 3;
        }
        if (ch == 'a') {
            test = 4;
        }
        if (test > 0) {
            doMouseClick(test);
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
