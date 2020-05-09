package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TimSortVisualiserTest {
    private final TimSortVisualiser timSortVisualiser = new TimSortVisualiser();

    private ArrayList<Integer> data;
    private ArrayList<Integer> expected;

    @Mock
    private Graphics g;

    @Test
    public void moveToNextStepThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                timSortVisualiser.moveToNextStep(data));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataIsEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(timSortVisualiser.isSorted());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(timSortVisualiser.isSorted());
    }

    @Test
    public void moveToNextStepSetsInitialVariableValuesAndRunningToTrueWhenNotRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        int keyIndex = timSortVisualiser.getKeyIndex();
        assertEquals(data.size(), timSortVisualiser.getNumberOfElements());
        assertEquals(timSortVisualiser.getMinimumRun(), timSortVisualiser.getRunSize());
        assertEquals(timSortVisualiser.getInsertionSortLoopIndex(), timSortVisualiser.getLeft());
        assertEquals(timSortVisualiser.getInsertionSortLoopIndex() + 1, keyIndex);
        assertEquals(data.get(keyIndex), timSortVisualiser.getKey());
        assertEquals(keyIndex - 1, timSortVisualiser.getSortedElementIndex());
        assertTrue(timSortVisualiser.isRunningInsertionSort());
        assertTrue(timSortVisualiser.isRunning());
    }

    @Test
    public void moveToNextStepShiftsSortedElementToTheRightWhenRunningInsertionSortAndKeyLessThanSortedElement() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 9, 5, 3));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = 2;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(data.get(keyIndex));
        timSortVisualiser.setSortedElementIndex(keyIndex - 1);
        expected = new ArrayList<>(Arrays.asList(7, 9, 9, 3));

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepInsertsKeyIntoCorrectPositionWhenRunningInsertionSortAndShouldInsertKey() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = 1;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(data.get(keyIndex));
        timSortVisualiser.setSortedElementIndex(keyIndex - 1);
        expected = new ArrayList<>(Arrays.asList(7, 9, 5, 3));

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepSetsVariableValuesWhenRunningInsertionSortAndIncrementedKeyIndexLessThanDataSize() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 9, 5, 3));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = 1;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(data.get(keyIndex - 1));
        timSortVisualiser.setSortedElementIndex(-1);
        timSortVisualiser.setRight(data.size() - 1);

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(keyIndex + 1, timSortVisualiser.getKeyIndex());
        assertEquals(data.get(keyIndex + 1), timSortVisualiser.getKey());
        assertEquals(keyIndex, timSortVisualiser.getSortedElementIndex());
    }

    @Test
    public void moveToNextStepIncreasesInsertionSortLoopIndexWhenKeyJustInsertedAndKeyIndexEqualsRightmostIndex() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(5, 7, 7, 9));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = data.size() - 1;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(3);
        timSortVisualiser.setSortedElementIndex(0);
        timSortVisualiser.setRight(data.size() - 1);

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(timSortVisualiser.getMinimumRun(), timSortVisualiser.getInsertionSortLoopIndex());
    }

    @Test
    public void moveToNextStepAdjustsKeyAndSortedElementIndexWhenIncrementedKeyIndexLessThanOrEqualToRightmostIndex() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(3, 5, 9, 7));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = 1;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(data.get(keyIndex));
        timSortVisualiser.setSortedElementIndex(-1);
        timSortVisualiser.setRight(data.size() - 1);

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(data.get(keyIndex + 1), timSortVisualiser.getKey());
        assertEquals(keyIndex, timSortVisualiser.getSortedElementIndex());
    }

    @Test
    public void moveToNextStepIncreasesInsertionSortLoopIndexWhenIncrementedKeyIndexEqualsRightMostIndex() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(3, 5, 7, 9));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = 2;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(data.get(0));
        timSortVisualiser.setSortedElementIndex(-1);
        timSortVisualiser.setRight(data.size() - 1);

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(timSortVisualiser.getMinimumRun(), timSortVisualiser.getInsertionSortLoopIndex());
    }

    @Test
    public void moveToNextStepSetsInsertionSortLoopVariablesWhenRunningInsertionSortAndTerminationExpressionInvalid() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 3, 2, 1));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(true);
        timSortVisualiser.setSortedElementIndex(-1);
        timSortVisualiser.setKeyIndex(timSortVisualiser.getMinimumRun() - 1);
        timSortVisualiser.setRight(timSortVisualiser.getMinimumRun() - 1);
        timSortVisualiser.setInsertionSortLoopIndex(timSortVisualiser.getMinimumRun());
        timSortVisualiser.setNumberOfElements(data.size());

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(timSortVisualiser.getInsertionSortLoopIndex(), timSortVisualiser.getLeft());
        assertEquals(data.size() - 1, timSortVisualiser.getRight());

        int keyIndex = timSortVisualiser.getKeyIndex();
        assertEquals(timSortVisualiser.getInsertionSortLoopIndex() + 1, keyIndex);
        assertEquals(data.get(keyIndex), timSortVisualiser.getKey());
        assertEquals(keyIndex - 1, timSortVisualiser.getSortedElementIndex());
    }

    @Test
    public void moveToNextStepSetsRunningInsertionSortToFalseWhenFinishedRunningInsertionSortForAllRuns() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 1, 2, 3));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(true);
        timSortVisualiser.setSortedElementIndex(timSortVisualiser.getMinimumRun() - 1);
        timSortVisualiser.setLeft(timSortVisualiser.getMinimumRun());
        timSortVisualiser.setRight(data.size() - 1);
        timSortVisualiser.setKeyIndex(data.size() - 1);
        timSortVisualiser.setInsertionSortLoopIndex(timSortVisualiser.getMinimumRun() * 2);

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertFalse(timSortVisualiser.isRunningInsertionSort());
    }

    @Test
    public void moveToNextStepMergesDataIfRunSizeLessThanNumberOfElements() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 1, 2, 3));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(false);
        timSortVisualiser.setRunSize(timSortVisualiser.getMinimumRun());
        timSortVisualiser.setNumberOfElements(data.size());
        timSortVisualiser.setInMergeForLoop(false);
        expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataFullySorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35));
        timSortVisualiser.setRunning(true);
        timSortVisualiser.setRunningInsertionSort(false);
        timSortVisualiser.setRunSize(timSortVisualiser.getMinimumRun() * 2);
        timSortVisualiser.setNumberOfElements(data.size());

        // Act
        timSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(timSortVisualiser.isSorted());
    }

    @Test
    public void paintPaintsOneBarForEachDataElement() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 200);

        // Act
        timSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(data.size())).fillRect(anyInt(), eq(0), anyInt(), anyInt());
    }

    @Test
    public void paintPaintsAllBarsMagentaWhenSortedIsTrue() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        timSortVisualiser.setSorted(true);

        // Act
        timSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(data.size())).setColor(Color.MAGENTA);
    }

    @Test
    public void paintPaintsBarOrangeWhenRunningInsertionSortAndCurrentElementIsPartOfSortedSubArray() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 3, 2, 1));
        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = 1;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(data.get(keyIndex));

        // Act
        timSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(1)).setColor(Color.ORANGE);
    }

    @Test
    public void paintPaintsKeyCyanWhenItHasNotBeenReplacedBySortedElement() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 3, 2, 1));
        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = 1;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(data.get(keyIndex));

        // Act
        timSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(1)).setColor(Color.CYAN);
    }

    @Test
    public void paintPaintsElementAtKeyIndexOrangeWhenKeyHasBeenReplacedBySortedElement() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 4, 2, 1));
        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = 1;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(data.get(3));

        // Act
        timSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(2)).setColor(Color.ORANGE);
    }

    @Test
    public void paintPaintsElementOrangeWhenInsertionSortNotRunningAndElementIsBeingMerged() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 1, 2, 3));

        timSortVisualiser.setKeyIndex(data.size());
        timSortVisualiser.setPreviousMergeStartIndex(0);
        timSortVisualiser.setMergeEndIndex(data.size() - 1);

        // Act
        timSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(data.size())).setColor(Color.ORANGE);
    }

    @Test
    public void paintPaintsBarsBlackWhenNotBeingSortedOrMerged() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19,
                18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1));

        timSortVisualiser.setRunningInsertionSort(true);

        int keyIndex = 1;
        timSortVisualiser.setKeyIndex(keyIndex);
        timSortVisualiser.setKey(data.get(keyIndex));

        // Act
        timSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(data.size() - (keyIndex + 1))).setColor(Color.BLACK);
    }

    // todo: consider extracting these common methods to a separate test class
    @Test
    public void fillRectangleThrowsExceptionWhenGraphicsObjectIsNull() {
        // Arrange
        String expected = "The Graphics object should not be null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                timSortVisualiser.fillRectangle(null, 1, 1, 1, 1, 1,
                        data));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void fillRectangleThrowsExceptionWhenDataIsNull() {
        // Arrange
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                timSortVisualiser.fillRectangle(g, 1, 1, 1, 1, 1,
                        null));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void fillRectangleCallsFillRectWithCorrectParameters() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        int xCoordinate = 1;
        int width = 1;

        // Act
        timSortVisualiser.fillRectangle(g, 1, 1, 1, xCoordinate, width, data);

        // Assert
        verify(g, times(1)).fillRect(eq(xCoordinate), eq(0), eq(width), anyInt());
    }
}