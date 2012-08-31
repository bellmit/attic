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
    OP_CONTINUE;
    OP_DONE;
    OP_EIF;
    OP_END_EXCEPT;
    OP_END_FINALLY;
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
    OP_PUSH_LABEL;
    OP_PUSH_LITERAL;
    OP_PUT;
    OP_RETURN0;
    OP_RETURN;
    OP_TRY_EXCEPT;
    OP_TRY_FINALLY;
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
    |   try_statement
    |   try_finally_statement
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

try_statement
scope {
    excepts; // presuppose the first one, since it's required.
}
    :   ^(ENDTRY
            ^(first_except=EXCEPT
                except_conditions[$first_except]
                first_except_handler[$first_except]
            ) { excepts = 1; }
            (
                ^(next_except=EXCEPT
                    except_conditions[$next_except]
                    except_handler[$next_except, $ENDTRY]
                ) { excepts += 1; }
            )*
            statement*
        )
        ->  except_conditions+
            ^(OP_TRY_EXCEPT INT[str(excepts)])
            statement*
            ^(OP_END_EXCEPT LABEL[str($ENDTRY.token.index)])
            first_except_handler
            except_handler*
            LABEL[str($ENDTRY.token.index)]
    ;

except_conditions[except_node]
    :   expression
        ->  expression
            ^(OP_PUSH_LABEL LABEL[str($except_node.token.index)])
    ;

/*
 * The emitted bytecode for all but the final except handler ends with an
 * unconditional jump out of the try/except chain:
 *
 *  ; block for except1
 *  handler1: POP
 *            <statements1>
 *            JUMP done
 *  ; end of except1
 *  ; block for except2
 *  handler2: POP
 *            <statemments2>
 *            JUMP done
 *  ; end of except2
 *  ; block for except3
 *  handler3: POP
 *            <statemments3>
 *  ; end of except3
 *  done:     <rest of program
 *
 * However, because the unconditional jumps are all identical, you can look at
 * this as generating the *first* except block without a *leading* jump:
 *
 *  ; block for except1
 *  handler1: POP
 *            <statements1>
 *  ; end of except1
 *  ; block for except2
 *            JUMP done
 *  handler2: POP
 *            <statemments2>
 *  ; end of except2
 *  ; block for except3
 *            JUMP done
 *  handler3: POP
 *            <statemments3>
 *  ; end of except3
 *  done:     <rest of program
 * 
 * The following rules apply this stunt to the parse tree for an except block.
 */

first_except_handler[except_node]
    :   statement*
        ->  LABEL[str($except_node.token.index)]
            OP_POP
            statement*
    |   ^(LOOP_TAG IDENTIFIER) statement*
        ->  // No jump here.
            LABEL[str($except_node.token.index)]
            ^(OP_PUT IDENTIFIER)
            OP_POP
            statement*
    ;

except_handler[except_node, exit_node]
    :   statement*
        ->  ^(OP_JUMP LABEL[str($exit_node.token.index)])
            LABEL[str($except_node.token.index)]
            OP_POP
            statement*
    |   ^(LOOP_TAG IDENTIFIER) statement*
        ->  ^(OP_JUMP LABEL[str($exit_node.token.index)])
            LABEL[str($except_node.token.index)]
            ^(OP_PUT IDENTIFIER)
            OP_POP
            statement*
    ;

try_finally_statement
    :   ^(ENDTRY ^(FINALLY finally_body=statement*) try_body=statement*)
        ->  ^(OP_TRY_FINALLY LABEL[str($ENDTRY.token.index)])
            $try_body*
            OP_END_FINALLY
            LABEL[str($ENDTRY.token.index)]
            $finally_body*
            OP_CONTINUE
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
    |   ANY
        -> ^(OP_PUSH_LITERAL ANY)
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
