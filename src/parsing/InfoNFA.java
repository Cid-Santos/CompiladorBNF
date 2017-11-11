/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Cid
 */
public class InfoNFA extends NFA {

    public int[] info;
    public int terminal;

    public InfoNFA(int n) {
        super(n);
        info = new int[nbStates];
    }

    /**
     * Creates an NFA with <code>n</code> states on the alphabet <code>a</code>.
     */
    public InfoNFA(int n, Alphabet a) {
        this(n);
        alphabet = a;
        nbLetters = alphabet.size();
    }

    /**
     * Implements the determinization algorithm.
     */
    public InfoDFA toInfoDFA() throws Exception {
        LinkedList t = new LinkedList();       /* table of sets of states */

        InfoDFA b = new InfoDFA(Nmax, alphabet);
        for (int i = 0; i < b.nbStates; i++) {
            b.info[i] = -1;
        }
        Set I = closure(initial);
        t.add(I);             /* add I to t */

        t = explore(t, 0, b);
        b.nbStates = t.size();
        b.nbLetters = nbLetters;
        b.alphabet = alphabet;
        b.initial = 0;
        for (Iterator i = t.iterator(); i.hasNext();) {
            Set p = (Set) i.next();
            int q = t.indexOf(p);
            if (p.isEmpty()) {
                b.sink = q;  //q is the sink state.
            }
            for (Iterator j = p.iterator(); j.hasNext();) {
                HalfEdge e = (HalfEdge) j.next();
                int n = info[e.end];
                if (n != -1) {  // q is a reduce state
                    if (b.info[q] != -1) {
                        throw new Exception("Grammar not SLR: R/R conflict");
                    } else {
                        b.info[q] = n;
                    }
                    if (e.end == terminal) {
                        b.terminal = q;
                    }
                }
            }
        }
        return b;
    }
}
