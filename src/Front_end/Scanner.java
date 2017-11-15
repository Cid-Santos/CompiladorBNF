/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Front_end;

/**
 *
 * @author Cid
 */
public class Scanner {

    // Retorna 'true' se o  caracter e um DELIMITADOR.
    public boolean isDelimiter(char ch) {
        if (ch == ' ' || ch == '+' || ch == '-' || ch == '*'
                || ch == '/' || ch == ',' || ch == ';' || ch == '>'
                || ch == '<' || ch == '=' || ch == '(' || ch == ')'
                || ch == '[' || ch == ']' || ch == '{' || ch == '}') {
            return (true);
        }
        return (false);
    }

    // Retorna 'true' se o character e um OPERATOR.
    public boolean isOperator(char ch) {
        if (ch == '+' || ch == '-' || ch == '*'
                || ch == '/' || ch == '>' || ch == '<'
                || ch == '=') {
            return (true);
        }
        return (false);
    }

    // Retorna 'true' se a string e uma VALID IDENTIFIER.
    public boolean validIdentifier(String str) {
        if (str.charAt(0) == '0' || str.charAt(0) == '1' || str.charAt(0) == '2'
                || str.charAt(0) == '3' || str.charAt(0) == '4' || str.charAt(0) == '5'
                || str.charAt(0) == '6' || str.charAt(0) == '7' || str.charAt(0) == '8'
                || str.charAt(0) == '9' || isDelimiter(str.charAt(0)) == true) {
            return (false);
        }
        return (true);
    }

    // Retorna 'true' se a string e uma KEYWORD.
    public boolean isKeyword(String str) {
        if (!str.equals("if") || !str.equals("else")
                || !str.equals("while") || !str.equals("do")
                || !str.equals("break")
                || !str.equals("continue") || !str.equals("int")
                || !str.equals("double") || !str.equals("float")
                || !str.equals("return") || !str.equals("char")
                || !str.equals("case") || !str.equals("char")
                || !str.equals("sizeof") || !str.equals("long")
                || !str.equals("short") || !str.equals("typedef")
                || !str.equals("switch") || !str.equals("unsigned")
                || !str.equals("void") || !str.equals("static")
                || !str.equals("struct") || !str.equals("goto")) {
            return (true);
        }
        return (false);
    }

    // Retorna 'true' se a string e um INTEGER.
    public boolean isInteger(String str) {
        int i, len = str.length();

        if (len == 0) {
            return (false);
        }
        for (i = 0; i < len; i++) {
            if (str.charAt(i) != '0' && str.charAt(i) != '1' && str.charAt(i) != '2'
                    && str.charAt(i) != '3' && str.charAt(i) != '4' && str.charAt(i) != '5'
                    && str.charAt(i) != '6' && str.charAt(i) != '7' && str.charAt(i) != '8'
                    && str.charAt(i) != '9' || (str.charAt(i) == '-' && i > 0)) {
                return (false);
            }
        }
        return (true);
    }

    // Retorna 'true' se a string e um REAL NUMBER.
    public boolean isRealNumber(String str) {
        int i, len = str.length();
        boolean hasDecimal = false;
        if (len == 0) {
            return (false);
        }
        for (i = 0; i < len; i++) {
            if (str.charAt(i) != '0' && str.charAt(i) != '1' && str.charAt(i) != '2'
                    && str.charAt(i) != '3' && str.charAt(i) != '4' && str.charAt(i) != '5'
                    && str.charAt(i) != '6' && str.charAt(i) != '7' && str.charAt(i) != '8'
                    && str.charAt(i) != '9' && str.charAt(i) != '.'
                    || (str.charAt(i) == '-' && i > 0)) {
                return (false);
            }
            if (str.charAt(i) == '.') {
                hasDecimal = true;
            }
        }
        return (hasDecimal);
    }
    
    //Retorna texto parametrizado 
     public static String trimAll(String text) {
        String string = text.trim();
        string = string.replace(" ", "");
        string = string.replace("|", " | ");
        string = string.replace("::=", " ::= ");
        string = string.replace("<", " <").replace(">", "> ");
        string = string.replace("[ <", "[<").replace("> ]", ">]");
        string = string.replace("\"<", "\" <").replace(">\"", "> \"");
        string = string.substring(1, string.length());
        return string;
    }

}
