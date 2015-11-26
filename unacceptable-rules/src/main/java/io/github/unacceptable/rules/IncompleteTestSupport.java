package io.github.unacceptable.rules;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static org.junit.Assert.fail;

/**
 * Intercepts test results and interprets them as described in {@link Incomplete}.
 */
public class IncompleteTestSupport implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        Incomplete annotation = description.getAnnotation(Incomplete.class);
        if (annotation == null)
            return base;

        return new IncompleteTestStatement(base, description);
    }

    private void evaluateIncomplete(Statement base, Description description) throws Throwable {
        try {
            base.evaluate();
        } catch (AssertionError ae) {
            String message = String.format(
                    "The test '%s' was expected to fail, because it describes an @Incomplete feature.", description.getDisplayName());
            throw new AssumptionViolatedException(message, ae);
        } catch (Exception ae) {
            String message = String.format(
                    "The test '%s' was expected to fail, because it describes an @Incomplete feature.", description.getDisplayName());
            throw new AssumptionViolatedException(message, ae);
        }
        String message = String.format(
                "The test '%s' was expected to fail, because it describes an @Incomplete feature. The test has passed; consider removing the @Incomplete annotation.", description.getDisplayName());
        fail(message);
    }

    private class IncompleteTestStatement extends Statement {
        private final Statement base;
        private final Description description;

        public IncompleteTestStatement(Statement base, Description description) {
            this.base = base;
            this.description = description;
        }

        @Override
        public void evaluate() throws Throwable {
            evaluateIncomplete(base, description);
        }
    }
}
