# The MOO Bytecode Interpreter

## Bytecode vectors

Each program contains one or more bytecode vectors. A bytecode vector is an
encoded sequence of instructions, stored as a sequence of bytes (eight-bit
unsigned numbers). In this document, bytecode sequences are represented in
hexadecimal, unless otherwise noted.

## Temporary Values

The runtime has a single `temp` register capable of holding any MOO value. The
`OP_PUT_TEMP` and `OP_PUSH_TEMP` instructions manipulate this value.

## Variable-length immediate values

Several kinds of immediate value in the MOO bytecode language have variable
lengths. The length of each kind of immediate value is stored with the program
and applies to all values of that type appearing in the program. Each
immediate value is an unsigned integer, encoded using the host system's native
byte ordering (little-endian, on x86). The length of the encoding is
controlled by the largest value of a given kind that could possibly appear:

* Up to 256: all values of this kind are one byte long.
* Up to 65536: all values of this kind are two bytes long.
* Larger: all values of this kind are four bytes long.

### Forks

Every program contains a "main" vector, containing the program to run when a
verb is invoked (either directly or by another verb). Programs can also
contain a sequence of "fork" vectors, containing alternate programs associated
with MOO-language `fork` statements. Various instructions allow programs to
manipulate fork vectors and execution.

Fork identifiers are usually immediate values, encoded as above.

### Labels

Labels are addresses used for jumps and references to other locations in the
code stream. Each label encodes a number: the label encoding 0 represents the
start of the bytecode vector, the label encoding 1 represents the next byte,
and so on.

Labels are immediate values, encoded as above.

### Local variables

Each program has a table of local variables, initialized with known values for
each execution before evaluating instructions. Local variables are initially
unset, otherwise.

Local variables are identified using immediate values, encoded as above.

### Literals

Each program has a table of literal values used in the program, initialized
by the compiler.

Literals are identified using immediate values, encoded as above.

## Built-in Functions

Built-in functions are stored in a table with a one-byte (unsigned) index. The
`BI_FUNC_CALL` instruction requires a function ID (<var>funcid</var>)
immediate operand.

## Equivalent instructions

The MOO runtime requires that bytecode programs be deterministically
convertable back into MOO-language source code. To aid this, the bytecode
language frequently provides several opcodes with identical semantics: for
example, the MOO-language 'if', 'elseif', and 'while' statements all emit
conditional jump opcodes; emitting distinct opcodes for each kind of statement
allows the decompiler to reliably determine which statement to emit.

## Implementation leaks

Several instructions (notably `REF` and `RANGE_REF`) were implemented using a
copy-on-write/reference-counting implementation in the original LambdaMOO, and
this has leaked into the opcode names. The reference-ness of the results of
these expressions is not visible in the semantics of the operations.

## "Short" instructions

Variable store and load instructions have 32 "short" versions (`PUT_0` through
`PUT_31` and `PUSH_0` through `PUSH_31` respectively). These have the same
semantics as the `PUT` and `PUSH` instructions, but bake the immediate value
into the instruction byte.

## `BYTECODE_REDUCE_REF`

I've left out the opcodes introduced by LambdaMOO's `BYTECODE_REDUCE_REF`
build configuration. Operations from `IMM` onwards have different numbers if
this build option is enabled, but correctly emitting these instructions during
compilation is problematic.

These instructions had the same structure and semantics as `PUSH_n` and
`PUSH`, but also unset the referenced variable to free up some memory
(especially relevant in the case of large or complex lists and long strings).

## Opcodes

<table>
<thead>
    <tr>
        <th>Encoding</th>
        <th>Name</th>
        <th>Stack Before</th>
        <th>Stack After</th>
        <th>Ticks</th>
        <th>Summary</th>
    </tr>
