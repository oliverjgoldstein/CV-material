// COMS22201: Syntax analyser

parser grammar Syn;

options {
  tokenVocab = Lex;
  output = AST;
}

@members
{
	private String cleanString(String s){
		String tmp;
		tmp = s.replaceAll("^'", "");
		s = tmp.replaceAll("'$", "");
		tmp = s.replaceAll("''", "'");
		return tmp;
	}
}

string
    scope { String tmp; }
    :
    s=STRING { $string::tmp = cleanString($s.text); }-> STRING[$string::tmp]
;


program :
    statements
  ;

statements :
    statement ( SEMICOLON^ statement )*
  ;

statement :
    variable ASSIGN^ exp
  | SKIP
  | IF^ boolexp THEN! statement ELSE! statement
  | WHILE^ boolexp DO! statement
  | READ^ OPENPAREN! variable CLOSEPAREN!
  | OPENPAREN! statements CLOSEPAREN!
  | WRITE^ OPENPAREN! ( string | arg ) CLOSEPAREN!
  | WRITELN
  ;

arg :
    (boolexp)=>boolexp
  | (exp)=> exp
  ;

boolexp :
  boolterm ((LOGAND^ | OR^) boolterm)*
  ;

boolterm :
    NOT^ bool
  | bool
  ;

bool :
    TRUE
  | FALSE
  | (exp  EQUAL exp)=>exp EQUAL^ exp
  | (exp SMALLEQUAL exp)=>exp SMALLEQUAL^ exp
  | (exp SMALLER exp)=>exp SMALLER^ exp
  | (exp GREATER exp)=>exp GREATER^ exp
  | (exp GREATEREQUAL exp)=>exp GREATEREQUAL^ exp
  | (exp NOTEQUAL exp)=>exp NOTEQUAL^ exp
  | OPENPAREN! boolexp CLOSEPAREN!
  ;

exp :
  term ((PLUS^ | MINUS^ | XOR^) term)*
  ;

term :
  factor ((MULTIPLY^ | DIVIDE^ ) factor)*
  ;

factor :
    variable
  | INTNUM
  | FLOAT
  | OPENPAREN! exp CLOSEPAREN!
  ;

variable :
  IDENT
;

