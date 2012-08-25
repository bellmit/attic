grammar lang;

options {
    language=Python;
    output=AST;
    ASTLabelType=CommonTree;
}

tokens {
    LIST;
    STATEMENT;
}

program
    : statement* EOF!
    ;

statement
    :   expression ';'
        -> ^(STATEMENT expression)
    ;

expression
    :   literal
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
    : '{' list_body '}'
        -> ^(LIST list_body?)
    ;

list_body
    :   (list_element (',' list_element)*)?
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

INT: '-'? DIGIT+;

fragment FLOAT_FRACTIONAL
    :   '.' DIGIT*
    ;

fragment FLOAT_EXPONENT
    :   'e' ('+'|'-')? DIGIT+
    ;

FLOAT
    :   INT ((FLOAT_FRACTIONAL FLOAT_EXPONENT?) | FLOAT_EXPONENT)
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
