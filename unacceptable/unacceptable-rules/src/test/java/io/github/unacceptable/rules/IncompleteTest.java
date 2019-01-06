package io.github.unacceptable.rules;

import org.junit.AssumptionViolatedException;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;

public class IncompleteTest {
    private final Incomplete annotation = mock(Incomplete.class);
    private final Description description = mock(Description.class);

    private final Statement passingStatement = new Statement() {
        @Override
        public void evaluate() throws Throwable {
            /* passing test */
        }
    };

    private final TestRule rule = new IncompleteTestSupport();

    private AssertionError assertionFailed = new AssertionError();
    private final Statement failingStatement = new Statement() {
        @Override
        public void evaluate() throws Throwable {
            throw assertionFailed;
        }
    };

    @Test
    public void passesNonAnnotatedTest() throws Throwable {
        doReturn(null).when(description).getAnnotation(Incomplete.class);

        Statement annotated = rule.apply(passingStatement, description);

        annotated.evaluate();
    }


    @Test
    public void failsNonAnnotatedTest() throws Throwable {
        doReturn(null).when(description).getAnnotation(Incomplete.class);

        Statement annotated = rule.apply(failingStatement, description);

        try {
            annotated.evaluate();
            fail();
        } catch(AssertionError expected) {
            assertThat(expected, sameInstance(assertionFailed));
        }
    }

    @Test
    public void failsPassingAnnotatedTest() throws Throwable {
        doReturn(annotation).when(description).getAnnotation(Incomplete.class);

        Statement annotated = rule.apply(passingStatement, description);

        try {
            annotated.evaluate();
            fail();
        } catch(AssertionError expected) {
            /* pass */
        }
    }

    @Test
    public void ignoresFailingAnnotatedTest() throws Throwable {
        doReturn(annotation).when(description).getAnnotation(Incomplete.class);

        Statement annotated = rule.apply(failingStatement, description);

        try {
            annotated.evaluate();
            fail();
        } catch(AssumptionViolatedException expected) {
            assertThat(expected.getCause(), sameInstance((Throwable)assertionFailed));
        }
    }
}
