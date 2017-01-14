use super::factory::*;
use super::reducer::*;

/// A container for states of type `S`, which dispatches actions of type `A` and
/// applies them to the contained state.
///
/// Useful stores generally must be mutable.
pub struct Store<S, R> {
    state: S,
    reducer: R,
}

impl<S, R> Store<S, R> {
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
    /// let mut store = redux::Store::from_state(5, add);
    /// assert_eq!(store.get_state(), &5);
    ///
    /// store.dispatch(&8);
    /// assert_eq!(store.get_state(), &13);
    /// ```
    pub fn from_state<A>(initial_state: S, reducer: R) -> Store<S, R> where
        R: Reducer<S, A> {
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
    /// let mut store = redux::Store::from_factory(|| 5, add);
    /// assert_eq!(store.get_state(), &5);
    ///
    /// store.dispatch(&8);
    /// assert_eq!(store.get_state(), &13);
    /// ```
    pub fn from_factory<A, F>(state_factory: F, reducer: R) -> Store<S, R> where
        F: Factory<S>,
        R: Reducer<S, A> {
        let initial_state = state_factory.create();

        Store::from_state(initial_state, reducer)
    }

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
    /// let mut store = redux::Store::from_default(add);
    /// assert_eq!(store.get_state(), &0);
    ///
    /// store.dispatch(&8);
    /// assert_eq!(store.get_state(), &8);
    /// ```
    pub fn from_default<A>(reducer: R) -> Store<S, R> where
        S: Default,
        R: Reducer<S, A> {
        let state_factory = Default::default;

        Store::from_factory(state_factory, reducer)
    }

    /// Borrow the current state for inspection.
    pub fn get_state(&self) -> &S {
        &self.state
    }

    /// Apply `action` to the current state through the reducer.
    pub fn dispatch<A>(&mut self, action: &A) where
        R: Reducer<S, A> {
        let reducer = &self.reducer;
        self.state = reducer.reduce(&self.state, action);
    }
}