</thead>
<tbody>
    <tr>
        <th colspan="6">Control Flow (Blocks)</th>
    </tr>
    <tr>
        <td><kbd>00 <var>label</var></kbd></td>
        <td><kbd>IF</kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            …
        </td>
        <td>1</td>
        <td>If <var>value</var> is false, execution continues at <var>label</var>
            instead of next instruction. Represents <kbd>if</kbd> constructs.
        </td>
    </tr>
    <tr>
        <td><kbd>01 <var>label</var></kbd></td>
        <td><kbd>WHILE</kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            …
        </td>
        <td>1</td>
        <td>Identical to <kbd>IF</kbd>; represents <kbd>while</kbd>
            constructs.</td>
    </tr>
    <tr>
        <td><kbd>02 <var>label</var></kbd></td>
        <td><kbd>EIF</kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            …
        </td>
        <td>1</td>
        <td>Identical to <kbd>IF</kbd>; represents <kbd>elseif</kbd>
            constructs.</td>
    </tr>
    <tr>
        <td><kbd>03 <var>fork</var></kbd></td>
        <td><kbd>FORK</kbd></td>
        <td>
            <var>delay</var><br>
            …
        </td>
        <td>
            <p>On original task:<br>
                …
            </p>
            <p>Empty on new task.</p>
        </td>
        <td>1</td>
        <td>Schedules a new task to start after <var>delay</var> seconds using
            code from fork vector <var>fork</var>.
        </td>
    </tr>
    <tr>
        <td><kbd>04 <var>fork</var> <var>var</var></kbd></td>
        <td><kbd>FORK_WITH_ID</kbd></td>
        <td>
            <var>delay</var><br>
            …
        </td>
        <td>
            <p>On original task:<br>
                …
            </p>
            <p>Empty on new task.</p>
        </td>
        <td>1</td>
        <td>As with <kbd>FORK</kbd>. The new process task's <var>taskid</var>
            is stored in <var>var</var>.</td>
    </tr>
    <tr>
        <td><kbd>05 <var>var</var> <var>label</var></kbd></td>
        <td><kbd>FOR_LIST</kbd></td>
        <td>
            <var>index</var><br>
            <var>list</var><br>
            …
        </td>
        <td>
            <p>Loop continues:<br>
                <var>index</var> + 1<br>
                <var>list</var><br>
                …
            </p>
            <p>Otherwise:<br>
                …
            </p>
        </td>
        <td>1</td>
        <td>if <var>index</var> is a valid index in <var>list</var> then
            <kbd><var>list</var>[<var>index</var>]</kbd> is stored in
            <var>var</var> and the index on the stack is incremented.
            Otherwise, execution continues at <var>label</var> instead of the
            next instruction.
        </td>
    </tr>
    <tr>
        <td><kbd>06 <var>var</var> <var>label</var></kbd></td>
        <td><kbd>FOR_RANGE</kbd></td>
        <td>
            <var>upper</var><br>
            <var>lower</var><br>
            …
        </td>
        <td>
            <p>Loop continues:<br>
                <var>upper</var><br>
                <var>lower</var> + 1<br>
                …
            </p>
            <p>Otherwise:<br>
                …
            </p>
        </td>
        <td>1</td>
        <td>If <var>lower</var> is no greater than <var>upper</var> then
            <var>lower</var> is stored in <var>var</var>, and the lower bound
            on the stack is updated as described. Otherwise, execution
            continues at <var>label</var> instead of the next instruction.
        </td>
    </tr>
