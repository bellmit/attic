tree grammar compiler;

options {
    language=Python;
    tokenVocab=lang;
    ASTLabelType=CommonTree;
    output=AST;
}

tokens {
    OP_CHECK_LIST_FOR_SPLICE;
    OP_DONE;
    OP_LIST_ADD_TAIL;
    OP_LIST_APPEND;
    OP_MAKE_EMPTY_LIST;
    OP_MAKE_SINGLETON_LIST;
    OP_NUM;
    OP_POP;
    OP_PUSH_LITERAL;
}

program
    :   statement* EOF
        -> statement* OP_DONE
    ;

statement
    :   ^(STATEMENT expression)
        -> expression OP_POP
    ;

expression
    :   literal
    ;

literal
    :   immediate_value
    |   list_literal
    ;

immediate_value
    :   INT
        -> OP_NUM INT
    |   FLOAT
        -> OP_PUSH_LITERAL FLOAT
    |   STRING
        -> OP_PUSH_LITERAL STRING
    |   OBJECT_NUM
        -> OP_PUSH_LITERAL OBJECT_NUM
    |   ERROR
        -> OP_PUSH_LITERAL ERROR
    ;

list_literal
    :   LIST
        -> OP_MAKE_EMPTY_LIST
    |   ^(LIST list_head list_tail*)
        -> list_head list_tail?
    ;

list_head
    :   expression
        -> expression OP_MAKE_SINGLETON_LIST
    |   list_splice
        -> list_splice OP_CHECK_LIST_FOR_SPLICE
    ;

list_tail
    :   expression
        -> expression OP_LIST_ADD_TAIL
    |   list_splice
        -> list_splice OP_LIST_APPEND
    ;

list_splice
    :   ^('@' expression)
        -> expression
    ;
