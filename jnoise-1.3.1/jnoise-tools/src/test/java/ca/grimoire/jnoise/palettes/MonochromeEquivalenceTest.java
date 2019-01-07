package ca.grimoire.jnoise.palettes;

import junit.framework.TestCase;

/**
 * Verifies that monochrome and corrected monochrome palettes can be made
 * equivalent.
 */
@SuppressWarnings ("deprecation")
public class MonochromeEquivalenceTest extends TestCase {

  private CorrectedMonochrome testPalette   = new CorrectedMonochrome (-1, 1,
                                                1.0);
  private Monochrome          authoritative = new Monochrome ();

  /**
   * Verifies that the exact black threshold of the test module matches the
   * exact black value of the original implementation.
   */
  public void testEquivalentBlack () {
    assertEquals (authoritative.getColor (-1.0), testPalette.getColor (-1.0));
  }

  /**
   * Verifies that a value far past the black threshold of the test module
   * matches the same black value of the original implementation.
   */
  public void testEquivalentFarBlack () {
    assertEquals (authoritative.getColor (-5.0), testPalette.getColor (-5.0));
  }

  /**
   * Verifies that the exact black threshold of the test module matches the
   * exact black value of the original implementation.
   */
  public void testEquivalentWhite () {
    assertEquals (authoritative.getColor (1.0), testPalette.getColor (1.0));
  }

  /**
   * Verifies that a value far past the white threshold of the test module
   * matches the same black value of the original implementation.
   */
  public void testEquivalentFarWhite () {
    assertEquals (authoritative.getColor (25.0), testPalette.getColor (25.0));
  }

  /**
   * Verifies that samples along the greyscale range of the test module match
   * the corresponding greyscale values on the original implementation.
   */
  public void testEquivalentGreyscale () {
    for (double value = -1.0; value <= 1.0; value += 0.1)
      assertEquals (authoritative.getColor (value), testPalette
          .getColor (value));
  }
}
