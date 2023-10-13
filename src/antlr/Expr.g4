grammar Expr;
/*The grammer name and file must  match */
@header{
package antlr;
}
//Start Variable we have three prdduction rules here prog,decl expr
prog: (decl | expr)+ EOF  #Programm
    ;
decl:ID ':' INT_TYPE '=' NUM  #Declaration
    ;
/* ANTLR resolves ambiuities in favor of the alternative given first*/
expr: expr '*' expr   #Multiplication
    | expr '+' expr   #Addition
    | ID              #Variable
    | NUM             #Number
    ;
 /*Tokens*/
ID: [a-z][a-zA-Z0-9_]*; //identifiers
NUM: '0' | '-'?[1-9][0-9]*;
INT_TYPE:'INT';
COMMENT: '--' ~[\r\n]* -> skip;
WS: [ \t\n\r]+ -> skip;