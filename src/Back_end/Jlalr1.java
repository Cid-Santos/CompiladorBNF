package Back_end;

import java.util.Scanner;

/**
 *
 * @author Cid
 */
public class Jlalr1 {
     public static final void main(String[] args) {
        Grammar grammar;
        try {
            grammar = Util.readGrammar(new Scanner(System.in));
            Util.writeGrammar(grammar);
        } catch(Error e) {
            System.err.println("Error reading grammar: "+e);
            System.exit(1);
            return;
        }
        Generator jlalr = new Generator(grammar);
        try {
            jlalr.computeFirstFollowNullable();
            jlalr.generateLALR1Table();
            jlalr.generateOutput();
        } catch(Error e) {
            System.err.println("Error performing LALR(1) construction: "+e);
            System.exit(1);
            return;
        } 
    }
}
