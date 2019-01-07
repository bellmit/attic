package ca.grimoire.bom.maven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

public class PathsTest {
    @Test
    public void stripParent() {
        assertEquals("example.txt",
                Paths.strip(new File("foo/example.txt"), new File("foo/")));
    }

    @Test
    public void stripNested() {
        assertEquals("bar/example.txt",
                Paths.strip(new File("foo/bar/example.txt"), new File("foo/")));
    }

    @Test
    public void stripNonParent() {
        try {
            Paths.strip(new File("monkey/bar/example.txt"), new File("foo/"));
            fail();
        } catch (IllegalArgumentException expected) {
            // Success case: strip rejected the non-parent.
        }
    }
}
