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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InsertionSortVisualiserTest {
    private final InsertionSortVisualiser insertionSortVisualiser = new InsertionSortVisualiser();
    
    private ArrayList<Integer> data;

    @Mock
    private Graphics g;

    @Test
    public void moveToNextStepThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                insertionSortVisualiser.moveToNextStep(data));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));

        // Act
        insertionSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(insertionSortVisualiser.isSorted());
    }

    @Test
    public void moveToNextStepSetsRunningToTrueAndInitialisesVariablesWhenSortNotRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        insertionSortVisualiser.setRunning(false);
        int expectedKeyIndex = 1;
        int expectedKey = data.get(expectedKeyIndex);
        int expectedSortedElementIndex = expectedKeyIndex - 1;

        // Act
        insertionSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(insertionSortVisualiser.isRunning());
        assertEquals(expectedKeyIndex, insertionSortVisualiser.getKeyIndex());
        assertEquals(expectedKey, insertionSortVisualiser.getKey());
        assertEquals(expectedSortedElementIndex, insertionSortVisualiser.getSortedElementIndex());
    }

    @Test
    public void moveToNextStepShiftsSortedElementToTheRightWhenKeyLessThanSortedElement() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        insertionSortVisualiser.setRunning(true);
        insertionSortVisualiser.setSortedElementIndex(0);

        int keyIndex = 1;
        insertionSortVisualiser.setKeyIndex(keyIndex);
        insertionSortVisualiser.setKey(data.get(keyIndex));

        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(9, 9));

        // Act
        insertionSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepInsertsKeyInCorrectPositionIfKeyNotLessThanElementOnLeft() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 9, 5));
        insertionSortVisualiser.setRunning(true);
        insertionSortVisualiser.setSortedElementIndex(-1);
        insertionSortVisualiser.setKeyIndex(1);
        insertionSortVisualiser.setKey(7);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(7, 9, 5));

        // Act
        insertionSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepSetsVariableValuesWhenIncrementedKeyIndexLessThanDataSize() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 9, 5));
        insertionSortVisualiser.setRunning(true);
        insertionSortVisualiser.setSortedElementIndex(-1);

        int keyIndex = 1;
        insertionSortVisualiser.setKeyIndex(keyIndex);
        insertionSortVisualiser.setKey(7);
        int expectedKeyIndex = keyIndex + 1;
        int expectedKey = data.get(expectedKeyIndex);
        int expectedSortedElementIndex = expectedKeyIndex - 1;

        // Act
        insertionSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(expectedKeyIndex, insertionSortVisualiser.getKeyIndex());
        assertEquals(expectedKey, insertionSortVisualiser.getKey());
        assertEquals(expectedSortedElementIndex, insertionSortVisualiser.getSortedElementIndex());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenIncrementedKeyIndexNotLessThanDataSize() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 7, 9));
        insertionSortVisualiser.setRunning(true);
        insertionSortVisualiser.setSortedElementIndex(-1);
        insertionSortVisualiser.setKeyIndex(2);
        insertionSortVisualiser.setKey(5);

        // Act
        insertionSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(insertionSortVisualiser.isSorted());
    }

    @Test
    public void paintPaintsOneBarForEachDataElement() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 200);

        // Act
        insertionSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(data.size())).fillRect(anyInt(), eq(0), anyInt(), anyInt());
    }

    @Test
    public void paintSetsColourToSortedColourWhenSortedIsTrue() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        insertionSortVisualiser.setSorted(true);

        // Act
        insertionSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(data.size())).setColor(Color.MAGENTA);
    }

    @Test
    public void paintPaintsAllSortedSubArrayElementsOrange() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 5, 6, 3, 2, 1));
        int keyIndex = 3;
        insertionSortVisualiser.setKeyIndex(keyIndex);
        insertionSortVisualiser.setKey(data.get(keyIndex));

        // Act
        insertionSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(keyIndex)).setColor(Color.ORANGE);
    }

    @Test
    public void paintPaintsKeyCyanWhenItHasNotBeenReplacedBySortedElement() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 5, 6, 3, 2, 1));
        int keyIndex = 3;
        insertionSortVisualiser.setKeyIndex(keyIndex);
        insertionSortVisualiser.setKey(data.get(keyIndex));

        // Act
        insertionSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(1)).setColor(Color.CYAN);
    }

    @Test
    public void paintPaintsElementAtKeyIndexOrangeWhenKeyHasBeenReplacedBySortedElement() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 5, 6, 6, 2, 1));
        int keyIndex = 3;
        insertionSortVisualiser.setKeyIndex(keyIndex);
        insertionSortVisualiser.setKey(3);

        // Act
        insertionSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(4)).setColor(Color.ORANGE);
    }

    @Test
    public void paintPaintsElementsToRightOfKeyIndexBlack() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2, 1));
        int keyIndex = 3;
        insertionSortVisualiser.setKeyIndex(keyIndex);
        insertionSortVisualiser.setKey(data.get(keyIndex));

        // Act
        insertionSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(data.size() - (keyIndex + 1))).setColor(Color.BLACK);
    }
}