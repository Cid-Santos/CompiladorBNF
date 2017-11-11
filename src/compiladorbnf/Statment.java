package compiladorbnf;

/**
 *
 * @author Cid
 */
public class Statment {

    public Token nonTerminal;
    public Token[] definitions = new Token[100];
    public Statment() {}


    public void oneline() {
        String definitionString = "";
        for (Token token : definitions) {
            if (token != null) {
                definitionString += token.token + " ";
            }
        }
        System.out.println(nonTerminal.token + " ::= " + definitionString);
    }


    @Override
    public String toString() {
        String definitionString = "";
        for (Token token : definitions) {
            if (token != null) {
                definitionString += token.token + " ";
            }
        }
        return nonTerminal.token + " ::= " + definitionString;
    }
}
