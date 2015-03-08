# Acceptance Testing

Login Box acceptance tests are based on the LMAX [Simple-DSL](https://github.com/LMAX-Exchange/Simple-DSL/wiki) testing style. Tests can be run against running server from your IDE, or as part of a normal build, using a server kicked off within the build process.

The actual tests are JUnit tests extending `DslTestCase`. These tests must be contained in the `src/main/acceptanceTest` source tree, and should be in a package starting with `com.loginbox.app.acceptance.framework.tests`. For details about the layout, see the existing tests.

To run tests from the build run `gradle accceptanceTest` or `gradle check`. Gradle will scan the `src/main/acceptanceTest` for _all_ JUnit-style tests, and run them. Reports will be combined with the unit test reports, in `build/test-results`. (Tests do not need to be named `FooTest` or `FooIT` to be detected.)

## Test Authoring Guidelines

As spelled out in the Simple-DSL documentation, acceptance tests are written in a simplified dialect of Java:

* No variables.
* No nested expressions or arithmetic.
* No flow control.
* No method calls.
* No checked exceptions.
* Simple, string-based argument conventions.
* No interdependencies between tests.

These guidelines are meant to render tests more readable and easier to author by non-programmers.

Tests should be structued by functional area, _not_ by story. Being able to audit and understand the overall behaviour of the system, area by area, is more important than being able to audit individual stories.

## An Example

The following example illustrates how to write a test for the simple paths of a sign in page:

	public class SignIn extends DslTestCase {
		@Before
		public void createTestUser() {
			webUi.open();
			webUi.signUp("password: acceptance-test");
			webUi.close();
		}

		@Test
		public void userCanSignIn() {
			webUi.open();
			webUi.signIn(password: acceptance-test");
			webUi.profilePage.ensureProfileShown("username: acceptance-test");
		}

		@Test
		public void userMustUseCorrectPassword() {
			webUi.open();
			webUi.signIn("password: wrong-password", "expect: FAILED");
		}
	}

## The Testing Framework

For a complete introduction to the style, see Simple-DSL's own documentation.

Acceptance tests use a layered approach to turn simplified Java into complex behavioural tests. The layers break down as follows, from top to bottom:

1. The tests themselves, written in simplified Java. These tests describe the desired behaviour of the system from the perspective of some user generally a player or prospective player; occasionally an API user or administrator). Each step in a test is a method call to some domain-specific language adapter.

2. The DSL adapter layer. All layers from here down may use the full range of  Java features, as they're maintained by (and often authored by) developers. This layer handles concerns such as assertions, sequencing, waits, failures, retries, and exceptions. The underlying machinery is implemented by calls to an underlying driver layer. The vocabulary in this layer is still largely oriented towards abstract domain concepts, not, for example, specific API calls or page elements.

    This layer also handles generating unique data from the templates used in the actual tests.

3. The driver layer interacts with external frameworks, such as Selenium. This layer's vocabulary targets specific features of the implementation, such as specific HTML elements or API messages. _Generally_, this layer should not concern itself with test success or failure, except as an incidental side effect of other actions.

For a list of DSL interfaces, have a look at the `protected` fields exposed by `DslTestCase`.

The underlying machinery is tied together by `SystemDriver`, which handles configuration (such as the root URL of the app, or the default Selenium timeout) and lazily creates drivers as required by the executing tests.

Tests are expected to provide their own test data as part of their execution, either during `@Before` hooks or as part of the test body. To simplify repeated runs and avoid a complex cleanup phase, the DSL layer uses a `TestContext` to convert between aliases (used in test statements) and randomly-generated values (used in inputs and API calls). For example, a test which tries to

	webUi.login("username: example-user");

will be converted in the DSL layer to a test which, this run, tries to log in as `example-user-ZNEig8y0`, and next run, will try to log in as `example-user- BWzDS6IH`. Most DSL methods provide sensible default aliases, but where the correlation between setup and testing is important, tests should spell out the actual alias.

## DSL Conventions

The DSL adapters expose two sets of interfaces: a `public` interface, which is available to tests, and a default-visibiliy interface, which is available to other DSL classes. This is meant to allow DSLs to be composed out of other DSLs.

To expose a DSL action to other DSL classes, split the action into two parts. The first (`public`) part should accept DSL parameters and decode them, then pass them _positionally_ to a `...Values` method in the default-visibility interface with the same name. Other DSLs should call the `...Values` method, not the DSL method. To ensure DSL parameters are compatible, it's wise to also provide a static `DslParam` factory method other DSLs can call.

This split allows `DslParams` to sanity-check arguments on a per-DSL-method basis, correctly rejecting extra arguments, while also allowing DSL methods to be defined in terms of other DSLs.

## Driver Conventions

Web-based drivers often expose "actions" (fill out a form, click a link, and so on) to their callers. These actions should be split into a `findFoo` method and a `clickFoo` method that delegates to it. (If the action isn't `click`, substitute appropriately.) This basic factoring makes it much easier to ensure consistency for page parts that support multiple actions by providing a single, authoritative way to look up the target elements.

For an example of this see the `WebUi.signUp()` DSL method, which relies on `SignUpPage` for much of its operation.

## Incomplete Features

Tests can be written in advance for incomplete features. Pull requests for `master` for these tests _must_ mark them as `@Ignore` to permit builds to pass. On feature branches, tests for the feature under development should be allowed to fail normally. Broken builds related to the feature under development are normal and healthy for feature branches.

Long-lived `@Ignore` annotations, however, suggest that the feature may never be implemented as specced. Periodically, go back and clean up `@Ignore`d tests that have not been implemented, or follow up to ensure that the feature gets completed.
