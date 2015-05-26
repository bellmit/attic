# Transactor [![Build Status](https://circleci.com/gh/login-box/transactor.svg)](https://circleci.com/gh/login-box/transactor) [ ![Download](https://api.bintray.com/packages/login-box/releases/transactor/images/download.svg) ](https://bintray.com/login-box/releases/transactor/_latestVersion)

This library provides a quasi-monadic interface to transactable resources. This
is very much unlike traditional Java transaction management; instead of binding
a transaction context to the thread through out-of-band magic, the transaction
context is explicit, and managed by a `Transactor` object which evaluates
transactable callbacks.

The interface is loosely inspired by a number of sources, including Haskell's
`IO`, `Reader`, and `Writer` monads, Prevayler's API (but not its internals),
Axon Framework events, and RxJava.

This is appropriate when you need close control of transaction boundaries,
independent of the underlying framework. Traditional Java dynamically-scoped
transaction enlistment and demarcation gives applications poor visibility into
the state of a transaction, and sharply limits the guarantees applications can
provide in response to underlying transaction failures.

## Installation

This library can be obtained from JCenter. In Gradle:

```
repositories {
    jcenter()
}

// …

dependencies {
    compile 'com.loginbox.transactor:transactor:+'
}
```

In Maven:

```
<repositories>
    <repository>
        <url>http://jcenter.bintray.com/</url>
    </repository>
</repositories>
<!-- ... -->
<dependencies>
    <dependency>
        <groupId>com.loginbox.transactor</groupId>
        <artifactId>transactor</artifactId>
        <version>${transactor.version}</version>
    </dependency>
</dependencies>
```

