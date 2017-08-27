package hexagonalpets;

public class SymbolicCoding {

    static int[][] sixth_root = new int[6][2];

    public static int coding(Complex v) {
        sixth_root[0] = new int[]{2, 0};
        sixth_root[1] = new int[]{1, 1};
        sixth_root[2] = new int[]{-1, 1};
        sixth_root[3] = new int[]{-1, -1};
        sixth_root[4] = new int[]{1, -1};
        sixth_root[5] = new int[]{-2, 0};

        if (Math.abs(v.y) < Math.pow(10, -6)) {
            if (v.x > 0) {
                return 0;
            }
            if (v.x < 0) {
                return 5;
            }
        } else {
            if (v.y > 0 & v.x > 0) {
                return 1;
            }
            if (v.y > 0 & v.x < 0) {
                return 2;
            }
            if (v.y < 0 & v.x > 0) {
                return 4;
            }
            if (v.y < 0 & v.x < 0) {
                return 3;
            }
        }

        return -1;
    }
}
