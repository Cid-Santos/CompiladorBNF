package Front_end;

import java.util.regex.Pattern;

/**
 *
 * @author Cid
 */
public final class Token {

    private static final Pattern PIPE_TOKEN = Pattern.compile("^\\|$");
    private static final Pattern ALFANUMMI_TOKEN = Pattern.compile("^[a-z0-9][a-z0-9]*$");
    private static final Pattern ALFANUMMA_TOKEN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$");

    public String token;
    public String type;
    public String message;

    //Construtor Token
    public Token(String token) {
        this.token = token;
        valToven();
        if (!"ERRO".equals(type)) {
            type();
        }
    }

    // Define a mensagem de erro e erro se o token não for válido
    protected void valToven() {
        char[] c = token.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (!(c[i] > '!' && c[i] < '~')) {
                type = "ERRO";
                message = "ERRO CODE 0: Entrada não esta de acordo com o formato";
            }
        }
    }

    // tipo e menssagem do token fornecido
    protected void type() {
        if (isOpcional(token)) {
            type = "OPCIONAL";
            message = "opcional";
        } else if (PIPE_TOKEN.matcher(token).matches()) {
            type = "PIPE";
            message = "alternativa";
        } else if (isTerminal(token)) {
            type = "TERMINAL";
            message = "símbolo terminal";
        } else if (noTerminal(token)) {
            type = "NONTERMINAL";
            message = "símbolo não-terminal";
        } else if (isProducao(token)) {
            type = "PRODUCAO";
            message = "produção (->)";
        } else {
            type = "ERRO";
            message = "ERRO CODE 0: Entrada não esta de acordo com o formato";
        }
    }

    // Imprima todos os vars para token
    public void debug() {
        System.out.println("Token: " + token);
        System.out.println("Type: " + type);
        System.out.println("Message: " + message);
    }

    public boolean isProducao(String s) {
        boolean isProducao = false;
        String replace = s.replace(" ", "");
        if ("::=".equals(replace)) {
            isProducao = true;
        }
        return isProducao;
    }

    public boolean isOpcional(String s) {
        boolean isOpcional = false;
        if (s.contains("[") && s.contains("]")) {
            String replace = s.replace("[", "").replace("]", "");
            if (replace.contains("<") && replace.contains(">")) {
                replace = replace.replace("<", "").replace(">", "");
            }
            if (ALFANUMMA_TOKEN.matcher(replace).matches()) {
                isOpcional = true;
            }
        }
        return isOpcional;
    }

    public boolean isTerminal(String s) {
        boolean isTerminal = false;
        if (s.contains("\"") && s.contains("\"")) //verifica se e um nao terminal
        {
            String replace = s.replace("\"", "");
            if (ALFANUMMI_TOKEN.matcher(replace).matches() || replace.equals("")) {
                isTerminal = true;
            }
        }else  if (ALFANUMMI_TOKEN.matcher(s).matches()) {
                isTerminal = true;
            }
        return isTerminal;
    }

    public boolean noTerminal(String s) {
        boolean isNoTerminal = false;
        if (s.contains("<") && s.contains(">")) //verifica se e um nao terminal
        {
            String replace = s.replace("<", "").replace(">", "");
            if (ALFANUMMA_TOKEN.matcher(replace).matches()) {
                isNoTerminal = true;
            }
        }
        if (!isNoTerminal) {
            if (isLL(s)) {
                isNoTerminal = true;
            }
        }
        return isNoTerminal;
    }

    public boolean isLL(String s) {
        boolean isNoTerminal = false;

        if (s.contains("<") && s.contains(">") && s.contains("\"")) //verifica se e um nao terminal
        {
            String replace = s.replace("<", "").replace(">", "").replace("\"", "");
            if (ALFANUMMA_TOKEN.matcher(replace).matches()) {
                if (!s.contains(">\"")) {
                    isNoTerminal = true;
                }
            }
        }
        return isNoTerminal;
    }
}
