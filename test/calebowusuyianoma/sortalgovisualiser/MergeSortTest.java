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

    @Mock private Map<String, Integer> currentPointerMap;
    @Mock private Map<Integer, Integer> parentNodes;
    @Mock private Map<Integer, Integer> numberOfSortedChildrenMap;
    @Mock private Map<Integer, Boolean> sortedTreeNodes;
    @Mock private Map<Integer, Map<String, Integer>> treeNodePointerMaps;

    @Test
    public void moveToNextStepThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                mergeSort.moveToNextStep(data));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataIsEmpty() {
        // Arrange
        data = new ArrayList<>();
        mergeSort.setSorted(false);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(mergeSort.isSorted());
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

        setCurrentPointerMap(0, data.size() - 1);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        verify(currentPointerMap).put(eq(MergeSort.getMiddle()), anyInt());
        Assertions.assertFalse(mergeSort.getShouldCalculateMiddleIndex());
    }

    @Test
    public void moveToNextStepMovesToLeftChildIfMiddleCalculatedAndCurrentSubArrayContainsMultipleUnsortedElements() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSort.setRunning(true);
        mergeSort.setShouldCalculateMiddleIndex(false);

        int middleIndex = 1;
        when(currentPointerMap.get(MergeSort.getMiddle())).thenReturn(middleIndex);
        setCurrentPointerMap(0, data.size() - 1);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
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

        setCurrentPointerMap(0, 0);

        int parentNode = 1;
        setParentNodes(initialTreeNode, parentNode);
        setNumberOfSortedChildrenMap(parentNode, 0);

        int middleIndex = 0;
        setTreeNodePointerMaps(parentNode, middleIndex, 1);

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

        setCurrentPointerMap(1, 1);

        int parentNode = 1;
        setParentNodes(initialTreeNode, parentNode);
        setNumberOfSortedChildrenMap(parentNode, 1);

        setTreeNodePointerMaps(parentNode, 0, 1);

        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(7, 9, 5, 3));

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

        setCurrentPointerMap(2, 3);

        when(sortedTreeNodes.containsKey(initialTreeNode)).thenReturn(true);
        mergeSort.setSortedTreeNodes(sortedTreeNodes);

        int parentNode = 6;
        setParentNodes(initialTreeNode, parentNode);
        setNumberOfSortedChildrenMap(parentNode, 1);

        setTreeNodePointerMaps(parentNode, 1, data.size() - 1);

        // Act
        mergeSort.moveToNextStep(data);

        // Assert
        Assertions.assertTrue(mergeSort.isSorted());
    }

    @Test
    public void sortThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                mergeSort.sort(data));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
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
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsNearlySortedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 5, 3, 7, 9));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));

        // Act
        mergeSort.sort(data);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void sortCorrectlySortsReversedData() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2, 1));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

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

    @Test
    public void mergeThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, 0, 1, 2));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeLeavesDataUnchangedWhenIsEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        mergeSort.merge(data, 0, 1, 2);

        // Assert
        Assertions.assertTrue(data.isEmpty());
    }

    @Test
    public void mergeLeavesDataUnchangedWhenContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));
        ArrayList<Integer> expected = new ArrayList<>(Collections.singletonList(6));

        // Act
        mergeSort.merge(data, 0, 1, 2);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    @Test
    public void mergeThrowsExceptionWhenLowIndexLessThanZero() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        int lowIndex = -1;
        String expected = "lowIndex must be >= 0 but is " + lowIndex;

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, lowIndex, 1, 2));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeThrowsExceptionWhenMiddleIndexGreaterThanDataSizeMinusOne() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        int middleIndex = 2;
        String expected = "middleIndex must be <= data.size() - 1, but middleIndex is " +
                middleIndex + " and (data.size() - 1) equals " + (data.size() - 1);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, 0, middleIndex, 3));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeThrowsExceptionWhenLowIndexGreaterThanMiddleIndex() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        int lowIndex = 2;
        int middleIndex = 1;
        String expected = "lowIndex must be <= middleIndex, but lowIndex is " + lowIndex +
                " and middleIndex is " + middleIndex;

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, lowIndex, middleIndex, 3));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeThrowsExceptionWhenHighIndexGreaterThanDataSizeMinusOne() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        int highIndex = 2;
        String expected = "highIndex must be <= data.size() - 1, but highIndex is " +
                highIndex + " and (data.size() - 1) equals " + (data.size() - 1);

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, 0, 1, highIndex));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeThrowsExceptionWhenMiddleIndexGreaterThanHighIndex() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7));
        int middleIndex = 1;
        int highIndex = 0;
        String expected = "middleIndex must be <= highIndex, but middleIndex is " + middleIndex +
                " and highIndex is " + highIndex;

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                mergeSort.merge(data, 0, middleIndex, highIndex));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void mergeCorrectlyMergesValidDataWhenIndicesAreValid() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 9, 3, 5));
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(3, 5, 7, 9));

        // Act
        mergeSort.merge(data, 0, 1, data.size() - 1);

        // Assert
        Assertions.assertEquals(expected, data);
    }

    private void setCurrentPointerMap(int low, int high) {
        when(currentPointerMap.get(MergeSort.getLow())).thenReturn(low);
        when(currentPointerMap.get(MergeSort.getHigh())).thenReturn(high);
        mergeSort.setCurrentPointerMap(currentPointerMap);
    }

    private void setParentNodes(int childNode, int parentNode) {
        when(parentNodes.get(childNode)).thenReturn(parentNode);
        mergeSort.setParentNodes(parentNodes);
    }

    private void setNumberOfSortedChildrenMap(int node, int numberOfSortedChildren) {
        when(numberOfSortedChildrenMap.get(node)).thenReturn(numberOfSortedChildren);
        mergeSort.setNumberOfSortedChildrenMap(numberOfSortedChildrenMap);
    }

    private void setTreeNodePointerMaps(int node, int middle, int high) {
        Map<String, Integer> pointerMap = new HashMap<>();
        pointerMap.put(MergeSort.getLow(), 0);
        pointerMap.put(MergeSort.getMiddle(), middle);
        pointerMap.put(MergeSort.getHigh(), high);
        when(treeNodePointerMaps.get(node)).thenReturn(pointerMap);
        mergeSort.setTreeNodePointerMaps(treeNodePointerMaps);
    }
}