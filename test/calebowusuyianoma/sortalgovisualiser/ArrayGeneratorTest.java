package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ArrayGeneratorTest {
    private final ArrayGenerator arrayGenerator = new ArrayGenerator();

    private int minimumPossibleValue, maximumPossibleValue;

    @Test
    public void generateRandomIntegerArrayReturnsArrayWithExpectedSize() {
        int size = arrayGenerator.generateRandomIntegerInRange(0, 100);
        minimumPossibleValue = 5;
        maximumPossibleValue = 50;
        ArrayList<Integer> data = arrayGenerator.generateRandomIntegerArray(
                size, minimumPossibleValue, maximumPossibleValue);

        Assertions.assertEquals(size, data.size());
    }

    @Test
    public void generateRandomIntegerInRangeReturnsIntegerInCorrectRange() {
        minimumPossibleValue = 5;
        maximumPossibleValue = 50;
        int randomInt = arrayGenerator.generateRandomIntegerInRange(minimumPossibleValue, maximumPossibleValue);

        Assertions.assertTrue(randomInt >= minimumPossibleValue && randomInt < maximumPossibleValue);
    }

    @Test
    public void generateRandomIntegerInRangeThrowsIfMaxLessThanMin() {
        minimumPossibleValue = 50;
        maximumPossibleValue = 5;
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> arrayGenerator.generateRandomIntegerInRange(minimumPossibleValue, maximumPossibleValue));

        String expectedMessage = "max should be >= min, but max is " +
                maximumPossibleValue + " and min is " + minimumPossibleValue;
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
}