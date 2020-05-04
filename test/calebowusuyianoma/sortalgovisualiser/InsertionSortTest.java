package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

class InsertionSortTest {
    private final InsertionSort insertionSort = new InsertionSort();

    private ArrayList<Integer> data;

    // TODO: extract common tests into parent test class if necessary
    @Test
    public void moveToNextStepThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expectedMessage = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                insertionSort.moveToNextStep(data));

        // Assert
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        insertionSort.setSorted(false);

        // Act
        insertionSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(insertionSort.sorted());
    }

    @Test
    public void moveToNextStepSetsRunningToTrueAndInitialisesVariablesWhenSortNotRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        insertionSort.setRunning(false);
        int expectedKeyIndex = 1;
        int expectedKey = data.get(expectedKeyIndex);
        int expectedSortedElementIndex = expectedKeyIndex - 1;

        // Act
        insertionSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(insertionSort.running());
        Assertions.assertEquals(expectedKeyIndex, insertionSort.getKeyIndex());
        Assertions.assertEquals(expectedKey, insertionSort.getKey());
        Assertions.assertEquals(expectedSortedElementIndex, insertionSort.getSortedElementIndex());
    }

    @Test
    public void moveToNextStepShiftsSortedElementToTheRightWhenKeyLessThanSortedElement() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        insertionSort.setRunning(true);
        insertionSort.setSortedElementIndex(0);
        int keyIndex = 1;
        insertionSort.setKeyIndex(keyIndex);
        insertionSort.setKey(data.get(keyIndex));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(9, 9));

        // Act
        insertionSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepInsertsKeyInCorrectPositionIfKeyNotLessThanElementOnLeft() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 9, 5));
        insertionSort.setRunning(true);
        insertionSort.setSortedElementIndex(-1);
        insertionSort.setKeyIndex(1);
        insertionSort.setKey(7);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(7, 9, 5));

        // Act
        insertionSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepSetsVariableValuesWhenIncrementedKeyIndexLessThanDataSize() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 9, 5));
        insertionSort.setRunning(true);
        insertionSort.setSortedElementIndex(-1);
        int keyIndex = 1;
        insertionSort.setKeyIndex(keyIndex);
        insertionSort.setKey(7);
        int expectedKeyIndex = keyIndex + 1;
        int expectedKey = data.get(expectedKeyIndex);
        int expectedSortedElementIndex = expectedKeyIndex - 1;

        // Act
        insertionSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(expectedKeyIndex, insertionSort.getKeyIndex());
        Assertions.assertEquals(expectedKey, insertionSort.getKey());
        Assertions.assertEquals(expectedSortedElementIndex, insertionSort.getSortedElementIndex());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenIncrementedKeyIndexNotLessThanDataSize() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 7, 9));
        insertionSort.setRunning(true);
        insertionSort.setSorted(false);
        insertionSort.setSortedElementIndex(-1);
        int keyIndex = 2;
        insertionSort.setKeyIndex(keyIndex);
        insertionSort.setKey(5);

        // Act
        insertionSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(insertionSort.sorted());
    }

    @Test
    public void sortThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expectedMessage = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data));

        // Assert
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expectedMessage = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, 0, 3));

        // Assert
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenLeftIndexLessThanZero() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int left = -1;
        int right = 3;
        String expectedMessage = "Indices left and right should be >= 0, but left is " + left +
                " and right is " + right;

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, left, right));

        // Assert
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenRightIndexLessThanZero() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int left = -1;
        int right = 3;
        String expectedMessage = "Indices left and right should be >= 0, but left is " + left +
                " and right is " + right;

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, left, right));

        // Assert
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenLeftIndexEqualToDataSizeMinusOne() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int left = data.size() - 1;
        int right = 3;
        String expectedMessage = "Index left must be < data.size() - 1, but left is " + left +
                " and (data.size() - 1) equals " + (data.size() - 1);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, left, 3));

        // Assert
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenLeftIndexGreaterThanDataSizeMinusOne() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int left = data.size();
        String expectedMessage = "Index left must be < data.size() - 1, but left is " + left +
                " and (data.size() - 1) equals " + (data.size() - 1);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, left, 3));

        // Assert
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void sortWithIndicesThrowsExceptionWhenRightIndexGreaterThanDataSizeMinusOne() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        int right = 3;
        String expectedMessage = "Index right must be <= data.size() - 1, but right is " + right +
                " and (data.size() - 1) equals " + (data.size() - 1);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                insertionSort.sort(data, 0, right));

        // Assert
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void sortLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        insertionSort.sort(data);

        // Assert
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortWithIndicesLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        insertionSort.sort(data, 0, 2);

        // Assert
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        insertionSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsNearlySortedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        insertionSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsReversedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        insertionSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsRandomData() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 2001);

        // Act
        insertionSort.sort(data);

        // Assert
        Assertions.assertTrue(TestUtilities.isSorted(data));
    }
}