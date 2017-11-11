/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

/**
 *
 * @author Cid
 */
public class RandomFMinimizer {

    static int N = 5000;

    public static IDFA minimize(IDFA a) {
        IDFA b = a;
        int n;
        do {
            n = b.nbStates;
            b.orderEdges();
            int[] x = fusion(b);
            b.ecoQuotient(x);
            System.out.println(b.nbStates);
        } while (b.nbStates < n);
        return b;
    }

    /**
     * Returns the smallest partition pi of states such that pi < t
     */

    public static int[] fusion(IDFA a) {
        int[] d = new int[a.nbStates];
        int m = 0;
        int hitCount = 0;
        for (int q = 0; q < a.nbStates; q++) {
            if (q % 1000 == 0) {
                System.out.println("q = " + q);
            }
	    //boolean found = false; 
            //for(int loopCount = 0; loopCount < q && hitCount < N; loopCount++){
            int p = (int) (Math.random() * q);
            if (equiv(a, p, q)) {
                d[q] = d[p]; //found = true; hitCount++;
                //break;
            } //}
            //if (! found) 
            else {
                d[q] = m++;
            }
        }
        System.out.println("endFusion");
        return d;
    }

    public static boolean equiv(IDFA a, int p, int q) {
        if (a.terminal[p] != a.terminal[q]) {
            return false;
        }
        PairIntList l = a.edges[p].front;
        PairIntList m = a.edges[q].front;
        for (; l != null && m != null; l = l.next, m = m.next) {
            if (m.val != l.val || l.elem != m.elem) {
                return false;
            }
        }
        if (l != null || m != null) {
            return false;
        }
        return true;
    }

}
