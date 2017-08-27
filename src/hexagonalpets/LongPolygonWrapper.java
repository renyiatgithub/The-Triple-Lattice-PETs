package hexagonalpets;

import java.awt.geom.*;

public class LongPolygonWrapper extends PolygonWrapper {

    Complex[] z = new Complex[50000];
    int count;

    public LongPolygonWrapper() {
    }

    public LongPolygonWrapper(LongPolygonWrapper X) {
        this.count = X.count;
        for (int i = 0; i < count; ++i) {
            this.z[i] = new Complex(X.z[i]);
        }
    }

    public Complex center() {
        Complex w = new Complex();
        for (int i = 0; i < count; ++i) {
            w = Complex.plus(w, this.z[i]);
        }
        w.x = w.x / count;
        w.y = w.y / count;
        return (w);
    }

    public GeneralPath Path() {
        GeneralPath gp = new GeneralPath();
        gp.moveTo((float) (z[0].x), (float) (z[0].y));
        for (int i = 0; i < count; ++i) {
            gp.lineTo((float) (z[i].x), (float) (z[i].y));
        }
        return (gp);
    }
}
