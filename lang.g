grammar lang;

options {
    language=Python;
    output=AST;
    ASTLabelType=CommonTree;
}

tokens {
    BLOCK;
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
    |   fork_statement
    |   try_statement
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
    |   for_range_statement
    ;

for_list_statement
    :   FOR IDENTIFIER IN '(' expression ')' statement* ENDFOR
        -> ^(ENDFOR ^(LOOP_TAG IDENTIFIER) expression statement*)
    ;

for_range_statement
    :   FOR IDENTIFIER IN range statement* ENDFOR
        -> ^(ENDFOR ^(LOOP_TAG IDENTIFIER) range statement*)
    ;

fork_statement
    :   FORK '(' expression ')' statement* ENDFORK
        -> ^(FORK expression statement*)
    |   FORK IDENTIFIER '(' expression ')' statement* ENDFORK
        -> ^(FORK ^(LOOP_TAG IDENTIFIER) expression statement*)
    ;

try_statement
    :   try_except_statement
    ;

try_except_statement
    :   TRY statement* except_block+ ENDTRY
        -> ^(ENDTRY except_block+ statement*)
    ;

except_block
    :   EXCEPT error_list statement*
        -> ^(EXCEPT error_list statement*)
    |   EXCEPT IDENTIFIER error_list statement*
        -> ^(EXCEPT error_list ^(LOOP_TAG IDENTIFIER) statement*)
    ;

error_list
    :   open_paren='(' error_list_body ')'
        -> ^(LIST_START[$open_paren] error_list_body)
    ;

error_list_body
    :   ANY
        -> ANY
    |   list_body
        -> list_body
    ;

range
    :   RANGE_START start=expression TO end=expression RANGE_END
        -> ^(RANGE_START $start $end)
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
    :   LIST_START list_body? LIST_END
        -> ^(LIST_START list_body?)
    ;

list_body
    :   list_element (',' list_element)*
        -> list_element+
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
FORK: 'fork';
ENDFORK: 'endfork';
TRY: 'try';
EXCEPT: 'except';
ENDTRY: 'endtry';
RETURN: 'return';

LIST_START: '{';
LIST_END: '}';
RANGE_START: '[';
RANGE_END: ']';

// Matching for DOT tokens is handled in FLOAT, below.
// Notionally: DOT: '.';
fragment DOT: ;
// Matching for TO tokens is handled in FLOAT, below.
// Notionally: TO: '..';
fragment TO: ;

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

// Matching for INT tokens is handled in FLOAT, below.
// Notionally: INT: SIGN DIGIT+;
fragment INT: ;

fragment FLOAT_EXPONENT
    :   'e' ('+'|'-')? DIGIT+
    ;

// Complex rule tree deciding several logical lexer rules: INT, FLOAT, DOT,
// and TO. See http://www.antlr.org/wiki/display/ANTLR3/Lexer+grammar+for+floating+point%2C+dot%2C+range%2C+time+specs
// for the origin of the idea. This is required because of the ambiguity of
// inputs like "1..3" -- in some contexts, it can be a range; in others, it's
// two floats. We always parse it as a range, which is consistent with the C
// implementation.
FLOAT
    :   SIGN DIGIT+ ( // Leading sign and digits: might be FLOAT, might be INT.
            // Two dots means an INT followed by a TO.
            { self.input.LA(2) != ord('.') }?=> '.' (
                DIGIT* FLOAT_EXPONENT? { $type = FLOAT; }
            )
            | FLOAT_EXPONENT { $type = FLOAT; }
            | { $type = INT; }
        )
        | '-' '.' ( // Leading sign and dot, must be float.
            DIGIT+ FLOAT_EXPONENT? { $type = FLOAT; }
        )
        | '.' ( // Leading dot: might be FLOAT, DOT, or TO.
            '.' { $type = TO; }
            | DIGIT+ FLOAT_EXPONENT? { $type = FLOAT; }
            | { $type = DOT; }
        )
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
