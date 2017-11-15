/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Front_end;

import static Front_end.CompiladorBNF.appendToOutput;
import static Front_end.CompiladorBNF.deleteOutput;
import static Front_end.CompiladorBNF.getInput;
import static Front_end.CompiladorBNF.getInputName;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author pedro
 */
public class Midle_front {

    private static final Boolean DEBUG = true;
    public static String inputString = new String();
    private static String caminho_arquivo = "src/input/example1";
    public static String fileName = "exemplo1";
    public static Token[] tokens;
    public static Grammar grammar;

    public String ler_arquio() {

        File file = new File(caminho_arquivo);

        String path = file.getPath(); //irá retornar "\src\path"

        fileName = getInputName(path);

        inputString = getInput(path);//Leitura do arquivo de entrada 

        if (DEBUG) {
            // INPUT
            System.out.println("   ENTRADA   ");
            appendToOutput(fileName + ".DEBUG", "   ENTRADA   \n");
            System.out.println(inputString);//Imprime entrada
            appendToOutput(fileName + ".DEBUG", inputString + "\n");//Grava em arquivo
        }
        return inputString;
    }

    public String processa_token() {

        String dados = "";
        // Obtem Array Token
        String[] rawTokens = inputString.split("\\s+");//Fatia o arquivo de entrada separado por espaco

        // Inicia Array de Token 
        tokens = new Token[rawTokens.length];

        // Exclua o arquivo de saída se existir
        deleteOutput(fileName);
        deleteOutput(fileName + ".DEBUG");

        // Seção DEBUG
        if (DEBUG) {
            // TOKEN
            dados = "\n   TOKENS   ";
            appendToOutput(fileName + ".DEBUG", "\n   TOKENS   \n");
        }

        //Create Tokens Array (Para cada Token em Raw Token Array DO :)
        int count = 0;
        for (String arg : rawTokens) {
            //Obter Token
            Token token = new Token(arg);
            // Alocar Token Object para conjunto de token
            tokens[count] = token;
            if (DEBUG) {
                token.oneline();
                dados = dados + "\n" + token.token + "\t" + token.message;
                appendToOutput(fileName + ".DEBUG", token.token + " " + token.type + "\n");
            }
            count++;
        }
        return dados;
    }

    public String processa_gramatica() {
        // DEBUG Section
        if (DEBUG) {
            System.out.println("\n\nGRAMÁTICA   ");
            appendToOutput(fileName + ".DEBUG", "\n   GRAMÁTICA   \n");
        }

        // Inicializa o Grammar Array
        grammar = new Grammar(tokens);
        if ("ERRO".equals(grammar.type)) {
            System.out.println(grammar.message);
            appendToOutput(fileName, grammar.message);
            if (DEBUG) {
                appendToOutput(fileName + ".DEBUG", grammar.message);
            }
        } else {
            // DEBUG Section
            if (DEBUG) {
                appendToOutput(fileName + ".DEBUG", grammar.toString());
            }
        }
        return grammar.toString();
    }

    public Set<String> terminais() {
        Set<String> terminals = new LinkedHashSet<>();
        for (int i = 0; i < tokens.length; i++) {
            if (("TERMINAL".equals(tokens[i].type) && (!terminals.contains(tokens[i].token)))) {
                terminals.add(tokens[i].token);
            }
        }

        return terminals;
    }

    public Set<String> nonterminais() {
        Set<String> nonterminals = new LinkedHashSet<>();
        for (int i = 0; i < tokens.length; i++) {
            if ("NONTERMINAL".equals(tokens[i].type) && (!nonterminals.contains(tokens[i].token))) {
                nonterminals.add(tokens[i].token);
            }
        }

        return nonterminals;
    }



    public String processa_first_follow() {
        String first_follow = "\n";
        if (DEBUG) {
            appendToOutput(fileName + ".DEBUG", "\n   FIRST   \n");
        }

        //Calcula os conjuntos First 
        for (Token nonTerminal : grammar.nonTerminals) {
            if (nonTerminal != null) {
                // Para cada Não Terminal, obtenha Primeiros Conjuntos
                String firstString = "FIRST(" + nonTerminal.token + ") = {";
                for (Token token : grammar.first(nonTerminal)) {
                    if (token != null) {
                        firstString += token.token + ", ";
                    }
                }
                firstString += "}";
                firstString = firstString.replace(", }", "}");
                // DEBUG Section
                if (DEBUG) {
                    appendToOutput(fileName + ".DEBUG", firstString + "\n");
                }
                // Saida do  conjuntos First 
                first_follow = first_follow + firstString + "\n";
                appendToOutput(fileName, firstString + "\n");
            }
        }

        // DEBUG Section
        if (DEBUG) {
            first_follow = first_follow + "\n";
            appendToOutput(fileName + ".DEBUG", "\n   FOLLOW   \n");
        }
        // Calcula os conjuntos FOLLOW
        for (Token nonTerminal : grammar.nonTerminals) {
            if (nonTerminal != null) {
                // Para cada Não Terminal, obtenha Primeiros Conjuntos
                String followString = "FOLLOW(" + nonTerminal.token + ") = {";
                for (Token token : grammar.follow(nonTerminal)) {
                    if (token != null) {
                        followString += token.token + ", ";
                    }
                }
                followString += "}";
                followString = followString.replace(", }", "}");
                // DEBUG Section
                if (DEBUG) {
                    appendToOutput(fileName + ".DEBUG", followString + "\n");
                }
                // Saida do  conjuntos First
                first_follow = first_follow + followString + "\n";
                appendToOutput(fileName, followString + "\n");
            }
        }

        return first_follow;
    }

}
