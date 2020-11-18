package com.calebkoy.sortingalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BubbleSortVisualiserTest {
    private final BubbleSortVisualiser bubbleSortVisualiser = new BubbleSortVisualiser();

    private ArrayList<Integer> data;

    @Mock private Graphics g;

    @Test
    public void moveToNextStepThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should not be null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                bubbleSortVisualiser.moveToNextStep(data));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));

        // Act
        bubbleSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(bubbleSortVisualiser.isSorted());
    }

    @Test
    public void moveToNextStepSetsRunningToTrueAndInitialisesPointerWhenSortNotRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        bubbleSortVisualiser.setRunning(false);

        // Act
        bubbleSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(bubbleSortVisualiser.isRunning());
        assertEquals(data.size() - 1, bubbleSortVisualiser.getInnerForLoopVariable());
    }

    @Test
    public void moveToNextStepSwapsElementsWhenCurrentElementIsLessThanElementOnLeft() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        bubbleSortVisualiser.setRunning(true);
        bubbleSortVisualiser.setInnerForLoopVariable(data.size() - 1);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(7, 9));

        // Act
        bubbleSortVisualiser.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepLeavesElementsUnchangedWhenCurrentElementIsGreaterThanElementOnLeft() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 9));
        bubbleSortVisualiser.setRunning(true);
        bubbleSortVisualiser.setInnerForLoopVariable(data.size() - 1);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(7, 9));

        // Act
        bubbleSortVisualiser.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepDecrementsInnerLoopVariableWhenInnerLoopBodyJustFinishesRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        bubbleSortVisualiser.setRunning(true);
        int innerLoopVariableInitialValue = data.size() - 1;
        bubbleSortVisualiser.setInnerForLoopVariable(innerLoopVariableInitialValue);

        // Act
        bubbleSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(innerLoopVariableInitialValue - 1,
                bubbleSortVisualiser.getInnerForLoopVariable());
    }

    @Test
    public void moveToNextStepIncrementsOuterForLoopVariableWhenOuterLoopBodyJustFinishesRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        bubbleSortVisualiser.setRunning(true);
        bubbleSortVisualiser.setInnerForLoopVariable(data.size() - 1);

        // Act
        bubbleSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(1, bubbleSortVisualiser.getOuterForLoopVariable());
    }

    @Test
    public void moveToNextStepLeavesOuterLoopVariableUnchangedWhenOuterLoopBodyStillRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        bubbleSortVisualiser.setRunning(true);
        bubbleSortVisualiser.setInnerForLoopVariable(data.size() - 1);

        // Act
        bubbleSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(0, bubbleSortVisualiser.getOuterForLoopVariable());
    }

    @Test
    public void moveToNextStepReInitialisesInnerLoopVariableWhenInnerLoopTerminatesAndOuterLoopStillRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(5, 9, 7));
        bubbleSortVisualiser.setRunning(true);
        bubbleSortVisualiser.setInnerForLoopVariable(0);

        // Act
        bubbleSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(data.size() - 1, bubbleSortVisualiser.getInnerForLoopVariable());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenBothLoopsTerminate() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(5, 7, 9));
        bubbleSortVisualiser.setRunning(true);
        bubbleSortVisualiser.setOuterForLoopVariable(data.size() - 1);
        bubbleSortVisualiser.setInnerForLoopVariable(data.size() - 2);

        // Act
        bubbleSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(bubbleSortVisualiser.isSorted());
    }

    @Test
    public void paintPaintsOneBarForEachDataElement() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 200);

        // Act
        bubbleSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        Mockito.verify(g, Mockito.times(data.size())).fillRect(ArgumentMatchers.anyInt(), ArgumentMatchers.eq(0), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
    }

    @Test
    public void paintSetsColourToSortedColourWhenSortedIsTrue() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        bubbleSortVisualiser.setSorted(true);

        // Act
        bubbleSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        Mockito.verify(g, Mockito.times(data.size())).setColor(Color.MAGENTA);
    }

    @Test
    public void paintUsesDifferentColourForLoopVariableBarsWhenDataNotSorted() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 200);
        bubbleSortVisualiser.setRunning(true);
        bubbleSortVisualiser.setOuterForLoopVariable(0);
        bubbleSortVisualiser.setInnerForLoopVariable(data.size() - 1);

        // Act
        bubbleSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        Mockito.verify(g, Mockito.times(data.size() - 2)).setColor(Color.BLACK);
        Mockito.verify(g, Mockito.times(2)).setColor(Color.CYAN);
    }
}