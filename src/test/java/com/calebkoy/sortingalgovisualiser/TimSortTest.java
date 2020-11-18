package com.calebkoy.sortingalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

class TimSortTest {
    private final TimSort timSort = new TimSort();

    private ArrayList<Integer> data;
    private ArrayList<Integer> expected;

    @Test
    public void sortThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should not be null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> timSort.sort(data));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        timSort.sort(data);

        // Assert
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        timSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsNearlySortedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        timSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsReversedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        timSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsRandomData() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 2001);
        ArrayList<Integer> expected = new ArrayList<>(data);
        Collections.sort(expected);

        // Act
        timSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }
}