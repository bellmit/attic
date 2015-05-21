# Transactor [![Build Status](https://circleci.com/gh/login-box/transactor.svg)](https://circleci.com/gh/login-box/transactor)

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
