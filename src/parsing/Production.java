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
public class Production {

    /**
     * The left side (a variable).
     */
    char left;
    /**
     * The right side (a string of terminals and variables).
     */
    String right;

    /**
     * Creates a production <code>c -> s</code> with left side
     * <code>c</code> and right side <code>s</code>.
     */
    public Production(char c, String s) {
        left = c;
        right = s;
    }

    public String toString() {
        return left + "->" + right;
    }
}
