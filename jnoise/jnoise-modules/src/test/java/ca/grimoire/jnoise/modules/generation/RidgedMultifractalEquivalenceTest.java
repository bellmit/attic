package ca.grimoire.jnoise.modules.generation;

import ca.grimoire.jnoise.modules.basic.Gradient;
import junit.framework.TestCase;

/**
 * Validates that the two ridged multifractal constructors can generate
 * equivalent constructors. For each test, a module is constructed without a
 * defined weight and frequency, and another module with an explicit weight and
 * frequency taken from the RidgedMultifractal default values.
 */
@SuppressWarnings ("deprecation")
public class RidgedMultifractalEquivalenceTest extends TestCase {

  private static final int              SEED          = 0;
  private static final int              OCTAVES       = 0;
  private static final Gradient.Quality QUALITY       = Gradient.Quality.HIGH;
  private static final double           LACUNARITY    = 0;

  private static final double           STEP          = 0.1;

  private RidgedMultifractal            testModule    = new RidgedMultifractal (
                                                          SEED,
                                                          OCTAVES,
                                                          LACUNARITY,
                                                          RidgedMultifractal.DEFAULT_WEIGHT,
                                                          RidgedMultifractal.DEFAULT_FREQUENCY,
                                                          QUALITY);
  private RidgedMultifractal            authoritative = new RidgedMultifractal (
                                                          SEED, OCTAVES,
                                                          LACUNARITY, QUALITY);

  /**
   * Verifies that both modules produce indentical values within a sample
   * region.
   */
  public void testRegionalValueEquivalence () {
    for (double z = 0.5; z <= 1.5; z += STEP)
      for (double y = -5.2; y <= -2.5; y += STEP)
        for (double x = -1; x <= 1; x += STEP)
          assertEquals (authoritative.getValue (x, y, z), testModule.getValue (
              x, y, z));
  }

  /**
   * Verifies that both modules produce identical hashcodes.
   */
  public void testHashCode () {
    assertEquals (authoritative.hashCode (), testModule.hashCode ());
  }

  /**
   * Verifies that the two modules are correctly equal.
   */
  public void testEquals () {
    assertTrue (authoritative.equals (testModule));
    assertTrue (testModule.equals (authoritative));
  }
}
