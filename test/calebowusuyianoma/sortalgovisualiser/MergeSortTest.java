package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MergeSortTest {
    private final MergeSort mergeSort = new MergeSort();

    private ArrayList<Integer> data;
    private ArrayList<Integer> expected; // TODO: get rid of this if necessary

    @Mock
    private Map<String, Integer> currentPointerMapMock;

    // TODO: Extract common (and ugly) code into private functions

    @Test
    public void moveToNextStepThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expectedMessage = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                mergeSort.moveToNextStep(data));

        // Assert
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        mergeSort.setSorted(false);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(mergeSort.isSorted());
    }

    @Test
    public void moveToNextStepSetsRunningToTrueAndInitialisesMapsWhenSortNotRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSort.setRunning(false);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(mergeSort.isRunning());
        Assertions.assertFalse(mergeSort.getTreeNodePointerMaps().isEmpty());
        Assertions.assertFalse(mergeSort.getParentNodes().isEmpty());
        Assertions.assertFalse(mergeSort.getNumberOfSortedChildrenMap().isEmpty());
    }

    @Test
    public void moveToNextStepCalculatesMiddleIndexIfNotCalculatedAndCurrentSubArrayContainsMultipleUnsortedElements() {
        // Arrange

        // TODO: delete this old code; the new code is working
        // Old code
//        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
//        mergeSort.setRunning(true);
//
//        Map<String, Integer> pointerMap = new HashMap<>();
//        pointerMap.put(MergeSort.getLow(), 0);
//        pointerMap.put(MergeSort.getMiddle(), Integer.MIN_VALUE);
//        pointerMap.put(MergeSort.getHigh(), data.size() - 1);
//        mergeSort.setCurrentPointerMap(pointerMap);
//
//        mergeSort.setShouldCalculateMiddleIndex(true);

        // New code
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSort.setRunning(true);

        when(currentPointerMapMock.get(MergeSort.getLow())).thenReturn(0);
        when(currentPointerMapMock.get(MergeSort.getHigh())).thenReturn(data.size() - 1);
        mergeSort.setCurrentPointerMap(currentPointerMapMock);

        mergeSort.setShouldCalculateMiddleIndex(true);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        verify(currentPointerMapMock).put(eq(MergeSort.getMiddle()), anyInt());
        Assertions.assertFalse(mergeSort.getShouldCalculateMiddleIndex());
    }

    // TODO: use mocking here if necessary/helpful
    @Test
    public void moveToNextStepMovesToLeftChildIfMiddleIndexCalculatedAndCurrentSubArrayContainsMultipleUnsortedElements() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSort.setRunning(true);

        Map<String, Integer> pointerMap = new HashMap<>(); // TODO: consider making this a private field at the top of this class
        pointerMap.put(MergeSort.getLow(), 0);
        pointerMap.put(MergeSort.getMiddle(), 1);
        pointerMap.put(MergeSort.getHigh(), data.size() - 1);
        mergeSort.setCurrentPointerMap(pointerMap);

        mergeSort.setShouldCalculateMiddleIndex(false);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        int currentTreeNode = mergeSort.getCurrentTreeNode();
        Map<String, Integer> currentPointerMap = mergeSort.getTreeNodePointerMaps().get(currentTreeNode);
        Assertions.assertEquals(pointerMap.get(MergeSort.getMiddle()), currentPointerMap.get(MergeSort.getHigh()));
    }

    // TODO: use mocking here if necessary/helpful
    @Test
    public void moveToNextStepMovesToRightChildWhenOnlyLeftChildSorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSort.setRunning(true);

        // TODO: complete the Arrange section

        // Old code
//        Map<String, Integer> pointerMap = new HashMap<>(); // TODO: consider making this a private field at the top of this class
//        pointerMap.put(MergeSort.getLow(), 0);
//        pointerMap.put(MergeSort.getMiddle(), Integer.MIN_VALUE);
//        pointerMap.put(MergeSort.getHigh(), 0);
//        mergeSort.setCurrentPointerMap(pointerMap);
//
//        int currentTreeNode = 0;
//        mergeSort.setCurrentTreeNode(currentTreeNode);
//
//        Map<Integer, Integer> parentNodes = mergeSort.getParentNodes();
//        int low = 0;
//        int high = 1;
//        int parentNode = ((Math.max(low, high) * (Math.max(low, high) + 1)) / 2) + Math.min(low, high);
//        parentNodes.put(currentTreeNode, parentNode);
//        mergeSort.setParentNodes(parentNodes);
//
//        Map<Integer, Integer> numberOfSortedChildrenMap

        // Using mocking


        // Act
        mergeSort.moveToNextStep(data);

        // Assert

    }

    @Test
    public void sortLeavesDataUnchangedWhenEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void sortLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsNearlySortedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsReversedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2, 1));
        expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsRandomData() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 2001);

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertTrue(mergeSort.isSorted(data));
    }
}