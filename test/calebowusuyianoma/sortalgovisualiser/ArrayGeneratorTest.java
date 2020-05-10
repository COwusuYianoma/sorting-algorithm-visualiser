package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArrayGeneratorTest {
    @Test
    public void generateRandomIntegerArrayReturnsArrayWithExpectedSize() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(0, 101);

        // Act
        ArrayList<Integer> data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 5, 50);

        // Assert
        assertEquals(size, data.size());
    }

    @Test
    public void generateRandomIntegerArrayThrowsExceptionWhenMaxIsNonPositive() {
        // Arrange
        int min = 3;
        int max = -3;
        String expectedMessage = "max and min should be > 0, but max is " + max + " and min is " + min;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ArrayGenerator.generateRandomPositiveIntegerArray(100, min, max));

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void generateRandomIntegerArrayThrowsExceptionWhenMinIsNonPositive() {
        // Arrange
        int min = 0;
        int max = 3;
        String expectedMessage = "max and min should be > 0, but max is " + max + " and min is " + min;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ArrayGenerator.generateRandomPositiveIntegerArray(100, min, max));

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void generateRandomIntegerArrayThrowsExceptionWhenMaxLessThanOrEqualToMin() {
        // Arrange
        int min = 50;
        int max = 5;
        String expectedMessage = "max should be > min, but max is " + max + " and min is " + min;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                ArrayGenerator.generateRandomPositiveIntegerArray(100, min, max));

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }
}