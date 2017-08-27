package hexagonalpets;

import java.util.ArrayList;
import java.util.List;

public class ConjugationMap {

    public static PolygonWrapper center_to_origin(PolygonWrapper P, PolygonWrapper RT) {
        PolygonWrapper Q = new PolygonWrapper(P);
        Q = BasicPolygon.move(Q, new Complex(-RT.center().x, -RT.center().y));
        Q.vector = P.vector;
        return (Q);
    }

    public static PolygonWrapper scale(PolygonWrapper P, double c) {
        PolygonWrapper Q = new PolygonWrapper(P);
        for (int i = 0; i < Q.count; i++) {
            Q.z[i] = new Complex(c * Q.z[i].x, c * Q.z[i].y);
        }
        Q.vector = new Complex(P.vector.x * c, P.vector.y * c);
        return (Q);
    }

    public static PolygonWrapper affine(PolygonWrapper P, PolygonWrapper RT, double c) {
        PolygonWrapper Q = new PolygonWrapper(P);
        Q = center_to_origin(Q, RT);
        Q = BasicPolygon.rotation(Q, new Complex(0, 0), 2.0 / 3 * Math.PI);
        Q = BasicPolygon.flip_horizontal(Q, 0);
        Q = scale(Q, c);
        return (Q);
    }

    public static PolygonWrapper[] return_cell_list(double s0) {
        
        List<PolygonWrapper> ReturnSetList = new ArrayList<PolygonWrapper>();
        PolygonWrapper return_set = FirstReturn.returnSet_SS(s0);
        ReturnSetList.add(return_set);
       List<PolygonWrapper> temp= FirstReturn.returnCell_list(s0, ReturnSetList);
        PolygonWrapper[] cell = new PolygonWrapper[temp.size()];    
        for (int i = 0; i < cell.length; i++) {
            cell[i] = ConjugationMap.affine(temp.get(i), return_set, 1.0 / s0);
            
        }
        return (BasicPolygon.sort(cell));
    }
    
    public static List<PolygonWrapper> rotation(List<PolygonWrapper> PL, 
            Complex z, double c){
        List<PolygonWrapper> QL = new ArrayList<PolygonWrapper>();
        for(PolygonWrapper P : PL){
            PolygonWrapper Q = BasicPolygon.rotation(P, z, c);
            Q.vector = BasicPolygon.rotation(P.vector, c, z);
            QL.add(Q);
        }
        return(QL);
    }
    
    public static PolygonWrapper[] rotation(PolygonWrapper[] PL, 
            Complex z, double c){
        PolygonWrapper[] QL = new PolygonWrapper[PL.length];
        for(int i=0; i<PL.length; i++){
            QL[i] = BasicPolygon.rotation(PL[i], z, c);
            QL[i].vector = BasicPolygon.rotation(PL[i].vector, c, z);         
        }
        return(QL);
    }
    
    public static List<PolygonWrapper> move(List<PolygonWrapper> PL, Complex z){
        List<PolygonWrapper> QL = new ArrayList<PolygonWrapper>();
        for(PolygonWrapper P: PL){
            PolygonWrapper Q = BasicPolygon.move(P, z);
            Q.vector = P.vector;
            QL.add(Q);
        }
        return(QL);
    }

}
