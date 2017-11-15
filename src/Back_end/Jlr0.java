package Back_end;

import java.util.Scanner;

/**
 *
 * @author Cid
 */
public class Jlr0 {
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
        Generator jlr = new Generator(grammar);
        try {
            jlr.computeFirstFollowNullable();
            jlr.generateLR0Table();
            jlr.generateOutput();
        } catch(Error e) {
            System.err.println("Error performing LR(0) construction: "+e);
            System.exit(1);
            return;
        } 
    }
}
