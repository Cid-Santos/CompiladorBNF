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
public class InfoDFA extends DFA {

    int[] info;
    int sink;
    int terminal;

    public InfoDFA(int n, Alphabet a) {
        super(n, a);
        info = new int[n];
        sink = -1;
    }
}
