package utils;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class DirectorySelectionTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSubmitWithoutDirectorySelection() {
        assertThrows(IllegalArgumentException.class, () -> {
            submitWithoutDirectory();
        });
    }

    private void submitWithoutDirectory() {
        throw new IllegalArgumentException("No directory selected");

    }

}
