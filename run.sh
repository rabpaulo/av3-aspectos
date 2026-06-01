# 1) Gerar o lexer (JFlex)
java -jar Compiler-FrontEnd/jflex-full-1.9.1.jar src/lexico.flex

# 2) Gerar o parser (Java CUP)
#    -expect 1 aceita o conflito shift/reduce do dangling-else
java -jar ./CompiladorFrontEnd/lib/java-cup-11b.jar \
     -expect 1 -parser Sintax -symbols sym src/sintatico.cup

# 3) Mover arquivos gerados para o package compiler
mv Sintax.java src/compiler/ || true
mv sym.java src/compiler/ || true

# 4) Compilar (incluir o JAR do CUP no classpath)
mkdir -p out
javac -cp ./CompiladorFrontEnd/lib/java-cup-11b.jar \
      -d out $(find src -name '*.java')

# 5) Executar
java -cp out:./CompiladorFrontEnd/lib/java-cup-11b.jar \
     compiler.Main input.txt
