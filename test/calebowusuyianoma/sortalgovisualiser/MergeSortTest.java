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

    @Mock private Map<String, Integer> currentPointerMap;
    @Mock private Map<Integer, Integer> parentNodes;
    @Mock private Map<Integer, Integer> numberOfSortedChildrenMap;
    @Mock private Map<Integer, Boolean> sortedTreeNodes;
    @Mock private Map<Integer, Map<String, Integer>> treeNodePointerMaps;

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
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSort.setRunning(true);
        mergeSort.setShouldCalculateMiddleIndex(true);

        when(currentPointerMap.get(MergeSort.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSort.getHigh())).thenReturn(data.size() - 1);
        mergeSort.setCurrentPointerMap(currentPointerMap);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        verify(currentPointerMap).put(eq(MergeSort.getMiddle()), anyInt());
        Assertions.assertFalse(mergeSort.getShouldCalculateMiddleIndex());
    }

    @Test
    public void moveToNextStepMovesToLeftChildIfMiddleIndexCalculatedAndCurrentSubArrayContainsMultipleUnsortedElements() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSort.setRunning(true);
        mergeSort.setShouldCalculateMiddleIndex(false);

        int middleIndex = 1;
        when(currentPointerMap.get(MergeSort.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSort.getMiddle())).thenReturn(middleIndex);
        when(currentPointerMap.get(MergeSort.getHigh())).thenReturn(data.size() - 1);
        mergeSort.setCurrentPointerMap(currentPointerMap);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        //int currentTreeNode = mergeSort.getCurrentTreeNode();
        //Map<String, Integer> currentPointerMap = mergeSort.getTreeNodePointerMaps().get(currentTreeNode);
        Map<String, Integer> currentPointerMap = mergeSort.getCurrentPointerMap();
        Assertions.assertEquals(middleIndex, currentPointerMap.get(MergeSort.getHigh()));
    }

    @Test
    public void moveToNextStepMovesToRightChildWhenOnlyLeftChildSorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSort.setRunning(true);
        int initialTreeNode = 0;
        mergeSort.setCurrentTreeNode(initialTreeNode);

        when(currentPointerMap.get(MergeSort.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSort.getHigh())).thenReturn(0);
        mergeSort.setCurrentPointerMap(currentPointerMap);

        int parentNode = 1;
        when(parentNodes.get(initialTreeNode)).thenReturn(parentNode);
        when(numberOfSortedChildrenMap.get(parentNode)).thenReturn(0);
        mergeSort.setParentNodes(parentNodes);
        mergeSort.setNumberOfSortedChildrenMap(numberOfSortedChildrenMap);

        int middleIndex = 0;
        Map<String, Integer> parentPointerMap = new HashMap<>();
        parentPointerMap.put(MergeSort.getLow(), 0);
        parentPointerMap.put(MergeSort.getMiddle(), middleIndex);
        parentPointerMap.put(MergeSort.getHigh(), 1);
        when(treeNodePointerMaps.get(parentNode)).thenReturn(parentPointerMap);
        mergeSort.setTreeNodePointerMaps(treeNodePointerMaps);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        Map<String, Integer> currentPointerMap = mergeSort.getCurrentPointerMap();
        Assertions.assertEquals(middleIndex + 1, currentPointerMap.get(MergeSort.getLow()));
    }

    @Test
    public void moveToNextStepMergesWhenBothChildrenOfCurrentNodeAreSorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSort.setRunning(true);
        int initialTreeNode = 2;
        mergeSort.setCurrentTreeNode(initialTreeNode);

        when(currentPointerMap.get(MergeSort.getLow())).thenReturn(1);
        when(currentPointerMap.get(MergeSort.getHigh())).thenReturn(1);
        mergeSort.setCurrentPointerMap(currentPointerMap);

        int parentNode = 1;
        when(parentNodes.get(initialTreeNode)).thenReturn(parentNode);
        when(numberOfSortedChildrenMap.get(parentNode)).thenReturn(1);
        mergeSort.setParentNodes(parentNodes);
        mergeSort.setNumberOfSortedChildrenMap(numberOfSortedChildrenMap);

        Map<String, Integer> parentPointerMap = new HashMap<>();
        parentPointerMap.put(MergeSort.getLow(), 0);
        parentPointerMap.put(MergeSort.getMiddle(), 0);
        parentPointerMap.put(MergeSort.getHigh(), 1);
        when(treeNodePointerMaps.get(parentNode)).thenReturn(parentPointerMap);
        mergeSort.setTreeNodePointerMaps(treeNodePointerMaps);

        expected = new ArrayList<>(Arrays.asList(7, 9, 5, 3));

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenChildrenOfRootNodeAreMerged() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 9, 3, 5));
        mergeSort.setRunning(true);
        mergeSort.setSorted(false);
        int initialTreeNode = 8;
        mergeSort.setCurrentTreeNode(initialTreeNode);

        when(currentPointerMap.get(MergeSort.getLow())).thenReturn(2);
        when(currentPointerMap.get(MergeSort.getHigh())).thenReturn(3);
        mergeSort.setCurrentPointerMap(currentPointerMap);

        when(sortedTreeNodes.containsKey(initialTreeNode)).thenReturn(true);
        mergeSort.setSortedTreeNodes(sortedTreeNodes);

        int parentNode = 6;
        when(parentNodes.get(initialTreeNode)).thenReturn(parentNode);
        when(numberOfSortedChildrenMap.get(parentNode)).thenReturn(1);
        mergeSort.setParentNodes(parentNodes);
        mergeSort.setNumberOfSortedChildrenMap(numberOfSortedChildrenMap);

        Map<String, Integer> parentPointerMap = new HashMap<>();
        parentPointerMap.put(MergeSort.getLow(), 0);
        parentPointerMap.put(MergeSort.getMiddle(), 1);
        parentPointerMap.put(MergeSort.getHigh(), data.size() - 1);
        when(treeNodePointerMaps.get(parentNode)).thenReturn(parentPointerMap);
        mergeSort.setTreeNodePointerMaps(treeNodePointerMaps);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(mergeSort.isSorted());
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