# The MOO Bytecode Interpreter

## Bytecode vectors

Each program contains one or more bytecode vectors. A bytecode vector is an
encoded sequence of instructions, stored as a sequence of bytes (eight-bit
unsigned numbers). In this document, bytecode sequences are represented in
hexadecimal, unless otherwise noted.

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

## Forks

Every program contains a "main" vector, containing the program to run when a
verb is invoked (either directly or by another verb). Programs can also
contain a sequence of "fork" vectors, containing alternate programs associated
with MOO-language `fork` statements. Various instructions allow programs to
manipulate fork vectors and execution.

Fork identifiers are usually immediate values, encoded as above.

## Labels

Labels are addresses used for jumps and references to other locations in the
code stream. Each label encodes a number: the label encoding 0 represents the
start of the bytecode vector, the label encoding 1 represents the next byte,
and so on.

Labels are immediate values, encoded as above.

## Local variables

Each program has a table of local variables, initialized with known values for
each execution before evaluating instructions. Local variables are initially
unset, otherwise.

Local variables are identified using immediate values, encoded as above.

## Equivalent instructions

The MOO runtime requires that bytecode programs be deterministically
convertable back into MOO-language source code. To aid this, the bytecode
language frequently provides several opcodes with identical semantics: for
example, the MOO-language 'if', 'elseif', and 'while' statements all emit
conditional jump opcodes; emitting distinct opcodes for each kind of statement
allows the decompiler to reliably determine which statement to emit.

## Opcodes

<table>
<thead>
    <tr>
        <th>Encoding</th>
        <th>Name</th>
        <th>Ticks</th>
        <th>Summary</th>
    </tr>
</thead>
<tbody>
    <tr>
        <th colspan="4">Control Flow</th>
    </tr>
    <tr>
        <td><kbd>00 <var>label</var></kbd></td>
        <td><kbd>IF</kbd></td>
        <td>1</td>
        <td>POP(); if false, execution continues at <var>label</var> instead
            of next instruction. Represents <kbd>if</kbd> constructs.</td>
    </tr>
    <tr>
        <td><kbd>01 <var>label</var></kbd></td>
        <td><kbd>WHILE</kbd></td>
        <td>1</td>
        <td>Identical to <kbd>IF</kbd>; represents <kbd>while</kbd>
            constructs.</td>
    </tr>
    <tr>
        <td><kbd>02 <var>label</var></kbd></td>
        <td><kbd>EIF</kbd></td>
        <td>1</td>
        <td>Identical to <kbd>IF</kbd>; represents <kbd>elseif</kbd>
            constructs.</td>
    </tr>
    <tr>
        <td><kbd>03 <var>fork</var></kbd></td>
        <td><kbd>FORK</kbd></td>
        <td>1</td>
        <td><var>delay</var> = POP(); <var>delay</var> must be an integer
            (<kbd>E_TYPE</kbd>); <var>delay</var> must be >= <kbd>0</kbd>
            (<kbd>E_INVARG</kbd>). Schedules a new task to start after
            <var>delay</var> seconds using code from fork vector
            <var>fork</var>.</td>
    </tr>
    <tr>
        <td><kbd>04 <var>fork</var> <var>var</var></kbd></td>
        <td><kbd>FORK_WITH_ID</kbd></td>
        <td>1</td>
        <td>As with <kbd>FORK</kbd>. The new process task's <var>taskid</var>
            is stored in <var>var</var>.</td>
    </tr>
    <tr>
        <td><kbd>05 <var>var</var> <var>label</var></kbd></td>
        <td><kbd>FOR_LIST</kbd></td>
        <td>1</td>
        <td><var>index</var> = POP(), <var>list</var> = POP(), if
            <var>index</var> is a valid index in <var>list</var> then
            <kbd><var>list</var>[<var>index</var>]</kbd> is stored in
            <var>var</var>, <var>list</var> is pushed back onto the stack, and
            <kbd><var>index</var> + 1</kbd> is pushed onto the stack.
            Otherwise, execution continues at <var>label</var> instead of the
            next instruction.</td>
    </tr>
    <tr>
        <td><kbd>06 <var>var</var> <var>label</var></kbd></td>
        <td><kbd>FOR_RANGE</kbd></td>
        <td>1</td>
        <td><var>upper</var> = POP(), <var>lower</var> = POP(), if
            <var>lower</var> is no greater than <var>upper</var> then
            <var>lower</var> is stored in <var>var</var>,
            <kbd><var>lower</var> + 1</kbd> is pushed onto the stack, and
            <var>upper</var> is pushed back onto the stack. Otherwise,
            execution continues at <var>label</var> instead of the next
            instruction.</td>
    </tr>
</tbody>
</table>
