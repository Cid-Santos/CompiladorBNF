package compiladorbnf;

import static compiladorbnf.Scanner.trimAll;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Cid
 */
public class CompiladorBNF {

    private static final Boolean DEBUG = true;
    private static String inputString = new String();

    /**
     * @param args os argumentos da linha de comando
     */
    public static void main(String[] args) {
        // Definir arquivos de entrada

        File file = new File("src/input/example1");

        String path = file.getPath(); //irá retornar "\src\path"

        String fileName = getInputName(path);

        inputString = getInput(path);//Leitura do arquivo de entrada 

        // Obtem Array Token
        String[] rawTokens = inputString.split("\\s+");//Fatia o arquivo de entrada separado por espaco

        // Inicia Array de Token 
        Token[] tokens = new Token[rawTokens.length];

        // Exclua o arquivo de saída se existir
        deleteOutput(fileName);
        deleteOutput(fileName + ".DEBUG");

        // Seção DEBUG
        if (DEBUG) {
            // INPUT
            System.out.println("   ENTRADA   ");
            appendToOutput(fileName + ".DEBUG", "   ENTRADA   \n");
            System.out.println(inputString);//Imprime entrada
            appendToOutput(fileName + ".DEBUG", inputString + "\n");//Grava em arquivo
            // TOKEN
            System.out.println("\n   TOKENS   ");
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
                appendToOutput(fileName + ".DEBUG", token.token + " " + token.type + "\n");
            }
            count++;
        }

        // DEBUG Section
        if (DEBUG) {
            System.out.println("\n   GRAMÁTICA   ");
            appendToOutput(fileName + ".DEBUG", "\n   GRAMÁTICA   \n");
        }

        // Inicializa o Grammar Array
        Grammar grammar = new Grammar(tokens);
        if ("ERRO".equals(grammar.type)) {
            System.out.println(grammar.message);
            appendToOutput(fileName, grammar.message);
            if (DEBUG) {
                appendToOutput(fileName + ".DEBUG", grammar.message);
            }
            return;
        } else {
            System.out.print(grammar.toString());
            // DEBUG Section
            if (DEBUG) {
                appendToOutput(fileName + ".DEBUG", grammar.toString());
            }
        }

        // DEBUG Section
        if (DEBUG) {
            System.out.println("\n   FIRST   ");
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
                System.out.println(firstString);
                appendToOutput(fileName, firstString + "\n");
            }
        }

        // DEBUG Section
        if (DEBUG) {
            System.out.println("\n   FOLLOW   ");
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
                System.out.println(followString);
                appendToOutput(fileName, followString + "\n");
            }
        }
    }

    // Excluir arquivo de saída
    protected static void deleteOutput(String file) {
        File f = new File("src/output/" + file + ".out");
        if (f.exists()) {
            f.delete();
        }
    }

    //Anexar ao arquivo de saída
    protected static void appendToOutput(String file, String str) {
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
    protected static String getInputName(String file) {
        File f = new File(file);
        return f.getName();
    }

    // Arquivo para String
    protected static String getInput(String file) {
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
