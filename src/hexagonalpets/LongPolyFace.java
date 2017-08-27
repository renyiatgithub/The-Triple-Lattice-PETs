package hexagonalpets;


/**
 * The purpose of this routine is to extract the list of faces of an integer
 * polyhedron (LongPolyhedron). The idea is that we consider all subsets of the
 * vertex set, and accept those which satisfy 3 properties.
 *
 * -- the vertices are coplanar -- the vertices are on the boundary of P -- the
 * set in question is a maximal set satisfying the first 2 properties.
 *
 * Suppose P has N vertices. Then the subsets of P are encoded by the integers
 * a=0,1,...,(2^N-1). To extract the subset, we convert the integer a into
 * binary and then include those members which correspond to a 1 digit.
 */
public class LongPolyFace {

    /*This gets the raw list of faces.  The subsets are
     not cyclically ordered.  We don't really use this
     routine in practice because, once we use it once,
     we are done with it.*/
    public static int[] faceList(LongPolyhedron P) {            
        int N = P.count;
        int lim = (int) Math.pow(2, N);
        int[] A = new int[lim];
        int count = 0;
        for (int i = 0; i < lim; ++i) {
            LongVector[] V = getList(P, i);
            if (isCoplanar(V) == true) {
                if (isFace(P, V) == true) {                
                        A[count] = i;
                        ++count;
                    
                }
            }
        }
        
        int[] B = Combinatorics.copy(A, count);
        B = Combinatorics.trim(N, B); 
        
        if(P.count == 4){
            LongVector[] V = new LongVector[4];
            for(int j=0; j<4; j++){
                V[j] = new LongVector(P.V[j]);
            }
            if(isCoplanar(V)==false){
            return(new int[]{7,11,13,14});           
            }
        }
        return (B);
    }

    /*This gets a subset of the points of a polyhedron*/
    public static LongVector[] getList(LongPolyhedron P, int a) {
        int N = P.count;
        int[] t = Combinatorics.binaryList(N, a);
        LongVector[] V = new LongVector[t.length];
        for (int i = 0; i < t.length; ++i) {
            V[i] = P.V[t[i]];
        }
                                  
        return (V);
    }

    /**
     * This tests whether a list of vectors is coplanar
     */
    public static boolean isCoplanar(LongVector[] V) {
        if (V.length < 3) {
            return (false);
        }
        if (V.length == 3) {
            return (true);
        }               
        LongVector X = LongVector.normal(V);  
        double test0 = LongVector.dot(X, V[0]);
        for (int i = 1; i < V.length; i++) {
            double test = LongVector.dot(X, V[i]);
            if (Math.abs(test - test0) > Math.pow(10, -8)) {
                 return (false);
            }
        }
        return (true);
    }
    
    
    /**
     * tests whether the whole polyhedron lies in a
       plane
     */

    public static boolean isCoplanar(LongPolyhedron X) {
        LongVector[] W = new LongVector[X.count];
        for (int i = 0; i < X.count; ++i) {
            W[i] = new LongVector(X.V[i]);
        }
        return (isCoplanar(W));
    }

    /*This tests whether a list of vectors is contained 
     in a face of the polyhedron*/
    public static boolean isFace(LongPolyhedron P, LongVector[] V) {
        LongVector X = LongVector.normal(V);       
        double[] m = LongPolyCombinatorics.range(X, P);
        double min = m[0];
        double max = m[1];
        
        double target = LongVector.dot(X, V[0]);
        if (max <= target + Math.pow(10, -8)) {
            return (true);
        }
        if (min >= target - Math.pow(10, -8)) {
            return (true);        
        }
        
        
        return (false);
    }


}
