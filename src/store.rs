use super::factory::*;
use super::reducer::*;

/// A container for states of type `S`, which dispatches actions of type `A` and
/// applies them to the contained state.
///
/// Useful stores generally must be mutable.
pub struct Store<'a, S, A> where
    S: 'a,
    A: 'a {
    state: S,
    reducer: &'a Reducer<S, A>,
}

impl<'a, S, A> Store<'a, S, A> {
    /// Make a new store from a fixed state.
    ///
    /// The initial state will be the provided `initial_state`, and actions will be
    /// applied to it through `reducer`.
    ///
    /// # Examples
    ///
    /// ```
    /// fn add(state: &i32, action: &i32) -> i32 {
    ///     state + action
    /// }
    ///
    /// let reducer = &add;
    /// let mut store = redux::Store::from_state(5, reducer);
    /// assert_eq!(store.get_state(), &5);
    ///
    /// store.dispatch(&8);
    /// assert_eq!(store.get_state(), &13);
    /// ```
    pub fn from_state(initial_state: S, reducer: &'a Reducer<S, A>) -> Store<'a, S, A> {
        Store {
            state:   initial_state,
            reducer: reducer,
        }
    }

    /// Make a new store from a state factory.
    ///
    /// The initial state will be taken from `state_factory`, and actions will
    /// be applied to it through `reducer`.
    ///
    /// # Examples
    ///
    /// ```
    /// fn add(state: &i32, action: &i32) -> i32 {
    ///     state + action
    /// }
    ///
    /// let reducer = &add;
    /// let mut store = redux::Store::from_factory(|| 5, reducer);
    /// assert_eq!(store.get_state(), &5);
    ///
    /// store.dispatch(&8);
    /// assert_eq!(store.get_state(), &13);
    /// ```
    pub fn from_factory<F: Factory<S>>(state_factory: F, reducer: &'a Reducer<S, A>) -> Store<'a, S, A>
        where F: FnOnce() -> S {
        let initial_state = state_factory.create();

        Store::from_state(initial_state, reducer)
    }

    /// Borrow the current state for inspection.
    pub fn get_state(&self) -> &S {
        &self.state
    }

    /// Apply `action` to the current state through the reducer.
    pub fn dispatch(&mut self, action: &A) {
        let reducer = &self.reducer;
        self.state = reducer.reduce(&self.state, action);
    }
}

impl<'a, S: Default, A> Store<'a, S, A> {
    /// Make a new store from the default value of the state type.
    ///
    /// The initial state will be taken from the `Default` trait, and actions will
    /// be applied to it through `reducer`.
    ///
    /// # Examples
    ///
    /// ```
    /// fn add(state: &i32, action: &i32) -> i32 {
    ///     state + action
    /// }
    ///
    /// let reducer = &add;
    /// let mut store = redux::Store::from_default(reducer);
    /// assert_eq!(store.get_state(), &0);
    ///
    /// store.dispatch(&8);
    /// assert_eq!(store.get_state(), &8);
    /// ```
    pub fn from_default(reducer: &'a Reducer<S, A>) -> Store<'a, S, A> {
        let state_factory = Default::default;

        Store::from_factory(state_factory, reducer)
    }
}
