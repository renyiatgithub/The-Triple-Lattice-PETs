package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class CanvasTiling extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    static PolygonWrapper D;
    DrawMe draw = new DrawMe();
    static boolean draw_tile, draw_map, draw_pt,
            draw_orbit, draw_returnSet, draw_largeSet, draw_returncell = false;
    static List<PolygonWrapper> image, domain, cell, orbit;
    static List<PolygonWrapper> ReturnSet, LargeSet, returncell;
    static List<Complex> TV;
    static List<List<Integer>> list_index, return_index;
    static Complex pt, pt_image;
    static int[] ps = {150, 200, 200};
    static String string_cfe = "0";
    Point JX;

    public CanvasTiling() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        D = new PolygonWrapper();
        image = new ArrayList<PolygonWrapper>();
        domain = new ArrayList<PolygonWrapper>();
        orbit = new ArrayList<PolygonWrapper>();
        TV = new ArrayList<Complex>();
        pt = new Complex(0, 0);
        pt_image = new Complex(0, 0);
        list_index = new ArrayList<List<Integer>>();
        ReturnSet = new ArrayList<PolygonWrapper>();
        LargeSet = new ArrayList<PolygonWrapper>();
        returncell = new ArrayList<PolygonWrapper>();
        return_index = new ArrayList<List<Integer>>();
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.drawString("'f': zoom in", 300, 350);
        g.drawString("'d': zoom out", 300, 370);
        g.drawString(string_cfe, 180, 20);

        if (draw_map) {
            PolygonWrapper Q0 = BasicPolygon.trans(D, ps[0], ps[1], ps[2]);
            paint_frame(g, Q0, Color.white, 2);
        }

        if (draw_tile) {
            for (PolygonWrapper cell : cell) {
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
            Complex p = BasicPolygon.trans(pt, ps[0], ps[1], ps[2]);
            Complex q = BasicPolygon.trans(pt_image, ps[0], ps[1], ps[2]);
            p = transform(p.x, p.y);
            q = transform(q.x, q.y);
            draw.drawDots(g, p, Color.red);
            draw.drawDots(g, q, Color.blue);
        }
        if (draw_orbit) {
            for (PolygonWrapper obt : orbit) {
                obt = BasicPolygon.trans(obt, ps[0], ps[1], ps[2]);
                paint_frame(g, obt, Color.red, 3);
                fill_frame(g, obt, new Color(255, 255, 204));
            }
        }
        if (draw_returnSet) {
            draw_orbit = false;
            for (PolygonWrapper rst : ReturnSet) {
                rst = BasicPolygon.trans(rst, ps[0], ps[1], ps[2]);
                fill_frame(g, rst, new Color(255, 255, 204, 90));
                paint_frame(g, rst, Color.blue, 2);

            }
        }
        if (draw_largeSet) {
            for (PolygonWrapper lst : LargeSet) {
                lst = BasicPolygon.trans(lst, ps[0], ps[1], ps[2]);
                fill_frame(g, lst, new Color(255, 255, 204, 90));
                paint_frame(g, lst, Color.blue, 2);
            }
        }

        if (draw_returncell) {
            for (PolygonWrapper lst : returncell) {
                lst = BasicPolygon.trans(lst, ps[0], ps[1], ps[2]);
                paint_frame(g, lst, Color.blue, 2);
            }
        }
    }

    public static void Tiling(double s) {
        cell = new ArrayList<PolygonWrapper>();
        D = CanvasLattice.F[0];
        draw_map = true;
        domain = CanvasCell.domain;
        TV = CanvasCell.TV;
        draw_tile = true;
        for (int i = 0; i < 200; i++) {
            Complex z = new Complex(-1 + Math.random() * 2, s * Math.sqrt(3) * (Math.random() - 0.5));
            if (PolygonWrapper.contains(D, z) && GetTiling.list_contain_pt(cell, z)==false) {                
                List<Integer> index = GetTiling.index_seq(z);
                if (list_index.contains(index) == false) {                                     
                    cell.addAll(GetTiling.orbit(z));
                }
            }
        }
        ReturnSet.add(FirstReturn.returnSet_SS(s));
        LargeSet.add(FirstReturn.largeSet_SS(s));
        string_cfe = CanvasControl.string_cfe;
    }

    public static List<PolygonWrapper> ReturnCell(double s, List<PolygonWrapper> Plist) {
        List<PolygonWrapper> returncell = new ArrayList<PolygonWrapper>();
        domain = CanvasCell.domain;
        TV = CanvasCell.TV;
        draw_map = true;
        for (PolygonWrapper D : cell) {
            Complex center = D.center();
            for (int i = 0; i < Plist.size(); i++) {
                if (PolygonWrapper.contains(Plist.get(i), center)) {
                    List<Integer> return_seq = FirstReturn.return_seq(center,
                            Plist.get(i), domain, TV);
                    if (return_index.contains(return_seq) == false) {
                        PolygonWrapper R = FirstReturn.returnCell(center,
                                Plist.get(i), domain, TV);
                        return_index.add(return_seq);
                        returncell.add(R);
                    }
                }
            }
        }
        System.out.println("tiling size: " + CanvasTiling.cell.size());
        draw_returncell = true;
        return (returncell);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
        pt = unTransform(J.X);
        if (e.getButton() == MouseEvent.BUTTON1) {
            pt = BasicPolygon.untrans(pt, ps[0], ps[1], ps[2]);
            pt_image = Pet.map(pt, domain, TV);
            if (PolygonWrapper.contains(D, pt)) {
                orbit = GetTiling.orbit(pt);
                List<Integer> index = GetTiling.index_seq(pt);
                if (list_index.contains(index) == false) {
                    cell.addAll(GetTiling.orbit(pt));
                }
                if (draw_largeSet == true && draw_returnSet == false) {
                    int n = Pet.region(pt, LargeSet);
                    if (n != -1) {
                        pt_image = FirstReturn.firstReturn(pt, LargeSet.get(n),
                                domain, TV);
                    }
                }
                if (draw_returnSet == true && draw_largeSet == false) {
                    int n = Pet.region(pt, ReturnSet);
                    if (n != -1) {
                        pt_image = FirstReturn.firstReturn(pt, ReturnSet.get(n),
                                domain, TV);
                    }
                }
                draw_pt = true;
                System.out.println("index size: " + index.size());
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
        if (mode == 3) {
            draw_returnSet = !draw_returnSet;
            draw_orbit = false;
        }
        if (mode == 4) {
            draw_largeSet = !draw_largeSet;
            draw_orbit = false;
        }
        if (mode == 5) {
            returncell.clear();
            return_index.clear();
            returncell.addAll(ReturnCell(Main.value, LargeSet));
            draw_returnSet = false;
            draw_largeSet = true;
        }
        if (mode == 6) {
            returncell.clear();
            return_index.clear();
            returncell.addAll(ReturnCell(Main.value, ReturnSet));
            draw_largeSet = false;
            draw_returnSet = true;
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
        if(ch == 'r'){
            test = 5;
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
