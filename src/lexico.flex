package compiler;
import java_cup.runtime.Symbol;

%%

%class Lexer
%public
%cup
%unicode

DIGIT = [0-9]
LETTER = [a-zA-Z_]
ID = {LETTER}({LETTER}|{DIGIT})*
INT = {DIGIT}+

%%

/* whitespace */
[ \t\r\n]+    { /* skip whitespace */ }

/* single-line comment */
"//".*        { /* skip comment */ }

/* block comment (simple handling) */
"/*"([^*]|\*+[^*/])*"*/"    { /* skip block comment */ }

/* Keywords */
"if"       { return new Symbol(sym.IF); }
"else"     { return new Symbol(sym.ELSE); }
"while"    { return new Symbol(sym.WHILE); }
"return"   { return new Symbol(sym.RETURN); }
"int"      { return new Symbol(sym.INT); }
"float"    { return new Symbol(sym.FLOAT); }
"void"     { return new Symbol(sym.VOID); }

/* operators (multi-char first) */
"=="       { return new Symbol(sym.EQEQ); }
"!="       { return new Symbol(sym.NEQ); }
"<="       { return new Symbol(sym.LE); }
">="       { return new Symbol(sym.GE); }

"+"        { return new Symbol(sym.PLUS); }
"-"        { return new Symbol(sym.MINUS); }
"*"        { return new Symbol(sym.MUL); }
"/"        { return new Symbol(sym.DIV); }
"<"        { return new Symbol(sym.LT); }
">"        { return new Symbol(sym.GT); }
"="        { return new Symbol(sym.ASSIGN); }

/* delimiters */
"("        { return new Symbol(sym.LPAREN); }
")"        { return new Symbol(sym.RPAREN); }
"{"        { return new Symbol(sym.LBRACE); }
"}"        { return new Symbol(sym.RBRACE); }
";"        { return new Symbol(sym.SEMI); }
","        { return new Symbol(sym.COMMA); }

/* floating: 123.456, .5, 1e10, 1.2e-3 */
{DIGIT}+"."{DIGIT}+([eE][+-]?{DIGIT}+)?   { return new Symbol(sym.FLOAT_CONST, Double.valueOf(yytext())); }
"."{DIGIT}+([eE][+-]?{DIGIT}+)?           { return new Symbol(sym.FLOAT_CONST, Double.valueOf(yytext())); }
{DIGIT}+[eE][+-]?{DIGIT}+                  { return new Symbol(sym.FLOAT_CONST, Double.valueOf(yytext())); }

/* integer */
{INT}      { return new Symbol(sym.INT_CONST, Integer.valueOf(yytext())); }

/* identifier (after keywords so keywords are matched first) */
{ID}       { return new Symbol(sym.ID, yytext()); }

/* EOF */
<<EOF>>    { return new Symbol(sym.EOF); }
