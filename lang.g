grammar lang;

options {
    language=Python;
    output=AST;
    ASTLabelType=CommonTree;
}

tokens {
    BLOCK;
    LIST;
    PROGRAM;
    STATEMENT;
    LOOP_TAG;
}

program
    : statement* EOF
        -> ^(PROGRAM statement*)
    ;

statement
    :   simple_statement ';'
        -> simple_statement
    |   if_statement
    |   while_statement
    |   for_statement
    |   ';'
        ->
    ;

simple_statement
    :   expression
        -> ^(STATEMENT expression)
    |   RETURN expression?
        -> ^(RETURN expression?)
    ;

// This writes out the branches if the IF statement *BACKWARDS*. Be careful!
// Doing it the "wrong" way around makes generating jump targets in compile.g
// way easier.
if_statement
    :   if_part elseif_parts? else_part? ENDIF
        -> ^(ENDIF else_part? elseif_parts? if_part)
    ;

if_part
    :   IF condition statement*
        -> ^(IF condition statement*)
    ;

elseif_parts
    :   ELSEIF condition statement* elseif_parts?
        -> elseif_parts? ^(ELSEIF condition statement*)
    ;

else_part
    :   ELSE statement*
        -> ^(ELSE statement*)
    ;

while_statement
    :   WHILE condition statement* ENDWHILE
        -> ^(ENDWHILE condition statement*)
    |   WHILE IDENTIFIER condition statement* ENDWHILE
        -> ^(ENDWHILE ^(LOOP_TAG IDENTIFIER) condition statement*)
    ;

for_statement
    :   for_list_statement
    ;

for_list_statement
    :   FOR IDENTIFIER IN '(' expression ')' statement* ENDFOR
        -> ^(ENDFOR ^(LOOP_TAG IDENTIFIER) expression statement*)
    ;

condition
    :   '(' expression ')'
        -> expression
    ;

expression
    :   root_expression
    ;

root_expression
    :   literal
    |   IDENTIFIER
    ;

literal
    :   INT
    |   FLOAT
    |   STRING
    |   OBJECT_NUM
    |   ERROR
    |   list_literal
    ;

list_literal
    :   '{' list_body? '}'
        -> ^(LIST list_body?)
    ;

list_body
    :   list_element (',' list_element)*
        -> list_element*
    ;

list_element
    :   expression
    |   list_splice
    ;

list_splice
    :   '@' expression
        -> ^('@' expression)
    ;

// --------------------
// Fixed-content tokens
// --------------------

ERROR
    :   'e_range'
    |   'e_recmove'
    |   'e_none'
    |   'e_propnf'
    |   'e_quota'
    |   'e_div'
    |   'e_args'
    |   'e_varnf'
    |   'e_verbnf'
    |   'e_perm'
    |   'e_invind'
    |   'e_nacc'
    |   'e_type'
    |   'e_float'
    |   'e_invarg'
    |   'e_maxrec'
    ;

ANY: 'any';
IF: 'if';
ELSEIF: 'elseif';
ELSE: 'else';
ENDIF: 'endif';
WHILE: 'while';
ENDWHILE: 'endwhile';
FOR: 'for';
IN: 'in';
ENDFOR: 'endfor';
RETURN: 'return';

// -----------------------
// Variable-content tokens
// -----------------------

// printable ASCII, minus double quote and backslash.
fragment STRING_CHAR
    :   '\u0020'..'\u0021'
    |   '\u0023'..'\u005B'
    |   '\u005D'..'\u007E'
    ;

fragment STRING_ESCAPE
    :   '\\' ('"' | '\\')
    ;

STRING: '"' (STRING_CHAR | STRING_ESCAPE) * '"';

fragment DIGIT: '0'..'9';
fragment SIGN: '-'?;

INT: SIGN DIGIT+;

fragment FLOAT_EXPONENT
    :   'e' ('+'|'-')? DIGIT+
    ;

FLOAT
    :   SIGN DIGIT+ FLOAT_EXPONENT
    |   SIGN DIGIT+ '.' DIGIT* FLOAT_EXPONENT?
    |   SIGN '.' DIGIT+ FLOAT_EXPONENT?
    ;

OBJECT_NUM
    :   '#' '-'? DIGIT+
    ;

fragment IDENT_FIRST_CHAR
    :   'a'..'z' | '_'
    ;

fragment IDENT_CHAR
    :   IDENT_FIRST_CHAR | DIGIT
    ;

IDENTIFIER
    :   IDENT_FIRST_CHAR IDENT_CHAR*
    ;

// Newlines aren't technically legal in MOO strings, where most code comes
// from. However, permitting them in the language means source from files can
// be compiled without stripping newlines in advance.
WHITESPACE
    :   (' ' | '\t' | '\r' | '\n') { $channel = HIDDEN; }
    ;

COMMENT
    :   '/*' .* '*/' { $channel = HIDDEN; }
    ;