You can also download this library
[directly](https://bintray.com/login-box/releases/transactor/_latestVersion)
and use it; it has no dependencies.

## Operating Theory

We can divide the parts of a computer program into two groups of behaviours:
"pure" computational steps, devoid of meaningful side-effects, and "actions",
which apply side effects to the outside world. The side effects of an action
can be anything from reading or writing to a file to complex network
interactions with an RDBMS or message broker system.

Pure computation is sequence-free: side-effect-free operations can be done in
any order, any number of times, and will always yield the same results for the
same initial conditions. Actions, however, are ordered by time, which imposes a
partial order on the program as a whole: a computation that requires data that
will be obtained by an action cannot be sequenced before that action, and a
subsequent action that requires the result of the computation cannot be
sequenced before the computation step.

A transaction, then, is a sequence of actions that all apply side effects to
the same outside context.

This library provides tools for structuring parts of your program along the
lines described above: it provides conventions for packaging side-effect-ful
actions up as values, and a system for applying those actions to transaction
contexts that supports passing data into, out of, or through actions to and
from the rest of your program.

## Contexts

A `Transactor<C>` manages contexts (objects of type `C`, which must implement
`AutoCloseable`) for transactions, opening and closing them around each unit of
work. Code using this library must subclass `Transactor` to specialize it for a
specific kind of transaction. Subclasses provide three methods:

* `C createContext()` opens new transaction contexts. For example, a JDBC
    transactor might implement this as

        @Override
        public Connection createContext() throws SQLException {
            return dataSource.createConnection();
        }

* `void finish(C context)` handles finishing a successful transaction. For
    example, a JDBC transactor might implement this as

        @Override
        public void finish(Connection context) throws SQLException {
            context.commit();
        }

* `void abort(C context)` handles finishing a failed transaction (which has 
    raised an exception). For example, a JDBC transactor might implement this
    as

        @Override
        public void abort(Connection context) throws SQLException {
            context.rollback();
        }

Given these three methods, and an `AutoCloseable` context type, `Transactor`
can manage units of work around "transactable" objects.

## Transactables

The transactable interfaces are functional interfaces encapsulating some work
to be done in the context of a transaction. Each receives the context as a
parameter, and there are multiple kinds of transactable depending on the number
of additional parameters and the return type.

There are two kinds of void transactables, which return nothing:

* An `Action` is the simplest possible transactable: it accepts only a context,
    and returns nothing. All of its side effects should be against the context.
    They are evaluated by `transactor.execute(action)`.

* A `Sink` is a transactable that accepts a context and a single value, and
    returns nothing. They are evaluated by `transactor.consume(sink, value)`.

There are three kinds of non-void transactables, which return values:

* A `Query` is a transactable that accepts only a context, and returns a value.
    They are evaluated by `result = transactor.fetch(query)`.

* A `Transform` is a transactable that accepts a context and a single value, and
    returns a new value. They are evaluated by
    `result = transactor.apply(transform, value)`.

* A `Merge` is a transactable that accepts a context and a pair of values, and
    returns a new value. They are evaluated by
    `result = transactor.combine(mutator, valueOne, valueTwo)`.

## Exceptions

Transactables can fail by raising any exception. As described above, this will
cause the transactor to run the `abort` method, passing the context, before
closing the context. Exceptions arising during cleanup of a failed transactable
will be suppressed, just as if they'd been thrown in the context's `close()`
method.

If cleanup (either `finish(context)` or `context.close()`) fails, that
exception will be reported to the transactor's caller, instead of the normal
result of the passed operation. Transactors make no effort to determine whether
a failure during cleanup has actually reversed the transaction's side effects,
and will not call `abort(context)` once `finish(context)` has been called.

## Composition Operators

Transactables can be composed, so long as the result would accept no more than
two values, and would return no more than one value. The generic sequencing
operators are named depending on the types of their downstream argument:

* `andThen(Action)` appends an Action to any void transactable, producing a new
    void transactable with the same number of inputs, executing the action as
    its last step.

* `before(Query)` appends a Query to any void transactable, producing a new
    non-void transactable with the same number of inputs, executing the query as
    its last step to produce the final output.

* `transformedBy(Transform)` appends a Transform to any non-void transactable,
    producing a new non-void transactable with the same number of inputs,
    executing the transform as its last step to produce the final output.

* `consumedBy(Sink)` appends a Sink to any non-void transactable, producing a
    new void transactable with the same number of inputs, executing the sink as
    its last step. (This cannot be applied to Merges directly; one of the
    `intoLeft` or `intoRight` operators must be applied first, to turn the
    Merge into another kind of non-void transactable.)

Actions provide a unique operator, which appends the action to a transactable
without changing its result:

* `andThen(Action)` appends an Action to any _non-void_ transactable, producing
    a new _non-void_ transactable with the same number of inputs, executing the
    action as its last step. The result of the initial transactable will be
    used as the result of the combined transactable.

This allows actions to be sequenced inline with other transactables.

Merges provide two unique operators, which combine other transactables:

* `intoLeft` appends a Merge to any non-void transactable _other than a Merge_,
    passing the result of the transactable into the left argument of the Merge.

* `intoRight` appends a Merge to any non-void transactable _other than a Merge_,
    passing the result of the transactable into the right argument of the Merge.

These operators allow multiple transactable sequences to be combined pairwise.

### Ordering

All transaction operators force the ordering of their operands to sequence
directly. For example, combining an action with a query using

    Action<Transaction> a = ...;
    Query<Transaction, String> b = ...;
    
    Query<Transaction, String> combined = a.andThen(b);

produces a Query that performs `a`, and then immediately performs `b`. No
further operator will insert steps between `a` and `b`.

For most sequences, this behaviour should be intuitive. However, Merge steps
can be sequenced in multiple ways, and this library provides only limited tools
for controlling that sequence. For example:

    Query<Transaction, String> a = ...;
    Query<Transaction, String> b = ...;
    Merge<Transaction, String, String, String> c = ...;

    Query<Transaction, String> combined =
        a.transformedBy(b.intoLeft(c));

This will perform `a`, then `b`, and then finally `c`, while the following:

    Query<Transaction, String> combined =
        b.transformedBy(a.intoRight(c));

evaluates `b`, then `a`, and then finally `c`.

## Context Adapters

Transactors can be shifted to other contexts. An `Adapter` is a function for
replacing one context with another. Given an Adapter from context Outer to
context Inner, and any transactable in context Inner, you can compose them into
a new transactable in context Outer using

    transactableInOuter = adapter.around(transactableInInner);

If the inner context is `AutoCloseable`, using `ClosingAdapter` will close the
inner context before returning.

## Lifts

Non-transactable functions can be embedded in transactor sequences using
`lift`s for each transactable type. Every `lift` method produces a transactable
which ignores its context and applies the lifted operation to the remaining
arguments. This mechanism allows computation steps to be interleaved with
transacted steps cleanly, without exiting the transaction context.

The following types can be lifted:

* `Runnable` can be lifted to `Action`.
* `Consumer` can be lifted to `Sink`.
* `Supplier` can be lifted to `Query`.
* `Function` can be lifted to `Transform`.
* `BiFunction` can be lifted to `Merge`.

## A Small Example

An example, for basic JDBC. First, we define a transactor. This might be
provided via (and created using) a dependency injection framework, such as
Guice or Spring.

    public class ConnectionTransactor extends Transactor<Connection> {
        private final DataSource dataSource; /* constructor omitted for brevity */
        
        @Override
        protected Connection createContext() throws SQLException {
            return dataSource.getConnection();
        }
        
        @Override
        protected void finish(Connection connection) throws SQLException {
            connection.commit();
        }
        
        @Override
        protected void abort(Connection connection) throws SQLException {
            connection.rollback();
        }
    }

Then a transactable action. Note that the `createUser()` method below _returns
a transactable_ rather than performing a transaction.

    public class UserRepository {
        private ClosingAdapter<Connection, PreparedStatement> preparing(String query) {
            return connection -> connection.prepare(statement);
        }
        
        public Sink<Connection, String> createUser() {
            return preparing("insert into users values (?)")
                .around((statement, username) -> {
                    statement.setString(1, username);
                    statement.execute();
                });
        }
    }

In a larger system, the `preparing` helper could easily be extracted to a
static helper class.

Finally, a client, whose job is to provide the demarcation point for
transactions:

    /* ... */
    transactor.consume(userReposiory.createUser(), "ojacobson");
    /* ... */
