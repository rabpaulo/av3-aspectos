# Front-End de Compilador: Scanner + Parser (JFlex + Java CUP)

Este repositório contém a implementação completa de um front-end de compilador funcional (análise léxica, sintática e semântica) usando **JFlex** e **Java CUP** em Java.

---

## Pré-requisitos (Arch Linux)

### 1. Instalar o Java JDK
```bash
sudo pacman -S jdk-openjdk
```

### 2. Obter JFlex e Java CUP

Os arquivos `.jar` necessários já estão referenciados nos subdiretórios ou podem ser baixados:
- JFlex: `Compiler-FrontEnd/jflex-full-1.9.1.jar`
- Java CUP: `./CompiladorFrontEnd/lib/java-cup-11b.jar`

## Como Compilar e Executar

Você pode usar o script `run.sh` para executar os passos 1 a 4 automaticamente, ou rodar os comandos manualmente conforme abaixo.

### Comandos Manuais

**1. Gerar o Analisador Léxico (JFlex)**
```bash
java -jar Compiler-FrontEnd/jflex-full-1.9.1.jar src/lexico.flex
```

**2. Gerar o Analisador Sintático/Semântico (Java CUP)**
O parâmetro `-expect 1` é utilizado para aceitar o conflito shift/reduce esperado (dangling-else).
```bash
java -jar ./CompiladorFrontEnd/lib/java-cup-11b.jar -expect 1 -parser Sintax -symbols sym src/sintatico.cup
```

**3. Mover arquivos gerados para o package (se necessário)**
```bash
mv Lexer.java src/compiler/ || true
mv Sintax.java src/compiler/ || true
mv sym.java src/compiler/ || true
```

**4. Compilar o projeto Java**
```bash
mkdir -p out
javac -cp ./CompiladorFrontEnd/lib/java-cup-11b.jar -d out $(find src -name '*.java')
```

**5. Executar informando o arquivo de entrada**
```bash
java -cp out:./CompiladorFrontEnd/lib/java-cup-11b.jar compiler.Main input.txt
```

---

## Estrutura de arquivos do projeto

- `src/lexico.flex` — Especificação de tokens, delimitadores e expressões regulares (JFlex).
- `src/sintatico.cup` — Gramática da linguagem, precedência de operadores e ações semânticas (Java CUP).
- `src/compiler/Main.java` — Código principal que gerencia a leitura do arquivo, execução do scanner e chamada do parser.
- `src/compiler/Semantic.java` — Tabela de símbolos e lógicas de validação de tipos/escopos.
- `input.txt` — Arquivo contendo o código fonte exemplo para validação e testes.
- `run.sh` — Script automatizado para geração e compilação do compilador.
