package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class CanvasScissor extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    static PolygonWrapper D, P;
    DrawMe draw = new DrawMe();
    static boolean draw_tile, draw_map, draw_pt, draw_domain, draw_image,
            draw_orbit, draw_returnSet, draw_largeSet, draw_return_cells,
            draw_image_return_cells, draw_tile_black, draw_return_square,
            draw_symmetry, draw_ptlist, draw_inv_max_domain, draw_reflection = false;
    static List<PolygonWrapper> domain, image, tiles, orbit;
    static List<PolygonWrapper> ReturnSet, LargeSet, return_cells,
            image_return_cells, return_square, symmetry_sets, reflection_sets;
    static List<Complex> TV;
    static List<List<Integer>> list_index, return_index;
    static Complex pt, pt_image, pt_closest = new Complex(0, 0);
    static List<Complex> ptlist;
    static int[] ps = {150, 250, 200};
    static String string_cfe = "0";
    static double side, value = 0;
    static PolygonWrapper inv_max_domain;
    CanvasGraph CG;
    Point JX;

    public CanvasScissor() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        D = new PolygonWrapper();
        P = new PolygonWrapper();
        domain = new ArrayList<PolygonWrapper>();
        image = new ArrayList<PolygonWrapper>();
        tiles = new ArrayList<PolygonWrapper>();
        orbit = new ArrayList<PolygonWrapper>();
        TV = new ArrayList<Complex>();
        list_index = new ArrayList<List<Integer>>();
        ReturnSet = new ArrayList<PolygonWrapper>();
        LargeSet = new ArrayList<PolygonWrapper>();
        return_cells = new ArrayList<PolygonWrapper>();
        image_return_cells = new ArrayList<PolygonWrapper>();
        return_index = new ArrayList<List<Integer>>();
        return_square = new ArrayList<PolygonWrapper>();
        symmetry_sets = new ArrayList<PolygonWrapper>();
        reflection_sets = new ArrayList<PolygonWrapper>();
        ptlist = new ArrayList<Complex>();
        inv_max_domain = new PolygonWrapper();

        CG = Main.G;
    }

    public void setCanvasGraph(CanvasGraph C) {
        this.CG = C;
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.lightGray);
        g.drawString("'f/d': zoom in/out", 20, 360);
        g.drawString("'b': limit set", 150, 360);
        g.drawString("'w': show orbit", 270, 360);
        g.drawString("'a,s,t': return set", 20, 380);
        g.drawString("'g,h': show cells", 150, 380);
        g.drawString("'r': show return cells", 270, 380);
        g.drawString("'m': symm. sets", 20, 400);
        g.drawString("'n': refl. sets", 150, 400);
        g.drawString("'e': clear pts", 270, 400);
        g.drawString(string_cfe, 20, 20);

        if (draw_map) {
            PolygonWrapper Q0 = BasicPolygon.trans(D, ps[0], ps[1], ps[2]);
            paint_frame(g, Q0, Color.white, 3);
        }

        if (draw_tile) {
            for (PolygonWrapper cell : tiles) {
                int c = 3 * cell.colormode + 1;
                Color col = new Color(150 + Math.abs(255 - 41 * c) % 104,
                        170 + Math.abs(255 - 31 * c) % 84,
                        201 + Math.abs(255 - 21 * c) % 44);
                PolygonWrapper tcell = BasicPolygon.trans(cell, ps[0], ps[1], ps[2]);
                paint_frame(g, tcell, Color.black, 1);
                fill_frame(g, tcell, col);
            }
        }

        if (draw_orbit) {
            for (PolygonWrapper obt : orbit) {
                PolygonWrapper tcell = BasicPolygon.trans(obt, ps[0], ps[1], ps[2]);
                paint_frame(g, tcell, Color.red, 1);
                fill_frame(g, tcell, Color.black);
            }
        }

        if (draw_tile_black) {
            PolygonWrapper Q0 = BasicPolygon.trans(D, ps[0], ps[1], ps[2]);
            paint_frame(g, Q0, new Color(70, 130, 180), 2);
            fill_frame(g, Q0, new Color(70, 130, 180));
            for (PolygonWrapper cell : tiles) {
                PolygonWrapper tcell = BasicPolygon.trans(cell, ps[0], ps[1], ps[2]);
                paint_frame(g, tcell, Color.white, 1);
                fill_frame(g, tcell, Color.white);
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

        if (draw_ptlist) {
            for (int i = 0; i < (int) (0.5 * ptlist.size()); i++) {
                Complex p = BasicPolygon.trans(ptlist.get(2 * i), ps[0], ps[1], ps[2]);
                Complex q = BasicPolygon.trans(ptlist.get(2 * i + 1), ps[0], ps[1], ps[2]);
                p = transform(p.x, p.y);
                q = transform(q.x, q.y);
                draw.drawDots(g, p, Color.green, Color.red);
                draw.drawDots(g, q, Color.green, Color.blue);
            }
        }

        if (draw_returnSet) {
            draw_orbit = false;
            for (PolygonWrapper rst : ReturnSet) {
                PolygonWrapper trst = BasicPolygon.trans(rst, ps[0], ps[1], ps[2]);
                fill_frame(g, trst, new Color(0, 0, 0, 80));
                paint_frame(g, trst, Color.blue, 2);

            }
        }
        if (draw_largeSet) {
            for (PolygonWrapper lst : LargeSet) {
                PolygonWrapper tlst = BasicPolygon.trans(lst, ps[0], ps[1], ps[2]);
                fill_frame(g, tlst, new Color(255, 255, 204, 90));
                paint_frame(g, tlst, Color.black, 2);
            }
        }

        if (draw_return_cells) {
            for (PolygonWrapper lst : return_cells) {
                try {
                    PolygonWrapper tlst = BasicPolygon.trans(lst, ps[0], ps[1], ps[2]);
                    fill_frame(g, tlst, new Color(255, 255, 204, 90));
                    paint_frame(g, tlst, Color.blue, 2);
                } catch (NullPointerException ne) {

                }
            }
        }

        if (draw_return_square) {
            for (PolygonWrapper lst : return_square) {
                try {
                    PolygonWrapper tlst = BasicPolygon.trans(lst, ps[0], ps[1], ps[2]);
                    //                   fill_frame(g, tlst, new Color(255, 255, 204, 90));
                    paint_frame(g, tlst, Color.red, 2);
                } catch (NullPointerException ne) {

                }
            }
        }

        if (draw_image_return_cells) {
            for (PolygonWrapper lst : image_return_cells) {
                try {
                    PolygonWrapper tlst = BasicPolygon.trans(lst, ps[0], ps[1], ps[2]);
                    fill_frame(g, tlst, new Color(255, 255, 204, 90));
                    paint_frame(g, tlst, Color.red, 2);
                } catch (NullPointerException ne) {

                }
            }
        }

        if (draw_symmetry) {
            for (PolygonWrapper rst : symmetry_sets) {
                PolygonWrapper trst = BasicPolygon.trans(rst, ps[0], ps[1], ps[2]);
                fill_frame(g, trst, new Color(255, 255, 204, 90));
                paint_frame(g, trst, Color.blue, 2);
            }
        }

        if (draw_reflection) {
            for (PolygonWrapper ref : reflection_sets) {
                PolygonWrapper M = BasicPolygon.trans(ref, ps[0], ps[1], ps[2]);
                fill_frame(g, M, new Color(0, 0, 0, 90));
                paint_frame(g, M, Color.black, 2);
            }
        }

        if (draw_domain) {
            BasicPolygon.rational_print(D);
            PolygonWrapper Q0 = BasicPolygon.trans(D, ps[0], ps[1], ps[2]);
            //           paint_frame(g, Q0, Color.white, 2);

            PolygonWrapper[] D = BasicPolygon.copy_list(domain);

            for (int i = 0; i < D.length; i++) {
                PolygonWrapper d = new PolygonWrapper(D[i]);
//                System.out.println();
//                System.out.println("index " + i);
                Complex[] ordered_verticies = BasicPolygon.order_vertex(d);
//                for (int j = 0; j < ordered_verticies.length; j++) {
//                    ordered_verticies[j].print_rational();
//                }

                //               d.vector.print_rational();
                Complex ds = new Complex(d.center().x, d.center().y);
                Complex dv = new Complex(ds);
                if (d.vector.norm() > Math.pow(10, -10)) {
                    dv = Complex.plus(ds, Complex.unit(d.vector).scale(0.1));
                }

                d = BasicPolygon.trans(d, ps[0],
                        ps[1], ps[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.red, 2);

                ds = BasicPolygon.trans(ds, ps[0], ps[1], ps[2]);
                ds = transform(ds.x, ds.y);
                dv = BasicPolygon.trans(dv, ps[0], ps[1], ps[2]);
                dv = transform(dv.x, dv.y);
                g.setFont(new Font("TimesRoman", Font.BOLD, 11));
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);

            }
        }

        if (draw_image) {

            PolygonWrapper[] M = BasicPolygon.copy_list(Pet.map_list(domain));
            for (int i = 0; i < M.length; i++) {
                PolygonWrapper d = new PolygonWrapper(M[i]);
                System.out.println();
                System.out.println("index " + i);

                Complex ds = new Complex(d.center().x, d.center().y);
                Complex dv = new Complex(ds);
                if (d.vector.norm() > Math.pow(10, -10)) {
                    dv = Complex.plus(ds, Complex.unit(d.vector).scale(0.1));
                }

                d = BasicPolygon.trans(d, ps[0],
                        ps[1], ps[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.blue, 2);

                ds = BasicPolygon.trans(ds, ps[0], ps[1], ps[2]);
                ds = transform(ds.x, ds.y);
                dv = BasicPolygon.trans(dv, ps[0], ps[1], ps[2]);
                dv = transform(dv.x, dv.y);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 11));
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);

            }

        }

    }

    public static void Tiling(double s) {
        value = s;
        ps[1] = (int) (ps[1] - s * 150);
        D = CanvasLattice.F[0];
        string_cfe = CanvasControl.string_cfe;
        int a = (int) Math.floor(1.0 / s);
        side = 2 * (1 - a * s);

        int[] rat_side = MathRational.approximate(side, Math.pow(10, -10));
        System.out.println("side: " + rat_side[0] + '/' + rat_side[1]);

        P.count = 4;
        P.z[0] = new Complex(D.z[0]);
        P.z[1] = new Complex(D.z[0].x + side, P.z[0].y);
        P.z[2] = new Complex(D.z[3].x + side, D.z[3].y);
        P.z[3] = new Complex(D.z[3]);

        for (int i = 0; i < CanvasCell.domain.size(); i++) {
            PolygonWrapper d = CanvasCell.domain.get(i);
            Complex d_center = d.center();
            PolygonWrapper m = CanvasCell.image.get(i);
            Complex m_center = m.center();

            if (PolygonWrapper.contains(P, d_center)) {
                d = BasicPolygon.move(d, new Complex(2, 0));
                d_center = d.center();
            }
            if (PolygonWrapper.contains(P, m_center)) {
                m = BasicPolygon.move(m, new Complex(2, 0));
                m_center = m.center();
            }
            PolygonWrapper dd = BasicPolygon.move(d, new Complex(-side, 0));
            dd.vector = Complex.minus(m_center, d_center);
            domain.add(dd);
            PolygonWrapper mm = BasicPolygon.move(m, new Complex(-side, 0));
            image.add(mm);
            TV.add(Complex.minus(m_center, d_center));
        }

//        List<PolygonWrapper> domain_order = new ArrayList<PolygonWrapper>(domain);
//        domain.clear();
//        try {
//            domain.add(domain_order.get(2));
//            domain.add(domain_order.get(8));
//            domain.add(domain_order.get(1));
//            domain.add(domain_order.get(6));
//            domain.add(domain_order.get(3));
//            domain.add(domain_order.get(4));
//            domain.add(domain_order.get(11));
//            domain.add(domain_order.get(0));
//            domain.add(domain_order.get(5));
//            domain.add(domain_order.get(10));
//            domain.add(domain_order.get(9));
//            domain.add(domain_order.get(7));
//        } catch (java.lang.IndexOutOfBoundsException e) {
//
//        }

//        List<Complex> TV_order = new ArrayList<Complex>(TV);
//        TV.clear();
//        try {
//            TV.add(TV_order.get(2));
//            TV.add(TV_order.get(8));
//            TV.add(TV_order.get(1));
//            TV.add(TV_order.get(6));
//            TV.add(TV_order.get(3));
//            TV.add(TV_order.get(4));
//            TV.add(TV_order.get(11));
//            TV.add(TV_order.get(0));
//            TV.add(TV_order.get(5));
//            TV.add(TV_order.get(10));
//            TV.add(TV_order.get(9));
//            TV.add(TV_order.get(7));
//        } catch (java.lang.IndexOutOfBoundsException e) {
//
//        }

        for (PolygonWrapper tile : CanvasTiling.cell) {
            Complex center = tile.center();
            if (PolygonWrapper.contains(P, center)) {
                PolygonWrapper tile0 = BasicPolygon.move(tile, new Complex(2, 0));
                tile0.colormode = tile.colormode;
                tiles.add(BasicPolygon.move(tile0, new Complex(-side, 0)));
            } else {
                tiles.add(BasicPolygon.move(tile, new Complex(-side, 0)));
            }
        }
        LargeSet.add(FirstReturn.largeSet_SS(s));
        ReturnSet.add(FirstReturn.returnSet_SS(s));
        draw_tile = true;
        draw_map = true;
//        return_cells = ReturnCell(s, ReturnSet);
        return_cells = FirstReturn.returnCell_list(value, ReturnSet);
        return_square.add(FirstReturn.returnSet_square(s));
        image_return_cells = Pet.map_list(return_cells);
        symmetry_sets = FirstReturn.SymmetrySet(s);
        reflection_sets = FirstReturn.ReflectionSet(s);

    }

    public static List<PolygonWrapper> ReturnCell(double s, List<PolygonWrapper> Plist) {
        List<PolygonWrapper> returncell = new ArrayList<PolygonWrapper>();
        draw_map = true;
        for (PolygonWrapper D : tiles) {
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
        draw_return_cells = true;
        return (returncell);
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
        try {
            orbit.clear();
            pt = unTransform(J.X);
            if (e.getButton() == MouseEvent.BUTTON1) {
                pt = BasicPolygon.untrans(pt, ps[0], ps[1], ps[2]);
                pt_image = Pet.map(pt, domain, TV);

                if (PolygonWrapper.contains(D, pt)) {
                    List<Integer> index = GetTiling.index_seq(pt, domain, TV);
                    if (GetTiling.list_contain_pt(tiles, pt) == false) {
                        tiles.addAll(GetTiling.orbit(pt, domain, TV));
                    }
                    orbit.addAll(GetTiling.orbit(pt, domain, TV));

                    if (draw_largeSet == true && draw_returnSet == false) {
                        int n = Pet.region(pt, LargeSet);
                        if (n != -1) {
                            pt_image = FirstReturn.firstReturn(pt, LargeSet.get(n),
                                    domain, TV);
                            List<Integer> seq = FirstReturn.return_seq(pt, LargeSet.get(n), domain, TV);
                            GetTiling.printlist(seq);
                            System.out.println();
                            System.out.println("return sequence: ");
                            for (int i = 0; i < seq.size(); i++) {
                                System.out.println(seq.get(i) + " ");
                            }
                        } else {
                            System.out.println("not in the return set");
                        }
                    }

                    if (draw_returnSet == true && draw_largeSet == false) {
                        int n = Pet.region(pt, ReturnSet);
                        if (n != -1) {
                            pt_image = FirstReturn.firstReturn(pt, ReturnSet.get(n),
                                    domain, TV);
                            List<Integer> seq = FirstReturn.return_seq(pt, ReturnSet.get(n), domain, TV);
                            System.out.println("index size: " + index.size());
                            GetTiling.printlist(seq);
                            CanvasGraph.graph_units(pt, value);
                            CanvasGraph.graph_return(pt, value);
                        }
                    }

                    if (draw_largeSet == false && draw_returnSet == false
                            && draw_reflection == false && draw_symmetry == true) {
                        int n = Pet.region(pt, symmetry_sets);
                        List<PolygonWrapper> image = Pet.map_list(domain);
                        if (n == 0) {
                            pt_image = FirstReturn.firstReturn(pt, symmetry_sets.get(n),
                                    domain, TV);
                            List<Integer> seq = FirstReturn.return_seq(pt,
                                    symmetry_sets.get(n), domain, TV);
                            GetTiling.printlist(seq);
                            ptlist.add(pt);
                            ptlist.add(pt_image);
                            draw_ptlist = true;
                        }
                        if (n == 1 || n == 2) {
                            pt_image = FirstReturn.inverse_first_return(pt,
                                    symmetry_sets.get(n), domain, TV);
                            inv_max_domain = FirstReturn.inv_returnCell(pt,
                                    symmetry_sets.get(n), domain, TV);
                            ptlist.add(pt);
                            ptlist.add(pt_image);
                            draw_ptlist = true;
                            draw_inv_max_domain = true;

                        } else {
                            System.out.println("not in the symmetry triangle");
                        }
                    }

                    if (draw_largeSet == false && draw_returnSet == false
                            && draw_reflection == true && draw_symmetry == false) {
                        int n = Pet.region(pt, reflection_sets);
                        List<PolygonWrapper> image = Pet.map_list(domain);
                        if (n == 0) {
                            pt_image = FirstReturn.firstReturn(pt, reflection_sets.get(n),
                                    domain, TV);
                            List<Integer> seq = FirstReturn.return_seq(pt,
                                    reflection_sets.get(n), domain, TV);
                            GetTiling.printlist(seq);
                            ptlist.add(pt);
                            ptlist.add(pt_image);
                            draw_ptlist = true;
                        }
                        if (n == 1) {
                            pt_image = FirstReturn.inverse_first_return(pt,
                                    reflection_sets.get(n), domain, TV);
                            inv_max_domain = FirstReturn.inv_returnCell(pt,
                                    reflection_sets.get(n), domain, TV);
                            ptlist.add(pt);
                            ptlist.add(pt_image);
                            draw_ptlist = true;
                            draw_inv_max_domain = true;

                        } else {
                            System.out.println("not in the symmetry triangle");
                        }
                    }

                    draw_pt = true;
                    if (CG.mode == 1) {
                        CanvasGraph.graph_orbit(pt);
                    }
                    if (CG.mode == 2) {
                        CanvasGraph.graph_units(pt, value);
                    }
                    CG.draw_pt = true;
                    CG.repaint();

                    PolygonWrapper Rt = ReturnSet.get(0);
                }
            }
        } catch (NullPointerException out) {
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
            draw_largeSet = false;
            draw_orbit = false;
        }
        if (mode == 4) {
            draw_largeSet = !draw_largeSet;
            draw_returnSet = false;
            draw_orbit = false;
        }
        if (mode == 5) {
            draw_domain = !draw_domain;
        }
        if (mode == 6) {
            draw_return_cells = !draw_return_cells;
        }
        if (mode == 7) {
            draw_image_return_cells = !draw_image_return_cells;
        }
        if (mode == 8) {
            draw_tile_black = !draw_tile_black;
            draw_tile = !draw_tile;
        }
        if (mode == 9) {
            draw_orbit = !draw_orbit;
        }
        if (mode == 10) {
            draw_return_square = !draw_return_square;
        }
        if (mode == 11) {
            draw_symmetry = !draw_symmetry;
            if (draw_symmetry == false || draw_reflection == false) {
                ptlist.clear();
            }
        }
        if (mode == 12) {
            ptlist.clear();
        }
        if (mode == 13) {
            draw_image = !draw_image;
        }
        if (mode == 14) {
            draw_reflection = !draw_reflection;
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
        if (ch == 'g') {
            test = 5;
        }
        if (ch == 'r') {
            test = 6;
        }
        if (ch == 't') {
            test = 7;
        }
        if (ch == 'b') {
            test = 8;
        }
        if (ch == 'w') {
            test = 9;
        }
        if (ch == 't') {
            test = 10;
        }
        if (ch == 'm') {
            test = 11;
        }
        if (ch == 'e') {
            test = 12;
        }
        if (ch == 'h') {
            test = 13;
        }
        if (ch == 'n') {
            test = 14;
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
