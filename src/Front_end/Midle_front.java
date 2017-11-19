/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Front_end;

import static Front_end.Scanner.trimAll;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

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
            appendToOutput(fileName + ".DEBUG", "   ENTRADA   \n");
            appendToOutput(fileName + ".DEBUG", inputString + "\n");//Grava em arquivo
        }
        return inputString;
    }

    public String gera_tabela(String table) {

        List<String> simbolos = new ArrayList<>();

        String[] separado_linha = table.split("\n");
        String[] dados = separado_linha[0].split(" ");
        int estado = Integer.parseInt(dados[4]);
        int transicoes = Integer.parseInt(dados[8]);
        for (int i = 1; i <= transicoes; i++) {
            String[] aux = separado_linha[i].split(" ");
            if (!simbolos.contains(aux[1])) {
                simbolos.add(aux[1]);
            }
        }

        String[][] tabela = new String[estado + 1][(simbolos.size()) + 1];
        String tipo = new String();
        for (int i = 0; i < simbolos.size(); i++) {
            tabela[0][i + 1] = "" + simbolos.get(i);
        }
        for (int i = 1; i < estado + 1; i++) {
            tabela[i][0] = "" + (i - 1);
        }
        for (int i = 1; i <= transicoes; i++) {
            String[] aux = separado_linha[i].split(" ");

            int estado_atual = Integer.parseInt(aux[0]);
            String simbolo_atual = aux[1];
            int index_simbolo = simbolos.indexOf(aux[1]);
            if ("reduce".equals(aux[2])) {
                tipo = "r";
            } else {
                tipo = "";
            }
            String texto = tipo + aux[3];
            tabela[estado_atual + 1][index_simbolo + 1] = texto;
        }
        return Imprime_Tabela(tabela, estado, simbolos);
    }

    public String Imprime_Tabela(String[][] tabela, int estado, List<String> simbolos) {
        String output = "";
        for (int i = 0; i < estado + 1; i++) {
            for (int j = 0; j < simbolos.size() + 1; j++) {
                if (tabela[i][j] != null) {
                    output += (tabela[i][j] + "\t");
                } else {
                    tabela[i][j] = "-";
                    output += ("-\t");
                }
            }
            output += ("\n");
        }
        return output;
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
            appendToOutput(fileName + ".DEBUG", "\n   GRAMÁTICA   \n");
        }
        // Inicializa o Grammar Array
        grammar = new Grammar(tokens);
        if ("ERRO".equals(grammar.type)) {
            JOptionPane.showMessageDialog(null, grammar.message, "Erro", JOptionPane.ERROR_MESSAGE);
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
        for (Token token : tokens) {
            if ("TERMINAL".equals(token.type) && (!terminals.contains(token.token))) {
                terminals.add(token.token);
            }
        }
        return terminals;
    }

    public Set<String> nonterminais() {
        Set<String> nonterminals = new LinkedHashSet<>();
        for (Token token : tokens) {
            if ("NONTERMINAL".equals(token.type) && (!nonterminals.contains(token.token))) {
                nonterminals.add(token.token);
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

    // Excluir arquivo de saída
    public static void deleteOutput(String file) {
        File f = new File("src/output/" + file + ".out");
        if (f.exists()) {
            f.delete();
        }
    }

    //Anexar ao arquivo de saída
    public static void appendToOutput(String file, String str) {
        try {
            File f = new File("src/output/" + file + ".out");
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fileWritter = new FileWriter("src/output/" + file + ".out", true);
            try (BufferedWriter bufferWritter = new BufferedWriter(fileWritter)) {
                bufferWritter.write(str);
            }
        } catch (IOException e) {
        }
    }

    // Obter o nome do arquivo de entrada
    public static String getInputName(String file) {
        File f = new File(file);
        return f.getName();
    }

    // Arquivo para String
    public static String getInput(String file) {
        String result = null;
        DataInputStream in = null;
        try {
            File f = new File(file);
            byte[] buffer = new byte[(int) f.length()];
            in = new DataInputStream(new FileInputStream(f));
            in.readFully(buffer);
            result = new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException("IO Erro", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return trimAll(result);
    }
}
