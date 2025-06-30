
lexer grammar Lex;

READ       : 'read' ;
ELSE       : 'else' ;
THEN       : 'then' ;
DO         : 'do' ;
WHILE      : 'while' ;
IF         : 'if' ;
TRUE       : 'true' ;
FALSE      : 'false' ;
WRITE      : 'write' ;
WRITELN    : 'writeln' ;
SKIP       : 'skip' ;

SEMICOLON    : ';' ;
OPENPAREN    : '(' ;
CLOSEPAREN   : ')' ;

EQUAL        : '=' ;
SMALLEQUAL   : '<=' ;
NOT          : '!' ;
ASSIGN       : ':=' ;
MULTIPLY     : '*' ;
LOGAND       : '&' ;
COMMA        : ',' ;
PLUS         : '+' ;
MINUS        : '-' ;

FLOAT        : ('0'..'9')+('.')('0'..'9')+ ;
INTNUM       : ('0'..'9')+ ;
STRING       : '\'' ('\'' '\'' | ~'\'')* '\'';
COMMENT      : '{' (~'}')* '}' {skip();} ;
WS           : (' ' | '\t' | '\r' | '\n' )+ {skip();} ;
IDENT        : ('a'..'z' | 'A'..'Z' | '_')('a'..'z' | 'A'..'Z' | '0'..'9')*;

// Additional Features

SMALLER      : '<' ;
GREATER      : '>' ;
GREATEREQUAL : '>=' ;
NOTEQUAL     : '!=' ;
XOR          : '^' ;
DIVIDE       : '/' ;
OR           : '||' ;
