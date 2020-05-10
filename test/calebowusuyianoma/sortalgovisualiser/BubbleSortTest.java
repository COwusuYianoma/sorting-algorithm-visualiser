package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BubbleSortTest {
    private final BubbleSort bubbleSort = new BubbleSort();

    private ArrayList<Integer> data;

    @Test
    public void sortThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bubbleSort.sort(data));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        bubbleSort.sort(data);

        // Assert
        assertTrue(data.isEmpty());
    }

    @Test
    public void sortLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        bubbleSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsNearlySortedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        bubbleSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsReversedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        bubbleSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsRandomData() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 2001);
        ArrayList<Integer> expected = new ArrayList<>(data);
        Collections.sort(expected);

        // Act
        bubbleSort.sort(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void swapThrowsExceptionWhenDataIsNull() {
        // Arrange
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bubbleSort.swap(null, 1));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void swapThrowsExceptionWhenKeyIsLessThanZero() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        int key = -1;
        String expected = "The key should be a valid index of the array, but key is " +
                key + " and the array has size " + data.size();

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bubbleSort.swap(data, key));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void swapThrowsExceptionWhenKeyGreaterThanMaxArrayIndex() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        int key = data.size();
        String expected = "The key should be a valid index of the array, but key is " +
                key + " and the array has size " + data.size();

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bubbleSort.swap(data, key));

        // Assert
        assertEquals(expected, exception.getMessage());
    }
}