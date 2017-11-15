package Back_end;

import java.util.Scanner;


/**
 *
 * @author Cid
 */
public class Jslr1 {
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
        Generator jslr = new Generator(grammar);
        try {
            jslr.computeFirstFollowNullable();
            jslr.generateSLR1Table();
            jslr.generateOutput();
        } catch(Error e) {
            System.err.println("Error performing SLR(1) construction: "+e);
            System.exit(1);
            return;
        } 
    }
}
