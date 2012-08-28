tree grammar compiler;

options {
    language=Python;
    tokenVocab=lang;
    ASTLabelType=CommonTree;
    output=AST;
}

tokens {
    LABEL;
    OP_CHECK_LIST_FOR_SPLICE;
    OP_DONE;
    OP_EIF;
    OP_IF;
    OP_JUMP;
    OP_LIST_ADD_TAIL;
    OP_LIST_APPEND;
    OP_MAKE_EMPTY_LIST;
    OP_MAKE_SINGLETON_LIST;
    OP_NUM;
    OP_POP;
    OP_PUSH;
    OP_PUSH_LITERAL;
    OP_RETURN0;
    OP_RETURN;
    OP_WHILE;
    OP_WHILE_ID;
}


program 
    :   ^(PROGRAM statement*)
        -> statement* OP_DONE
    ;

statement
    :   simple_statement
    |   return_statement
    |   if_statement
    |   while_statement
    ;

simple_statement
    :   ^(STATEMENT expression)
        -> expression OP_POP
    ;

return_statement
    :   RETURN
        -> OP_RETURN0
    |   ^(RETURN expression)
        -> expression OP_RETURN
    ;

if_statement
    :   ^(ENDIF if_sequence[$ENDIF, $ENDIF])
        -> if_sequence LABEL[str($ENDIF.token.index)]
    ;

// Un-reverse reversed IF sequences, stitching together jump targets as we go.
if_sequence[next_node, exit_node]
    :   elseif_sequence[$next_node, $exit_node]
    |   else_part elseif_sequence[$else_part.start, $exit_node]
        ->  elseif_sequence
            ^(LABEL[str($else_part.start.token.index)])
            else_part
    ;

elseif_sequence[next_node, exit_node]
    :   if_part[next_node, exit_node]
    |   elseif_part[next_node, exit_node] elseif_sequence[$elseif_part.start, $exit_node]
        ->  elseif_sequence
            ^(LABEL[str($elseif_part.start.token.index)])
            elseif_part
    ;

if_part[next_node, exit_node]
    :   ^(IF expression statement*)
        ->  expression
            ^(OP_IF LABEL[str(next_node.token.index)])
            statement*
            ^(OP_JUMP LABEL[str(exit_node.token.index)])
    ;

elseif_part[next_node, exit_node]
    :   ^(ELSEIF expression statement*)
        ->  expression
            ^(OP_EIF LABEL[str(next_node.token.index)])
            statement*
            ^(OP_JUMP LABEL[str(exit_node.token.index)])
    ;

// Tree rewriting seems to hate empty output trees in certain contexts,
// including this one. To permit empty ELSE statements, include a dummy label
// node (which will be coalesced and discarded during bytecode emission) in
// the resulting subtree.
else_part
    :   ^(ELSE statement*)
        -> LABEL[str($ELSE.token.index)] statement*
    ;

while_statement
    :   ^(ENDWHILE expression statement*)
        ->  LABEL[str($expression.start.token.index)]
            expression
            ^(OP_WHILE LABEL[str($ENDWHILE.token.index)])
            statement*
            ^(OP_JUMP LABEL[str($expression.start.token.index)])
            LABEL[str($ENDWHILE.token.index)]
    |   ^(ENDWHILE ^(LOOP_TAG IDENTIFIER) expression statement*)
        ->  LABEL[str($expression.start.token.index)]
            expression
            LABEL[str($expression.start.token.index)]
            ^(OP_WHILE_ID IDENTIFIER LABEL[str($ENDWHILE.token.index)])
            statement*
            ^(OP_JUMP LABEL[str($expression.start.token.index)])
            LABEL[str($ENDWHILE.token.index)]
    ;

expression
    :   root_expression
    ;

root_expression
    :   literal
    |   IDENTIFIER
        -> ^(OP_PUSH IDENTIFIER)
    ;

literal
    :   immediate_value
    |   list_literal
    ;

immediate_value
    :   INT
        -> ^(OP_NUM INT)
    |   FLOAT
        -> ^(OP_PUSH_LITERAL FLOAT)
    |   STRING
        -> ^(OP_PUSH_LITERAL STRING)
    |   OBJECT_NUM
        -> ^(OP_PUSH_LITERAL OBJECT_NUM)
    |   ERROR
        -> ^(OP_PUSH_LITERAL ERROR)
    ;

list_literal
    :   LIST
        -> OP_MAKE_EMPTY_LIST
    |   ^(LIST list_head list_tail*)
        -> list_head list_tail*
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
