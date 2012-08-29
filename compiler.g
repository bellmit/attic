tree grammar compiler;

options {
    language=Python;
    tokenVocab=lang;
    ASTLabelType=CommonTree;
    output=AST;
}

tokens {
    FORK_VECTOR;
    LABEL;
    OP_CHECK_LIST_FOR_SPLICE;
    OP_DONE;
    OP_EIF;
    OP_FOR_LIST;
    OP_FOR_RANGE;
    OP_FORK;
    OP_FORK_WITH_ID;
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
    |   for_statement
    |   fork_statement
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

for_statement
    :   for_list_statement
    |   for_range_statement
    ;

for_list_statement
    :   ^(ENDFOR ^(LOOP_TAG IDENTIFIER) expression statement*)
        ->  expression
            ^(OP_NUM INT['1'])
            LABEL[str($expression.start.token.index)]
            ^(OP_FOR_LIST IDENTIFIER LABEL[str($ENDFOR.token.index)])
            statement*
            ^(OP_JUMP LABEL[str($expression.start.token.index)])
            LABEL[str($ENDFOR.token.index)]
    ;

for_range_statement
    :   ^(ENDFOR
            ^(LOOP_TAG IDENTIFIER)
            ^(RANGE_START start=expression end=expression)
            statement*
        )
        ->  $start
            $end
            LABEL[str($RANGE_START.token.index)]
            ^(OP_FOR_RANGE IDENTIFIER LABEL[str($ENDFOR.token.index)])
            statement*
            ^(OP_JUMP LABEL[str($RANGE_START.token.index)])
            LABEL[str($ENDFOR.token.index)]
    ;

fork_statement
    :   ^(FORK expression statement*)
        ->  expression
            ^(OP_FORK FORK_VECTOR[str($FORK.token.index)])
            ^(FORK_VECTOR[str($FORK.token.index)] statement*)
    |   ^(FORK ^(LOOP_TAG IDENTIFIER) expression statement*)
        ->  expression
            ^(OP_FORK_WITH_ID FORK_VECTOR[str($FORK.token.index)] IDENTIFIER)
            ^(FORK_VECTOR[str($FORK.token.index)] statement*)
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
    :   LIST_START
        -> OP_MAKE_EMPTY_LIST
    |   ^(LIST_START list_head list_tail*)
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
