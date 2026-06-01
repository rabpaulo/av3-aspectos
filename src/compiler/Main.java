package compiler;

import java.io.*;
import java.lang.reflect.Field;
import java_cup.runtime.Symbol;

public class Main {
    public static void main(String[] args) {
        String filename = args.length > 0 ? args[0] : "input.txt";
        try {
            System.out.println("== Tokenização (pré-análise) ==");
            Lexer lexerPrint = new Lexer(new FileReader(filename));
            Symbol s;
            while ((s = lexerPrint.next_token()) != null) {
                if (s.sym == sym.EOF) { System.out.println("[LEX] EOF"); break; }
                String name = tokenName(s.sym);
                System.out.println(String.format("[LEX] %s -> %s", name, s.value));
            }

            System.out.println("\n== Análise sintática (parse) ==");
            Lexer lexer = new Lexer(new FileReader(filename));
            Sintax parser = new Sintax(lexer);
            parser.parse();

            if (Semantic.instance.hasErrors()) {
                System.err.println("Erros semanticos detectados.");
            } else {
                System.out.println("Checagem semantica: OK");
            }

            System.out.println("\n== Fim ==");
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + filename);
        } catch (Exception e) {
            System.err.println("Erro durante a análise:");
            e.printStackTrace();
        }
    }

    private static String tokenName(int code) {
        try {
            Field[] f = sym.class.getFields();
            for (Field ff : f) {
                if (ff.getType() == int.class) {
                    if (ff.getInt(null) == code) return ff.getName();
                }
            }
        } catch (Exception e) { /* ignore */ }
        return Integer.toString(code);
    }
}
