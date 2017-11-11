package compiladorbnf;

import java.util.regex.Pattern;

/**
 *
 * @author Cid
 */
public final class Grammar {

    private static final Pattern BASIC_GRAMMAR = Pattern.compile("^((NONTERMINALPRODUCAO|TERMINALPRODUCAO)(NONTERMINAL|TERMINAL|PIPE)+)+$");
    private static final Pattern VALID_GRAMMAR = Pattern.compile("^(NONTERMINALPRODUCAO(NONTERMINAL|TERMINAL|PIPE)+)+$");
    public String type;
    public String message;
    public Statment[] statments = new Statment[100];
    public Token[] nonTerminals = new Token[100];

    // Gramar construtor
    public Grammar(Token[] tokens) {
        validate(tokens);// Validata Grammar antes do Parse
        if ("ERRO".equals(type)) {
            // Não analise gramática
        } else {
            // Indexs set
            int statmentIndex = -1;
            int tokenIndex = 0;
            int definitionIndex = 0;
            int nonTerminalIndex = 0;
            // Criar declarações da matriz de tokens...
            do {
                // Se Nonterminal antes ASSIGNMENT
                if (tokens[tokenIndex].type.equals("NONTERMINAL") && tokens[tokenIndex + 1].type.equals("PRODUCAO")) {
                    // Incremento para próximo índice de segmentos
                    statmentIndex++;
                    //inicia Declaração 
                    statments[statmentIndex] = new Statment();
                    // Atribuir NONTERMINAL
                    statments[statmentIndex].nonTerminal = tokens[tokenIndex];
                    // Redefinir o índice de definição na criação de uma nova declaração
                    definitionIndex = 0;
                    // adiciona ao nonTerminals Array
                    nonTerminals[nonTerminalIndex] = tokens[tokenIndex];
                    // Empurar past ASSIGNMENT
                    tokenIndex++;
                    // Aumento ao próximo índice não terminal
                    nonTerminalIndex++;
                } // Else se token for PIPE '|'
                else if (tokens[tokenIndex].type.equals("PIPE")) {
                    // Incremento para próximo índice de segmentos
                    statmentIndex++;
                    // Declaração inicial
                    statments[statmentIndex] = new Statment();
                    // Assign NONTERMINAL
                    statments[statmentIndex].nonTerminal = statments[statmentIndex - 1].nonTerminal;
                    // Redefinir o índice de definição na criação de uma nova declaração
                    definitionIndex = 0;
                } // Else é a definição
                else {
                    // Token de envio para definição de declaração
                    statments[statmentIndex].definitions[definitionIndex] = tokens[tokenIndex];
                    // Incremento para o próximo Índice de Definição
                    definitionIndex++;
                }
                // Incrementar para o próximo índice Token
                tokenIndex++;
            } while (tokenIndex < tokens.length);

            nonTerminalCheck();
        }
    }

    // Define erro e mensagem de erro se a gramática não for válida
    protected void validate(Token[] tokens) {
        String tokenString = "";
        for (Token token : tokens) {
            tokenString += token.type;
        }
        if (!BASIC_GRAMMAR.matcher(tokenString).matches()) {
            type = "ERRO";
            message = "ERRO CODE 0: A entrada não esta de acordo com o formato";
            return;
        }
        if (!VALID_GRAMMAR.matcher(tokenString).matches()) {
            type = "ERRO";
            message = "ERRO CODE 2: Símbolo terminal aparecendo no lado esquerdo de uma regra";
        }
    }

    protected void nonTerminalCheck() {
        for (Statment statment : statments) {
            if (statment != null) {
                //Se Definição não terminais na matriz nonTerminals
                for (Token definition : statment.definitions) {
                    if (definition != null) {
                        // Não é Terminal e está em verificação de matriz
                        if ("NONTERMINAL".equals(definition.type)) {
                            if (inTokenArray(nonTerminals, definition) == -1) {
                                type = "ERRO";
                                message = "ERRO CODE 1: Símbolo não-terminal listado em uma regra, mas não possui uma descrição";
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    protected int inTokenArray(Token[] tokenArray, Token matchToken) {
        int index = 0;
        for (Token arrayToken : tokenArray) {
            if (arrayToken != null) {
                if (matchToken.token.equals(arrayToken.token)) {
                    return index;
                }
            }
            index++;
        }
        return -1;
    }

    public Token[] first(Token nonTerminal) {
        // Definir Índices Incrementados
        int setIndex = 0;
        Token[] set = new Token[100];
        // Cada declaração em gramática
        for (Statment statment : statments) {
            if (statment != null) {
                // Se for passado nonTerminal
                if (statment.nonTerminal.token.equals(nonTerminal.token)) {
                    // se for TERMINAL
                    if ("TERMINAL".equals(statment.definitions[0].type) && inTokenArray(set, statment.definitions[0]) == -1) {
                        set[setIndex] = statment.definitions[0];
                        setIndex++;
                    } // Se for NonTerminal
                    else if ("NONTERMINAL".equals(statment.definitions[0].type)) {
                        for (Token token : first(statment.definitions[0])) {
                            if (token != null && inTokenArray(set, token) == -1) {
                                set[setIndex] = token;
                                setIndex++;
                            }
                        }
                    }
                }
            }
        }
        return set;
    }

    public Token[] follow(Token nonTerminal) {
        // Definir Índices Incrementados
        int setIndex = 0;
        int definitionIndex = 0;
        int nonTerminalIndex = 0;
        Token[] set = new Token[100];
        // Cada declaração em gramática
        for (Statment statment : statments) {
            if (statment != null) {
                // Para cada definição em declaração
                for (Token definition : statment.definitions) {
                    if (definition != null) {
                        // Se for passado nonTerminal
                        if (definition.token.equals(nonTerminal.token)) {
                            // Se +1 for TERMINAL
                            if (statment.definitions[definitionIndex + 1] != null && "TERMINAL".equals(statment.definitions[definitionIndex + 1].type) && inTokenArray(set, statment.definitions[definitionIndex + 1]) == -1) {
                                set[setIndex] = statment.definitions[definitionIndex + 1];
                                setIndex++;
                            } // Se for Não Terminal
                            else if (statment.definitions[definitionIndex + 1] != null && "NONTERMINAL".equals(statment.definitions[definitionIndex + 1].type)) {
                                for (Token token : first(statment.definitions[definitionIndex + 1])) {
                                    if (token != null && inTokenArray(set, token) == -1) {
                                        set[setIndex] = token;
                                        setIndex++;
                                    }
                                }
                            }
                        }
                        // Índice de definição de incremento
                        definitionIndex++;
                    }
                }
                // Índice de Definição de Reset
                definitionIndex = 0;
            }
        }
        if (set[0] == null) {
            set[0] = new Token("$");
        } else if (statments[0].nonTerminal.token.equals(nonTerminal.token)) {
            set[setIndex] = new Token("$");
        }
        return set;
    }

    @Override
    public String toString() {
        String grammarString = "";
        grammarString += "Não Terminais: \n";
        for (Token nonTerminal : nonTerminals) {
            if (nonTerminal != null) {
                grammarString += nonTerminal.token + "\n";
            }
        }
        grammarString += "\nProdução: \n";
        for (Statment statment : statments) {
            if (statment != null) {
                grammarString += statment.toString() + "\n";
            }
        }
        return grammarString;
    }
}