</tbody>
<tbody>
    <tr>
        <th colspan="6">Elemental Expressions</th>
    </tr>
    <tr>
        <td><kbd>07</kbd></td>
        <td><kbd>INDEXSET</kbd></td>
        <td>
            <var>value</var><br>
            <var>index</var><br>
            <var>list</var><br>
            …
        </td>
        <td>
            <var>list</var> (modified)<br>
            …
        </td>
        <td>1</td>
        <td>Stores <var>value</var> in <var>list</var> at <var>index</var>.
            Pushes the modified value of <var>list</var> back onto the stack.
        </td>
    </tr>
    <tr>
        <td><kbd>08</kbd></td>
        <td><kbd>PUSH_GET_PROP</kbd></td>
        <td>
            <var>name</var><br>
            <var>obj</var><br>
            …
        </td>
        <td>
            (<var>obj</var>.<var>name</var>)<br>
            <var>name</var><br>
            <var>obj</var><br>
            …
        </td>
        <td>1</td>
        <td>Pushes <kbd><var>obj</var>.<var>name</var></kbd> onto the
            stack. (<var>name</var> and <var>obj</var> will be left on the
            stack.)
        </td>
    </tr>
    <tr>
        <td><kbd>09</kbd></td>
        <td><kbd>GET_PROP</kbd></td>
        <td>
            <var>name</var><br>
            <var>obj</var><br>
            …
        </td>
        <td>
            (<var>obj</var>.<var>name</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Pushes <kbd><var>object</var>.<var>name</var></kbd> onto the stack.
        </td>
    </tr>
    <tr>
        <td><kbd>0A</kbd></td>
        <td><kbd>CALL_VERB</kbd></td>
        <td>
            <var>args</var><br>
            <var>name</var><br>
            <var>obj</var><br>
            …
        </td>
        <td>
            Returned value<br>
            …
        </td>
        <td>1</td>
        <td>Invokes <kbd><var>obj</var>:<var>verb</var>(@<var>args)</kbd> and
            pushes its return value onto the stack.
        </td>
    </tr>
    <tr>
        <td><kbd>0B</kbd></td>
        <td><kbd>PUT_PROP</kbd></td>
        <td>
            <var>value</var><br>
            <var>name</var><br>
            <var>obj</var><br>
            …
        </td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>1</td>
        <td>Stores <var>value</var> in <kbd><var>obj</var>.<var>name</var></kbd>.
            Pushes <var>value</var> back onto the stack.
        </td>
    </tr>
    <tr>
        <td><kbd>0C <var>funcid</var></kbd></td>
        <td><kbd>BI_FUNC_CALL</kbd></td>
        <td>
            <var>args</var><br>
            …
        </td>
        <td>
            Returned value<br>
            …
        </td>
        <td>1</td>
        <td>Calls built-in function <kbd><var>funcid</var>(<var>args</var>)</kbd>
            and pushes the return value onto the stack.
        </td>
    </tr>
    <tr>
        <td><kbd>0D <var>label</var></kbd></td>
        <td><kbd>IF_QUES</kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            …
        </td>
        <td>1</td>
        <td>Identical to <kbd>IF</kbd>; represents <kbd>cond ? expr1 |
            expr2</kbd> constructs.
        </td>
    </tr>
    <tr>
        <td><kbd>0E</kbd></td>
        <td><kbd>REF</kbd></td>
        <td>
            <var>index</var><br>
            <var>list</var><br>
            …
        </td>
        <td>
            (<var>list</var>[<var>index</var>])<br>
            …
        </td>
        <td>1</td>
        <td>Pushes <kbd><var>list</var>[<var>index</var>]</kbd> onto the stack.
        </td>
    </tr>
    <tr>
        <td><kbd>0F</kbd></td>
        <td><kbd>RANGE_REF</kbd></td>
        <td>
            <var>end</var><br>
            <var>start</var><br>
            <var>list</var><br>
            …
        </td>
        <td>
            (<var>list</var>[<var>start</var>..<var>end</var>])<br>
            …
        </td>
        <td>1</td>
        <td>Pushes
            <kbd><var>list</var>[<var>start</var>..<var>end</var>]</kbd> onto
            the stack.
        </td>
    </tr>
</tbody>
<tbody>
    <tr>
        <th colspan="6">List Creation</th>
    </tr>
    <tr>
        <td><kbd>10</kbd></td>
        <td><kbd>MAKE_SINGLETON_LIST</kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            {<var>value</var>}<br>
            …
        </td>
        <td>1</td>
        <td>Replaces <var>value</var> with a list whose only element is
            <var>value</var>.
        </td>
    </tr>
    <tr>
        <td><kbd>11</kbd></td>
        <td><kbd>CHECK_LIST_FOR_SPLICE</kbd></td>
        <td>
            <var>list</var><br>
            …
        </td>
        <td>
            <var>list</var><br>
            …
        </td>
        <td>1</td>
        <td>Raises <kbd>E_TYPE</kbd> if the top of the stack is not a list.
        </td>
    </tr>
</tbody>
<tbody>
    <tr>
        <th colspan="6">Arithmetic</th>
    </tr>
    <tr>
        <td><kbd>12</kbd></td>
        <td><kbd>MULT</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> * <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Multiplies the top two stack entries.</td>
    </tr>
    <tr>
        <td><kbd>13</kbd></td>
        <td><kbd>DIV</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> / <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Divides the top two stack entries.</td>
    </tr>
    <tr>
        <td><kbd>14</kbd></td>
        <td><kbd>MOD</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> % <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Takes remainder of <kbd><var>a</var> / <var>b</var></kbd>.</td>
    </tr>
    <tr>
        <td><kbd>15</kbd></td>
        <td><kbd>ADD</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> + <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Adds the top two stack entries.</td>
    </tr>
    <tr>
        <td><kbd>15</kbd></td>
        <td><kbd>MINUS</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> - <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Takes the difference of the top two stack entries.</td>
    </tr>
</tbody>
<tbody>
    <tr>
        <th colspan="6">Comparisons</th>
    </tr>
    <tr>
        <td><kbd>16</kbd></td>
        <td><kbd>EQ</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> == <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Compares the top two stack entries for equality.</td>
    </tr>
    <tr>
        <td><kbd>17</kbd></td>
        <td><kbd>NE</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> != <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Compares the top two stack entries for inequality.</td>
    </tr>
    <tr>
        <td><kbd>18</kbd></td>
        <td><kbd>LT</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> &lt; <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Compares the top two stack entries for ordering.</td>
    </tr>
    <tr>
        <td><kbd>19</kbd></td>
        <td><kbd>LE</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> &lt;= <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Compares the top two stack entries for ordering.</td>
    </tr>
    <tr>
        <td><kbd>1A</kbd></td>
        <td><kbd>GT</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> &gt; <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Compares the top two stack entries for ordering.</td>
    </tr>
    <tr>
        <td><kbd>1B</kbd></td>
        <td><kbd>GE</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> &gt;= <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Compare the top two stack entries for ordering.</td>
    </tr>
    <tr>
        <td><kbd>1C</kbd></td>
        <td><kbd>IN</kbd></td>
        <td>
            <var>value</var><br>
            <var>list</var><br>
            …
        </td>
        <td>
            <var>index</var><br>
            …
        </td>
        <td>1</td>
        <td>Pushes first index of <var>value</var> in <var>list</var>, or 0 if
            <var>value</var> is not in <var>list</var>.
        </td>
    </tr>
</tbody>
<tbody>
    <tr>
        <th colspan="6">Boolean Logic</th>
    </tr>
    <tr>
        <td><kbd>1D</kbd></td>
        <td><kbd>AND</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> AND <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Boolean-AND of the top two stack entries.</td>
    </tr>
    <tr>
        <td><kbd>1E</kbd></td>
        <td><kbd>OR</kbd></td>
        <td>
            <var>b</var><br>
            <var>a</var><br>
            …
        </td>
        <td>
            (<var>a</var> OR <var>b</var>)<br>
            …
        </td>
        <td>1</td>
        <td>Boolean-OR of the top two stack entries.</td>
    </tr>
</tbody>
<tbody>
    <tr>
        <th colspan="6">Unary Operators</th>
    </tr>
    <tr>
        <td><kbd>1F</kbd></td>
        <td><kbd>UNARY_MINUS</kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            -<var>value</var><br>
            …
        </td>
        <td>1</td>
        <td>Negates top of stack.</td>
    </tr>
    <tr>
        <td><kbd>20</kbd></td>
        <td><kbd>NOT</kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            !<var>value</var><br>
            …
        </td>
        <td>1</td>
        <td>Boolean-NOT of top of stack.</td>
    </tr>
</tbody>
<tbody>
    <tr>
        <th colspan="6">Variables</th>
    </tr>
    <tr>
        <td><kbd>21</kbd><br>
            <kbd>22</kbd><br>
            …<br>
            <kbd>21 + <var>var</var></kbd><br>
            …<br>
            <kbd>40</kbd><br>
        </td>
        <td><kbd>PUT_<var>var</var></kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>1</td>
        <td>Stores top of stack in <var>var</var> (0 &lt;= <var>var</var>
            &lt; 32).</td>
    </tr>
    <tr>
        <td><kbd>41 <var>var</var></kbd></td>
        <td><kbd>PUT</kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>1</td>
        <td>Stores top of stack in <var>var</var>.</td>
    </tr>
    <tr>
        <td><kbd>42</kbd><br>
            <kbd>43</kbd><br>
            …<br>
            <kbd>42 + <var>var</var></kbd><br>
            …<br>
            <kbd>61</kbd><br>
        </td>
        <td><kbd>PUSH_<var>var</var></kbd></td>
        <td>
            …
        </td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>0</td>
        <td>Pushes value of <var>var</var> (0 &lt;= <var>var</var>
            &lt; 32) onto the stack.</td>
    </tr>
    <tr>
        <td><kbd>62</kbd></td>
        <td><kbd>PUSH</kbd></td>
        <td>
            …
        </td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>0</td>
        <td>Pushes value of <var>var</var> onto the stack.</td>
    </tr>
</tbody>
<tbody>
    <tr>
        <th colspan="6">Immediate Values</th>
    </tr>
    <tr>
        <td><kbd>63 <var>literal</var></kbd></td>
        <td><kbd>IMM</kbd></td>
        <td>
            …
        </td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>0</td>
        <td>Pushes value of <var>literal</var> onto the stack.</td>
    </tr>
    <tr>
        <td><kbd>64</kbd></td>
        <td><kbd>MAKE_EMPTY_LIST</kbd></td>
        <td>
            …
        </td>
        <td>
            {}<br>
            …
        </td>
        <td>0</td>
        <td>Pushes an empty lsit onto the stack.</td>
    </tr>
    <tr>
        <td><kbd>65</kbd></td>
        <td><kbd>LIST_ADD_TAIL</kbd></td>
        <td>
            <var>value</var><br>
            <var>list</var><br>
            …
        </td>
        <td>
            {@<var>list</var>, <var>value</var>}<br>
            …
        </td>
        <td>0</td>
        <td>Appends <var>value</var> onto the end of <var>list</var>.</td>
    </tr>
    <tr>
        <td><kbd>66</kbd></td>
        <td><kbd>LIST_APPEND</kbd></td>
        <td>
            <var>tail</var><br>
            <var>list</var><br>
            …
        </td>
        <td>
            {@<var>list</var>, @<var>tail</var>}<br>
            …
        </td>
        <td>0</td>
        <td>Concatenates <var>list</var> and <var>tail</var>.</td>
    </tr>
    <tr>
        <td><kbd>67</kbd></td>
        <td><kbd>PUSH_REF</kbd></td>
        <td>
            <var>index</var><br>
            <var>list</var><br>
            …
        </td>
        <td>
            (<var>list</var>[<var>index</var>])</br>
            <var>index</var><br>
            <var>list</var><br>
            …
        </td>
        <td>0</td>
        <td>Pushes <kbd><var>list</var>[<var>index</var>]</kbd> onto the
            stack. (<var>index</var> and <var>list</var> will be left on the
            stack.)
        </td>
    </tr>
    <tr>
        <td><kbd>68</kbd></td>
        <td><kbd>PUT_TEMP</kbd></td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>0</td>
        <td>Stores <var>value</var> in the runtime's <kbd>temp</kbd> register.
        </td>
    </tr>
    <tr>
        <td><kbd>69</kbd></td>
        <td><kbd>PUSH_TEMP</kbd></td>
        <td>
            …
        </td>
        <td>
            <var>value</var><br>
            …
        </td>
        <td>0</td>
        <td>Pushes the <var>value</var> in the runtime's <kbd>temp</kbd>
            register onto the stack.
        </td>
    </tr>
</tbody>
</table>
