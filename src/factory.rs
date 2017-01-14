//! Defines the Factory trait and default implementations.
//!
//! Redux uses factories internally to implement various kinds of implicit state
//! creation.

/// Allows store clients to construct the initial state using a callback, rather
/// than creating it prior to creating the store.
///
/// # Examples
///
/// Any kind of `FnOnce() -> S` function is usable as a Factory for S:
///
/// ```
/// // Sample factory client (just calls it)
/// fn from_factory<F>(f: F) -> i32
///     where F: redux::factory::Factory<i32> {
///     f.create()
/// }
///
/// assert_eq!(from_factory(|| 0), 0);
///
/// let count = 5;
/// assert_eq!(from_factory(move || count), 5);
///
/// let mut count = 0;
/// assert_eq!(from_factory(|| { count = count + 1; count }), 1);
/// ```
pub trait Factory<S> {
    /// Create an instance of `S` from this factory.
    fn create(self) -> S;
}

impl<S, F: FnOnce() -> S> Factory<S> for F {
    /// Create an `S` by calling the underlying function, once.
    fn create(self) -> S {
        self()
    }
}
