//! Reducers are the fundamental mechanism for applying changes to states.

/// Reducers combine states with actions, producing new states.
///
/// This is a special case of an arbitrary pure function in two arguments, where
/// the return type is usable (with an additional borrow) as the first argument
/// of the next call. Most implementations should _be_ functions - any `Fn(&S,
/// &A) -> S` is usable as a Reducer.
///
/// # Examples
///
/// ```
/// fn add(state: &i32, action: &i32) -> i32 {
///     state + action
/// }
///
/// let reducer: &redux::reducer::Reducer<_, _> = &add;
/// assert_eq!(reducer.reduce(&1, &2), 3);
/// ```
pub trait Reducer<S, A> {
    /// Reduce a borrowed state and action to produce a new result state.
    fn reduce(&self, state: &S, action: &A) -> S;
}

impl<S, A, F: Fn(&S, &A) -> S> Reducer<S, A> for F {
    fn reduce(&self, state: &S, action: &A) -> S {
        self(state, action)
    }
}
