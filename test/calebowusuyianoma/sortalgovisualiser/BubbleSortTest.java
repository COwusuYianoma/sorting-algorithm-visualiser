package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

class BubbleSortTest {
    private final BubbleSort bubbleSort = new BubbleSort();

    private ArrayList<Integer> data;

    @Test
    public void moveToNextStepThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                bubbleSort.moveToNextStep(data));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        bubbleSort.setSorted(false);

        // Act
        bubbleSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(bubbleSort.isSorted());
    }

    @Test
    public void moveToNextStepSetsRunningToTrueAndInitialisesPointerWhenSortNotRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        bubbleSort.setRunning(false);

        // Act
        bubbleSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(bubbleSort.isRunning());
        Assertions.assertEquals(data.size() - 1, bubbleSort.getInnerForLoopVariable());
    }

    @Test
    public void moveToNextStepSwapsElementsWhenCurrentElementIsLessThanElementOnLeft() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        bubbleSort.setRunning(true);
        bubbleSort.setInnerForLoopVariable(data.size() - 1);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(7, 9));

        // Act
        bubbleSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepLeavesElementsUnchangedWhenCurrentElementIsGreaterThanElementOnLeft() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 9));
        bubbleSort.setRunning(true);
        bubbleSort.setInnerForLoopVariable(data.size() - 1);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(7, 9));

        // Act
        bubbleSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepDecrementsInnerLoopVariableWhenInnerLoopBodyJustFinishesRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        bubbleSort.setRunning(true);
        int innerLoopVariableInitialValue = data.size() - 1;
        bubbleSort.setInnerForLoopVariable(innerLoopVariableInitialValue);

        // Act
        bubbleSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(innerLoopVariableInitialValue - 1, bubbleSort.getInnerForLoopVariable());
    }

    @Test
    public void moveToNextStepIncrementsOuterForLoopVariableWhenOuterLoopBodyJustFinishesRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        bubbleSort.setRunning(true);
        bubbleSort.setInnerForLoopVariable(data.size() - 1);

        // Act
        bubbleSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(1, bubbleSort.getOuterForLoopVariable());
    }

    @Test
    public void moveToNextStepLeavesOuterLoopVariableUnchangedWhenOuterLoopBodyStillRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5));
        bubbleSort.setRunning(true);
        bubbleSort.setInnerForLoopVariable(data.size() - 1);

        // Act
        bubbleSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(0, bubbleSort.getOuterForLoopVariable());
    }

    @Test
    public void moveToNextStepReInitialisesInnerLoopVariableWhenInnerLoopTerminatesAndOuterLoopStillRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(5, 9, 7));
        bubbleSort.setRunning(true);
        bubbleSort.setInnerForLoopVariable(0);

        // Act
        bubbleSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(data.size() - 1, bubbleSort.getInnerForLoopVariable());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenBothLoopsTerminate() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(5, 7, 9));
        bubbleSort.setRunning(true);
        bubbleSort.setSorted(false);
        bubbleSort.setOuterForLoopVariable(data.size() - 1);
        bubbleSort.setInnerForLoopVariable(data.size() - 2);

        // Act
        bubbleSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(bubbleSort.isSorted());
    }

    @Test
    public void sortThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                bubbleSort.sort(data));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void sortLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        bubbleSort.sort(data);

        // Assert
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        bubbleSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsNearlySortedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        bubbleSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsReversedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3, 1));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        bubbleSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsRandomData() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 2001);

        // Act
        bubbleSort.sort(data);

        // Assert
        Assertions.assertTrue(bubbleSort.isSorted(data));
    }
}